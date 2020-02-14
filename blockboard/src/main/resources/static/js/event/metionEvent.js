/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file metionEvent.java
 */
$(document).on('click', '.mentions_tag', function () {
  let userID = $(this).attr("data-id");
  console.log(userID);
});