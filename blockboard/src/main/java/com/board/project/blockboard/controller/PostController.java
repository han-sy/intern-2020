package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private JwtService jwtService;
    /**
     * 게시물 작성
     * @param boardid 게시물을 올릴 게시판 id
     * @param receivePost 받은 게시물 정보
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    @ResponseBody
    public void insertPost(@PathVariable("boardid") int boardid, @ModelAttribute PostDTO receivePost) {
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();

        receivePost.setUserID(userID);
        receivePost.setCompanyID(companyID);
        receivePost.setBoardID(boardid);
        postService.insertPost(receivePost);
    }

    /**
     * 게시물 삭제
     * @param postid 삭제할 게시물 id
     * @return
     */
    @DeleteMapping("/{postid}")
    @ResponseBody
    public void deletePost(@PathVariable("postid") int postid) {
        postService.deletePost(postid);
    }

    /**
     * 수정 화면 진입시, 현재 게시글을 에디터로 불러온다.
     * @param postid 수정할 게시물 id
     * @return 게시물 제목, 내용
     */
    @GetMapping("/{postid}/editor")
    @ResponseBody
    public Map<String, Object> selectPostByPostID(@PathVariable("postid") int postid) {
        Map<String, Object> map = new HashMap<String, Object>();
        PostDTO getPost = postService.selectPostByPostID(postid);
        // 수정화면 들어갈 때 postID의 정보를 띄워준다.
        map.put("postTitle", getPost.getPostTitle());
        map.put("postContent", getPost.getPostContent());
        return map;
    }

    /**
     * 수정한 게시물을 저장할 때
     * @param boardid 수정 후 게시물을 올릴 게시판 id
     * @param postid 수정할 게시물 id
     * @param requestPost 수정할 게시물 제목과 내용이 담긴 객체
     * @return
     */
    @PutMapping("/{postid}")
    @ResponseBody
    public void updatePost(@PathVariable("boardid") int boardid, @PathVariable("postid") int postid, @ModelAttribute PostDTO requestPost) {
        requestPost.setPostID(postid);
        requestPost.setBoardID(boardid);
        postService.updatePost(requestPost);
    }
}
