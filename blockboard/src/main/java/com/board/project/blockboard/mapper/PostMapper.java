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

  List<PostDTO> selectMyPostsByPostStatus(Map<String, Object> map);

  List<PostDTO> selectMyPostsIncludeMyReplies(Map<String, Object> map);

  List<PostDTO> selectRecentPosts(Map<String, Object> map);

  List<PostDTO> selectPostByBoardId(int boardId, int startIndex, int pageSize);

  List<PostDTO> selectPopularPostListByCompanyId(int companyId);

  PostDTO selectPostByPostId(int postId);

  PostDTO selectPostByAlarmId(int alarmId);

  void temporaryDeletePost(PostDTO post);

  void restorePost(PostDTO post);

  void insertPost(PostDTO post);

  void deletePostByPostId(int postId);

  void updatePost(PostDTO post);

  void updateViewCnt(int postId);

  String selectUserIdByPostId(int postId);

  int selectPostCountByBoardId(int boardId);

  int getMyPostsCountByPostStatus(Map<String, Object> attributes);

  int getPostsCountIncludeMyReplies(UserDTO user);

  int getRecentPostsCount(int companyId);

  int getPopularPostsCount(int companyId);

  void updateCommentCountPlus1(int postId);

  void updateCommentCountMinus1(int postId);

  Integer selectPostIdByCommentId(int commentId);

  int selectCommentsCountByPostId(int postId);
}
