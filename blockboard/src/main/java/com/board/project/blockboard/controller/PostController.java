package com.board.project.blockboard.controller;

import com.board.project.blockboard.Data.CurrentUserInfo;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/board/post")
public class PostController {
    @Autowired
    private PostService postService;
    CurrentUserInfo currentUserInfo = CurrentUserInfo.getInstance();
    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(path = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> writePost(HttpServletRequest request) {
        PostDTO post = new PostDTO();
        Map<String,Object> map = new HashMap<String, Object>();
        String board_name = request.getParameter("post_board_name"); // ajax에서 전송한 게시판이름 저장

        post.setUser_id(currentUserInfo.getUser_id()); // 게시글의 user_id 설정
        post.setCom_id(currentUserInfo.getCom_id()); // 게시글의 com_id 설정
        map.put("board_name", board_name);
        map.put("company_id", post.getCom_id());
        int board_id = postService.getBoardId(map); // board_name으로 board_id 가져오기

        post.setBoard_id(board_id); // 게시글의 board_id 설정
        post.setPost_title(request.getParameter("post_title"));
        post.setPost_content(request.getParameter("post_content"));

        postService.insertPost(post);
        return map;
    }
}
