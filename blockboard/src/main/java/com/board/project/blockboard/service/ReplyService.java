package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.common.util.TagCheckUtils;
import com.board.project.blockboard.dto.AlarmDTO;
import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.mapper.AlarmMapper;
import com.board.project.blockboard.mapper.ReplyMapper;
import com.board.project.blockboard.mapper.UserMapper;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
  private CommentService commentService;
  @Autowired
  private ReplyMapper replyMapper;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private AlarmService alarmService;

  /**
   * 댓글 id 를 통해 답글 리스트 get
   */
  public List<CommentDTO> getReplyListByCommentID(int commentReferencedID,int startIndex) {
    return replyMapper.selectRepliesByCommentID(commentReferencedID,startIndex, PageSize.REPLY);
  }

  /**
   * 답글 insert
   */
  public int writeReplyWithUserInfo(String userID, int companyID, CommentDTO replyData) {
    updateReplyData(userID, companyID, replyData);
    replyMapper.insertNewReplyByCommentInfo(replyData);
    commentService.updateRepliesCountPlus1(replyData.getCommentReferencedID());
    alarmService.insertAlarm(replyData);
    return replyData.getCommentID();
  }

  private void updateReplyData(String userID, int companyID, CommentDTO replyData) {
    replyData.setUserID(userID);
    replyData.setCommentContentExceptHTMLTag(Jsoup.parse(replyData.getCommentContent()).text());
    replyData.setCompanyID(companyID);
    replyData.setUserName(userMapper.selectUserNameByUserID(userID));
  }

}
