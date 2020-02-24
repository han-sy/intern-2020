package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.FileDTO;
import java.io.File;
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

  void insertFile(FileDTO fileData);

  void updateIDsByStoredFileName(FileDTO fileData);

  List<FileDTO> selectFileListByEditorID(FileDTO fileData);

  FileDTO selectFileByFileId(int fileId);

  void deleteFileByStoredFileName(String storedFileName);

  boolean selectFileCheckByFileName(String fileName);

  FileDTO selectFileByStoredFileName(String storedFileName);
}
