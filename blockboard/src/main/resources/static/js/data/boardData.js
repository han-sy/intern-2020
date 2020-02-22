/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    boardData.js
 */

function getBoardIDInPost() {
  console.log("getBoardIDInPost");
  return $('#boardIdInPost').html();
}

function getBoardIDInPostList() {
  return $(this).attr("data-board");
}
