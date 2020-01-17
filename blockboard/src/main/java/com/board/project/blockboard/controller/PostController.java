package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.PostService;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.util.WebUtils.getCookie;

@Controller
@RequestMapping("/board/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;

    private String key = "slgi3ibu5phi8euf";
    private String userID;
    private int companyID;
    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/insert")
    @ResponseBody
    public void insertPost(HttpServletRequest request) throws UnsupportedEncodingException {
        PostDTO post = new PostDTO();
        Map<String, Object> map_Board = new HashMap<String, Object>();

        Cookie[] cookies = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String decode = null;

        for(int i=0; i<cookies.length; i++) {
            Cookie c = cookies[i];
            String name = c.getName();
            String value = c.getValue();
            if (name.equals("sessionID")) {
                try {
                    decode = aes256.aesDecode(codec.decode(value));
                    userID = decode.substring(0, decode.length() - 6); // id 자르기
                    logger.info(decode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        post.setUserID(userID);
        companyID = boardService.getCompanyIDByUserID(userID);
        map_Board.put("companyID", companyID);
        map_Board.put("boardName", request.getParameter("boardName"));
        post.setCompanyID(companyID);
        post.setPostTitle(request.getParameter("postTitle"));
        post.setPostContent(request.getParameter("postContent"));
        post.setBoardID(postService.getBoardID(map_Board));
        postService.insertPost(post);
    }
}
