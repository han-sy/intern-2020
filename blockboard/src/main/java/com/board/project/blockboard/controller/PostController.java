package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.PostService;
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

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public void writePost(HttpServletRequest request) {
        PostDTO post = new PostDTO();
        post.setBoard_id("444");
        post.setPost_content("우혁 테스트~~");
        postService.writePost(post);
    }
}
