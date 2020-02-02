/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ReplyController.java
 */
package com.board.project.blockboard.controller;


import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.service.CommentService;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/boards/{boardid}/posts/{postid}/comments/{commentid}")
public class ReplyController {
    @Autowired
    private ReplyService replyService;
    @Autowired
    private JwtService jwtService;

    /**
     * 답글 조회
     * @param postID
     * @param commentReferencedID
     * @param request
     * @return
     */
    @GetMapping("")
    public List<CommentDTO> getReplysByComment(@PathVariable("postid") int postID, @PathVariable("commentid") int commentReferencedID, HttpServletRequest request) {
        List<CommentDTO> replyList = replyService.getReplyListByCommentID(commentReferencedID);
        return replyList;
    }

    /**
     * 답글 추가
     * @param postID
     * @param boardID
     * @param commentReferencedID 참조하는 댓글 id
     * @param commentContent  답글내용
     * @param request
     */
    @PostMapping("")
    public void writeReply(@RequestParam("postID") int postID,
                           @RequestParam("boardID") int boardID,
                           @RequestParam("commentContent") String commentContent,
                           @RequestParam("commentReferencedID") int commentReferencedID,
                           @RequestParam("commentReferencedUserID") String commentReferencedUserID,
                           HttpServletRequest request) {
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();
        replyService.writeReplyWithUserInfo(userID,companyID,postID,boardID,commentContent,commentReferencedID,commentReferencedUserID);
    }
}
