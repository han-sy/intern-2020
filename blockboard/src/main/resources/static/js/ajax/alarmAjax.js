/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file alarmAjax.js
 */

function getAlarms() {
  $.ajax({
    type: 'GET',
    url: '/alarms',
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      if(data === "") {
        emptyAlarmUI();
      } else {
        updateAlarmUI(data, data.length);
      }
    }
  });
}

function removeAlarmItem(alarmID) {
  $.ajax({
    type: 'DELETE',
    url: `/alarms/${alarmID}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function () {

    }
  });
}