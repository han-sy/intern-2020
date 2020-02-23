/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postData.js
 */

function getPostIDInPost() {
  return $("#postID").html();
}

function getPostIDInPostList() {
  return $(this).attr("data-post");
}