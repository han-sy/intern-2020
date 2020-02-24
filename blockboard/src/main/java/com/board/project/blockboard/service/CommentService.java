/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.FunctionID;
import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.common.constant.ConstantData.RangeSize;
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
import org.springframework.scheduling.annotation.Async;
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


  public List<CommentDTO> getCommentListByPostID(int postID,int pageNumber) {
    int pageCount = postService.getCommentsCountByPostID(postID);
    PaginationDTO pageInfo = new PaginationDTO("comments",pageCount,pageNumber,
        PageSize.COMMENT, RangeSize.COMMENT);
    return commentMapper.selectCommentsByPostID(postID,pageInfo.getStartIndex(),PageSize.COMMENT);
  }

  //TODO 카운트는 비동기로 트랜잭션 처리보다야
  public int writeCommentWithUserInfo(CommentDTO commentData, String userID, int companyID) {
    updateCommentData(commentData, userID, companyID);

    commentMapper.insertNewCommentByCommentInfo(commentData);
    postService.updateCommentCountPlus1(commentData.getPostID());
    alarmService.insertAlarm(commentData);
    return commentData.getCommentID();
  }



  //TODO 카운트는 비동기로 트랜잭션 처리보다야
  //
  public void deleteComment(int commentID) {
    int postID = postService.getPostIDByCommentID(commentID);
    Integer commentReferencedID = commentMapper.selectCommentReferencedIDByCommentID(commentID);
    commentMapper.deleteCommentByCommentReferencedID(commentID);
    commentMapper.deleteCommentByCommentID(commentID);
    updateCountMinus1(postID, commentReferencedID);

  }

  @Async
  public void updateCountMinus1(int postID, Integer commentReferencedID) {
    if(commentReferencedID!=null){//답글일때
      updateRepliesCountMinus1(commentReferencedID);
    } else{//댓글일때
      postService.updateCommentCountMinus1(postID);
    }
  }

  public void updateComment(CommentDTO commentData) {
    commentData.setCommentContentExceptHTMLTag( Jsoup.parse(commentData.getCommentContent()).text());
    commentMapper.updateComment(commentData);
  }

  public void updateRepliesCountPlus1(int commentID) {
    commentMapper.updateRepliesCountPlus1(commentID);
  }
  private void updateRepliesCountMinus1(int commentReferencedID) {
    commentMapper.updateRepliesCountMinus1(commentReferencedID);
  }


  public int getRepliesCountByCommentReferencedID(int commentReferencedID) {
    Integer count = commentMapper.selectRepliesCountByCommentReferencedID(commentReferencedID);
    if (count == null)
      return 0;
    return count;
  }

  public CommentDTO selectCommentByCommentId(int commentId) {
    return commentMapper.selectCommentByCommentId(commentId);
  }

  private void updateCommentData(CommentDTO commentData, String userID, int companyID) {
    log.info("getCommentContent : "+commentData.getCommentContent());
    commentData.setCommentContentExceptHTMLTag(Jsoup.parse(commentData.getCommentContent()).text());
    commentData.setUserName(userMapper.selectUserNameByUserID(userID));
    commentData.setUserID(userID);
    commentData.setCompanyID(companyID);
  }
}
