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
@RequestMapping("/boards/{boardId}/posts")
public class PostController {

  @Autowired
  private PostService postService;

  /**
   * 게시물 목록 띄우기 boardId 받아와서 해당하는 게시판의 게시글목록들 리턴
   *
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  @GetMapping("")
  public List<PostDTO> getPostListByBoardID(@PathVariable("boardId") int boardID,
      @RequestParam("pageNumber") int pageNumber, HttpServletRequest request) {
    UserDTO userDTO = new UserDTO(request);
    return postService
        .getPostListByBoardID(boardID, pageNumber, userDTO.getCompanyID());
  }

  @PostMapping("")
  public int insertPost(@PathVariable("boardId") int boardID,
      @ModelAttribute PostDTO receivePost, HttpServletRequest request,
      HttpServletResponse response) {
    return postService.insertPost(receivePost, boardID, request, response);
  }

  @GetMapping("/{postId}")
  public PostDTO getPostByPostID(@PathVariable("postId") int postID,
      @PathVariable("boardId") int boardID, HttpServletRequest request,
      HttpServletResponse response) {
    return postService.selectPostByPostID(postID, boardID, request, response);
  }

  @PutMapping("/{postId}")
  public void updatePost(@PathVariable("postId") int postID,
      @ModelAttribute PostDTO requestPost, HttpServletRequest request,
      HttpServletResponse response) {
    postService.updatePost(requestPost, postID, request, response);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(@PathVariable("boardId") int boardID,
      @PathVariable("postId") int postID, HttpServletRequest request,
      HttpServletResponse response) {
    postService.deletePost(postID, boardID, request, response);
  }

  @PutMapping("/{postId}/restore")
  public void restorePost(@PathVariable("postId") int postID, HttpServletRequest request,
      HttpServletResponse response) {
    postService.restorePost(postID, request, response);
  }

  @GetMapping("/search")
  public List<PostDTO> searchPost(@RequestParam("option") String option,
      @RequestParam("keyword") String keyword, HttpServletResponse response) {
    return postService.searchPost(option, keyword, response);
  }

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

  @GetMapping("/tempBox")
  public List<PostDTO> getTempPosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO requestUser = new UserDTO(request);
    return postService.selectMyTempPosts(requestUser, pageNumber);
  }

  @GetMapping("/recycleBin")
  public List<PostDTO> getRecyclePosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO requestUser = new UserDTO(request);
    return postService.getMyRecyclePosts(requestUser, pageNumber);
  }

  @GetMapping("/popular-board")
  public List<PostDTO> getPopularPosts(HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    return postService.getPopularPostList(userData.getCompanyID());
  }
}
