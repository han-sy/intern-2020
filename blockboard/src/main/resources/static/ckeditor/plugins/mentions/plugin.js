/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */

'use strict';

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
      var userList = [];

      CKEDITOR.ajax.load("/users", function (data) {
        arrTools.forEach(JSON.parse(data), function (item) {
          userList.push(item);
        });
      });

      editor.config.mentions = [{
        feed: userList,
        minChars: 0,
        itemTemplate: '<li data-id="{id}" class="mentions list-group-item">'
            + '<div class="row">'
            + '<div class="col-2">'
            + '<img src="http://cdn2-aka.makeshop.co.kr/design/jogunshop/MakeshopRenewal/img/wish_icon.png">'
            + '</div>'
            + '<div class="col">'
            + '<div class="row mentions_list_top">'
            + '<a>{userID}</a>'
            + '</div>'
            + '<div class="row mentions_list_bottom">'
            + '<a>{userName} {userType}</a>'
            + '<div class="col" style="text-align:right">{companyName}</div>'
            + '</div>'
            + '</div>'
            + '</div>',
        outputTemplate: `<a class="mentions_tag name" href="javascript:void(0)" data-id="{userID}"><strong>{TagName}</strong></a>&nbsp;`
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

  /**
   * The [Mentions](https://ckeditor.com/cke4/addon/mentions) plugin allows you to type a marker character and get suggested values for the
   * text matches so that you do not have to write it on your own.
   *
   * The recommended way to add the mentions feature to an editor is by setting the {@link CKEDITOR.config#mentions config.mentions} option:
   *
   * ```javascript
   * // Passing mentions configuration when creating the editor.
   * CKEDITOR.replace( 'editor', {
   * 		mentions: [ { feed: ['Anna', 'Thomas', 'John'], minChars: 0 } ]
   * } );
   *
   * // Simple usage with the CKEDITOR.config.mentions property.
   * CKEDITOR.config.mentions = [ { feed: ['Anna', 'Thomas', 'John'], minChars: 0 } ];
   * ```
   *
   * @class CKEDITOR.plugins.mentions
   * @since 4.10.0
   * @constructor Creates a new instance of mentions and attaches it to the editor.
   * @param {CKEDITOR.editor} editor The editor to watch.
   * @param {CKEDITOR.plugins.mentions.configDefinition} config Configuration object keeping information about how to instantiate the mentions plugin.
   */
  function Mentions(editor, config) {
    var feed = config.feed;

    /**
     * Indicates that a mentions instance is case-sensitive for simple items feed, i.e. an array feed.
     *
     * **Note:** This will take no effect on feeds using a callback or URLs, as in this case the results are expected to
     * be already filtered.
     *
     * @property {Boolean} [caseSensitive=false]
     * @readonly
     */
    this.caseSensitive = config.caseSensitive;

    /**
     * The character that should trigger autocompletion.
     *
     * @property {String} [marker='@']
     * @readonly
     */
    this.marker = config.hasOwnProperty('marker') ? config.marker : MARKER;

    /**
     * The number of characters that should follow the marker character in order to trigger the mentions feature.
     *
     * @property {Number} [minChars=2]
     * @readonly
     */
    this.minChars = config.minChars !== null && config.minChars !== undefined
        ? config.minChars : MIN_CHARS;

    /**
     * The pattern used to match queries.
     *
     * The default pattern matches words with the query including the {@link #marker config.marker} and {@link #minChars config.minChars} properties.
     *
     * ```javascript
     * // Match only words starting with "a".
     * var pattern = /^a+\w*$/;
     * ```
     *
     * @property {RegExp} pattern
     * @readonly
     */
    this.pattern = config.pattern || createPattern(this.marker, this.minChars);

    /**
     * Indicates if the URL feed responses will be cached.
     *
     * The cache is based on the URL request and is shared between all mentions instances (including different editors).
     *
     * @property {Boolean} [cache=true]
     * @readonly
     */
    this.cache = config.cache !== undefined ? config.cache : true;

    /**
     * @inheritdoc CKEDITOR.plugins.mentions.configDefinition#throttle
     * @property {Number} [throttle=200]
     * @readonly
     */
    this.throttle = config.throttle !== undefined ? config.throttle : 200;

    /**
     * {@link CKEDITOR.plugins.autocomplete Autocomplete} instance used by the mentions feature to implement autocompletion logic.
     *
     * @property {CKEDITOR.plugins.autocomplete}
     * @private
     */
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

    /**
     * Destroys the mentions instance.
     *
     * The view element and event listeners will be removed from the DOM.
     */
    destroy: function () {
      this._autocomplete.destroy();
    }
  };

  function createPattern(marker, minChars) {
    // Match also diacritic characters (#2491).
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
          var itemName = item.userID;
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
            userID: item.userID,
            userType: item.userType,
            companyName: item.companyName,
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
              {userID: item.userID, TagName: name});
        });

        callback(newData);
      }
    };
  }

  CKEDITOR.plugins.mentions = Mentions;
})();
