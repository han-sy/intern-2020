/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.service.JwtService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/boards/{boardid}/posts/{postid}/comments")
public class CommentController {

  @Autowired
  private CommentService commentService;
  @Autowired
  private JwtService jwtService;

  /**
   * postID 일치하는 댓글 목록 리턴 ( 대댓글은 반환하지 않는다.)
   *
   * @param postID
   * @return
   */
  @GetMapping("")
  public List<CommentDTO> getCommentsByPost(@PathVariable("postid") int postID) {
    List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);
    return commentList;
  }

  @GetMapping("/counts")
  public int getCommentsCountSByPostID(@PathVariable("postid") int postID) {
    int commentCount = commentService.getCommentCountByPostID(postID);
    return commentCount;
  }

  /**
   * 댓글 추가
   *
   * @param postID
   * @param commentContent
   */
  @PostMapping("")
  public void writeComment(@RequestParam("postID") int postID,
      @RequestParam("commentContent") String commentContent) {
    Map<String, Object> userInfo = jwtService.getBody();
    String userID = userInfo.get("userID").toString();
    int companyID = Integer.parseInt(userInfo.get("companyID").toString());
    commentService.writeCommentWithUserInfo(userID, commentContent, companyID, postID);
  }

  /**
   * 댓글 삭제
   *
   * @param commentID
   * @param postID
   */
  @DeleteMapping("/{commentid}")
  public void deleteComment(@PathVariable("commentid") int commentID,
      @PathVariable("postid") int postID) {
    commentService.deleteComment(commentID);
  }

  /**
   * 댓글 수정하기
   *
   * @param commentID
   * @param postID
   * @param boardID
   * @param newComment 변경된 새로운 내용
   */
  @PutMapping("/{commentid}")
  public void editComment(@PathVariable("commentid") int commentID,
      @PathVariable("postid") int postID, @PathVariable("boardid") int boardID,
      @RequestParam("newComment") String newComment) {
    commentService.updateComment(commentID, newComment);
  }
}
