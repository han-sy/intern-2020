/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData.FunctionID;
import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.mapper.CommentMapper;
import com.board.project.blockboard.mapper.UserMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommentService {

  @Autowired
  private FunctionService functionService;
  @Autowired
  private AlarmService alarmService;
  @Autowired
  private CommentMapper commentMapper;
  @Autowired
  private UserMapper userMapper;


  public List<CommentDTO> getCommentListByPostID(int postID) {
    return commentMapper.selectCommentsByPostID(postID);
  }

  public int writeCommentWithUserInfo(String userID, String commentContent, int companyID,
      int postID) {
    CommentDTO comment = new CommentDTO();
    comment.setCommentContent(commentContent);
    comment.setCommentContentExceptHTMLTag(Jsoup.parse(commentContent).text());
    comment.setUserName(userMapper.selectUserNameByUserID(userID));
    comment.setPostID(postID);
    comment.setUserID(userID);
    comment.setCompanyID(companyID);
    commentMapper.insertNewCommentByCommentInfo(comment);
    alarmService.insertAlarm(comment);
    return comment.getCommentID();
  }

  public void deleteComment(int commentID) {
    commentMapper.deleteCommentByCommentReferencedID(commentID);
    commentMapper.deleteCommentByCommentID(commentID);
  }

  public void updateComment(int commentID, String newComment) {
    Map<String, Object> commentAttribute = new HashMap<String, Object>();
    commentAttribute.put("commentID", commentID);
    commentAttribute.put("commentContent", newComment);
    commentAttribute.put("commentContentExceptHTMLTag", Jsoup.parse(newComment).text());
    commentMapper.updateComment(commentAttribute);
  }


  public int getCommentCountByPostID(int postID, int companyID) {
    boolean isCommentOn = functionService.getFunctionStatus(companyID, FunctionID.COMMENT);
    boolean isReplyOn = functionService.getFunctionStatus(companyID, FunctionID.REPLY);
    if (isCommentOn) {//댓글ON
      if (isReplyOn) {//답글ON
        return commentMapper.getAllCommentsCountByPostID(postID);
      } else {//답글 OFF
        return commentMapper.getOnlyCommentsCountByPostID(postID);
      }
    }
    return 0;//댓글OFF
  }

  public CommentDTO selectCommentByCommentId(int commentId) {
    return commentMapper.selectCommentByCommentId(commentId);
  }
}
