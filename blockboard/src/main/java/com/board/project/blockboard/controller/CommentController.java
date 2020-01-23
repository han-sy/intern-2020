package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.util.SessionTokenizer;
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

    /**
     * postID 일치하는 댓글 목록 리턴 ( 대댓글은 반환하지 않는다.)
     * @param postID
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/{postid}/comments")
    public List<CommentDTO> getCommentsByPost(@PathVariable("postid") int postID, HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        log.info("!!!!");
        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);

        return commentList;
    }

    @PostMapping("/{postid}/comments")
    public List<CommentDTO> writeComment(@PathVariable("postid") int postID,@PathVariable("boardid") int boardID,@ModelAttribute CommentDTO receiveComment, HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        log.info("writeComment 들어옴");
        SessionTokenizer session = new SessionTokenizer(request);
        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        receiveComment.setBoardID(boardID);
        receiveComment.setUserID(userID);
        receiveComment.setPostID(postID);
        commentService.writeCommentWithUserInfo(userID,receiveComment,boardID,companyID,postID);

        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);
        return commentList;
    }
    @DeleteMapping("/{postid}/comments/{commentid}")
    public List<CommentDTO> deleteComment(@PathVariable("commentid") int commentID,@PathVariable("postid") int postID,@PathVariable("boardid") int boardID, HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        log.info("writeComment 들어옴");
        /*SessionTokenizer session = new SessionTokenizer(request);
        String userID = session.getUserID();
        int companyID = session.getCompanyID();*/
        commentService.deleteComment(commentID);

        List<CommentDTO> commentList = commentService.getCommentListByPostID(postID);
        return commentList;
    }
}
