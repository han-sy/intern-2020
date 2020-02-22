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
  $('#alarm-content').html(item);
  updateAlarmCount(data);
}

function updateAlarmCount(data) {
  $("#alarmCount").remove();
  let alarmCount = countUnreadAlarm(data);
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