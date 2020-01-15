package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PostService {
    void insertPost(PostDTO post);
    int getBoard_id(Map<String, Object> map);
}
