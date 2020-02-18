/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    alarmUI.js
 */

//알람 내용
function updateAlarmUI(data, alarmCount) {
  $("#alarmCount").remove();
  var source = $('#alarmList-template').html();
  var template = Handlebars.compile(source);
  var post = {alarms: data};
  var item = template(post);
  $('#alarmcontent').html(item);

  source = $('#alarm-count-template').html();
  template = Handlebars.compile(source);
  var count = {alarmCount: alarmCount};
  item = template(count);
  $("#alarm_icon").append(item);
}

function emptyAlarmUI() {
  $("#alarmCount").remove();
  $('#alarmcontent').html("표시할 알람이 없습니다.");
}