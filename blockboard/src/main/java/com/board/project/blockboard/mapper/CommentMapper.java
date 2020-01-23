package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentMapper {
    List<CommentDTO> selectCommentsByPostID(int postID);
    int insertNewCommentByCommentInfo(CommentDTO commentInfo);
    void deleteCommentByCommentID(int commentID);
    void deleteCommentsByPostID(int postID);
}
