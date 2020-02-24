/**
 * @author  @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    commentData.js
 */

/**
 * commentReferenceID 반환
 * @returns
 */
function getCommentReferencedIdInReferenceCommentContainer() {
  return $(this).closest(".referenceCommentContainer").attr(
      "data-id");
}

/**
 * comment Container에서 commentId 반환
 */
function getCommentIdInCommentContainer() {
  return $(this).closest(".commentContainer").attr("id").substring(COMMENT_PREFIX.length);
}

/**
 * 참조한 유저이름 반환
 */
function getReferenceUserName() {
  return $(this).closest(".commentContainer").find(
      ".name").html();
}

/**
 * 참조한 유저아이디 반환
 */
function getReferenceUserId() {
  return $(this).closest(".commentContainer").find(".name").attr(
      "data-id");
}

/**
 * replyinputContainerID (실제 태그문의 id로 사용될 id)반환
 */
function getReplyInputContainerID(referenceCommentContainer) {
  return $('#reply_input_container' + referenceCommentContainer.attr(
      "data-id")).attr("id");
}

/**
 * refereceCommentContainer반환
 */
function getReferenceCommentContainer() {
  return $(this).closest(".referenceCommentContainer");
}

/**
 * 댓글의 fileContainer 반환
 */
function getFileContainerInLocalCommentContainer() {
  return $(this).closest(".localCommentContainer").find(
      ".attached_file_list_container_comment");
}

/**
 * 출력된 답글개수
 */
function getCountPrintedReplies() {
  return $(".replyItem").length;
}


class Comment{
  constructor(boardId,postId,commentId,commentReferencedId,commentContent) {
    this.boardId = boardId;
    this.postId = postId;
    this.commentReferencedId = commentReferencedId;
    this.commentContent = commentContent;
  }

}