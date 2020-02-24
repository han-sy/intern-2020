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
  getUnreadAlarmCount();
});

$(document).on('click', 'li.alarm-item', function () {
  let alarmId = parseInt($(this)[0].dataset.id);
  $(this).addClass("alarm-read");
  postClear();
  clearEditor();
  getAlarmByAlarmId(alarmId);
});

$(document).on('click', '.btn-alarm-delete-all', function () {
  let alarmItem = $(this).closest("li")[0];
  if (!$(alarmItem).hasClass("alarm-read") &&
      confirm("확인하지 않은 알람이 있습니다. 모두 삭제하시겠습니까?") === false) {
    return;
  }
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

function showCommentAlarmContent(commentId) {
  if (commentId == 0) {
    return;
  }

  setTimeout(function () {
    let offset = 0;
    $('.referenceCommentContainer').each(function (index, item) {
      if (item.dataset.id == commentId) {
        offset = $(item).offset().top;
        $('html, body').animate({
          scrollTop: offset
        }, 500);
        return false;
      }
    });
    if (offset === 0) { // 현재 페이지에 없어 댓글 모달창을 띄웁니다.
      getCommentForShowModal(commentId);
    }
  }, 100);
}

$('#alarm-content').scroll(function () {
  let scroll_position = $(this).scrollTop();
  let bottom_position = $(this)[0].scrollHeight - $(this).height();
  if (scroll_position === bottom_position) {
    hasMoreAlarmItems();
  }
});

function hasMoreAlarmItems() {
  let currentAlarmCount = $('#alarm-content').children('li').length;
  let currentPageNumber = currentAlarmCount / ALARM_COUNT_PER_PAGE;
  if (currentPageNumber - parseInt(currentPageNumber) !== 0) {
    return;
  }
  getAlarms(currentPageNumber + 1);
}