/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ReplyMapper.java
 */
package com.board.project.blockboard.mapper;


import com.board.project.blockboard.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReplyMapper {
    List<CommentDTO> selectRepliesByCommentID(int commentReferencedID);
    int insertNewReplyByCommentInfo(CommentDTO reply);
}
