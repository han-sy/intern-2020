/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    boardData.js
 */

function getBoardIdInPost() {
  return $('#boardIdInPost').html();
}

function getBoardIdInPostList() {
  return $(this).attr("data-board");
}
