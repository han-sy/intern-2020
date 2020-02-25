/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.CommentDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CommentMapper {

  List<CommentDTO> selectCommentsByPostId(int postId, int startIndex, int pageSize);

  CommentDTO selectCommentByCommentId(int commentId);

  String selectUserIdByCommentId(int commentId);

  Integer selectCommentReferencedIdByCommentId(int commentId);

  Integer selectRepliesCountByCommentReferencedId(int commentReferencedId);

  int insertNewCommentByCommentInfo(CommentDTO commentInfo);

  void deleteCommentByCommentId(int commentId);

  void updateComment(CommentDTO commentData);

  void deleteCommentByCommentReferencedId(int commentId);


  void updateRepliesCountPlus1(int commentReferencedId);

  void updateRepliesCountMinus1(int commentReferencedId);


}
