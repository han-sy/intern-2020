/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    alarmUI.js
 */

//알람 내용
function updateAlarmUI(data) {
  let source = $('#alarmList-template').html();
  let template = Handlebars.compile(source);
  let post = {alarms: data};
  let item = template(post);
  $('#alarm-content').append(item);
}

function updateAlarmCount(alarmCount) {
  $("#alarmCount").remove();
  if (alarmCount === 0) {
    return;
  }
  let source = $('#alarm-count-template').html();
  let template = Handlebars.compile(source);
  let count = {alarmCount: alarmCount};
  let item = template(count);
  $("#alarm_icon").append(item);
}

function emptyAlarmUI() {
  $("#alarmCount").remove();
  $('#alarm-content').html(
      '<p align="center" style="font-size:16px; color: dimgrey">표시할 알람이 없습니다.</p>');
}

function showCommentInputModalOfAlarm(data) {
  $('#comment-alarm-modal').modal('show');
  let source = $('#click-comment-alarmItem-template').html();
  let template = Handlebars.compile(source);
  let comment = {comment: data};
  let item = template(comment);
  $('#comment-alarm-input').html(item);
  getCommentInputHtml("답글", "입력",
      `<a class="mentions_tag" style="cursor:pointer; text-decoration: none;" href="javascript:void(0)"`
      + ` data-id="${data.userId}"><strong>@${data.userName}</strong></a>&nbsp;`,
      "#comment-alarm-container", "btn_openReply", 'is_reply_input',
      "commentTextInAlarm");
}