package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.PostService;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
    public List<PostDTO> searchPost(@RequestParam("option") String option, @RequestParam("keyword") String keyword) {
        switch (option) {
            case "제목":
                option = "post_title";
                break;
            case "내용":
                option = "post_content";
                break;
            case "작성자":
                option = "user_name";
                break;
            default:
                option = "mix";
        }
        return postService.searchPost(option, keyword);
    }
}
