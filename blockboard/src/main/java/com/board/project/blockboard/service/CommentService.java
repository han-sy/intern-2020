/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.FunctionID;
import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.PaginationDTO;
import com.board.project.blockboard.mapper.CommentMapper;
import com.board.project.blockboard.mapper.UserMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Autowired PostService postService;


  public List<CommentDTO> getCommentListByPostID(int postID,int pageNumber,int companyID) {
    int pageCount = postService.getCommentsCountByPostID(postID);
    PaginationDTO pageInfo = new PaginationDTO("comments",pageCount,pageNumber,
        ConstantData.COMMENT_PAGE_SIZE,ConstantData.COMMENT_RANGE_SIZE);
    return commentMapper.selectCommentsByPostID(postID,pageInfo.getStartIndex(),ConstantData.COMMENT_PAGE_SIZE);
  }

  //TODO 카운트는 비동기로 트랜잭션 처리보다야
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
    postService.updateCommentCountPlus1(postID);
    alarmService.insertAlarm(comment);
    return comment.getCommentID();
  }

  //TODO 카운트는 비동기로 트랜잭션 처리보다야
  public void deleteComment(int commentID) {
    int postID = postService.getPostIDByCommentID(commentID);
    Integer commentReferencedID = commentMapper.selectCommentReferencedIDByCommentID(commentID);
    commentMapper.deleteCommentByCommentReferencedID(commentID);
    commentMapper.deleteCommentByCommentID(commentID);
    if(commentReferencedID!=null){//답글일때

      updateRepliesCountMinus1(commentReferencedID);
    } else{//댓글일때
      postService.updateCommentCountMinus1(postID);
    }

  }

  public void updateComment(int commentID, String newComment) {
    Map<String, Object> commentAttribute = new HashMap<String, Object>();
    commentAttribute.put("commentID", commentID);
    commentAttribute.put("commentContent", newComment);
    commentAttribute.put("commentContentExceptHTMLTag", Jsoup.parse(newComment).text());
    commentMapper.updateComment(commentAttribute);
  }

  public void updateRepliesCountPlus1(int commentID) {
    commentMapper.updateRepliesCountPlus1(commentID);
  }
  private void updateRepliesCountMinus1(int commentReferencedID) {
    commentMapper.updateRepliesCountMinus1(commentReferencedID);
  }


  public int getRepliesCountByCommentReferencedID(int commentReferencedID) {
    Integer count = commentMapper.selectRepliesCountByCommentReferencedID(commentReferencedID);
    if(count==null)
      return 0;
    return count;

  public int getCommentCountByPostID(int postID, int companyID) {
    boolean isCommentOn = functionService.isUseFunction(companyID, FunctionID.COMMENT);
    boolean isReplyOn = functionService.isUseFunction(companyID, FunctionID.REPLY);
    if (isCommentOn) {//댓글ON
      if (isReplyOn) {//답글ON
        return commentMapper.getAllCommentsCountByPostID(postID);
      } else {//답글 OFF
        return commentMapper.getOnlyCommentsCountByPostID(postID);
      }
    }
    return 0;//댓글OFF
  }
}
