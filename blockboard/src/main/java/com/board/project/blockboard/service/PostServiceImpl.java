package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.PostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostMapper postMapper;

    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void insertPost(PostDTO post) {
        postMapper.insertPost(post);
    }

    @Override
    public int getBoard_id(Map<String, Object> map) {
        return postMapper.getBoard_id(map).getBoard_id();
    }
}
