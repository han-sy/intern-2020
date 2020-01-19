package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.PostService;
import com.board.project.blockboard.util.AES256Util;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/board/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;

    private String key = "slgi3ibu5phi8euf";
    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/insert")
    @ResponseBody
    public void insertPost(@ModelAttribute PostDTO receivePost, HttpServletRequest request) throws UnsupportedEncodingException {
        PostDTO sendPost = new PostDTO();
        Map<String, Object> map_Board = new HashMap<String, Object>();

        Cookie[] cookies = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String userID = "", decode = null;
        int companyID;

        for(Cookie c : cookies) {
            if (c.getName().equals("sessionID")) {
                try {
                    decode = aes256.aesDecode(codec.decode(c.getValue()));
                    userID = decode.substring(0, decode.length() - 6); // id 자르기
                    logger.info(decode);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        logger.info("BoardID = " + receivePost.getBoardID());

        sendPost.setUserID(userID);
        companyID = boardService.getCompanyIDByUserID(userID);
        sendPost.setCompanyID(companyID);
        sendPost.setPostTitle(receivePost.getPostTitle());
        sendPost.setPostContent(receivePost.getPostContent());
        sendPost.setBoardID(receivePost.getBoardID());
        postService.insertPost(sendPost);

    }

    @DeleteMapping("/delete")
    @ResponseBody
    public void deletePost(@RequestParam String postID) {
        postService.deletePost(Integer.parseInt(postID));
    }

    @GetMapping("/{postID}")
    @ResponseBody
    public Map<String, Object> getPostByPostID(@PathVariable("postID") int postID) {
        Map<String, Object> map = new HashMap<String, Object>();
        PostDTO getPost = new PostDTO(postService.getPostByPostID(postID));
        map.put("postTitle", getPost.getPostTitle());
        map.put("postContent", getPost.getPostContent());
        return map;
    }

    @PutMapping("/update")
    @ResponseBody
    public void updatePost(@ModelAttribute PostDTO requestPost) {
        postService.updatePost(requestPost);
    }
}
