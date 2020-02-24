/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file PaginationService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.common.constant.ConstantData.RangeSize;
import com.board.project.blockboard.dto.PaginationDTO;
import com.board.project.blockboard.dto.UserDTO;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaginationService {

  @Autowired
  private PostService postService;
  @Autowired
  private CommentService commentService;

  public PaginationDTO getPostPageListByPageNumberAboutBoard(int pageNumber, int boardID,
      UserDTO user) {
    int postCount;
    switch (boardID) {
      case ConstantData.BOARD_MY_POSTS:
        postCount = postService.getMyPostsCount(user);
        break;
      case ConstantData.BOARD_MY_REPLIES:
        postCount = postService.getMyRepliesCount(user);
        break;
      case ConstantData.BOARD_TEMP:
        postCount = postService.getMyTempPostsCount(user);
        break;
      case ConstantData.BOARD_RECYCLE:
        postCount = postService.getMyRecyclePostsCount(user);
        break;
      case ConstantData.BOARD_RECENT:
        postCount = postService.getRecentPostsCount(user.getCompanyID());
        break;
      case ConstantData.BOARD_POPULAR:
        postCount = postService.getPopularPostsCount(user.getCompanyID());
        if (postCount >= PageSize.POST) {
          postCount = PageSize.POST;
        }
        break;
      default:
        postCount = postService.getPostsCountByBoardID(boardID);
    }

    PaginationDTO paginationInfo = new PaginationDTO("posts", postCount, pageNumber, PageSize.POST,
        RangeSize.POST);
    paginationInfo.rangeSetting(pageNumber);
    return paginationInfo;
  }

  public PaginationDTO getCommentPageListByPageNumberAboutPost(int pageNumber, int postID,
      UserDTO user) {
    int commentCount = postService.getCommentsCountByPostID(postID);
    PaginationDTO paginationInfo = new PaginationDTO("comments", commentCount, pageNumber,
        PageSize.COMMENT, RangeSize.COMMENT);
    paginationInfo.rangeSetting(pageNumber);
    return paginationInfo;
  }

  public PaginationDTO getPageList(int pageNumber, int boardID, int postID, UserDTO user) {
    if (isBoardPage(boardID)) {
      return getPostPageListByPageNumberAboutBoard(pageNumber, boardID, user);
    } else {
      return getCommentPageListByPageNumberAboutPost(pageNumber, postID, user);
    }
  }

  public PaginationDTO getSearchPageList(int pageNumber, String keyword, String option,
      HttpServletRequest request) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("keyword", keyword);
    attributes.put("option", option);
    attributes.put("companyId", request.getAttribute("companyID"));
    int searchPostCount = postService.getSearchPostCount(attributes);
    PaginationDTO paginationInfo = new PaginationDTO("search", searchPostCount, pageNumber,
        PageSize.POST, RangeSize.POST);
    paginationInfo.rangeSetting(pageNumber);
    return paginationInfo;
  }

  public boolean isBoardPage(int boardID) {
    return boardID != 0;
  }
}
