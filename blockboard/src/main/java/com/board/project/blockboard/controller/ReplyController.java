/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ReplyController.java
 */
package com.board.project.blockboard.controller;


import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.service.ReplyService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/boards/{boardid}/posts/{postid}/comments/{commentid}/replies")
public class ReplyController {

  @Autowired
  private ReplyService replyService;
  @Autowired
  private CommentService commentService;

  /**
   * 답글 조회
   */
  @GetMapping("")
  public List<CommentDTO> getRepliesByComment(@PathVariable("postid") int postID,
      @PathVariable("commentid") int commentReferencedID, @RequestParam int startIndex,HttpServletRequest request) {
    List<CommentDTO> replyList = replyService.getReplyListByCommentID(commentReferencedID,startIndex);
    return replyList;
  }
  /**
   * 답글수 조회
   */
  @GetMapping("/counts")
  public int getRepliesByComment(@PathVariable("postid") int postID,
      @PathVariable("commentid") int commentReferencedID) {
    int repliesCount = commentService.getRepliesCountByCommentReferencedID(commentReferencedID);
    return repliesCount;
  }

  /**
   * 답글 추가
   */
  @PostMapping("")
  public int insertReply(@RequestBody CommentDTO replyData,
      HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    return replyService
        .writeReplyWithUserInfo(userData.getUserID(), userData.getCompanyID(), replyData);
  }
}
