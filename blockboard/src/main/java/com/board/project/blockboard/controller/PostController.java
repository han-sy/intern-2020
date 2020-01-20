package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.PostService;
import com.board.project.blockboard.util.AES256Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

@Slf4j
@Controller
@RequestMapping("/boards/{boardID}/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;

    private String key = "slgi3ibu5phi8euf";

    @PostMapping(value = "/insert")
    @ResponseBody
    public void insertPost(@ModelAttribute PostDTO receivePost, HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cookie[] cookies = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String decode = "";
        String userID = "";
        int companyID = -1;

        if(cookies != null){
            for(Cookie c : cookies){
                if(c.getName().equals("sessionID")) {
                    decode = aes256.aesDecode(codec.decode(c.getValue()));
                    log.info(decode);

                    // token[0]=userID, token[1]=companyID, token[2]=serverToken
                    StringTokenizer tokenizer = new StringTokenizer(decode, "#");
                    userID = tokenizer.nextToken();
                    companyID = Integer.parseInt(tokenizer.nextToken());
                }
            }
        }
        receivePost.setUserID(userID);
        receivePost.setCompanyID(companyID);
        postService.insertPost(receivePost);
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
