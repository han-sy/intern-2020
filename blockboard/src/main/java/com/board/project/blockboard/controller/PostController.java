/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.PostService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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

  /**
   * 게시물 작성
   *
   * @param boardID     게시물을 올릴 게시판 id
   * @param receivePost 받은 게시물 정보
   */
  @PostMapping("")
  public void insertPost(@PathVariable("boardid") int boardID,
      @ModelAttribute PostDTO receivePost, HttpServletRequest request,
      HttpServletResponse response) {
    postService.insertPost(receivePost, boardID, request, response);
  }

  /**
   * 게시물 목록 띄우기 boardid 받아와서 해당하는 게시판의 게시글목록들 리턴
   *
   * @param boardID
   * @return
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @GetMapping("")
  public List<PostDTO> getPostListByBoardID(@PathVariable("boardid") int boardID,
      @RequestParam("pageNumber") int pageNumber, HttpServletRequest request) {
    UserDTO userDTO = new UserDTO(request);
    return postService
        .getPostListByBoardID(boardID, pageNumber, userDTO.getCompanyID());
  }

  /**
   * 게시글 가져오기
   *
   * @return PostDTO + 유저일치여부 로 구성된 map
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @GetMapping(value = "/{postid}")
  public PostDTO getPostByPostID(@PathVariable("postid") int postID,
      @PathVariable("boardid") int boardID, HttpServletRequest request,
      HttpServletResponse response) {
    return postService.selectPostByPostID(postID, boardID, request, response);
  }

  /**
   * 게시물 완전 삭제
   *
   * @param postID 삭제할 게시물 id
   * @return
   */
  @DeleteMapping("/{postid}")
  public void deletePost(@PathVariable("boardid") int boardID,
      @PathVariable("postid") int postID, HttpServletRequest request,
      HttpServletResponse response) {
    postService.deletePost(postID, boardID, request, response);
  }

  /**
   * 수정한 게시물을 저장할 때
   *
   * @param originalBoardID 수정 전 게시물의 게시판 id
   * @param postID          수정할 게시물 id
   * @param requestPost     수정할 게시물 제목과 내용이 담긴 객체
   * @return
   */
  @PutMapping("/{postid}")
  public void updatePost(@PathVariable("boardid") int originalBoardID,
      @PathVariable("postid") int postID,
      @ModelAttribute PostDTO requestPost, HttpServletRequest request,
      HttpServletResponse response) {
    postService.updatePost(requestPost, postID, originalBoardID, request, response);

  }


  /**
   * 게시물 삭제 후 휴지통 이동
   *
   * @param boardID 휴디통으로 이동할 게시물의 게시판 id
   * @param postID  휴지통으로 이동할 게시물 id
   */
  @PutMapping("/{postid}/trash")
  public void deletePostTemporary(@PathVariable("boardid") int boardID,
      @PathVariable("postid") int postID, HttpServletRequest request,
      HttpServletResponse response) {
    postService.movePostToTrash(postID, boardID, request, response);
  }

  /**
   * 휴지통의 게시물을 복원한다.
   *
   * @return 해당 유저의 휴지통 목록
   */
  @PutMapping("/{postid}/restore")
  public void restorePost(@PathVariable("postid") int postID, HttpServletRequest request,
      HttpServletResponse response) {
    postService.restorePost(postID, request, response);
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
      @RequestParam("keyword") String keyword, HttpServletResponse response) {
    return postService.searchPost(option, keyword, response);
  }


  /**
   * 임시 저장 게시물 목록을 가져올 때 (현재 로그인된 userID, companyID이 필요)
   *
   * @return 임시 저장된 게시물 목록
   */
  @GetMapping("/temp")
  public List<PostDTO> getTempPosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO requestUser = new UserDTO(request);
    return postService.selectMyTempPosts(requestUser, pageNumber);
  }

  /**
   * 임시 저장 게시물을 가져올 때
   *
   * @return 임시 저장된 게시물 목록
   */
  @GetMapping("/temp/{postid}")
  public PostDTO getTempPost(@PathVariable("postid") int postID, HttpServletResponse response) {
    return postService.selectTempPost(postID, response);
  }

  /**
   * 임시 저장 게시물 삭제할 때
   *
   * @param postID 삭제할 게시물 id
   * @return
   */
  @DeleteMapping("/temp/{postid}")
  public void deleteTempPost(@PathVariable("postid") int postID,
      @PathVariable("boardid") int boardID, HttpServletRequest request,
      HttpServletResponse response) {
    postService.deletePost(postID, boardID, request, response);
  }

  /**
   * 가장 최근에 임시저장된 게시글 가져올 때
   *
   * @return 가장 최근에 임시저장된 게시물 객체
   */
  @GetMapping("/temp/recent")
  public PostDTO recentTempPost(HttpServletRequest request) {
    return postService.selectRecentTemp(request);
  }

  /**
   * 휴지통의 게시물 목록을 불러온다.
   *
   * @return 해당 유저의 휴지통 목록
   */
  @GetMapping("/recycleBin")
  public List<PostDTO> getRecyclePosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO requestUser = new UserDTO(request);
    return postService.getMyRecyclePosts(requestUser, pageNumber);
  }

  /**
   * 휴지통의 게시글을 불러온다.
   *
   * @return 해당 유저의 휴지통 목록
   */
  @GetMapping("/recycleBin/{postid}")
  public PostDTO getRecyclePost(@PathVariable("postid") int postID,
      HttpServletResponse response) {
    return postService.selectRecyclePost(postID, response);
  }

  /**
   * 요청한 회원이 작성한 게시글만 불러온다. (휴지통, 임시보관 제외)
   *
   * @return 해당 유저의 게시글 목록
   */
  @GetMapping("/myArticle")
  public List<PostDTO> selectMyPosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO user = new UserDTO(request);
    return postService.selectMyPosts(user, pageNumber);
  }

  @GetMapping("/myReply")
  public List<PostDTO> selectMyPostsIncludeMyReplies(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO user = new UserDTO(request);
    return postService.selectPostsIncludeMyReplies(user, pageNumber);
  }

  @GetMapping("/recent")
  public List<PostDTO> selectRecentPosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO user = new UserDTO(request);
    return postService.selectRecentPosts(user, pageNumber);
  }

  @GetMapping("/popular-board")
  public List<PostDTO> getPopularPosts(HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    return postService.getPopularPostList(userData.getCompanyID());
  }
}
