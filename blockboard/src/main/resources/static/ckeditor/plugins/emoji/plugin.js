/**
 * @license Copyright (c) 2003-2020, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */
/**
 * @customAuthor Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file ckeditor/emoji/plugin.js
 */
(function () {
  'use strict';
  let groupList = [];
  let emojiList;
  let currentPage;
  let totalGroupCount;
  let groupPerPage = 6;
  let cloneOfBlock; // 50번째 line 의 block 객체의 clone
  let cloneOfThis;  // EmojiDropdown proto 함수를 사용하기 위해 this 를 clone
  let isExistThis = false;
  let stylesLoaded = false,
      arrTools = CKEDITOR.tools.array,
      htmlEncode = CKEDITOR.tools.htmlEncode,
      EmojiDropdown = CKEDITOR.tools.createClass({
        $: function (editor, plugin) {
          var self = this,
              ICON_SIZE = 28;

          this.listeners = [];
          this.plugin = plugin;
          this.editor = editor;

          // Keeps html elements references to not find them again.
          this.elements = {};

          // Name is responsible for icon name also.
          editor.ui.add('EmojiPanel', CKEDITOR.UI_PANELBUTTON, {
            label: '스티커',
            title: '스티커',
            modes: {wysiwyg: 1},
            editorFocus: 0,
            toolbar: 'insert',
            panel: {
              css: [
                CKEDITOR.skin.getPath('editor'),
                plugin.path + 'skins/default.css'
              ],
              attributes: {
                role: 'listbox',
                'aria-label': this.title
              },
              markFirst: false
            },

            onBlock: function (panel, block) {
              var keys = block.keys;
              cloneOfBlock = block;
              keys[39] = 'next'; // ARROW-RIGHT
              keys[40] = 'next'; // ARROW-DOWN
              keys[9] = 'next'; // TAB
              keys[37] = 'prev'; // ARROW-LEFT
              keys[38] = 'prev'; // ARROW-UP
              keys[CKEDITOR.SHIFT + 9] = 'prev'; // SHIFT + TAB
              keys[32] = 'click'; // SPACE

              self.blockElement = block.element;
              emojiList = self.editor._.emoji.list;
              self.addEmojiToGroups();
              block.element.getAscendant('html').addClass('cke_emoji');
              block.element.getDocument().appendStyleSheet(
                  CKEDITOR.getUrl(CKEDITOR.basePath + 'contents.css'));
              block.element.addClass('cke_emoji-panel_block');
              block.element.setHtml(self.createEmojiBlock());
              block.element.removeAttribute('title');
              panel.element.addClass('cke_emoji-panel');
              self.items = block._.getItems();
              self.blockObject = block;
              self.elements.emojiItems = block.element.find(
                  '.cke_emoji-outer_emoji_block li > img');
              self.elements.emojiBlock = block.element.findOne(
                  '.cke_emoji-outer_emoji_block');
              self.elements.navigationItems = block.element.find('nav li');
              self.elements.sections = block.element.find('section');
              self.registerListeners();
              self.cloneProto();
            },

            onOpen: self.openReset()
          });
        },
        proto: {
          cloneProto: function () {
            if (!isExistThis) {
              isExistThis = true;
              cloneOfThis = this;
            }
          },
          registerListeners: function () {
            arrTools.forEach(this.listeners, function (item) {
              var root = this.blockElement,
                  selector = item.selector,
                  listener = item.listener,
                  event = item.event,
                  ctx = item.ctx || this;

              arrTools.forEach(root.find(selector).toArray(), function (node) {
                node.on(event, listener, ctx);
              });
            }, this);
          },
          createEmojiBlock: function () {
            var output = [];
            output.push(this.createGroupsNavigation());
            output.push(this.createEmojiListBlock());

            return '<div class="cke_emoji-inner_panel" id="emoji_panel">'
                + output.join('')
                + '</div>';
          },
          createGroupsNavigation: function () {
            var itemTemplate,
                items;

            // 스티커 카테고리 추가 부분
            itemTemplate = new CKEDITOR.template(
                '<li class="cke_emoji-navigation_item" data-cke-emoji-group="{group}">'
                +
                '<a draggable="false" title="{group}">' +
                '<span style="background-image:url({navIconSrc});background-size: cover;'
                +
                'background-repeat:no-repeat;"></span>' +
                '</a></li>'
            );

            items = arrTools.reduce(groupList, function (acc, item) {
              return acc + itemTemplate.output({
                group: htmlEncode(item.groupName),
                positionX: item.position.x,
                positionY: item.position.y,
                navIconSrc: item.navIconSrc
              });
            }, '');
            this.listeners.push({
              selector: '.cke_emoji-navigation_item',
              event: 'click',
              listener: function (event) {
                let activeElement = event.data.getTarget().getAscendant('li',
                    true);
                if (!activeElement) {
                  return;
                }
                arrTools.forEach(this.elements.navigationItems.toArray(),
                    function (node, index) {
                      if (node.equals(activeElement)) {
                        let groupName = groupList[index - 1].groupName;
                        loadStickersByGroupName(groupName).then(
                            function (data) {
                              refreshStickerSection(JSON.parse(data));
                              node.addClass('active');
                            });
                      } else {
                        node.removeClass('active');
                      }
                    });
              }
            });
            this.listeners.push({
              selector: '.prev',
              event: 'click',
              listener: function () {
                if (currentPage <= 1) {
                  return;
                }
                currentPage--;
                this.reloadStickerPage();
              }
            });
            this.listeners.push({
              selector: '.next',
              event: 'click',
              listener: function (event) {
                var limitPage = parseInt(totalGroupCount / groupPerPage) + 1;
                if (currentPage >= limitPage) {
                  return;
                }
                currentPage++;
                this.reloadStickerPage();
              }
            });
            var icon_path = this.plugin.path + 'icons/';
            return '<nav><ul><li class="prev sticker-arrow" style="float: left"><img src='
                + icon_path + "prev.png" + '></li>' + items
                + '<li class="next sticker-arrow" style="float: right"><img src='
                + icon_path + "next.png" + '></li></ul></nav>';
          },
          createEmojiListBlock: function () {
            var self = this;
            this.listeners.push({
              selector: '.cke_emoji-outer_emoji_block',
              event: 'scroll',
              listener: (function () {
                var buffer = CKEDITOR.tools.throttle(150,
                    self.refreshNavigationStatus, self);
                return buffer.input;
              })()
            });

            this.listeners.push({
              selector: '.sticker',
              event: 'click',
              listener: function (event) {
                this.editor.execCommand('insertSticker', {
                  sticker: event.data.getTarget()
                });
              }
            });
            return '<div id="sticker_area" class="cke_emoji-outer_emoji_block">'
                + this.getEmojiSection(groupList[0]) + '</div>';
          },
          getEmojiSection: function (item) {
            var groupName = htmlEncode(item.groupName),
                group = this.getEmojiListGroup(item.items);
            return '<section data-cke-emoji-group="' + groupName + '" ><ul>'
                + group + '</ul></section>';
          },
          getEmojiListGroup: function (items) {
            var emojiTpl = new CKEDITOR.template('<li class="cke_emoji-item">' +
                '<img class="sticker" width="121" height="auto" draggable="false" '
                + 'data-cke-emoji-group="{groupName}" src="{src}"></li>');
            return arrTools.reduce(items, function (acc, item) {
                  addEncodedName(item);
                  return acc + emojiTpl.output({
                    src: htmlEncode(item.src),
                    groupName: htmlEncode(item.groupName),
                  });
                },
                '',
                this
            );
          },
          openReset: function () {
            let self = this,
                firstCall;

            return function () {
              if (!firstCall) {
                firstCall = true;
              }
              self.elements.emojiBlock.$.scrollTop = 0;
              self.refreshNavigationStatus();
            };
          },
          refreshNavigationStatus: function () {
            let containerOffset = this.elements.emojiBlock.getClientRect().top,
                section,
                groupName;

            section = arrTools.filter(this.elements.sections.toArray(),
                function (element) {
                  let rect = element.getClientRect();
                  if (!rect.height) {
                    return false;
                  }
                  return rect.height + rect.top > containerOffset;
                });
            groupName = section.length ? section[0].data('cke-emoji-group')
                : false;

            arrTools.forEach(this.elements.navigationItems.toArray(),
                function (node) {
                  if (!groupName) {
                    return;
                  }
                  if (node.data('cke-emoji-group') === groupName) {
                    node.addClass('active');
                  } else {
                    node.removeClass('active');
                  }
                });
            this.moveFocus(groupName);
          },
          moveFocus: function (groupName) {
            let firstSectionItem = this.blockElement.findOne(
                'a[data-cke-emoji-group="' + htmlEncode(groupName) + '"]'),
                itemIndex;

            if (!firstSectionItem) {
              return;
            }

            itemIndex = this.getItemIndex(this.items, firstSectionItem);
            firstSectionItem.focus(true);
            firstSectionItem.getAscendant('section').getFirst().scrollIntoView(
                true);
            this.blockObject._.markItem(itemIndex);
          },
          getItemIndex: function (nodeList, item) {
            return arrTools.indexOf(nodeList.toArray(), function (element) {
              return element.equals(item);
            });
          },
          addEmojiToGroups: function () {
            var groupObj = {};
            arrTools.forEach(groupList, function (group) {
              groupObj[group.groupName] = group.items;
            }, this);
            arrTools.forEach(emojiList, function (emojiObj) {
              groupObj[emojiObj.groupName].push(emojiObj);
            }, this);
          },
          // '이전', '다음' 클릭시 Navigation Items 갱신
          reloadStickerPage: function () {
            let url = `/sticker/${currentPage}`;
            let editor = this.editor;

            let stickerDataInSessionStorage = sessionStorage.getItem(url);
            if (stickerDataInSessionStorage == null) {
              CKEDITOR.ajax.load(CKEDITOR.getUrl(url), function (data) {
                reloadNavigationItems(editor, data);
                sessionStorage.setItem(url, data);
              });
            } else {
              reloadNavigationItems(editor, stickerDataInSessionStorage);
            }
          }
        }
      });

  CKEDITOR.plugins.add('emoji', {
    requires: 'ajax,panelbutton,floatpanel',
    icons: 'emojipanel',
    hidpi: true,

    isSupportedEnvironment: function () {
      return !CKEDITOR.env.ie || CKEDITOR.env.version >= 11;
    },

    beforeInit: function () {
      if (!this.isSupportedEnvironment()) {
        return;
      }
      if (!stylesLoaded) {
        CKEDITOR.document.appendStyleSheet(this.path + 'skins/default.css');
        stylesLoaded = true;
      }
    },

    init: function (editor) {
      if (!this.isSupportedEnvironment()) {
        return;
      }
      currentPage = 1;
      let stickerListUrl = '/sticker/' + currentPage;
      let plugin = this;
      let stickerDataInSessionStorage = sessionStorage.getItem(stickerListUrl);

      // 모든 아이템 값 설정
      // sessionStorage 를 검사 후 서버로 요청을 보낼지 결정 
      if (stickerDataInSessionStorage == null) {
        CKEDITOR.ajax.load(CKEDITOR.getUrl(stickerListUrl), function (data) {
          setAllVariableOfSticker(editor, data, plugin);
          sessionStorage.setItem(stickerListUrl, data);
        });
      } else {
        setAllVariableOfSticker(editor, stickerDataInSessionStorage, plugin);
      }

      editor.addCommand('insertSticker', {
        exec: function (editor, data) {
          let cloneElement = data.sticker.clone();
          cloneElement.setAttributes({
            "class": "sticker",
            "width": "auto",
            "height": "auto"
          });
          editor.insertElement(cloneElement);
        }
      });

      if (editor.plugins.toolbar) {
        new EmojiDropdown(editor, this);
      }

    }
  });

  // 스티커 구성에 필요한 값 setting
  function setAllVariableOfSticker(editor, data, plugin) {
    if (data === null) {
      return;
    }
    if (editor._.emoji === undefined) {
      editor._.emoji = {};
    }
    if (editor._.emoji.list === undefined) {
      let json = JSON.parse(data);
      groupList = json.groups;
      totalGroupCount = json.totalGroupCount;
      $.each(groupList, function (index) {
        groupList[index].items = [];
      });
      editor._.emoji.list = json.items;
    }
    if (editor.plugins.toolbar) {
      new EmojiDropdown(editor, plugin);
    }
  }

  // 스티커 구성에 필요한 모든 값 초기화
  function initializeAllVariableOfSticker(editor, data) {
    editor._.emoji = {};
    groupList = data.groups;
    $.each(groupList, function (index) {
      groupList[index].items = [];
    });
  }

  // 50번째 line 은 plugin 이 add 될 때 실행되는 거라 재사용이 불가하여
  // 똑같은 로직을 가진 함수를 새로 생성하였다.
  function cloneOfonBlock(data) {
    cloneOfThis.listeners = [];
    cloneOfThis.blockElement = cloneOfBlock.element;
    emojiList = data.items;
    cloneOfThis.addEmojiToGroups();
    cloneOfBlock.element.setHtml(cloneOfThis.createEmojiBlock());
    cloneOfThis.items = cloneOfBlock._.getItems();
    cloneOfThis.blockObject = cloneOfBlock;
    cloneOfThis.elements.emojiItems = cloneOfBlock.element.find(
        '.cke_emoji-outer_emoji_block li > img');
    cloneOfThis.elements.emojiBlock = cloneOfBlock.element.findOne(
        '.cke_emoji-outer_emoji_block');
    cloneOfThis.elements.navigationItems = cloneOfBlock.element.find('nav li');
    cloneOfThis.elements.sections = cloneOfBlock.element.find('section');
    cloneOfThis.registerListeners();
    cloneOfThis.openReset();
  }

  // Navigation Items 을 갱신한다.
  function reloadNavigationItems(editor, data) {
    if (data === null) {
      return;
    }
    let json = JSON.parse(data);
    initializeAllVariableOfSticker(editor, json);
    cloneOfonBlock(json);
    showStickerAtFirstInNavigation();
  }

  // Navigation 중 첫번째 카테고리 스티커를 보여준다.
  function showStickerAtFirstInNavigation() {
    arrTools.forEach(cloneOfThis.elements.navigationItems.toArray(),
        function (node, index) {
          if (index === 1) {
            node.addClass('active');
          }
        });
  }

  // GroupName 에 해당하는 스티커들을 불러와 session Storage 에 저장
  function loadStickersByGroupName(groupName) {
    let url = `/sticker/groups/${groupName}`;
    return new Promise(function (resolve) {
      let stickerDataInSessionStorage = sessionStorage.getItem(url);
      if (stickerDataInSessionStorage == null) {
        CKEDITOR.ajax.load(CKEDITOR.getUrl(url), function (response) {
          sessionStorage.setItem(url, response);
          resolve(response);
        });
      } else {
        resolve(stickerDataInSessionStorage);
      }
    });
  }

  // 스티커 창 새로고침
  function refreshStickerSection(data) {
    let newHTML = cloneOfThis.getEmojiSection(data);
    cloneOfThis.elements.emojiBlock.setHtml(newHTML);
    cloneOfThis.elements.emojiBlock.$.scrollTop = 0;
    cloneOfThis.registerListeners();
  }

  function addEncodedName(item) {
    if (!item.name) {
      item.name = htmlEncode(
          item.id.replace(/::.*$/, ':').replace(/^:|:$/g, ''));
    }
    return item;
  }
})();