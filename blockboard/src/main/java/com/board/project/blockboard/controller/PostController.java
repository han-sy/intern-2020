/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.common.util.LengthCheckUtils;
import com.board.project.blockboard.common.validation.PostValidation;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.PostService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/boards/{boardid}/posts")
public class PostController {

  @Autowired
  private PostService postService;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private PostValidation postValidation;

  /**
   * 게시글 가져오기
   *
   * @return PostDTO + 유저일치여부 로 구성된 map
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @GetMapping(value = "/{postid}")
  public PostDTO getPostByPostID(@PathVariable("postid") int postID, HttpServletResponse response) {
    PostDTO post = postService.selectPostByPostID(postID);
    if (postValidation.isExistPost(post, response)) {
      JsonParse.setPostStatusFromJsonString(post);
      log.info(post.toString());
      return post;
    }
    return null;
  }

  /**
   * 게시물 작성
   *
   * @param boardid     게시물을 올릴 게시판 id
   * @param receivePost 받은 게시물 정보
   */
  @PostMapping("")
  public void insertPost(@PathVariable("boardid") int boardid,
      @ModelAttribute PostDTO receivePost, HttpServletResponse response) {
    log.info(receivePost.toString());
    if (LengthCheckUtils.isValid(receivePost, response)) {
      int receivedPostID = receivePost.getPostID();
      receivePost.setBoardID(boardid);
      receivePost.setPostContentExceptHTMLTag(Jsoup.parse(receivePost.getPostContent()).text());

      // '글쓰기' -> '저장'or'임시저장' 버튼을 누른 경우에는 html 안에 postID가 존재하지 않아 바로 insert
      if (receivedPostID == 0) {
        postService.setPostStatusIsTempAndIsTrash(receivePost, false, false);
        postService.insertPost(receivePost);
      } else {
        // [임시보관함]의 게시글에서 '저장'or'임시저장' 버튼을 눌렀는데 실제로 임시저장 되어있는 게시물이면 insert(update)
        PostDTO receivePostInDatabase = postService.getPostByPostID(receivedPostID);
        if (postValidation.isExistPost(receivePostInDatabase, response) &&
            postValidation.isTempSavedPost(receivePostInDatabase, response)) {
          postService.setPostStatusIsTempAndIsTrash(receivePost, true, false);
          postService.insertPost(receivePost);
        }
      }
    }
  }

  /**
   * 게시물 목록 띄우기 boardid 받아와서 해당하는 게시판의 게시글목록들 리턴
   *
   * @param boardID
   * @return
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @GetMapping("/{pagenumber}/postlist")
  public List<PostDTO> getPostListByBoardID(@PathVariable("boardid") int boardID,
      @PathVariable("pagenumber") int pageNumber) {
    List<PostDTO> postList = postService.getPostListByBoardID(boardID, pageNumber);
    return postList;
  }

  /**
   * 게시물 완전 삭제
   *
   * @param postID 삭제할 게시물 id
   * @return
   */
  @DeleteMapping("/{postid}")
  public void deletePost(@PathVariable("postid") int postID, HttpServletResponse response) {
    if (postValidation.isExistPost(postID, response)) {
      postService.deletePost(postID);
    }
  }

  /**
   * 게시물 삭제 후 휴지통 이동
   *
   * @param boardID 휴디통으로 이동할 게시물의 게시판 id
   * @param postID  휴지통으로 이동할 게시물 id
   */
  @PutMapping("/{postid}/trash")
  public void deletePostTemporary(@PathVariable("boardid") int boardID,
      @PathVariable("postid") int postID, HttpServletResponse response) {
    PostDTO post = postService.selectPostByPostID(postID);
    if (postValidation.isValidDelete(boardID, post, response)) {
      postService.setPostStatusIsTempAndIsTrash(post, false, true);
      postService.movePostToTrash(post);
    }
  }

