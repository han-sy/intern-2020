/**
 * @license Copyright (c) 2003-2020, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */

(function () {
  'use strict';
  let groupList = [];
  let emojiList;
  let currentPage;
  let totalGroupCount;
  let groupPerPage = 6;
  let origin_block;
  let thisElement;
  let isExistThis = false;
  var stylesLoaded = false,
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
              origin_block = block;
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

            },

            onOpen: self.openReset()
          });
        },
        proto: {
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
                '<span style="background-image:url({navSRC});background-size: cover;'
                +
                'background-repeat:no-repeat;"></span>' +
                '</a></li>'
            );

            items = arrTools.reduce(groupList, function (acc, item) {
              if (!item.items.length) {
                return acc;
              } else {
                return acc + itemTemplate.output({
                  group: htmlEncode(item.groupName),
                  positionX: item.position.x,
                  positionY: item.position.y,
                  navSRC: item.navsrc
                });
              }
            }, '');
            if (!isExistThis) {
              isExistThis = true;
              thisElement = this;
            }
            this.listeners.push({
              selector: '.cke_emoji-navigation_item',
              event: 'click',
              listener: function (event) {
                var activeElement = event.data.getTarget().getAscendant('li',
                    true);
                var refreshItems;
                if (!activeElement) {
                  return;
                }
                arrTools.forEach(this.elements.navigationItems.toArray(),
                    function (node, index) {
                      if (node.equals(activeElement)) {
                        refreshItems = groupList[index - 1];
                        node.addClass('active');
                      } else {
                        node.removeClass('active');
                      }
                    });
                // 선택한 스티커 그룹으로 목록 갱신
                var newHTML = this.getEmojiSection(refreshItems);
                this.elements.emojiBlock.setHtml(newHTML);
                this.elements.emojiBlock.$.scrollTop = 0;
                this.registerListeners();
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
            return '<nav><ul><li class="prev sticker-arrow" style="float: left"><img src=' + icon_path + "prev.png" + '></li>' + items
                + '<li class="next sticker-arrow" style="float: right"><img src=' + icon_path + "next.png" + '></li></ul></nav>';
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

            return arrTools.reduce(
                items,
                function (acc, item) {
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
            // Resets state of emoji dropdown.
            // Clear filters, reset focus, etc.
            var self = this,
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
            var containerOffset = this.elements.emojiBlock.getClientRect().top,
                section,
                groupName;

            section = arrTools.filter(this.elements.sections.toArray(),
                function (element) {
                  var rect = element.getClientRect();
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
            var firstSectionItem = this.blockElement.findOne(
                'a[data-cke-emoji-group="' + htmlEncode(groupName) + '"]'),
                itemIndex;

            if (!firstSectionItem) {
              return;
            }

            itemIndex = this.getItemIndex(this.items, firstSectionItem);
            //firstSectionItem.focus( true );
            //firstSectionItem.getAscendant( 'section' ).getFirst().scrollIntoView( true );
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
          reloadStickerPage: function () {
            var url = `/sticker/${currentPage}`;
            var editor = this.editor;
            var plugin = this.plugin;
            var addEmojiToGroups = this.addEmojiToGroups;
            CKEDITOR.ajax.load(CKEDITOR.getUrl(url), function (data) {
              if (data === null) {
                return;
              }
              var json = JSON.parse(data);
              editor._.emoji = {};
              groupList = json.groups;
              $.each(groupList, function (index) {
                groupList[index].items = [];
              });
              // onBlock과 똑같은 구조로
              thisElement.listeners = [];

              thisElement.blockElement = origin_block.element;
              emojiList = json.items;
              thisElement.addEmojiToGroups();

              origin_block.element.setHtml(thisElement.createEmojiBlock());
              thisElement.items = origin_block._.getItems();
              thisElement.blockObject = origin_block;
              thisElement.elements.emojiItems = origin_block.element.find(
                  '.cke_emoji-outer_emoji_block li > img');
              thisElement.elements.emojiBlock = origin_block.element.findOne(
                  '.cke_emoji-outer_emoji_block');
              thisElement.elements.navigationItems = origin_block.element.find('nav li');
              arrTools.forEach(thisElement.elements.navigationItems.toArray(),
                  function (node, index) {
                    if (index === 1) {
                      node.addClass('active');
                    }
                  });
              thisElement.elements.sections = origin_block.element.find('section');
              thisElement.registerListeners();
              thisElement.openReset();
            });
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
      var stickerListUrl = '/sticker/' + currentPage;
      var plugin = this;

      CKEDITOR.ajax.load(CKEDITOR.getUrl(stickerListUrl), function (data) {
        if (data === null) {
          return;
        }
        if (editor._.emoji === undefined) {
          editor._.emoji = {};
        }

        if (editor._.emoji.list === undefined) {
          var json = JSON.parse(data);
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
      });

      editor.addCommand('insertSticker', {
        exec: function (editor, data) {
          var cloneElement = data.sticker.clone();
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

  function addEncodedName(item) {
    if (!item.name) {
      item.name = htmlEncode(
          item.id.replace(/::.*$/, ':').replace(/^:|:$/g, ''));
    }
    return item;
  }
})();