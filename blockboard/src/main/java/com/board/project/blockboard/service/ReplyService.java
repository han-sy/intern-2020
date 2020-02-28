package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.common.util.JsoupUtils;
import com.board.project.blockboard.common.util.LengthCheckUtils;
import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.mapper.ReplyMapper;
import com.board.project.blockboard.mapper.UserMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
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
  public List<CommentDTO> getReplyListByCommentId(int commentReferencedId, int startIndex) {
    return replyMapper.selectRepliesByCommentId(commentReferencedId, startIndex, PageSize.REPLY);
  }

  /**
   * 답글 insert
   */
  public int writeReplyWithUserInfo(String userId, int companyId, CommentDTO replyData) {
    LengthCheckUtils.validCommentData(replyData);
    updateReplyData(userId, companyId, replyData);
    replyMapper.insertNewReplyByCommentInfo(replyData);
    commentService.updateRepliesCountPlus1(replyData.getCommentReferencedId());
    alarmService.insertAlarm(replyData);
    return replyData.getCommentId();
  }

  private void updateReplyData(String userId, int companyId, CommentDTO replyData) {
    String commentContent = replyData.getCommentContent();
    LengthCheckUtils.validCommentData(replyData);
    replyData.setUserId(userId);
    replyData.setCommentContent(StringEscapeUtils.escapeHtml4(commentContent));
    replyData.setCommentContentUnescapeHtml(JsoupUtils.unescapeHtmlFromStringOfFilteringXSS(JsoupUtils.filterStringForXSS(commentContent)));
    replyData.setCompanyId(companyId);
    replyData.setUserName(userMapper.selectUserNameByUserId(userId));
  }

}
