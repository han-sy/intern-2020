package com.board.project.blockboard.mapper;

<<<<<<< HEAD
import com.board.project.blockboard.dto.BoardDTO;
=======
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
import com.board.project.blockboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.Map;

@Repository
@Mapper
public interface PostMapper {
    void insertPost(PostDTO post);
    BoardDTO getBoardId(Map<String, Object> map);
=======
@Repository
@Mapper
public interface PostMapper {
    void writePost(PostDTO post);
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
}
