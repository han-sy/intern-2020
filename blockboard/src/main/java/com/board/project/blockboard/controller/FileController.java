/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.service.FileService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  /**
   * 드래그앤 드랍후 파일을 바로 업로드
   */
  @PostMapping(value = "/files")
  public String uploadFile(MultipartHttpServletRequest multipartRequest) throws IOException {
    return fileService.uploadFile(multipartRequest);

  }

  /**
   * 파일 테이블에 postID 업데이트 최종으로 리스트에 추가된 목록을 기준으로
   */
  @PutMapping(value = "/files")
  public void updateIdToFile(@RequestBody List<FileDTO> fileList ){
    log.info("!!!"+fileList.toString());
    fileService.updateIDs(fileList);
  }

  /**
   *  게시물 id를 가지고 게시물 첨부파일 리스트 가져오기
   */
  @GetMapping(value = "/files")
    public List<FileDTO> getFileList(@RequestParam int postID,@RequestParam int commentID){

    return fileService.getFileList(postID,commentID);
  }
  /**
   * 파일 삭제
   */
  @DeleteMapping(value = "/files")
  public void deleteFile(@RequestParam String storedFileName){
    log.info("storedFileName : "+storedFileName);
    fileService.deleteFile(storedFileName);
  }

  /**
   * 파일 다운로드
   */
  @GetMapping(value = "/files/{fileid}")
  public void downloadFile(@PathVariable("fileid") int fileID, HttpServletResponse response,
      HttpServletRequest request){
    fileService.downloadFile(fileID,response,request);
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  @PostMapping("/imageUpload")
  public String uploadImage(HttpServletResponse response, MultipartHttpServletRequest multiFile)
      throws Exception {
    return fileService.uploadImage(response, multiFile);
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  @GetMapping("/img/{fileName}")
  public byte[] getImage(@PathVariable("fileName") String fileName)
      throws IOException {
    return fileService.getImage(fileName);
  }
}
