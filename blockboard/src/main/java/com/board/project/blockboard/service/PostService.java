package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.mapper.PostMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PostService{
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private BoardMapper boardMapper;

    public void insertPost(PostDTO post) {
        postMapper.insertPost(post);

    }
    public void deletePost(int postID) {
        postMapper.deletePostByPostID(postID);
    }
    public PostDTO selectPostByPostID(int postID) {
        return postMapper.selectPostByPostID(postID);
    }
    public void updatePost(PostDTO post) {
        postMapper.updatePost(post);
    }
}
