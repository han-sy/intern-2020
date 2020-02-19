/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    alarmEvent.js
 */

$(document).on('click', '.alarm-items', function (event) {
  event.stopPropagation();
});

$(document).on('click', '.btn-alarm-delete', function() {
  let alarmItem_li = $(this).closest("li");
  let alarmID = $(alarmItem_li).children(".alarm-item").attr("data-id");
  $(alarmItem_li).remove();
  removeAlarmItem(alarmID);
  getAlarms();
});

$(document).on('click', '.btn-alarm-delete-all', function () {
  let alarmItems = $(".alarm-item").closest("li");
  for(var i = 0; i < alarmItems.length; i++) {
    var alarmID = $(alarmItems[i]).children(".alarm-item").attr("data-id");
    $(alarmItems[i]).remove();
    removeAlarmItem(alarmID);
  }
  getAlarms();
});