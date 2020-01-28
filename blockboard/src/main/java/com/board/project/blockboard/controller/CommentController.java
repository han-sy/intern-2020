package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.service.JwtService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
     */
    @GetMapping("/{postid}/comments")
    public List<CommentDTO> getCommentsByPost(@PathVariable("postid") int postID, HttpServletRequest request) {
        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);
        return commentList;
    }


    /**
     * 댓글 추가
     * @param postID
     * @param boardID
     * @param commentContent
     * @param request
     */
    @PostMapping("/{postid}/comments")
    public void writeComment(@RequestParam("postID") int postID,@RequestParam("boardID") int boardID,@RequestParam("commentContent") String commentContent, HttpServletRequest request) {
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();
        log.info("!!!!"+postID+","+boardID+":"+commentContent);
        commentService.writeCommentWithUserInfo(userID,commentContent,boardID,companyID,postID);
    }

    /**
     * 댓글 삭제
     * @param commentID
     * @param postID
     * @param boardID
     * @param request
     */
    @DeleteMapping("/{postid}/comments/{commentid}")
    public void deleteComment(@PathVariable("commentid") int commentID,@PathVariable("postid") int postID,@PathVariable("boardid") int boardID, HttpServletRequest request) {
        commentService.deleteComment(commentID);
    }

    /**
     * 댓글 수정하기
     * @param commentID
     * @param postID
     * @param boardID
     * @param newComment 변경된 새로운 내용
     * @param request
     */
    @PutMapping("/{postid}/comments/{commentid}")
    public void editComment(@PathVariable("commentid") int commentID,@PathVariable("postid") int postID,@PathVariable("boardid") int boardID,@RequestParam("newComment") String newComment, HttpServletRequest request) {
        commentService.updateComment(commentID,newComment);
    }
}
