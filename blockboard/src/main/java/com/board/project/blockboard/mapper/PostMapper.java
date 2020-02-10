/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file PostMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.dto.UserDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PostMapper {

  void insertPost(PostDTO post);

  void deletePostByPostID(int postID);

  PostDTO selectPostByPostID(int postID);

  void updatePost(PostDTO post);

  List<PostDTO> searchPost(String option, String keyword);

  PostDTO selectRecentTempPost(UserDTO requestUser);

  List<PostDTO> selectMyTempPosts(Map<String, Object> map);

  List<PostDTO> selectPostByBoardID(int boardID, int startIndex, int pageSize);

  int selectPostCountByBoardID(int boardID);

  void temporaryDeletePost(PostDTO post);

  void restorePost(PostDTO post);

  List<PostDTO> selectMyPosts(Map<String, Object> map);

  List<PostDTO> selectMyPostsIncludeMyReplies(Map<String, Object> map);

  List<PostDTO> selectMyRecyclePosts(Map<String, Object> map);

  int getMyPostsCount(UserDTO user);

  int getPostsCountIncludeMyReplies(UserDTO user);

  int getMyTempPostsCount(UserDTO user);

  int getMyRecyclePostsCount(UserDTO user);
}
