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

    public void insertPost(PostDTO post) {
        postMapper.insertPost(post);
    }
    public int getBoardID(Map<String, Object> map) {
        return boardMapper.getBoardIDByComIDAndBoardName(map).getBoardID();
    }
}
