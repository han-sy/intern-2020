package com.board.project.blockboard.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileMapper.java
 */
@Repository
@Mapper
public interface FileMapper {

  void insertFile(Map<String, Object> fileAttributes);

  void updatePostIDByStoredFileName(Map<String, Object> fileAttributes);
}
