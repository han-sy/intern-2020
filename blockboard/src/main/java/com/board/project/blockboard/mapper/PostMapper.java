package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PostMapper {
    void writePost(PostDTO post);
}
