/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.service.PostService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/boards/{boardId}/posts/{postId}/comments")
public class CommentController {

  @Autowired
  private PostService postService;
  @Autowired
  private CommentService commentService;

  /**
   * postId 일치하는 댓글 목록 리턴 ( 대댓글은 반환하지 않는다.)
   */
  @GetMapping("")
  public List<CommentDTO> getCommentsByPost(@PathVariable int postId,@RequestParam int pageNumber) {
    List<CommentDTO> commentList = commentService.getCommentListByPostId(postId,pageNumber);
    return commentList;
  }

  /**
   * 댓글 추가
   */
  @PostMapping("")
  public int insertComment(@RequestBody CommentDTO commentData, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    return commentService
        .writeCommentWithUserInfo(commentData,userData.getUserId(), userData.getCompanyId());
  }

  /**
   * 게시물 별로 댓글 개수를 반환한다.
   *
   * @param postId 게시물의 ID
   * @return 댓글개수를 반환
   */
  @GetMapping("/counts")
  public int getCommentsCountsByPostId(@PathVariable int postId) {
    int commentCount = postService.getCommentsCountByPostId(postId);
    return commentCount;
  }


  /**
   * 댓글 삭제
   */
  @DeleteMapping("/{commentId}")
  public void deleteComment(@RequestParam int commentId) {
    commentService.deleteComment(commentId);
  }

  /**
   * 댓글 수정하기
   *
   */
  @PutMapping("/{commentId}")
  public void editComment(@RequestBody CommentDTO commentData) {
    commentService.updateComment(commentData);
  }

  @GetMapping("/{commentId}")
  public CommentDTO selectCommentByCommentId(@PathVariable int commentId) {
    return commentService.selectCommentByCommentId(commentId);
  }
}
