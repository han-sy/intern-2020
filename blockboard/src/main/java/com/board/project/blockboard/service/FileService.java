/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileService.java
 */
package com.board.project.blockboard.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.board.project.blockboard.common.constant.ConstantData.Bucket;
import com.board.project.blockboard.common.constant.ConstantData.EditorName;
import com.board.project.blockboard.common.constant.ConstantData.FunctionId;
import com.board.project.blockboard.common.exception.FileValidException;
import com.board.project.blockboard.common.exception.FunctionValidException;
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
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
  private FunctionValidation functionValidation;

  @Autowired
  private FileValidation fileValidation;

  @Autowired
  private AuthorityValidation authorityValidation;

  @Autowired
  private AmazonS3Service amazonS3Service;

  @Autowired
  private AmazonRekognitionService amazonRekognitionService;

  /**
   * 파일 업로드
   *
   * @return 파일이름
   */
  public String uploadFile(MultipartHttpServletRequest multipartRequest, int companyId)
      throws IOException, FunctionValidException {
    String uuid = Common.getNewUUID();
    Iterator<String> itr = multipartRequest.getFileNames();
    if (!functionValidation
        .isFunctionOn(companyId, FunctionId.POST_ATTACH_FILE, FunctionId.COMMENT_ATTACH_FILE)) {
      return null;
    }
    FileDTO fileData = new FileDTO();
    while (itr.hasNext()) {
      setFileData(multipartRequest, uuid, itr, fileData);
      //파일 전체 경로
      fileMapper.insertFile(fileData);
    }
    log.info("fileData", fileData);
    return fileData.getStoredFileName();
  }

  /**
   * 파일 데이터 setting
   */
  private void setFileData(MultipartHttpServletRequest multipartRequest, String uuid,
      Iterator<String> itr, FileDTO fileData) throws IOException {
    MultipartFile mpf = multipartRequest.getFile(itr.next());

    fileData.setOriginFileName(mpf.getOriginalFilename());
    fileData.setStoredFileName(uuid + "_" + fileData.getOriginFileName());
    ObjectMetadata metadata = new ObjectMetadata();

    String url = amazonS3Service
        .upload(fileData.getStoredFileName(), Bucket.FILE, mpf.getInputStream(), metadata);
    fileData.setResourceUrl(url);
    fileData.setFileSize(mpf.getSize());
  }

  /**
   * id 업데이트 to 파일테이블
   */
  public void updateIDs(List<FileDTO> fileList, int companyId) throws FunctionValidException {
    if (!functionValidation
        .isFunctionOn(companyId, FunctionId.POST_ATTACH_FILE, FunctionId.COMMENT_ATTACH_FILE)) {
      return;
    }
    fileList.forEach(fileDTO -> fileMapper.updateIDsByStoredFileName(fileDTO));
  }

  /**
   * 파일리스트 반환
   */
  public List<FileDTO> getFileList(int postId, int commentId) {
    FileDTO fileDTO = new FileDTO();
    fileDTO.setPostId(postId);
    fileDTO.setCommentId(commentId);
    return fileMapper.selectFileListByEditorID(fileDTO);
  }

  /**
   * 파일 다운로드 (id를 가지고
   */
  public void downloadFile(int fileId, HttpServletResponse response, HttpServletRequest request,
      int companyId)
      throws IOException, FileValidException {
    if (!functionValidation
        .isFunctionOn(companyId, FunctionId.POST_ATTACH_FILE, FunctionId.COMMENT_ATTACH_FILE)) {
      return;
    }
    FileDTO fileData = fileMapper.selectFileByFileId(fileId);
    setRequestAndResponseForDownload(response, request, fileData);
    writeFile(response, fileData);

  }

  /**
   * 파일쓰기
   */
  private void writeFile(HttpServletResponse response, FileDTO fileData)
      throws IOException, FileValidException {
    OutputStream os = response.getOutputStream();

    S3ObjectInputStream s3is = amazonS3Service
        .download(fileData.getStoredFileName(), Bucket.FILE);
    int ncount = 0;
    byte[] bytes = new byte[512];

    while ((ncount = s3is.read(bytes)) != -1) {
      os.write(bytes, 0, ncount);
    }
    s3is.close();
    os.close();
  }

  /**
   * request와 response 설정
   */
  private void setRequestAndResponseForDownload(HttpServletResponse response,
      HttpServletRequest request, FileDTO fileData) {
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
  }


  /**
   * 파일 작성자 id 가져오기
   */
  public String getFileWriterUserId(FileDTO fileData) {
    if (fileData.getPostId() > 0) {//post의 첨부파일일때
      return postMapper.selectUserIdByPostId(fileData.getPostId());
    } else if (fileData.getCommentId() > 0) {//댓글의 첨부파일일때
      return commentMapper.selectUserIdByCommentId(fileData.getCommentId());
    }
    return null;
  }

  /**
   * 파일삭제
   */
  public void deleteFile(String storedFileName, HttpServletRequest request)
      throws FileValidException {
    UserDTO userData = new UserDTO(request);
    if (!canDeleteFile(storedFileName, userData)) {
      return;
    }
    FileDTO fileData = fileMapper.selectFileByStoredFileName(storedFileName);
    if (!authorityValidation.isWriter(fileData, userData)) {
      return;
    }
    deleteFileInAmazonS3(storedFileName);
  }

  /**
   * 파일삭제가능여부 반환
   */
  private boolean canDeleteFile(String storedFileName,
      UserDTO userData) {
    return functionValidation
        .isFunctionOn(userData.getCompanyId(), FunctionId.POST_ATTACH_FILE,
            FunctionId.COMMENT_ATTACH_FILE)
        && fileValidation.isExistFileInDatabase(storedFileName);
  }

  /**
   * amazonS3파일 삭제
   */
  private void deleteFileInAmazonS3(String storedFileName)
      throws FileValidException {
    if (amazonS3Service.deleteFile(storedFileName, Bucket.FILE)) {
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
  @SneakyThrows
  public String uploadImage(HttpServletResponse response, MultipartHttpServletRequest multiFile,
      int companyId, String editorName) {

    if (StringUtils.equals(editorName, "editor")) {
      if (!(functionValidation.isFunctionOn(companyId, FunctionId.POST_INLINE_IMAGE))) {
        return null;
      }
    } else {
      if (!(functionValidation.isFunctionOn(companyId, FunctionId.COMMENT_INLINE_IMAGE))) {
        return null;
      }
    }
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    MultipartFile file = multiFile.getFile("upload");
    fileValidation.validateUploadImageFile(file);
    executeUploadImage(response, editorName, companyId, file);
    return null;
  }

  private void executeUploadImage(HttpServletResponse response, String editorName, int companyId,
      MultipartFile file) throws IOException {
    ObjectMetadata metadata = new ObjectMetadata();
    String fileName = Common.getNewUUID();
    String fileUrl = amazonS3Service
        .upload(fileName, Bucket.INLINE, file.getInputStream(), metadata);
    JsonObject json = makeJsonObjectToReturnEditor(editorName, companyId, fileName, fileUrl);
    PrintWriter printWriter = response.getWriter();
    printWriter.println(json);
    printWriter.close();
  }

  private JsonObject makeJsonObjectToReturnEditor(String editorName, int companyId, String fileName,
      String fileUrl) {
    JsonObject json = new JsonObject();
    json.addProperty("uploaded", 1);
    json.addProperty("fileName", fileName);
    json.addProperty("url", fileUrl);
    checkEditorAndAutoTagIsOn(editorName, companyId, fileName, json);
    return json;
  }

  private void checkEditorAndAutoTagIsOn(String editorName, int companyId, String fileName,
      JsonObject json) {
    int functionIdToCheck = editorName.equals(EditorName.POST_EDITOR) ? FunctionId.POST_AUTO_TAG
        : FunctionId.COMMENT_AUTO_TAG;
    if (StringUtils.equals(editorName, EditorName.POST_EDITOR) && functionService
        .isUseFunction(companyId, functionIdToCheck)) {
      List<UserDTO> detectedUsers = detectedUserList(fileName, companyId);
      json.add("detectedUser", new Gson().toJsonTree(detectedUsers));
    }
  }

  /**
   * 이미지에 존재하는 유저리스트 반환
   */
  public List<UserDTO> detectedUserList(String fileName, int companyId) {
    String collectionID = Common.getNewUUID();
    //collection 등록
    amazonRekognitionService.registerCollection(collectionID);
    //collection에 이미지 등록
    amazonRekognitionService
        .registerImageToCollection(fileName, Bucket.INLINE, collectionID);
    //
    return getDetectedUsers(companyId, collectionID);
  }

  /**
   * 유저별로 얼굴이 이미지에 있는지검사 쓰레드를 통해
   */
  private List<UserDTO> getDetectedUsers(int companyId, String collectionID) {
    List<UserDTO> detectedUsers = new ArrayList<>();
    List<UserDTO> userList = userMapper.selectUsersByCompanyId(companyId);
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
        detected = amazonRekognitionService
            .searchFaceMatchingImageCollection(Bucket.USER,
                user.getImageFileName(),
                collectionID);
        if (detected) {
          detectedUsers.add(user);
        }
      }
    }
  }
}

