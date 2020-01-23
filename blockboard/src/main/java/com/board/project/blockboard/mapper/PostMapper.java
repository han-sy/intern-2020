/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    PostMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface PostMapper {
    void insertPost(PostDTO post);
    void deletePostByPostID(int postID);
    PostDTO selectPostByPostID(int postID);
    void updatePost(PostDTO post);
    List<PostDTO> search(String option, String keyword);
}
