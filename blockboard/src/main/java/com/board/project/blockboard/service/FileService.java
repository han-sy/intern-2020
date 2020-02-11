/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.Time;
import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.mapper.FileMapper;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Service
public class FileService {

  @Autowired
  private FileMapper fileMapper;

  public String uploadFile(MultipartHttpServletRequest multipartRequest) {
    String now = Time.getTime();
    log.info("now : "+now);
    Iterator<String> itr = multipartRequest.getFileNames();

    String filePath = ConstantData.ATTACH_FILE_PATH; //설정파일로 뺀다.
    String fileName="";

    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명

      String storedFileName = "file_attach_"+now+"_"+originFileName;
      String fileFullPath = filePath + "/" + storedFileName;
      long fileSize =mpf.getSize();
      File file = new File(fileFullPath);
      //파일 전체 경로

      try {
        mpf.transferTo(file);
        log.info("originFileName => " + originFileName);
        log.info("fileFullPath => " + fileFullPath);
        log.info("fileName => " + mpf.getName());
        fileName = storedFileName;

        Map<String, Object> fileAttributes = new HashMap<String, Object>();
        fileAttributes.put("originFileName", originFileName);
        fileAttributes.put("storedFileName", storedFileName);
        fileAttributes.put("fileSize", fileSize);
        fileMapper.insertFile(fileAttributes);
      } catch (Exception e) {
        //System.out.println("postTempFile_ERROR======>" + fileFullPath);
        e.printStackTrace();
      }

    }
    System.out.println("fileName => " + fileName);
    return fileName;
  }

  public void updatePostID(List<FileDTO> fileList) {
    for(FileDTO file:fileList){
      Map<String,Object> fileAttributes = new HashMap<String, Object>();
      fileAttributes.put("postID",file.getPostID());
      fileAttributes.put("storedFileName",file.getStoredFileName());
      fileMapper.updatePostIDByStoredFileName(fileAttributes);
    }
  }
}
