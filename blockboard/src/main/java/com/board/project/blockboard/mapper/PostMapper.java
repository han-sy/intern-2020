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

  List<PostDTO> searchPost(String option, String keyword);

  List<PostDTO> selectMyPosts(Map<String, Object> map);

  List<PostDTO> selectMyPostsIncludeMyReplies(Map<String, Object> map);

  List<PostDTO> selectMyRecyclePosts(Map<String, Object> map);

  List<PostDTO> selectRecentPosts(Map<String, Object> map);

  List<PostDTO> selectMyTempPosts(Map<String, Object> map);

  List<PostDTO> selectPostByBoardID(int boardID, int startIndex, int pageSize);

  List<PostDTO> selectPopularPostListByCompanyID(int companyID);

  PostDTO selectPostByPostID(int postID);

  PostDTO selectRecentTempPost(UserDTO requestUser);

  void temporaryDeletePost(PostDTO post);

  void restorePost(PostDTO post);

  void insertPost(PostDTO post);

  void deletePostByPostID(int postID);

  void updatePost(PostDTO post);

  void updateViewCnt(int postID);

  String selectUserIDByPostID(int postId);

  int selectPostCountByBoardID(int boardID);

  int getMyPostsCount(UserDTO user);

  int getPostsCountIncludeMyReplies(UserDTO user);

  int getMyTempPostsCount(UserDTO user);

  int getMyRecyclePostsCount(UserDTO user);

  int getRecentPostsCount(int companyID);

  int getPopularPostsCount(int companyID);


}
