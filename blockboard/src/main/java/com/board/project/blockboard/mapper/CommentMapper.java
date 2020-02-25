/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CommentMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.CommentDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CommentMapper {


  CommentDTO selectCommentByCommentId(int commentId);

  void deleteCommentByCommentId(int commentId);

  void deleteCommentsByPostId(int postId);

  void updateComment(CommentDTO commentData);

  void deleteCommentByCommentReferencedId(int commentId);

  String selectUserIdByCommentId(int commentId);

  int insertNewCommentByCommentInfo(CommentDTO commentInfo);

  int getAllCommentsCountByPostId(int postId);

  int getOnlyCommentsCountByPostId(int postId);

  List<CommentDTO> selectCommentsByPostId(int postId, int startIndex, int pageSize);

  void updateRepliesCountPlus1(int commentReferencedId);

  void updateRepliesCountMinus1(int commentReferencedId);

  Integer selectCommentReferencedIdByCommentId(int commentId);


  Integer selectRepliesCountByCommentReferencedId(int commentReferencedId);
}
