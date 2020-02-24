/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postData.js
 */

function getPostIdInPost() {
  return $("#postId").html();
}

function getPostIdInPostList() {
  return $(this).attr("data-post");
}

function getPostIdInEditor() {
 return $("#postIdInEditor").html();
}

function getOriginalBoardIdInEditor() {
  return $("#boardIdInEditor").html();
}

function getSelectedBoardIdInEditor() {
  return $('#selectableBoardIdInEditor option:selected').attr('data-tab');
}

function getPostTitleInEditor() {
  return $('#post_title').val();
}