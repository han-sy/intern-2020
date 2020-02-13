package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.mapper.ReplyMapper;
import com.board.project.blockboard.mapper.UserMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ReplyService.java
 */

@Slf4j
@Service
public class ReplyService {

  @Autowired
  private ReplyMapper replyMapper;
  @Autowired
  private UserMapper userMapper;

  /**
   * 댓글 id 를 통해 답글 리스트 get
   */
  public List<CommentDTO> getReplyListByCommentID(int commentReferencedID) {
    return replyMapper.selectRepliesByCommentID(commentReferencedID);
  }

  /**
   * 답글 insert
   */
  public int writeReplyWithUserInfo(String userID, int companyID, int postID,
      String commentContent, int commentReferencedID, String commentReferencedUserID) {
    CommentDTO reply = new CommentDTO();
    reply.setUserID(userID);
    reply.setCommentContent(commentContent);
    reply.setCompanyID(companyID);
    reply.setPostID(postID);
    reply.setUserName(userMapper.selectUserNameByUserID(userID));
    reply.setCommentReferencedID(commentReferencedID);
    reply.setCommentReferencedUserID(commentReferencedUserID);
    reply.setCommentReferencedUserName(userMapper.selectUserNameByUserID(commentReferencedUserID));
    replyMapper.insertNewReplyByCommentInfo(reply);
    return reply.getCommentID();
  }
}
