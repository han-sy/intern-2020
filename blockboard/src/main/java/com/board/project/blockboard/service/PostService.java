/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    PostService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.mapper.CommentMapper;
import com.board.project.blockboard.mapper.PostMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostService{
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private CommentMapper commentMapper;

    public void insertPost(PostDTO post) {
        log.info("post ID<" + post.getPostID() + ">");
        log.info("post Temp?<" + post.getIsTemp() + ">");
        postMapper.insertPost(post);
    }
    public void deletePost(int postID) {
        commentMapper.deleteCommentsByPostID(postID);
        postMapper.deletePostByPostID(postID);
    }
    public PostDTO selectPostByPostID(int postID) {

        return postMapper.selectPostByPostID(postID);
    }
    public void updatePost(PostDTO post) {

        postMapper.updatePost(post);
    }
    public List<PostDTO> searchPost(String option, String keyword) {

        return postMapper.search(option,keyword);
    }
    public PostDTO selectRecentTemp(Map<String, Object> param) {
        return postMapper.selectRecentTempPost(param);
    }
    public List<PostDTO> getTempPosts(Map<String,Object> param) {
        return postMapper.selectTempPosts(param);
    }

    /**
     * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
     * @param boardID
     * @return
     */
    public List<PostDTO> getPostListByBoardID(int boardID) {
        List<PostDTO> postlist = postMapper.selectPostByBoardID(boardID);
        return postlist;
    }

    /**
     * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
     * @param postID
     * @return
     */
    public PostDTO getPostByPostID(int postID) {
        PostDTO post = postMapper.selectPostByPostID(postID);
        return post;
    }
}
