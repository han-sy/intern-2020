package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
<<<<<<< HEAD
import com.board.project.blockboard.mapper.PostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;

    public void insertPost(PostDTO post) {
        postMapper.insertPost(post);
    }

    public int getBoardId(Map<String, Object> map) {
        return postMapper.getBoardId(map).getBoard_id();
    }
=======

public interface PostService {
    void writePost(PostDTO post);
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
}
