package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.mapper.CommentMapper;
import com.board.project.blockboard.mapper.FunctionMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;


    public List<CommentDTO> getCommentListByPostID(int postID) {
        return commentMapper.selectCommentsByPostID(postID);
    }
}
