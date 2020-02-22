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
        updateAlarmUI(data);
      }
    }
  });
}

function removeAlarmItem(alarmId) {
  $.ajax({
    type: 'DELETE',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
      getAlarms();
    },
    success: function () {
      getAlarms();
    }
  });
}

function showAlarmContent(alarmId) {
  $.ajax({
    type: 'GET',
    url: '/alarms/' + alarmId,
    error: function (xhr) {
      errorFunction(xhr);
      getAlarms();
    },
    success: function (data) {
      readMarkToAlarmItem(alarmId);
      getPostDataAfterPostClick(data.postID, data.boardID);
      getAlarms();
      clickBoardEventByBoardId(data.boardID);
    }
  })
}

function readMarkToAlarmItem(alarmId) {
  $.ajax({
    type: 'PUT',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
    }
  });
}