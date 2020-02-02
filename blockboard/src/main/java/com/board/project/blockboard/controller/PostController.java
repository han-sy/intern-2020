/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    PostController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.PostService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
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
    @PostMapping("")
    public void insertPost(@PathVariable("boardid") int boardid, @ModelAttribute PostDTO receivePost) {
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();
        log.info("받음 temp 변수 = " + receivePost.getIsTemp());
        receivePost.setUserID(userID);
        receivePost.setCompanyID(companyID);
        receivePost.setBoardID(boardid);
        postService.insertPost(receivePost);
    }
    /**
     * 게시물 목록 띄우기
     * boardid 받아와서 해당하는 게시판의 게시글목록들 리턴
     * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
     * @param boardID
     * @return
     */
    @GetMapping("")
    public List<PostDTO> getPostListByBoardID(@PathVariable("boardid") int boardID) {
        List<PostDTO> postList = boardService.getPostListByBoardID(boardID);
        return postList;
    }
    /**
     * 게시물 삭제
     * @param postid 삭제할 게시물 id
     * @return
     */
    @DeleteMapping("/{postid}")
    public void deletePost(@PathVariable("postid") int postid) {
        postService.deletePost(postid);
    }

    /**
     * 수정 화면 진입시, 현재 게시글을 에디터로 불러온다.
     * @param postid 수정할 게시물 id
     * @return 게시물 제목, 내용
     */
    @GetMapping("/{postid}/editor")
    public PostDTO selectPostByPostID(@PathVariable("postid") int postid) {
        return postService.selectPostByPostID(postid);
    }

    /**
     * 수정한 게시물을 저장할 때
     * @param boardid 수정 후 게시물을 올릴 게시판 id
     * @param postid 수정할 게시물 id
     * @param requestPost 수정할 게시물 제목과 내용이 담긴 객체
     * @return
     */
    @PutMapping("/{postid}")
    public void updatePost(@PathVariable("boardid") int boardid, @PathVariable("postid") int postid, @ModelAttribute PostDTO requestPost) {
        requestPost.setPostID(postid);
        requestPost.setBoardID(boardid);
        postService.updatePost(requestPost);
    }

    /**
     * 수정한 게시물을 저장할 때
     * @param option 검색 옵션 (ex : 제목, 내용, 작성자)
     * @param keyword 검색할 문자열
     * @return searchList 검색한 결과들이 담긴
     */
    @GetMapping("/search")
    public List<PostDTO> searchPost(@RequestParam("option") int option, @RequestParam("keyword") String keyword) {
        String optionName = "";
        switch (option) {
            case 0:
                optionName = "post_title";
                break;
            case 1:
                optionName = "user_name";
                break;
            case 2:
                optionName = "post_content";
                break;
            case 3:
                optionName = "mix";
                break;
            default:
                // 추후에 Exception 구현하기
        }
        return postService.searchPost(optionName, keyword);
    }

    /**
     * 가장 최근에 임시저장된 게시글 가져올 때
     * @return 가장 최근에 임시저장된 게시물 객체
     */
    @GetMapping("/recent")
    public PostDTO recentTempPost() {
        Map<String, Object> param = new HashMap<>();
        param.put("userID", jwtService.getUserId());
        param.put("companyID", jwtService.getCompanyId());
        return postService.selectRecentTemp(param);
    }

    /**
     * 임시 저장 게시물 가져올 때
     * @return 임시 저장된 게시물 목록
     */
    @GetMapping("/temp")
    public List<PostDTO> getTempPosts() {
        Map<String, Object> param = new HashMap<>();
        param.put("userID", jwtService.getUserId());
        param.put("companyID", jwtService.getCompanyId());
        param.put("userName", jwtService.getUserName());
        return postService.getTempPosts(param);
    }
}
