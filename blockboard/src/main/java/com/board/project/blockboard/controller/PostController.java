package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/board/post")
public class PostController {
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(getClass());
    @RequestMapping("/writeform")
    public String writeform() {
        logger.info("writeform");
        return "writeform";
    }
    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public void writePost(HttpServletRequest request) {
        PostDTO post = new PostDTO();
        post.setBoardID("444");
        post.setPostContent("우혁 테스트~~");
        postService.writePost(post);
    }
}
