/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    boardData.js
 */

function getBoardIdInPost() {
  return $('#boardIdInPost').html();
}

function getBoardIdInPostList() {
  return $(this).attr("data-board");
}

function isCheckedBoardDelete() {
  return $(this).is(":checked");
}

function getCheckedBoardId() {
  return $(this).val();
}

function getCheckedBoardName() {
  return $(this).attr("data-boardName");
}

class Boards{
  constructor(boardId,boardName) {
    this.boardId = boardId;
    this.boardName = boardName;
  }
}

function getOldBoardName() {
  return $(this).attr("data-oldname");
}

function getNewBoardName() {
  return $(this).val();
}