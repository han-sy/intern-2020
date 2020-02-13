/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.Common;
import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.mapper.FileMapper;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Service
public class FileService {

  @Autowired
  private FileMapper fileMapper;

  private final String IMAGE_PATH = "/home1/irteam/storage";

  public String uploadFile(MultipartHttpServletRequest multipartRequest) {
    String uuid = Common.getNewUUID();
    log.info("uuid : " + uuid);
    Iterator<String> itr = multipartRequest.getFileNames();

    String filePath = ConstantData.ATTACH_FILE_PATH; //설정파일로 뺀다.
    String fileName = "";

    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명

      String storedFileName = uuid + "_" + originFileName;
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

  public void updateIDs(List<FileDTO> fileList) {
    for (FileDTO file : fileList) {
      Map<String, Object> fileAttributes = new HashMap<String, Object>();
      log.info("fileInfo : "+file.getPostID()+","+file.getCommentID()+","+file.getStoredFileName());
      fileAttributes.put("postID", file.getPostID());
      fileAttributes.put("commentID",file.getCommentID());
      fileAttributes.put("storedFileName", file.getStoredFileName());
      fileMapper.updateIDsByStoredFileName(fileAttributes);
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

  public void deleteFile(String storedFileName) {

    File file = new File(ConstantData.ATTACH_FILE_PATH + "/", storedFileName);
    if(file.exists()){
      if(file.delete()){
        log.info("파일삭제 성공");
        fileMapper.deleteFileByStoredFileName(storedFileName);
      }else{
          log.info("파일삭제 실패");
          //TODO 파일삭제 실패에 대한 에러처리
      }
    }else{
      log.info("파일 존재하지 않음");
      //TODO 존재하지 않는 파일을 삭제하려고 할시에 대한 에러처리
    }
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  // TODO 추후 디비 저장 & 삭제 구현할 것 ( 로컬 or AWS S3)
  public String uploadImage(HttpServletResponse response,
      MultipartHttpServletRequest multiFile) throws Exception {
    JsonObject json = new JsonObject();
    PrintWriter printWriter = null;
    OutputStream out = null;
    MultipartFile file = multiFile.getFile("upload");
    if (file != null) {
      if (file.getSize() > 0 && !StringUtil.isBlank(file.getName())) {
        if (file.getContentType().toLowerCase().startsWith("image/")) {
          try {
            String fileName = file.getName();
            byte[] bytes = file.getBytes();
            String uploadPath = IMAGE_PATH + ("/img");
            File uploadFile = new File(uploadPath);
            if (!uploadFile.exists()) {
              uploadFile.mkdirs();
            }
            fileName = UUID.randomUUID().toString();
            uploadPath = uploadPath + "/" + fileName;
            out = new FileOutputStream(new File(uploadPath));
            out.write(bytes);
            printWriter = response.getWriter();
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            String fileUrl = "img/" + fileName;

            json.addProperty("uploaded", 1);
            json.addProperty("fileName", fileName);
            json.addProperty("url", fileUrl);

            printWriter.println(json);
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            if (out != null) {
              out.close();
            }
            if (printWriter != null) {
              printWriter.close();
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  public byte[] getImage(String fileName)
      throws IOException {
    String path = IMAGE_PATH + ("/img/") + fileName;
    log.info(path);
    InputStream in = new FileInputStream(path);
    byte[] imageByteArray = IOUtils.toByteArray(in);
    in.close();
    return imageByteArray;
  }
}

