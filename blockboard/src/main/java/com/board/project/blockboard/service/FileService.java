/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileService.java
 */
package com.board.project.blockboard.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.FunctionID;
import com.board.project.blockboard.common.exception.UserValidException;
import com.board.project.blockboard.common.util.Common;
import com.board.project.blockboard.common.validation.AuthorityValidation;
import com.board.project.blockboard.common.validation.FileValidation;
import com.board.project.blockboard.common.validation.FunctionValidation;
import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.CommentMapper;
import com.board.project.blockboard.mapper.FileMapper;
import com.board.project.blockboard.mapper.PostMapper;
import com.board.project.blockboard.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Service
public class FileService {

  @Autowired
  private FunctionService functionService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private PostMapper postMapper;

  @Autowired
  private CommentMapper commentMapper;

  @Autowired
  private FileMapper fileMapper;

  @Autowired
  FunctionValidation functionValidation;

  @Autowired
  FileValidation fileValidation;

  @Autowired
  AuthorityValidation authorityValidation;

  public String uploadFile(MultipartHttpServletRequest multipartRequest, HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    int companyID = Integer.parseInt(request.getAttribute("companyID").toString());
    String uuid = Common.getNewUUID();
    Iterator<String> itr = multipartRequest.getFileNames();
    if (!functionValidation.isFunctionOn(companyID, FunctionID.POST_ATTACH_FILE,FunctionID.COMMENT_ATTACH_FILE, response)) {
      return null;
    }
    String fileName = "";
    String url = "";
    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명
      String storedFileName = uuid + "_" + originFileName;
      ObjectMetadata metadata = new ObjectMetadata();
      AmazonS3Service amazonS3Service = new AmazonS3Service();

      url = amazonS3Service
          .upload(storedFileName, ConstantData.BUCKET_FILE, mpf.getInputStream(), metadata, "");
      log.info("url -->" + url);
      long fileSize = mpf.getSize();
      //파일 전체 경로

      log.info("originFileName => " + originFileName);

      log.info("fileName => " + mpf.getName());
      fileName = storedFileName;

      Map<String, Object> fileAttributes = new HashMap<String, Object>();
      fileAttributes.put("resourceUrl", url);
      fileAttributes.put("originFileName", originFileName);
      fileAttributes.put("storedFileName", storedFileName);
      fileAttributes.put("fileSize", fileSize);
      fileMapper.insertFile(fileAttributes);

    }
    log.info("fileName => " + fileName);
    return fileName;
  }

  public void updateIDs(List<FileDTO> fileList, HttpServletRequest request,
      HttpServletResponse response) {
    int companyID = Integer.parseInt(request.getAttribute("companyID").toString());
    if (!functionValidation.isFunctionOn(companyID, FunctionID.POST_ATTACH_FILE,FunctionID.COMMENT_ATTACH_FILE, response)) {
      return;
    }
    for (FileDTO file : fileList) {
      Map<String, Object> fileAttributes = new HashMap<String, Object>();
      log.info("fileInfo : " + file.getPostID() + "," + file.getCommentID() + "," + file
          .getStoredFileName());
      fileAttributes.put("postID", file.getPostID());
      fileAttributes.put("commentID", file.getCommentID());
      fileAttributes.put("storedFileName", file.getStoredFileName());
      fileMapper.updateIDsByStoredFileName(fileAttributes);
    }
  }

  public List<FileDTO> getFileList(int postID, int commentID) {
    Map<String, Object> fileAttributes = new HashMap<String, Object>();
    fileAttributes.put("postID", postID);
    fileAttributes.put("commentID", commentID);
    return fileMapper.selectFileListByEditorID(fileAttributes);
  }

  public void downloadFile(int fileID, HttpServletResponse response, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    if (!functionValidation
        .isFunctionOn(userData.getCompanyID(), FunctionID.POST_ATTACH_FILE,FunctionID.COMMENT_ATTACH_FILE, response)) {
      return;
    }
    FileDTO fileData = fileMapper.selectFileByFileID(fileID);

    String browser = request.getHeader("User-Agent");//브라우저 종류 가져옴.
    String downName = null;

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

      AmazonS3Service amazonS3Service = new AmazonS3Service();
      S3ObjectInputStream s3is = amazonS3Service
          .download(fileData.getStoredFileName(), ConstantData.BUCKET_FILE, response);
      int ncount = 0;
      byte[] bytes = new byte[512];

      while ((ncount = s3is.read(bytes)) != -1) {
        os.write(bytes, 0, ncount);
      }
      s3is.close();
      os.close();
    } catch (FileNotFoundException ex) {
      log.info("FileNotFoundException");
    } catch (IOException ex) {
      log.info("IOException");
    }

  }


  public String getFileWriterUserID(FileDTO fileData) {
    if (fileData.getPostID() > 0) {//post의 첨부파일일때
      return postMapper.selectUserIDByPostID(fileData.getPostID());
    } else if (fileData.getCommentID() > 0) {//댓글의 첨부파일일때
      return commentMapper.selectUserIDByCommentID(fileData.getCommentID());
    }
    return null;
  }

  public void deleteFile(String storedFileName, HttpServletRequest request,
      HttpServletResponse response) {
    UserDTO userData = new UserDTO(request);
    if (!functionValidation
        .isFunctionOn(userData.getCompanyID(), FunctionID.POST_ATTACH_FILE,FunctionID.COMMENT_ATTACH_FILE, response)
        || !fileValidation.isExistFileInDatabase(storedFileName, response)) {
      return;
    }
    FileDTO fileData = fileMapper.selectFileByStoredFileName(storedFileName);
    if (!authorityValidation.isWriter(fileData, userData, response)) {
      return;
    }
    AmazonS3Service amazonS3Service = new AmazonS3Service();
    if (amazonS3Service.deleteFile(storedFileName, ConstantData.BUCKET_FILE, response)) {
      log.info("파일삭제 성공");
      fileMapper.deleteFileByStoredFileName(storedFileName);
    } else {
      log.info("파일삭제 실패");
      //TODO 파일삭제 실패에 대한 에러처리
    }
  }

  /**
   * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
   */
  // TODO 추후 디비 저장 & 삭제 구현할 것 ( 로컬 or AWS S3)
  public String uploadImage(HttpServletResponse response,
      MultipartHttpServletRequest multiFile, HttpServletRequest request) throws Exception {
    int companyID = Integer.parseInt(request.getAttribute("companyID").toString());
    if (!(functionValidation
        .isFunctionOn(companyID, FunctionID.POST_INLINE_IMAGE, FunctionID.COMMENT_INLINE_IMAGE,
            response))) {
      return null;
    }
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    JsonObject json = new JsonObject();
    PrintWriter printWriter = null;
    OutputStream out = null;
    MultipartFile file = multiFile.getFile("upload");
    if (file != null) {
      if (file.getSize() > 0 && !StringUtil.isBlank(file.getName())) {
        if (file.getContentType().toLowerCase().startsWith("image/")) {
          try {
            String fileName = file.getName();
            ObjectMetadata metadata = new ObjectMetadata();
            AmazonS3Service amazonS3Service = new AmazonS3Service();
            fileName = Common.getNewUUID();
            String fileUrl = amazonS3Service
                .upload(fileName, ConstantData.BUCKET_INLINE, file.getInputStream(), metadata, "");
            printWriter = response.getWriter();
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");

            json.addProperty("uploaded", 1);
            json.addProperty("fileName", fileName);
            json.addProperty("url", fileUrl);

            if (functionService.getFunctionStatus(companyID, FunctionID.POST_AUTO_TAG)) {
              List<UserDTO> detectedUsers = detectedUserList(fileName, companyID);
              json.add("detectedUser", new Gson().toJsonTree(detectedUsers));
            }
            printWriter.println(json);
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            if (printWriter != null) {
              printWriter.close();
            }
          }
        }
      }
    }
    return null;
  }

  public List<UserDTO> detectedUserList(String fileName, int companyID) throws IOException {
    String collectionID = Common.getNewUUID();
    AmazonRekognitionService amazonRekognitionService = new AmazonRekognitionService();
    //collection 등록
    amazonRekognitionService.registerCollection(collectionID);
    //collection에 이미지 등록
    amazonRekognitionService
        .registerImageToCollection(fileName, ConstantData.BUCKET_INLINE, collectionID);
    //
    List<UserDTO> detectedUsers = new ArrayList<>();
    List<UserDTO> userList = userMapper.selectUsersByCompanyID(companyID);
    DetectThread detectThread = null;
    for (UserDTO user : userList) {
      detectThread = new DetectThread(user, collectionID, amazonRekognitionService, detectedUsers);
      detectThread.start();
    }
    try {
      detectThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      log.info("종료");
      if (collectionID != null) {
        amazonRekognitionService.deleteCollection(collectionID);
      }
      return detectedUsers;
    }
  }

  public boolean isExistFile(String fileName) {
    return fileMapper.selectFileCheckByFileName(fileName);
  }


  class DetectThread extends Thread {

    private UserDTO user;
    private String collectionID;
    private AmazonRekognitionService amazonRekognitionService;
    private boolean detected;
    private List<UserDTO> detectedUsers;

    DetectThread(UserDTO user, String collectionID,
        AmazonRekognitionService amazonRekognitionService, List<UserDTO> detectedUsers) {
      this.user = user;
      this.collectionID = collectionID;
      this.amazonRekognitionService = amazonRekognitionService;
      this.detected = false;
      this.detectedUsers = detectedUsers;
    }

    @Override
    public void run() {
      if (user.getImageFileName() != null) {
        try {
          detected = amazonRekognitionService
              .searchFaceMatchingImageCollection(ConstantData.BUCKET_USER,
                  user.getImageFileName(),
                  collectionID);
          if (detected) {
            detectedUsers.add(user);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}

