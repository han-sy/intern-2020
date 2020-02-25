/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file modalEvent.java
 */


//모달창 우선순위 제어
var count = 0;
$(document).on('show.bs.modal', '.modal', function () {
  let zIndex = 1040 + (10 * count);
  $(this).css('z-index', zIndex);
  setTimeout(function () {
    $('.modal-backdrop').not('.modal-stack').css('z-index',
        zIndex - 1).addClass('modal-stack');
  }, 0);
  count = count + 1;
});
