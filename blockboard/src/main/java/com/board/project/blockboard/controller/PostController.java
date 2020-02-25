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
  public List<PostDTO> getPostListByBoardId(@PathVariable int boardId,
      @RequestParam int pageNumber, HttpServletRequest request) {
    UserDTO userDTO = new UserDTO(request);
    return postService
        .getPostListByBoardId(boardId, pageNumber, userDTO.getCompanyId());
  }

  @PostMapping("")
  public int insertPost(@ModelAttribute PostDTO receivePost, HttpServletRequest request) {
    return postService.insertPost(receivePost, request);
  }

  @GetMapping("/{postId}")
  public PostDTO getPostByPostId(@PathVariable int postId, HttpServletRequest request) {
    return postService.selectPostByPostId(postId, request);
  }

  @PutMapping("/{postId}")
  public void updatePost(@PathVariable int postId,
      @ModelAttribute PostDTO requestPost, HttpServletRequest request) {
    postService.updatePost(requestPost, postId, request);
  }

  @DeleteMapping("/{postId}")
  public void deletePost(@PathVariable int postId, HttpServletRequest request) {
    postService.deletePost(postId, request);
  }

  @PutMapping("/{postId}/restore")
  public void restorePost(@PathVariable int postId, HttpServletRequest request) {
    postService.restorePost(postId, request);
  }

  @GetMapping("/search")
  public List<PostDTO> searchPost(@RequestParam("option") String option,
      @RequestParam("keyword") String keyword, @RequestParam("pageNumber") int pageNumber) {
    return postService.searchPost(option, keyword, pageNumber);
  }

  @GetMapping("/my-article")
  public List<PostDTO> selectMyPosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO user = new UserDTO(request);
    return postService.selectMyPosts(user, pageNumber);
  }

  @GetMapping("/my-reply")
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

  @GetMapping("/temp-box")
  public List<PostDTO> getTempPosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO requestUser = new UserDTO(request);
    return postService.selectMyTempPosts(requestUser, pageNumber);
  }

  @GetMapping("/recycle-bin")
  public List<PostDTO> getRecyclePosts(HttpServletRequest request,
      @RequestParam("pageNumber") int pageNumber) {
    UserDTO requestUser = new UserDTO(request);
    return postService.getMyRecyclePosts(requestUser, pageNumber);
  }

  @GetMapping("/popular-board")
  public List<PostDTO> getPopularPosts(HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    return postService.getPopularPostList(userData.getCompanyId());
  }
}
