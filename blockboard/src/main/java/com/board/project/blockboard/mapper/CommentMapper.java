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


  void deleteCommentByCommentID(int commentID);

  void deleteCommentsByPostID(int postID);

  void updateComment(Map<String, Object> commentAttribute);

  void deleteCommentByCommentReferencedID(int commentID);

  String selectUserIDByCommentID(int commentID);

  int insertNewCommentByCommentInfo(CommentDTO commentInfo);

  int getAllCommentsCountByPostID(int postID);

  int getOnlyCommentsCountByPostID(int postID);

  List<CommentDTO> selectCommentsByPostID(int postID, int startIndex, int pageSize);

  void updateRepliesCountPlus1(int commentReferencedID);

  void updateRepliesCountMinus1(int commentReferencedID);

  int selectCommentReferencedIDByCommentID(int commentID);


  int selectRepliesCountByCommentReferencedID(int commentReferencedID);
}
