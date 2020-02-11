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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    log.info("now : " + now);
    Iterator<String> itr = multipartRequest.getFileNames();

    String filePath = ConstantData.ATTACH_FILE_PATH; //설정파일로 뺀다.
    String fileName = "";

    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명

      String storedFileName = "file_attach_" + now + "_" + originFileName;
      String fileFullPath = filePath + "/" + storedFileName;
      long fileSize = mpf.getSize();
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
    for (FileDTO file : fileList) {
      Map<String, Object> fileAttributes = new HashMap<String, Object>();
      fileAttributes.put("postID", file.getPostID());
      fileAttributes.put("storedFileName", file.getStoredFileName());
      fileMapper.updatePostIDByStoredFileName(fileAttributes);
    }
  }

  public List<FileDTO> getFileList(int postID) {
    return fileMapper.selectFileListByPostID(postID);
  }

  public void downloadFile(int fileID, HttpServletResponse response, HttpServletRequest request) {
    String filePath = ConstantData.ATTACH_FILE_PATH;
    FileDTO fileData = fileMapper.selectFileByFileID(fileID);
    File file = new File(filePath + "/", fileData.getStoredFileName());
    String browser = request.getHeader("User-Agent");//브라우저 종류 가져옴.
    String downName = null;

    FileInputStream fileInputStream = null;
    ServletOutputStream servletOutputStream = null;

    try {
      if (browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")) {
        downName = URLEncoder.encode(fileData.getOriginFileName(), "UTF-8")
            .replaceAll("\\+", "%20");
      } else {
        downName = new String(fileData.getOriginFileName().getBytes("UTF-8"), "ISO-8859-1");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    response.setHeader("Content-Disposition", "attachment;filename=\"" + downName + "\"");

    response.setContentType("application/octer-stream");
    response.setHeader("Content-Transfer-Encoding", "binary;");

    try {
      OutputStream os = response.getOutputStream();
      FileInputStream fis = new FileInputStream(filePath + "/" + fileData.getStoredFileName());

      int ncount = 0;
      byte[] bytes = new byte[512];

      while ((ncount = fis.read(bytes)) != -1) {
        os.write(bytes, 0, ncount);
      }
      fis.close();
      os.close();
    } catch (FileNotFoundException ex) {
      System.out.println("FileNotFoundException");
    } catch (IOException ex) {
      System.out.println("IOException");
    }

  }
}

