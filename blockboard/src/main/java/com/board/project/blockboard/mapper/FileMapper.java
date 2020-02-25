package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.FileDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileMapper.java
 */
@Repository
@Mapper
public interface FileMapper {

  List<FileDTO> selectFileListByEditorID(FileDTO fileData);

  FileDTO selectFileByFileId(int fileId);

  FileDTO selectFileByStoredFileName(String storedFileName);

  boolean selectFileCheckByFileName(String fileName);

  void insertFile(FileDTO fileData);

  void updateIDsByStoredFileName(FileDTO fileData);

  void deleteFileByStoredFileName(String storedFileName);
}
