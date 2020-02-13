/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function (config) {
  // Define changes to default configuration here.
  // For complete reference see:
  // https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_config.html

  // The toolbar groups arrangement, optimized for a single toolbar row.
  config.toolbarGroups = [
    {name: 'document', groups: ['mode', 'document', 'doctools']},
    {name: 'clipboard', groups: ['clipboard', 'undo']},
    {name: 'editing', groups: ['find', 'selection', 'spellchecker']},
    {name: 'forms'},
    {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
    {name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi']},
    {name: 'links'},
    {name: 'insert'},
    {name: 'styles'},
    {name: 'colors'},
    {name: 'tools'},
    {name: 'others'},
    {name: 'about'}
  ];

  // 에디터의 높이
  config.height = 400;

  // 에디터에서 ENTER 키 처리를 <br>로 하게 한다.
  config.enterMode = CKEDITOR.ENTER_BR;

  // 에디터에서 '파일찾기' 할 때의 경로
  config.filebrowserImageUploadUrl = "/imageUpload";

  // 에디터의 추가 플러그인 설정
  config.extraPlugins = 'image2, '
      + 'dialog, '
      + 'filebrowser, '
      + 'filetools, '
      + 'popup, '
      + 'widget, '
      + 'widgetselection, '
      + 'lineutils, '
      + 'contextmenu, '
      + 'menu, '
      + 'floatpanel, '
      + 'panel, '
      + 'uploadimage, '
      + 'uploadwidget,'
      + 'notificationaggregator,'
      + 'floatpanel,'
      + 'panelbutton,'
      + 'button,'
      + 'emoji,'
      + 'xml,'
      + 'ajax';

  // The default plugins included in the basic setup define some buttons that
  // are not needed in a basic editor. They are removed here.
	config.removeButtons = 'Strike,Subscript,Superscript';

	CKEDITOR.on('dialogDefinition', function (evt) {
		var dialog = evt.data;

		if (dialog.name == 'sticker') {
			// Get dialog definition.
			var dialogName = evt.data.name;
			var dialogDefinition = evt.data.definition;

			console.log(dialogDefinition);
			dialogDefinition.dialog.resize(300,200);
		}
	});
  // Dialog windows are also simplified.
  config.removeDialogTabs = 'link:advanced';
};
