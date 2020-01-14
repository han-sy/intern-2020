package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class PostServiceImpl implements PostService{
    @Autowired
    private PostMapper postMapper;

    @Override
    public void writePost(PostDTO post) {

    }
}
