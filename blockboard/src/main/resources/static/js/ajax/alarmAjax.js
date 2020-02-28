/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file alarmAjax.js
 */

// 최하단으로 스크롤을 했지만 더 받아올 아이템이 없다면 false 가 되어 더 이상 서버로 요청이 가지 않는다.
var hasMoreAlarm = true;

function getAlarms(pageNum) {
  if (pageNum === 1) {
    hasMoreAlarm = true;
  }
  if (!hasMoreAlarm) {
    return;
  }
  $.ajax({
    type: 'GET',
    url: '/alarms',
    data: {pageNum: pageNum},
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      if (data === "" && pageNum === 1) {
        emptyAlarmUI();
      } else if (data === "" && pageNum > 1) {
        hasMoreAlarm = false;
      } else {
        updateAlarmUI(data);
      }
    }
  });
}

/**
 * 계속 서버에 요청해서 받아온 1페이지의 Alarm 중에서 현재 Alarm 창에 없는 것들만 골라낸다.
 * @param data 서버에서 받아온 1페이지의(6개)의 알람
 * @returns data 현재 알람창의 없는 것들만 골라낸 것
 */
function removeDuplicateAlarmItem(data) {
  let duplicateAlarmIndex = [];
  $(data).each(function (dataIndex, newAlarmItem) {
    $(Array.from($('li.alarm-item'))).each(
        function (alarmItemIndex, existAlarmItem) {
          if (alarmItemIndex > 10) { // 최대 10개 까지만 검사
            return false;
          }
          if (newAlarmItem.alarmId == existAlarmItem.dataset.id) {
            duplicateAlarmIndex.push(dataIndex);
            return false;
          }
        });
  });

  for (let i = 0; i < duplicateAlarmIndex.length; i++) {
    data.splice(duplicateAlarmIndex[i] - i, 1);
  }
  return data;
}

/**
 * 1페이지의 알람을 서버에 주기적으로 요청하여 받아온다.
 * data 에 현재 표시되어 있는 알람을 제외한 새로운 알람이 들어있다면
 * 알람창 제일 위에 추가한다.
 */
function getNewAlarms() {
  $.ajax({
    type: 'GET',
    url: '/alarms',
    data: {pageNum: 1},
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      let newAlarmItems = removeDuplicateAlarmItem(data);
      if (newAlarmItems.length > 0) {
        appendNewAlarmItems(newAlarmItems);
        getUnreadAlarmCount();
      }
    }
  });
}

/**
 * 읽지 않은 알람 개수를 UI 에 표시한다.
 */
function getUnreadAlarmCount() {
  $.ajax({
    type: 'GET',
    url: '/alarms/count',
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (alarmCount) {
      updateAlarmCount(alarmCount);
    }
  })
}

function removeAlarmItem(alarmId) {
  $.ajax({
    type: 'DELETE',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    complete: function () {
      getUnreadAlarmCount();
    }
  });
}

/**
 * 해당 알람의 정보를 가져온다.
 * @param alarmId 클릭한 알람의 ID
 */
function getAlarmByAlarmId(alarmId) {
  $.ajax({
    type: 'GET',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      showAlarmContent(data.alarmId, data.commentId);
    }
  });
}

/**
 * 알람 아이디에 해당하는 게시글이나 댓글 내용을 가져와 표시해준다.
 * @param alarmId
 * @param commentId 댓글 알람이 아닐 경우 값은 0 이다.
 */
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
      clickBoardEventByBoardId(data.boardId);
      // 게시물 목록에서 게시글 클릭할 때의 이벤트 재사용
      getPostDataAfterPostClick(data.postId, data.boardId);
      showCommentAlarmContent(commentId);
    },
    complete: function () {
      getUnreadAlarmCount();
    }
  });
}

// 알람을 클릭하면 해당 알람에 읽음 표시를 한다.
function readMarkToAlarmItem(alarmId) {
  $.ajax({
    type: 'PUT',
    url: `/alarms/${alarmId}`,
    error: function (xhr) {
      errorFunction(xhr);
    }
  });
}