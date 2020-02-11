/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.service.FileService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@RestController
@RequestMapping("")
public class FileController {
  @Autowired
  private FileService fileService;
  @PostMapping(value = "/fileUpload")
  public String uploadFile(MultipartHttpServletRequest multipartRequest) {
    return fileService.uploadFile(multipartRequest);

  }

  @PutMapping(value = "/fileUpload")
  public void updatePostIdToFile(@RequestBody List<FileDTO> fileList ){
    fileService.updatePostID(fileList);
  }
}
