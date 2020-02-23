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
      if (data === "") {
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
    },
    complete: function () {
      getAlarms();
    }
  });
}

function getAlarmByAlarmId(alarmId) {
  $.ajax({
    type: 'GET',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      showAlarmContent(data.alarmID, data.commentID);
    }
  });
}

function showAlarmContent(alarmId, commentId) {
  $.ajax({
    type: 'GET',
    url: `/alarms/${alarmId}/post`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      readMarkToAlarmItem(alarmId);
      // 해당 알람이 온 게시판으로 이동
      clickBoardEventByBoardId(data.boardID);
      // 게시물 목록에서 게시글 클릭할 때의 이벤트 재사용
      getPostDataAfterPostClick(data.postID, data.boardID);
      showCommentAlarmContent(commentId);
    },
    complete: function () {
      getAlarms();
    }
  });
}

// 알람 읽음 표시
function readMarkToAlarmItem(alarmId) {
  $.ajax({
    type: 'PUT',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
    }
  });
}