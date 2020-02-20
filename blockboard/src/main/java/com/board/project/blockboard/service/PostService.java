/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.LengthCheckUtils;
import com.board.project.blockboard.common.util.TagCheckUtils;
import com.board.project.blockboard.common.validation.PostValidation;
import com.board.project.blockboard.dto.AlarmDTO;
import com.board.project.blockboard.dto.PaginationDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.AlarmMapper;
import com.board.project.blockboard.mapper.PostMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostService {

  @Autowired
  private ViewRecordService viewRecordService;
  @Autowired
  private PostMapper postMapper;
  @Autowired
  private CommentService commentService;
  @Autowired
  private AlarmService alarmService;
  @Autowired
  private PostValidation postValidation;

  public int insertPost(PostDTO receivePost, int boardID, HttpServletRequest request,
      HttpServletResponse response) {
    if (LengthCheckUtils.isValid(receivePost, response)) {
      int receivedPostID = receivePost.getPostID();
      receivePost.setBoardID(boardID);
      receivePost.setUserID(request.getAttribute("userID").toString());
      receivePost.setUserName(request.getAttribute("userName").toString());
      receivePost.setCompanyID(Integer.parseInt(request.getAttribute("companyID").toString()));
      receivePost.setPostContentExceptHTMLTag(Jsoup.parse(receivePost.getPostContent()).text());
      // '글쓰기' -> '저장'or'임시저장' 버튼을 누른 경우에는 html 안에 postID가 존재하지 않아 바로 inse
      if (receivedPostID == 0) {
        postMapper.insertPost(receivePost);
      } else {
        // [임시보관함]의 게시글에서 '저장'or'임시저장' 버튼을 눌렀는데 실제로 임시저장 되어있는 게시물이면 insert(update)
        PostDTO receivePostInDatabase = postMapper.selectPostByPostID(receivedPostID);
        if (postValidation.isExistPost(receivePostInDatabase, boardID, response) &&
            postValidation.isTempSavedPost(receivePostInDatabase, response)) {
          postMapper.insertPost(receivePost);
        }
      }
    }
    // 일반 게시물에 삽입이 되면 태그된 유저 알람에 등록
    if (StringUtils.equals(receivePost.getPostStatus(), "normal")) {
      alarmService.insertAlarm(receivePost);
    }
    return receivePost.getPostID();
  }

  public void deletePost(int postID, int boardID, HttpServletRequest request,
      HttpServletResponse response) {
    PostDTO post = postMapper.selectPostByPostID(postID);
    UserDTO user = new UserDTO(request);
    if (postValidation.isExistPost(post, boardID, response) &&
        postValidation.isValidChange(post, user, response)) {
      if (StringUtils.equals(post.getPostStatus(), "recycle")) {
        postMapper.deletePostByPostID(postID);
      } else {
        postMapper.temporaryDeletePost(post);
      }
    }
  }

  public void updatePost(PostDTO requestPost, int postID, HttpServletRequest request,
      HttpServletResponse response) {
    UserDTO user = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostID(postID);
    if (LengthCheckUtils.isValid(requestPost, response)) {
      if (postValidation.isExistPost(post, response) && postValidation
          .isValidChange(post, user, response)) {
        post.setBoardID(requestPost.getBoardID());
        post.setPostTitle(requestPost.getPostTitle());
        post.setPostContent(requestPost.getPostContent());
        post.setPostContentExceptHTMLTag(Jsoup.parse(requestPost.getPostContent()).text());
        postMapper.updatePost(post);
      }
    }
  }

  /**
   * 게시글 목록 조회와 조회수 증가
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public PostDTO selectPostByPostID(int postID, int boardID, HttpServletRequest request,
      HttpServletResponse response) {
    UserDTO userData = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostID(postID);
    if (postValidation.isExistPost(post, boardID, response)) {
      if(!viewRecordService.isReadPostByUser(userData.getUserID(),postID)){//안읽은경우
        updateViewCnt(postID, request, response);//조회수 업데이트
        post.setViewCount(post.getViewCount()+1);//반환도 1증가
      }
      return post;
    }
    return null;
  }

  public List<PostDTO> searchPost(String option, String keyword, HttpServletResponse response) {
    if (postValidation.isValidSearch(option, keyword, response)) {
      return postMapper.searchPost(option, keyword);
    }
    return null;
  }

  public void restorePost(int postID, HttpServletRequest request, HttpServletResponse response) {
    UserDTO user = new UserDTO(request);
    PostDTO post = postMapper.selectPostByPostID(postID);
    if (postValidation.isValidChange(post, user, response) && postValidation
        .isValidRestore(post, response)) {
      postMapper.restorePost(post);
    }
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
    PaginationDTO pageInfo = new PaginationDTO(pageCount, pageNumber, ConstantData.PAGE_SIZE,
        ConstantData.RANGE_SIZE);
    map.put("user", user);
    map.put("startIndex", pageInfo.getStartIndex());
    map.put("pageSize", ConstantData.PAGE_SIZE);
    return map;
  }

  /**
   * 게시판 번호와 페이지번호를 가지고 출력할 게시물 목록 반환
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public List<PostDTO> getPostListByBoardID(int boardID, int pageNumber, int companyID) {
    int pageCount = getPostsCountByBoardID(boardID);
    PaginationDTO pageInfo = new PaginationDTO(pageCount, pageNumber, ConstantData.PAGE_SIZE,
        ConstantData.RANGE_SIZE);
    List<PostDTO> postList = postMapper
        .selectPostByBoardID(boardID, pageInfo.getStartIndex(), ConstantData.PAGE_SIZE);

    for (PostDTO post : postList) {
      int commentsCount = commentService.getCommentCountByPostID(post.getPostID(), companyID);
      post.setCommentsCount(commentsCount);
    }
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
  @Transactional
  public void updateViewCnt(int postID, HttpServletRequest request,
      HttpServletResponse response) {
    UserDTO userData = new UserDTO(request);
    viewRecordService.readPostByUser(userData.getUserID(),postID);
    postMapper.updateViewCnt(postID);
  }

  public List<PostDTO> getPopularPostList(int companyID) {
    List<PostDTO> postList = postMapper.selectPopularPostListByCompanyID(companyID);
    for (PostDTO post : postList) {
      int commentsCount = commentService.getCommentCountByPostID(post.getPostID(), companyID);
      post.setCommentsCount(commentsCount);
      post.setIsPopular(true);
    }
    return postList;
  }

  public int getPopularPostsCount(int companyID) {
    return postMapper.getPopularPostsCount(companyID);
  }
}

