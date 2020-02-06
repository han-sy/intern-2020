/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.PostMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostService {

  @Autowired
  private PostMapper postMapper;

  public void insertPost(PostDTO post) {
    postMapper.insertPost(post);
  }

  public void deletePost(int postID) {
    postMapper.deletePostByPostID(postID);
  }

  public PostDTO selectPostByPostID(int postID) {
    return postMapper.selectPostByPostID(postID);
  }

  public void updatePost(PostDTO post) {
    postMapper.updatePost(post);
  }

  public List<PostDTO> searchPost(String option, String keyword) {
    return postMapper.searchPost(option, keyword);
  }

  public PostDTO selectRecentTemp(Map<String, Object> param) {
    return postMapper.selectRecentTempPost(param);
  }

  public List<PostDTO> getTempPosts(UserDTO userDTO) {
    return postMapper.selectTempPosts(userDTO);
  }

  public PostDTO selectTempPost(int postID) {
    return postMapper.selectPostByPostID(postID);
  }

  public void deleteTempPost(int postID) {
    postMapper.deletePostByPostID(postID);
  }

  /**
   * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
   */
  public List<PostDTO> getPostListByBoardID(int boardID) {
    List<PostDTO> postlist = postMapper.selectPostByBoardID(boardID);
    return postlist;
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
  public int getPostsCountByBoardID(int boardID){
    int postCounts = postMapper.selectPostCountByBoardID(boardID);
    return postCounts;
  }
}
