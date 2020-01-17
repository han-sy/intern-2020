package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostService{
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private BoardMapper boardMapper;

    public int getBoardIDByComIDAndBoardName(Map<String, Object> map) {
        return boardMapper.selectBoardIDByComIDAndBoardName(map);
    }
    public void insertPost(PostDTO post) {
        postMapper.insertPost(post);
    }
    public void deletePost(int postID) { postMapper.deletePostByPostID(postID);}
    public PostDTO getPostByPostID(int postID) { return postMapper.getPostByPostID(postID); }
    public void updatePost(PostDTO post) {postMapper.updatePost(post);}
}