  /**
   * 수정한 게시물을 저장할 때
   *
   * @param boardid     수정 후 게시물을 올릴 게시판 id
   * @param postid      수정할 게시물 id
   * @param requestPost 수정할 게시물 제목과 내용이 담긴 객체
   * @return
   */
  @PutMapping("/{postid}")
  public void updatePost(@PathVariable("boardid") int boardid, @PathVariable("postid") int postid,
      @ModelAttribute PostDTO requestPost, HttpServletResponse response) {
    if (LengthCheckUtils.isValid(requestPost, response)) {
      requestPost.setPostID(postid);
      requestPost.setBoardID(boardid);
      requestPost.setPostContentExceptHTMLTag(Jsoup.parse(requestPost.getPostContent()).text());
      if (postValidation.isExistPost(requestPost.getPostID(), response)) {
        postService.updatePost(requestPost);
      }
    }
  }

  /**
   * 수정한 게시물을 저장할 때
   *
   * @param option  검색 옵션 (ex : 제목, 내용, 작성자)
   * @param keyword 검색할 문자열
   * @return searchList 검색한 결과들이 담긴
   */
  @GetMapping("/search")
  public List<PostDTO> searchPost(@RequestParam("option") String option,
      @RequestParam("keyword") String keyword, HttpServletResponse response) throws IOException {
    if (postValidation.isValidSearch(option, keyword, response)) {
      return postService.searchPost(option, keyword);
    }
    return null;
  }

  /**
   * 가장 최근에 임시저장된 게시글 가져올 때
   *
   * @return 가장 최근에 임시저장된 게시물 객체
   */
  @GetMapping("/recent")
  public PostDTO recentTempPost(HttpServletRequest request) {
    UserDTO requestUser = new UserDTO(request);
    return postService.selectRecentTemp(requestUser);
  }

  /**
   * 임시 저장 게시물 목록을 가져올 때 (현재 로그인된 userID, companyID이 필요)
   *
   * @return 임시 저장된 게시물 목록
   */
  @GetMapping("/temp")
  public List<PostDTO> getTempPosts(HttpServletRequest request) {
    UserDTO requestUser = new UserDTO(request);
    return postService.getTempPosts(requestUser);
  }

  /**
   * 임시 저장 게시물을 가져올 때
   *
   * @return 임시 저장된 게시물 목록
   */
  @GetMapping("/temp/{postid}")
  public PostDTO getTempPost(@PathVariable("postid") int postID, HttpServletResponse response) {
    PostDTO post = postService.selectTempPost(postID);
    if (postValidation.isTempSavedPost(post, response)) {
      return post;
    }
    return null;
  }

  /**
   * 임시 저장 게시물 삭제할 때
   *
   * @param postID 삭제할 게시물 id
   * @return
   */
  @DeleteMapping("/temp/{postid}")
  public void deleteTempPost(@PathVariable("postid") int postID, HttpServletResponse response) {
    if (postValidation.isExistPost(postID, response)) {
      postService.deletePost(postID);
    }
  }

  /**
   * 휴지통의 게시물 목록을 불러온다.
   *
   * @return 해당 유저의 휴지통 목록
   */
  @GetMapping("/trash")
  public List<PostDTO> getPostsInTrashBox(HttpServletRequest request) {
    UserDTO requestUser = new UserDTO(request);
    return postService.getPostsInTrashBox(requestUser);
  }

  /**
   * 휴지통의 게시물을 복원한다.
   *
   * @return 해당 유저의 휴지통 목록
   */
  @PutMapping("/{postid}/restore")
  public void restorePost(@PathVariable("postid") int postID, HttpServletResponse response) {
    PostDTO post = postService.selectPostByPostID(postID);
    if (postValidation.isExistPost(post, response) && postValidation.isInTrashBox(post, response)) {
      postService.setPostStatusIsTempAndIsTrash(post, false, false);
      postService.restorePost(post);
    }
  }
}
