package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/boards/{boardid}/posts")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private JwtService jwtService;

    /**
     * postID 일치하는 댓글 목록 리턴 ( 대댓글은 반환하지 않는다.)
     * @param postID
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/{postid}/comments")
    public List<CommentDTO> getCommentsByPost(@PathVariable("postid") int postID, HttpServletRequest request) {
        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);

        return commentList;
    }

    @PostMapping("/{postid}/comments")
    public List<CommentDTO> writeComment(@PathVariable("postid") int postID,@PathVariable("boardid") int boardID,@ModelAttribute CommentDTO receiveComment, HttpServletRequest request) {
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();

        receiveComment.setBoardID(boardID);
        receiveComment.setUserID(userID);
        receiveComment.setPostID(postID);
        commentService.writeCommentWithUserInfo(userID,receiveComment,boardID,companyID,postID);

        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);
        return commentList;
    }
    @DeleteMapping("/{postid}/comments/{commentid}")
    public List<CommentDTO> deleteComment(@PathVariable("commentid") int commentID,@PathVariable("postid") int postID,@PathVariable("boardid") int boardID, HttpServletRequest request) {
        commentService.deleteComment(commentID);

        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);
        return commentList;
    }
}
