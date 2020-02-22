/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    postData.js
 */

function getPostIDInPost() {
  console.log("getPostIDInPost");
  return $("#postID").html();
}

function getPostIDInPostList() {
  return $(this).attr("data-post");
}