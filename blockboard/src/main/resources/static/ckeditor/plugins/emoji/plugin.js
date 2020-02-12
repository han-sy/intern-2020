/**
 * @license Copyright (c) 2003-2020, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or https://ckeditor.com/legal/ckeditor-oss-license
 */

( function() {
	'use strict';

	var stylesLoaded = false,
		arrTools = CKEDITOR.tools.array,
		htmlEncode = CKEDITOR.tools.htmlEncode,
		EmojiDropdown = CKEDITOR.tools.createClass( {
			$: function( editor, plugin ) {
				var lang = this.lang = editor.lang.emoji,
					self = this,
					ICON_SIZE = 21;

				this.listeners = [];
				this.plugin = plugin;
				this.editor = editor;
				this.groups = [
						{
							name: 'people',
							sectionName: lang.groups.people,
							svgId: 'cke4-icon-emoji-2',
							position: {
								x: -1 * ICON_SIZE,
								y: 0
							},
							items: []
						},
						{
							name: 'nature',
							sectionName: lang.groups.nature,
							svgId: 'cke4-icon-emoji-3',
							position: {
								x: -2 * ICON_SIZE,
								y: 0
							},
							items: []
						},
						{
							name: 'food',
							sectionName: lang.groups.food,
							svgId: 'cke4-icon-emoji-4',
							position: {
								x: -3 * ICON_SIZE,
								y: 0
							},
							items: []
						},
						{
							name: 'travel',
							sectionName: lang.groups.travel,
							svgId: 'cke4-icon-emoji-6',
							position: {
								x: -2 * ICON_SIZE,
								y: -1 * ICON_SIZE
							},
							items: []
						},
						{
							name: 'activities',
							sectionName: lang.groups.activities,
							svgId: 'cke4-icon-emoji-5',
							position: {
								x: -4 * ICON_SIZE,
								y: 0
							},
							items: []
						},
						{
							name: 'objects',
							sectionName: lang.groups.objects,
							svgId: 'cke4-icon-emoji-7',
							position: {
								x: 0,
								y: -1 * ICON_SIZE
							},
							items: []
						},
						{
							name: 'symbols',
							sectionName: lang.groups.symbols,
							svgId: 'cke4-icon-emoji-8',
							position: {
								x: -1 * ICON_SIZE,
								y: -1 * ICON_SIZE
							},
							items: []
						},
						{
							name: 'flags',
							sectionName: lang.groups.flags,
							svgId: 'cke4-icon-emoji-9',
							position: {
								x: -3 * ICON_SIZE,
								y: -1 * ICON_SIZE
							},
							items: []
						}
					];

				// Keeps html elements references to not find them again.
				this.elements = {};

				// Below line might be removable
				editor.ui.addToolbarGroup( 'emoji', 'insert' );
				// Name is responsible for icon name also.
				editor.ui.add( 'EmojiPanel', CKEDITOR.UI_PANELBUTTON, {
					label: 'emoji',
					title: lang.title,
					modes: { wysiwyg: 1 },
					editorFocus: 0,
					toolbar: 'insert',
					panel: {
						css: [
							CKEDITOR.skin.getPath( 'editor' ),
							plugin.path + 'skins/default.css'
						],
						attributes: {
							role: 'listbox',
							'aria-label': lang.title
						},
						markFirst: false
					},

					onBlock: function( panel, block ) {
						var keys = block.keys,
							rtl = editor.lang.dir === 'rtl';

						keys[ rtl ? 37 : 39 ] = 'next'; // ARROW-RIGHT
						keys[ 40 ] = 'next'; // ARROW-DOWN
						keys[ 9 ] = 'next'; // TAB
						keys[ rtl ? 39 : 37 ] = 'prev'; // ARROW-LEFT
						keys[ 38 ] = 'prev'; // ARROW-UP
						keys[ CKEDITOR.SHIFT + 9 ] = 'prev'; // SHIFT + TAB
						keys[ 32 ] = 'click'; // SPACE

						self.blockElement = block.element;
						self.emojiList = self.editor._.emoji.list;

						self.addEmojiToGroups();

						block.element.getAscendant( 'html' ).addClass( 'cke_emoji' );
						block.element.getDocument().appendStyleSheet( CKEDITOR.getUrl( CKEDITOR.basePath + 'contents.css' ) );
						block.element.addClass( 'cke_emoji-panel_block' );
						block.element.setHtml( self.createEmojiBlock() );
						block.element.removeAttribute( 'title' );
						panel.element.addClass( 'cke_emoji-panel' );

						self.items = block._.getItems();

						self.blockObject = block;
						self.elements.emojiItems = block.element.find( '.cke_emoji-outer_emoji_block li > a' );
						self.elements.sectionHeaders = block.element.find( '.cke_emoji-outer_emoji_block h2' );
						self.elements.input = block.element.findOne( 'input' );
						self.inputIndex = self.getItemIndex( self.items, self.elements.input );
						self.elements.emojiBlock = block.element.findOne( '.cke_emoji-outer_emoji_block' );
						self.elements.navigationItems = block.element.find( 'nav li' );
						self.elements.sections = block.element.find( 'section' );
						self.registerListeners();

					},

					onOpen: self.openReset()
				} );
			},
			proto: {
				registerListeners: function() {
					arrTools.forEach( this.listeners, function( item ) {
						var root = this.blockElement,
							selector = item.selector,
							listener = item.listener,
							event = item.event,
							ctx = item.ctx || this;

						arrTools.forEach( root.find( selector ).toArray(), function( node ) {
							node.on( event, listener, ctx );
						} );
					}, this );
				},
				createEmojiBlock: function() {
					var output = [];

					output.push( this.createGroupsNavigation() );
					output.push( this.createEmojiListBlock() );

					return '<div class="cke_emoji-inner_panel">' + output.join( '' ) + '</div>';
				},
				createGroupsNavigation: function() {
					var itemTemplate,
						items,
						svgUrl,
						imgUrl,
						useAttr;

						imgUrl = CKEDITOR.getUrl( this.plugin.path + 'assets/iconsall.png' );

						// ajax로 디렉토리 리스트랑 파일리스트 받아올 수 있음
						// 스티커 카테고리 추가 부분
						itemTemplate = new CKEDITOR.template(
							'<li class="cke_emoji-navigation_item" data-cke-emoji-group="{group}">' +
							'<a href="#" draggable="false" _cke_focus="1" title="{name}">' +
							'<span style="background-image:url(' + imgUrl + ');' +
							'background-repeat:no-repeat;background-position:{positionX}px {positionY}px;"></span>' +
							'</a></li>'
						);

						items = arrTools.reduce( this.groups, function( acc, item ) {
							if ( !item.items.length ) {
								return acc;
							} else {
								return acc + itemTemplate.output( {
									group: htmlEncode( item.name ),
									name: htmlEncode( item.sectionName ),
									positionX: item.position.x,
									positionY: item.position.y
								} );
							}
						}, '' );

					this.listeners.push( {
						selector: 'nav',
						event: 'click',
						listener: function( event ) {
							var activeElement = event.data.getTarget().getAscendant( 'li', true );
							if ( !activeElement ) {
								return;
							}
							arrTools.forEach( this.elements.navigationItems.toArray(), function( node ) {
								if ( node.equals( activeElement ) ) {
									node.addClass( 'active' );
								} else {
									node.removeClass( 'active' );
								}
							} );

							this.clearSearchAndMoveFocus( activeElement );

							event.data.preventDefault();
						}
					} );

					return '<nav aria-label="' + htmlEncode( this.lang.navigationLabel ) + '"><ul>' + items + '</ul></nav>';
				},
				createEmojiListBlock: function() {
					var self = this;
					this.listeners.push( {
						selector: '.cke_emoji-outer_emoji_block',
						event: 'scroll',
						listener: ( function() {
							var buffer = CKEDITOR.tools.throttle( 150, self.refreshNavigationStatus, self );
							return buffer.input;
						} )()
					} );

					this.listeners.push( {
						selector: '.cke_emoji-outer_emoji_block',
						event: 'click',
						listener: function( event ) {
							if ( event.data.getTarget().data( 'cke-emoji-name' ) ) {
								this.editor.execCommand( 'insertEmoji', { emojiText: event.data.getTarget().data( 'cke-emoji-symbol' ) } );
							}
						}
					} );

					return '<div class="cke_emoji-outer_emoji_block">' + this.getEmojiSections() + '</div>';
				},
				getEmojiSections: function() {
					return arrTools.reduce( this.groups, function( acc, item ) {
						// If group is empty skip it.
						if ( !item.items.length ) {
							return acc;
						} else {
							return acc + this.getEmojiSection( item );
						}
					}, '', this );
				},
				getEmojiSection: function( item ) {
					var groupName = htmlEncode( item.name ),
						sectionName = htmlEncode( item.sectionName ),
						group = this.getEmojiListGroup( item.items );

					return '<section data-cke-emoji-group="' + groupName + '" ><ul>' + group + '</ul></section>';
				},
				getEmojiListGroup: function( items ) {
					var emojiTpl = new CKEDITOR.template( '<li class="cke_emoji-item">' +
						'<a draggable="false" data-cke-emoji-full-name="{id}" data-cke-emoji-name="{name}" data-cke-emoji-symbol="{symbol}" data-cke-emoji-group="{group}" ' +
						'data-cke-emoji-keywords="{keywords}" title="{name}" href="#" _cke_focus="1">{symbol}</a>' +
						'</li>' );

					return arrTools.reduce(
						items,
						function( acc, item ) {
							addEncodedName( item );
							return acc + emojiTpl.output( {
									symbol: htmlEncode( item.symbol ),
									id: htmlEncode( item.id ),
									name: item.name,
									group: htmlEncode( item.group ),
									keywords: htmlEncode( ( item.keywords || [] ).join( ',' ) )
								} );
						},
						'',
						this
					);
				},
				openReset: function() {
					// Resets state of emoji dropdown.
					// Clear filters, reset focus, etc.
					var self = this,
						firstCall;

					return function() {

						if ( !firstCall ) {
							self.filter( '' );
							firstCall = true;
						}

						self.elements.emojiBlock.$.scrollTop = 0;
						self.refreshNavigationStatus();

						// Reset focus:
						CKEDITOR.tools.setTimeout( function() {
							self.elements.input.focus( true );
							self.blockObject._.markItem( self.inputIndex );
						}, 0, self );
					};
				},
				refreshNavigationStatus: function() {
					var containerOffset = this.elements.emojiBlock.getClientRect().top,
						section,
						groupName;

					section = arrTools.filter( this.elements.sections.toArray(), function( element ) {
						var rect = element.getClientRect();
						if ( !rect.height || element.findOne( 'h2' ).hasClass( 'hidden' ) ) {
							return false;
						}
						return rect.height + rect.top > containerOffset;
					} );
					groupName = section.length ? section[ 0 ].data( 'cke-emoji-group' ) : false;

					arrTools.forEach( this.elements.navigationItems.toArray(), function( node ) {
						if ( node.data( 'cke-emoji-group' ) === groupName ) {
							node.addClass( 'active' );
						} else {
							node.removeClass( 'active' );
						}
					} );
				},
				clearSearchAndMoveFocus: function( activeElement ) {
					this.moveFocus( activeElement.data( 'cke-emoji-group' ) );
				},
				moveFocus: function( groupName ) {
					var firstSectionItem = this.blockElement.findOne( 'a[data-cke-emoji-group="' + htmlEncode( groupName ) + '"]' ),
						itemIndex;

					if ( !firstSectionItem ) {
						return;
					}

					itemIndex = this.getItemIndex( this.items, firstSectionItem );
					firstSectionItem.focus( true );
					firstSectionItem.getAscendant( 'section' ).getFirst().scrollIntoView( true );
					this.blockObject._.markItem( itemIndex );
				},
				getItemIndex: function( nodeList, item ) {
					return arrTools.indexOf( nodeList.toArray(), function( element ) {
						return element.equals( item );
					} );
				},
				addEmojiToGroups: function() {
					var groupObj = {};
					arrTools.forEach( this.groups, function( group ) {
						groupObj[ group.name ] = group.items;
					}, this );

					arrTools.forEach( this.emojiList, function( emojiObj ) {
						groupObj[ emojiObj.group ].push( emojiObj );
					}, this );
				}
			}
		} );


	CKEDITOR.plugins.add( 'emoji', {
		requires: 'ajax,panelbutton,floatpanel',
		lang: 'en', // %REMOVE_LINE_CORE%
		icons: 'emojipanel',
		hidpi: true,

		isSupportedEnvironment: function() {
			return !CKEDITOR.env.ie || CKEDITOR.env.version >= 11;
		},

		beforeInit: function() {
			if ( !this.isSupportedEnvironment() ) {
				return;
			}
			if ( !stylesLoaded ) {
				CKEDITOR.document.appendStyleSheet( this.path + 'skins/default.css' );
				stylesLoaded = true;
			}
		},

		init: function( editor ) {
			if ( !this.isSupportedEnvironment() ) {
				return;
			}

			var emojiListUrl = editor.config.emoji_emojiListUrl || 'plugins/emoji/emoji.json',
				arrTools = CKEDITOR.tools.array;


			CKEDITOR.ajax.load( CKEDITOR.getUrl( emojiListUrl ), function( data ) {
				if ( data === null ) {
					return;
				}
				if ( editor._.emoji === undefined ) {
					editor._.emoji = {};
				}

				if ( editor._.emoji.list === undefined ) {
					editor._.emoji.list = JSON.parse( data );
				}

				var emojiList = editor._.emoji.list,
					charactersToStart = editor.config.emoji_minChars === undefined ? 2 : editor.config.emoji_minChars;

				if ( editor.status !== 'ready' ) {
					editor.once( 'instanceReady', initPlugin );
				} else {
					initPlugin();
				}

			} );

			editor.addCommand( 'insertEmoji', {
				exec: function( editor, data ) {
					editor.insertHtml( data.emojiText );
				}
			} );

			if ( editor.plugins.toolbar ) {
				new EmojiDropdown( editor, this );
			}

		}
	} );

	function addEncodedName( item ) {
		if ( !item.name ) {
			item.name = htmlEncode( item.id.replace( /::.*$/, ':' ).replace( /^:|:$/g, '' ) );
		}
		return item;
	}
} )();