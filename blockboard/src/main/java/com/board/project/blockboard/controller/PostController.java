package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.PostService;
import com.board.project.blockboard.util.SessionTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/boards/{boardid}/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;

    @PostMapping("/")
    @ResponseBody
    public void insertPost(@PathVariable("boardid") int boardid, @ModelAttribute PostDTO receivePost, HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SessionTokenizer session = new SessionTokenizer(request);
        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        receivePost.setUserID(userID);
        receivePost.setCompanyID(companyID);
        receivePost.setBoardID(boardid);

        postService.insertPost(receivePost);
    }

    @DeleteMapping("/{postid}")
    @ResponseBody
    public void deletePost(@PathVariable("postid") int postid) {
        postService.deletePost(postid);
    }

    // 현재 게시글을 에디터로 불러온다.
    @GetMapping("/{postid}/editor")
    @ResponseBody
    public Map<String, Object> selectPostByPostID(@PathVariable("postid") int postid) {
        Map<String, Object> map = new HashMap<String, Object>();
        PostDTO getPost = postService.selectPostByPostID(postid);
        log.debug(String.valueOf(postid));
        log.debug(String.valueOf(getPost.getPostID()));
        // 수정화면 들어갈 때 postID의 정보를 띄워준다.
        map.put("postTitle", getPost.getPostTitle());
        map.put("postContent", getPost.getPostContent());
        return map;
    }

    @PutMapping("/{postid}")
    @ResponseBody
    public void updatePost(@PathVariable("boardid") int boardid, @PathVariable("postid") int postid, @ModelAttribute PostDTO requestPost) {
        requestPost.setPostID(postid);
        requestPost.setBoardID(boardid);
        postService.updatePost(requestPost);
    }
}
