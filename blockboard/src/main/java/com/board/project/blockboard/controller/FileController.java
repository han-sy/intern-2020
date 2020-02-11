/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.service.FileService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@RestController
@RequestMapping("")
public class FileController {
  @Autowired
  private FileService fileService;
  @PostMapping(value = "/files")
  public String uploadFile(MultipartHttpServletRequest multipartRequest) {
    return fileService.uploadFile(multipartRequest);

  }

  @PutMapping(value = "/files")
  public void updatePostIdToFile(@RequestBody List<FileDTO> fileList ){
    fileService.updatePostID(fileList);
  }

  @GetMapping(value = "/files")
  public List<FileDTO> updatePostIdToFile(@RequestParam int postID){

    return fileService.getFileList(postID);
  }

  @GetMapping(value = "/files/{fileid}")
  public void downloadFile(@PathVariable("fileid") int fileID, HttpServletResponse response,
      HttpServletRequest request){
    fileService.downloadFile(fileID,response,request);
  }
}
