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

  PostDTO selectRecentTempPost(Map<String, Object> map);

  List<PostDTO> selectTempPosts(UserDTO userDTO);

  List<PostDTO> selectPostByBoardID(int boardID,int startIndex,int pageSize);

  int selectPostCountByBoardID(int boardID);
}
