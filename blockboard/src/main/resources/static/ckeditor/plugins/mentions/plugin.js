/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */
/**
 * @customAuthor Woohyeok.jun@worksmobile.com
 */
'use strict';
const mentionNameInSessionStorage = "userList";

(function () {

  CKEDITOR._.mentions = {
    cache: {}
  };

  var MARKER = '@',
      MIN_CHARS = 2,
      cache = CKEDITOR._.mentions.cache,
      arrTools = CKEDITOR.tools.array;


  CKEDITOR.plugins.add('mentions', {
    requires: 'autocomplete,textmatch,ajax',
    instances: [],
    beforeInit: function (editor) {
      let userList = [];
      let userListInSessionStorage = sessionStorage.getItem(mentionNameInSessionStorage);

      if (userListInSessionStorage == null) {
        getUserList(userList);
      } else {
        loadUserListInSessionStorage(userListInSessionStorage, userList);
      }

      editor.config.mentions = [{
        feed: userList,
        minChars: 0,
        itemTemplate: '<li data-id="{id}" class="mentions list-group-item">'
            + '<div class="row">'
            + '<div class="col-3">'
            + '<img src="{thumbnailUrl}" width=70px height=auto>'
            + '</div>'
            + '<div class="col">'
            + '<div class="row mentions_list_top">'
            + '<a>{userId}</a>'
            + '</div>'
            + '<div class="row mentions_list_bottom">'
            + '<a>{userName} {userType}</a>'
            + '<div class="col" style="text-align:right">{companyName}</div>'
            + '</div>'
            + '</div>'
            + '</div>',
        outputTemplate: `<a class="mentions_tag name" href="javascript:void(0)" data-id="{userId}"><strong>{TagName}</strong></a>&nbsp;`
      }];
    },
    init: function (editor) {
      var self = this;

      editor.on('instanceReady', function () {
        CKEDITOR.tools.array.forEach(editor.config.mentions || [],
            function (config) {
              self.instances.push(new Mentions(editor, config));
            });
      });
    },
    isSupportedEnvironment: function (editor) {
      return editor.plugins.autocomplete.isSupportedEnvironment(editor);
    }
  });

  function Mentions(editor, config) {
    var feed = config.feed;

    this.caseSensitive = config.caseSensitive;
    this.marker = config.hasOwnProperty('marker') ? config.marker : MARKER;
    this.minChars = config.minChars !== null && config.minChars !== undefined
        ? config.minChars : MIN_CHARS;
    this.pattern = config.pattern || createPattern(this.marker, this.minChars);
    this.cache = config.cache !== undefined ? config.cache : true;
    this.throttle = config.throttle !== undefined ? config.throttle : 200;
    this._autocomplete = new CKEDITOR.plugins.autocomplete(editor, {
      textTestCallback: getTextTestCallback(this.marker, this.minChars,
          this.pattern),
      dataCallback: getDataCallback(feed, this),
      itemTemplate: config.itemTemplate,
      outputTemplate: config.outputTemplate,
      throttle: this.throttle,
      itemsLimit: config.itemsLimit
    });
  }

  Mentions.prototype = {
    destroy: function () {
      this._autocomplete.destroy();
    }
  };

  function loadUserListInSessionStorage(userListInSessionStorage, userList) {
    CKEDITOR.ajax.load(CKEDITOR.getUrl('/users/count'), function (countUser) {
      if (countUser != JSON.parse(userListInSessionStorage).length) {
        getUserList(userList);
        return;
      }
      arrTools.forEach(JSON.parse(userListInSessionStorage), function (item) {
        userList.push(item);
      });
    });
  }

  function getUserList(userList) {
    CKEDITOR.ajax.load(CKEDITOR.getUrl('/users'), function (data) {
      arrTools.forEach(JSON.parse(data), function (item) {
        userList.push(item);
      });
      sessionStorage.setItem(mentionNameInSessionStorage, data);
    });
  }

  function createPattern(marker, minChars) {
    var pattern = '\\' + marker + '[_a-zA-Z0-9À-ž]';

    if (minChars) {
      pattern += '{' + minChars + ',}';
    } else {
      pattern += '*';
    }
    pattern += '$';

    return new RegExp(pattern);
  }

  function getTextTestCallback(marker, minChars, pattern) {
    return function (range) {
      if (!range.collapsed) {
        return null;
      }

      return CKEDITOR.plugins.textMatch.match(range, matchCallback);
    };

    function matchCallback(text, offset) {
      var match = text.slice(0, offset)
      .match(pattern);

      if (!match) {
        return null;
      }

      // Do not proceed if a query is a part of word.
      var prevChar = text[match.index - 1];
      if (prevChar !== undefined && !prevChar.match(/\s+/)) {
        return null;
      }

      return {
        start: match.index,
        end: offset
      };
    }
  }

  function getDataCallback(feed, mentions) {
    return function (matchInfo, callback) {
      var query = matchInfo.query;

      // We are removing marker here to give clean query result for the endpoint callback.
      if (mentions.marker) {
        query = query.substring(mentions.marker.length);
      }

      if (CKEDITOR.tools.array.isArray(feed)) {
        createArrayFeed();
      } else if (typeof feed === 'string') {
        createUrlFeed();
      } else {
        feed({
          query: query,
          marker: mentions.marker
        }, resolveCallbackData);
      }

      function createArrayFeed() {
        var data = indexArrayFeed(feed).filter(function (item) {
          var itemName = item.userId;
          if (!mentions.caseSensitive) {
            itemName = itemName.toLowerCase();
            query = query.toLowerCase();
          }
          return itemName.indexOf(query) === 0;
        });

        resolveCallbackData(data);
      }

      // itemTemplate 으로 표시되는 부분
      function indexArrayFeed(feed) {
        var index = 1;
        return CKEDITOR.tools.array.reduce(feed, function (current, item) {
          current.push({
            userName: item.userName,
            userId: item.userId,
            userType: item.userType,
            companyName: item.companyName,
            thumbnailUrl: item.thumbnailUrl,
            id: index++
          });
          return current;
        }, []);
      }

      function createUrlFeed() {
        var encodedUrl = new CKEDITOR.template(feed)
        .output({encodedQuery: encodeURIComponent(query)});

        if (mentions.cache && cache[encodedUrl]) {
          return resolveCallbackData(cache[encodedUrl]);
        }

        CKEDITOR.ajax.load(encodedUrl, function (data) {
          var items = JSON.parse(data);

          // Cache URL responses for performance improvement (#1969).
          if (mentions.cache && items !== null) {
            cache[encodedUrl] = items;
          }
          resolveCallbackData(items);

        });
      }

      function resolveCallbackData(data) {
        if (!data) {
          return;
        }

        // We don't want to change item data, so lets create new one.
        // OutputTemplate 으로 표시되는 부분
        var newData = CKEDITOR.tools.array.map(data, function (item) {
          var name = MARKER + item.userName;
          return CKEDITOR.tools.object.merge(item,
              {userId: item.userId, TagName: name});
        });

        callback(newData);
      }
    };
  }

  CKEDITOR.plugins.mentions = Mentions;
})();
