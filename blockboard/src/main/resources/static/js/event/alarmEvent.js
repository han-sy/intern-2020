/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    alarmEvent.js
 */

$(document).on('click', '.alarm-items', function (event) {
  event.stopPropagation();
});

$(document).on('click', '.btn-alarm-delete', function (event) {
  event.stopPropagation();
  let alarmItem = $(this).closest("li")[0];
  if (!$(alarmItem).hasClass("alarm-read") &&
      confirm("확인하지 않은 알람입니다. 삭제하시겠습니까?") === false) {
    return;
  }
  let alarmID = alarmItem.dataset.id;
  alarmItem.remove();
  removeAlarmItem(alarmID);
  getAlarms();
});

$(document).on('click', 'li.alarm-item', function () {
  let alarmId = parseInt($(this)[0].dataset.id);
  $(this).addClass("alarm-read");
  postClear();
  editorClear();
  showAlarmContent(alarmId);
});

$(document).on('click', '.btn-alarm-delete-all', function () {
  deleteAlarmByClass("li.alarm-item");
});

$(document).on('click', '.btn-alarm-delete-read', function () {
  deleteAlarmByClass("li.alarm-read");
});

function deleteAlarmByClass(className) {
  let alarmItems = $(className);
  for (var i = 0; i < alarmItems.length; i++) {
    var alarmID = $(alarmItems)[i].dataset.id;
    $(alarmItems)[i].remove();
    removeAlarmItem(alarmID);
  }
}

function countUnreadAlarm(alarmList) {
  var unreadAlarmCount = 0;
  for (let i = 0; i < alarmList.length; i++) {
    if (alarmList[i].isRead === false) {
      unreadAlarmCount += 1;
    }
  }
  return unreadAlarmCount;
}