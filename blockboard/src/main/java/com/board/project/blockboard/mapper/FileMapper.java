package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.FileDTO;
import java.util.List;
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

  void updateIDsByStoredFileName(Map<String, Object> fileAttributes);

  List<FileDTO> selectFileListByPostID(int postID);

  FileDTO selectFileByFileID(int fileID);

  void deleteFileByStoredFileName(String storedFileName);
}
