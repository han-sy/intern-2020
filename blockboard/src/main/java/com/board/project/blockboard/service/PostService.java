/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.common.constant.ConstantData.PostStatus;
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
    int receivedPostId = receivePost.getPostId();
    setPostDataFromRequest(receivePost, request);
    // [임시보관함]의 게시글에서 '저장'or'임시저장' 버튼을 눌렀을 때 검증
    if (receivedPostId != 0) {
      PostDTO receivePostInDatabase = postMapper.selectPostByPostId(receivedPostId);
      PostValidation.validateTempPost(receivePostInDatabase);
    }
    postMapper.insertPost(receivePost);
    // 일반 게시물에 삽입이 되면 태그된 유저 알람에 등록
    if (StringUtils.equals(receivePost.getPostStatus(), PostStatus.NORMAL)) {
      alarmService.insertAlarm(receivePost);
    }
    return receivePost.getPostId();
  }

  private void setPostDataFromRequest(PostDTO receivePost, HttpServletRequest request) {
    receivePost.setUserId(request.getAttribute("userId").toString());
    receivePost.setUserName(request.getAttribute("userName").toString());
    receivePost.setCompanyId(Integer.parseInt(request.getAttribute("companyId").toString()));
    receivePost.setPostContentExceptHTMLTag(Jsoup.parse(receivePost.getPostContent()).text());
  }

  public void deletePost(int postId, HttpServletRequest request) {
    PostDTO post = postMapper.selectPostByPostId(postId);
    UserDTO user = new UserDTO(request);
    PostValidation.validateDelete(post, user);

    // 휴지통에 있는 게시글이면 바로삭제하고, 아니면 휴지통으로 보낸다.
    if (StringUtils.equals(post.getPostStatus(), PostStatus.RECYCLE)) {
      postMapper.deletePostByPostId(postId);
    } else {
      postMapper.temporaryDeletePost(post);
    }
  }

  public void updatePost(PostDTO newPost, int postId, HttpServletRequest request) {
    UserDTO user = new UserDTO(request);
    PostDTO oldPost = postMapper.selectPostByPostId(postId);

    LengthCheckUtils.validatePostData(newPost);
    PostValidation.isValidChange(oldPost, user);
    updateOldPostFromNewPost(oldPost, newPost);
    postMapper.updatePost(oldPost);
  }

  private void updateOldPostFromNewPost(PostDTO oldPost, PostDTO newPost) {
    oldPost.setBoardId(newPost.getBoardId());
    oldPost.setPostTitle(newPost.getPostTitle());
    oldPost.setPostContent(newPost.getPostContent());
    oldPost.setPostContentExceptHTMLTag(Jsoup.parse(newPost.getPostContent()).text());
  }

  public List<PostDTO> searchPost(String option, String keyword, int pageNumber) {
    PostValidation.isValidSearch(option, keyword);
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("option", option);
    attributes.put("keyword", keyword);
    attributes.put("startIndex", (pageNumber - 1) * PageSize.POST);
    attributes.put("pageSize", PageSize.POST);
    return postMapper.searchPost(attributes);
  }

  public void restorePost(int postId, HttpServletRequest request) {
    UserDTO user = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostId(postId);
    PostValidation.isValidRestore(post, user);
    postMapper.restorePost(post);
  }

  public PostDTO selectPostByAlarmId(int alarmId) {
    PostDTO post = postMapper.selectPostByAlarmId(alarmId);
    PostValidation.isExistPost(post);
    return post;
  }

  public List<PostDTO> selectMyPosts(UserDTO user, int pageNumber) {
    Map<String, Object> attributes = makeMapUserAndPostStatus(user, PostStatus.NORMAL);
    int pageCount = postMapper.getMyPostsCountByPostStatus(attributes);
    attributes = makeMapUserAndPageInfoPostStatus(user, pageCount, pageNumber, PostStatus.NORMAL);
    return postMapper.selectMyPostsByPostStatus(attributes);
  }

  public List<PostDTO> selectPostsIncludeMyReplies(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getPostsCountIncludeMyReplies(user);
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectMyPostsIncludeMyReplies(map);
  }

  public List<PostDTO> selectMyTempPosts(UserDTO user, int pageNumber) {
    Map<String, Object> attributes = makeMapUserAndPostStatus(user, PostStatus.TEMP);
    int pageCount = postMapper.getMyPostsCountByPostStatus(attributes);
    attributes = makeMapUserAndPageInfoPostStatus(user, pageCount, pageNumber, PostStatus.TEMP);
    return postMapper.selectMyPostsByPostStatus(attributes);
  }

  public List<PostDTO> getMyRecyclePosts(UserDTO user, int pageNumber) {
    Map<String, Object> attributes = makeMapUserAndPostStatus(user, PostStatus.RECYCLE);
    int pageCount = postMapper.getMyPostsCountByPostStatus(attributes);
    attributes = makeMapUserAndPageInfoPostStatus(user, pageCount, pageNumber, PostStatus.RECYCLE);
    return postMapper.selectMyPostsByPostStatus(attributes);
  }

  public List<PostDTO> selectRecentPosts(UserDTO user, int pageNumber) {
    int pageCount = postMapper.getRecentPostsCount(user.getCompanyId());
    Map<String, Object> map = makeMapUserAndPageInfo(user, pageCount, pageNumber);
    return postMapper.selectRecentPosts(map);
  }

  public int getMyPostsCount(UserDTO user) {
    Map<String, Object> attributes = makeMapUserAndPostStatus(user, PostStatus.NORMAL);
    return postMapper.getMyPostsCountByPostStatus(attributes);
  }

  public int getMyRepliesCount(UserDTO user) {
    return postMapper.getPostsCountIncludeMyReplies(user);
  }

  public int getMyTempPostsCount(UserDTO user) {
    Map<String, Object> attributes = makeMapUserAndPostStatus(user, PostStatus.TEMP);
    return postMapper.getMyPostsCountByPostStatus(attributes);
  }

  public int getMyRecyclePostsCount(UserDTO user) {
    Map<String, Object> attributes = makeMapUserAndPostStatus(user, PostStatus.RECYCLE);
    return postMapper.getMyPostsCountByPostStatus(attributes);
  }

  public int getRecentPostsCount(int companyId) {
    return postMapper.getRecentPostsCount(companyId);
  }

  public Map<String, Object> makeMapUserAndPageInfo(UserDTO user, int pageCount, int pageNumber) {
    Map<String, Object> map = new HashMap<>();
    PaginationDTO pageInfo = new PaginationDTO("posts", pageCount, pageNumber, PageSize.POST,
        RangeSize.POST);
    map.put("user", user);
    map.put("startIndex", pageInfo.getStartIndex());
    map.put("pageSize", PageSize.POST);
    return map;
  }

  public Map<String, Object> makeMapUserAndPageInfoPostStatus(UserDTO user, int pageCount,
      int pageNumber, String postStatus) {
    Map<String, Object> map = new HashMap<>();
    PaginationDTO pageInfo = new PaginationDTO("posts", pageCount, pageNumber, PageSize.POST,
        RangeSize.POST);
    map.put("user", user);
    map.put("startIndex", pageInfo.getStartIndex());
    map.put("pageSize", PageSize.POST);
    map.put("postStatus", postStatus);
    return map;
  }

  private Map<String, Object> makeMapUserAndPostStatus(UserDTO user, String postStatus) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("user", user);
    attributes.put("postStatus", postStatus);
    return attributes;
  }

  /**
   * 게시글 목록 조회와 조회수 증가
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public PostDTO selectPostByPostId(int postId, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostId(postId);
    PostValidation.isExistPost(post);
    if (!viewRecordService.isReadPostByUser(userData.getUserId(), postId)) {//안읽은경우
      updateViewCnt(postId, request);//조회수 업데이트
      post.setViewCount(post.getViewCount() + 1);//반환도 1증가
    }
    return post;
  }

  /**
   * 게시판 번호와 페이지번호를 가지고 출력할 게시물 목록 반환
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public List<PostDTO> getPostListByBoardId(int boardId, int pageNumber, int companyId) {
    int pageCount = getPostsCountByBoardId(boardId);
    PaginationDTO pageInfo = new PaginationDTO("posts", pageCount, pageNumber, PageSize.POST,
        RangeSize.POST);
    List<PostDTO> postList = postMapper
        .selectPostByBoardId(boardId, pageInfo.getStartIndex(), PageSize.POST);

    return postList;
  }

  /**
   * 게시물 출력에 필요한 정보를 추가한다. 댓글수
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  private List<PostDTO> updatePostInfo(List<PostDTO> postList, int companyId) {

    return postList;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public PostDTO getPostByPostId(int postId) {
    PostDTO post = postMapper.selectPostByPostId(postId);
    return post;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getPostsCountByBoardId(int boardId) {
    int postCounts = postMapper.selectPostCountByBoardId(boardId);
    return postCounts;
  }

  /**
   * 조회수 증가 알고리즘 조회시 5분동안은 조회수 증가불가
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  //TODO 휴지통인경우 임시저장함인경우는 따로 구분해서 조회수 증가 안되도록 해야됨. 1번방법 : 임시저장이나 휴지통인 경우 제외 ,2번방법 : 작성자 조회수증가에서 제외.
  //TODO 카운트는 비동기로 트랜잭션 처리보다야
  public void updateViewCnt(int postId, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    viewRecordService.readPostByUser(userData.getUserId(), postId);
    postMapper.updateViewCnt(postId);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public List<PostDTO> getPopularPostList(int companyId) {
    List<PostDTO> postList = postMapper.selectPopularPostListByCompanyId(companyId);
    for (PostDTO post : postList) {
      post.setIsPopular(true);
    }
    return postList;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getPopularPostsCount(int companyId) {
    return postMapper.getPopularPostsCount(companyId);
  }


  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public void updateCommentCountPlus1(int postId) {
    postMapper.updateCommentCountPlus1(postId);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public void updateCommentCountMinus1(int postId) {
    postMapper.updateCommentCountMinus1(postId);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getPostIdByCommentId(int commentId) {
    int postId = postMapper.selectPostIdByCommentId(commentId);
    return postId;
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public int getCommentsCountByPostId(int postId) {
    return postMapper.selectCommentsCountByPostId(postId);
  }

  public int getSearchPostCount(Map<String, Object> attributes) {
    return postMapper.selectSearchPostCount(attributes);
  }
}

