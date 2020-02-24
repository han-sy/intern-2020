/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.common.constant.ConstantData.RangeSize;
import com.board.project.blockboard.common.util.LengthCheckUtils;
import com.board.project.blockboard.common.validation.PostValidation;
import com.board.project.blockboard.dto.PaginationDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.PostMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostService {

  @Autowired
  private ViewRecordService viewRecordService;
  @Autowired
  private PostMapper postMapper;
  @Autowired
  private AlarmService alarmService;

  public int insertPost(PostDTO receivePost, HttpServletRequest request) {
    LengthCheckUtils.validatePostData(receivePost);
    int receivedPostID = receivePost.getPostID();
    setPostDataFromRequest(receivePost, request);
    // [임시보관함]의 게시글에서 '저장'or'임시저장' 버튼을 눌렀을 때 검증
    if (receivedPostID != 0) {
      PostDTO receivePostInDatabase = postMapper.selectPostByPostID(receivedPostID);
      PostValidation.validateTempPost(receivePostInDatabase);
    }
    postMapper.insertPost(receivePost);
    // 일반 게시물에 삽입이 되면 태그된 유저 알람에 등록
    if (StringUtils.equals(receivePost.getPostStatus(), "normal")) {
      alarmService.insertAlarm(receivePost);
    }
    return receivePost.getPostID();
  }

  private void setPostDataFromRequest(PostDTO receivePost, HttpServletRequest request) {
    receivePost.setUserID(request.getAttribute("userID").toString());
    receivePost.setUserName(request.getAttribute("userName").toString());
    receivePost.setCompanyID(Integer.parseInt(request.getAttribute("companyID").toString()));
    receivePost.setPostContentExceptHTMLTag(Jsoup.parse(receivePost.getPostContent()).text());
  }

  public void deletePost(int postID, HttpServletRequest request) {
    PostDTO post = postMapper.selectPostByPostID(postID);
    UserDTO user = new UserDTO(request);
    PostValidation.validateDelete(post, user);

    // 휴지통에 있는 게시글이면 바로삭제하고, 아니면 휴지통으로 보낸다.
    if (StringUtils.equals(post.getPostStatus(), "recycle")) {
      postMapper.deletePostByPostID(postID);
    } else {
      postMapper.temporaryDeletePost(post);
    }
  }

  public void updatePost(PostDTO newPost, int postID, HttpServletRequest request) {
    UserDTO user = new UserDTO(request);
    PostDTO oldPost = postMapper.selectPostByPostID(postID);

    LengthCheckUtils.validatePostData(newPost);
    PostValidation.isValidChange(oldPost, user);
    updateOldPostFromNewPost(oldPost, newPost);
    postMapper.updatePost(oldPost);
  }

  private void updateOldPostFromNewPost(PostDTO oldPost, PostDTO newPost) {
    oldPost.setBoardID(newPost.getBoardID());
    oldPost.setPostTitle(newPost.getPostTitle());
    oldPost.setPostContent(newPost.getPostContent());
    oldPost.setPostContentExceptHTMLTag(Jsoup.parse(newPost.getPostContent()).text());
  }

  public List<PostDTO> searchPost(String option, String keyword) {
    PostValidation.isValidSearch(option, keyword);
    return postMapper.searchPost(option, keyword);
  }

  public void restorePost(int postID, HttpServletRequest request) {
    UserDTO user = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostID(postID);
    PostValidation.isValidRestore(post, user);
    postMapper.restorePost(post);
  }

  public PostDTO selectPostByAlarmId(int alarmId) {
    PostDTO post = postMapper.selectPostByAlarmId(alarmId);
    PostValidation.isExistPost(post);
    return post;
  }

  public List<PostDTO> selectMyPosts(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getMyPostsCount(user);
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectMyPosts(map);
  }

  public List<PostDTO> selectPostsIncludeMyReplies(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getPostsCountIncludeMyReplies(user);
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectMyPostsIncludeMyReplies(map);
  }

  public List<PostDTO> selectMyTempPosts(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getMyTempPostsCount(user);
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectMyTempPosts(map);
  }

  public List<PostDTO> getMyRecyclePosts(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getMyRecyclePostsCount(user);
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectMyRecyclePosts(map);
  }

  public List<PostDTO> selectRecentPosts(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getRecentPostsCount(user.getCompanyID());
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectRecentPosts(map);
  }

  public int getMyPostsCount(UserDTO user) {
    return postMapper.getMyPostsCount(user);
  }

  public int getMyRepliesCount(UserDTO user) {
    return postMapper.getPostsCountIncludeMyReplies(user);
  }

  public int getMyTempPostsCount(UserDTO user) {
    return postMapper.getMyTempPostsCount(user);
  }

  public int getMyRecyclePostsCount(UserDTO user) {
    return postMapper.getMyRecyclePostsCount(user);
  }

  public int getRecentPostsCount(int companyID) {
    return postMapper.getRecentPostsCount(companyID);
  }

  public Map<String, Object> makeMapUserAndPageInfo(UserDTO user, int pageCount, int pageNumber) {
    Map<String, Object> map = new HashMap<>();
    PaginationDTO pageInfo = new PaginationDTO("posts",pageCount, pageNumber, PageSize.POST,
        RangeSize.POST);
    map.put("user", user);
    map.put("startIndex", pageInfo.getStartIndex());
    map.put("pageSize", PageSize.POST);
    return map;
  }

  /**
   * 게시글 목록 조회와 조회수 증가
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public PostDTO selectPostByPostID(int postID, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostID(postID);
    PostValidation.isExistPost(post);
    if (!viewRecordService.isReadPostByUser(userData.getUserID(), postID)) {//안읽은경우
      updateViewCnt(postID, request);//조회수 업데이트
      post.setViewCount(post.getViewCount() + 1);//반환도 1증가
    }
    return post;
  }

  /**
   * 게시판 번호와 페이지번호를 가지고 출력할 게시물 목록 반환
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public List<PostDTO> getPostListByBoardID(int boardID, int pageNumber, int companyID) {
    int pageCount = getPostsCountByBoardID(boardID);
    PaginationDTO pageInfo = new PaginationDTO("posts",pageCount, pageNumber, PageSize.POST,
        RangeSize.POST);
    List<PostDTO> postList = postMapper
        .selectPostByBoardID(boardID, pageInfo.getStartIndex(), PageSize.POST);

    return postList;
  }

  /**
   * 게시물 출력에 필요한 정보를 추가한다. 댓글수
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  private List<PostDTO> updatePostInfo(List<PostDTO> postList, int companyID) {

    return postList;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public PostDTO getPostByPostID(int postID) {
    PostDTO post = postMapper.selectPostByPostID(postID);
    return post;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getPostsCountByBoardID(int boardID) {
    int postCounts = postMapper.selectPostCountByBoardID(boardID);
    return postCounts;
  }

  /**
   * 조회수 증가 알고리즘 조회시 5분동안은 조회수 증가불가
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  //TODO 휴지통인경우 임시저장함인경우는 따로 구분해서 조회수 증가 안되도록 해야됨. 1번방법 : 임시저장이나 휴지통인 경우 제외 ,2번방법 : 작성자 조회수증가에서 제외.
  //TODO 카운트는 비동기로 트랜잭션 처리보다야
  public void updateViewCnt(int postID, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    viewRecordService.readPostByUser(userData.getUserID(), postID);
    postMapper.updateViewCnt(postID);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public List<PostDTO> getPopularPostList(int companyID) {
    List<PostDTO> postList = postMapper.selectPopularPostListByCompanyID(companyID);
    for (PostDTO post : postList) {
      post.setIsPopular(true);
    }
    return postList;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getPopularPostsCount(int companyID) {
    return postMapper.getPopularPostsCount(companyID);
  }

  
  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @Async
  public void updateCommentCountPlus1(int postID) {
    postMapper.updateCommentCountPlus1(postID);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @Async
  public void updateCommentCountMinus1(int postID){
    postMapper.updateCommentCountMinus1(postID);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getPostIDByCommentID(int commentID) {
    int postID = postMapper.selectPostIDByCommentID(commentID);
    return postID;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getCommentsCountByPostID(int postID) {
    return postMapper.selectCommentsCountByPostID(postID);
  }
}

