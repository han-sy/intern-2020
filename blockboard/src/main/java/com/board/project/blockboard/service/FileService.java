/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileService.java
 */
package com.board.project.blockboard.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.Common;
import com.board.project.blockboard.dto.FileDTO;
import com.board.project.blockboard.mapper.FileMapper;
import com.google.gson.JsonObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

  public String uploadFile(MultipartHttpServletRequest multipartRequest) throws IOException {
    String uuid = Common.getNewUUID();
    log.info("uuid : " + uuid);
    Iterator<String> itr = multipartRequest.getFileNames();


    String fileName = "";
    String url = "";
    while (itr.hasNext()) {
      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명
      String storedFileName = uuid + "_" + originFileName;
      ObjectMetadata metadata= new ObjectMetadata();
      AmazonS3Service amazonS3Service = new AmazonS3Service();

      url = amazonS3Service.upload(storedFileName,ConstantData.BUCKET_FILE,mpf.getInputStream(),metadata,"");
      log.info("url -->"+url);
      long fileSize = mpf.getSize();
      //파일 전체 경로

      log.info("originFileName => " + originFileName);

      log.info("fileName => " + mpf.getName());
      fileName = storedFileName;

      Map<String, Object> fileAttributes = new HashMap<String, Object>();
      fileAttributes.put("resourceUrl",url);
      fileAttributes.put("originFileName", originFileName);
      fileAttributes.put("storedFileName", storedFileName);
      fileAttributes.put("fileSize", fileSize);
      fileMapper.insertFile(fileAttributes);

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

  public List<FileDTO> getFileList(int postID,int commentID) {
    Map<String, Object> fileAttributes = new HashMap<String, Object>();
    fileAttributes.put("postID",postID);
    fileAttributes.put("commentID",commentID);
    return fileMapper.selectFileListByEditorID(fileAttributes);
  }

  public void downloadFile(int fileID, HttpServletResponse response, HttpServletRequest request) {

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

      AmazonS3Service amazonS3Service =new AmazonS3Service();
      S3ObjectInputStream s3is = amazonS3Service.download(fileData.getStoredFileName(),ConstantData.BUCKET_FILE);
      int ncount = 0;
      byte[] bytes = new byte[512];

      while ((ncount = s3is.read(bytes)) != -1) {
        os.write(bytes, 0, ncount);
      }
      s3is.close();
      os.close();
    } catch (FileNotFoundException ex) {
      System.out.println("FileNotFoundException");
    } catch (IOException ex) {
      System.out.println("IOException");
    }

  }

  public void deleteFile(String storedFileName) {
    AmazonS3Service amazonS3Service = new AmazonS3Service();
    if(amazonS3Service.deleteFile(storedFileName,ConstantData.BUCKET_FILE)){
      log.info("파일삭제 성공");
      fileMapper.deleteFileByStoredFileName(storedFileName);
    }else{
      log.info("파일삭제 실패");
      //TODO 파일삭제 실패에 대한 에러처리
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
            ObjectMetadata metadata= new ObjectMetadata();
            AmazonS3Service amazonS3Service = new AmazonS3Service();
            fileName = Common.getNewUUID();
            String fileUrl = amazonS3Service
                .upload(fileName,ConstantData.BUCKET_INLINE,file.getInputStream(),metadata,"");
            printWriter = response.getWriter();
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");


            json.addProperty("uploaded", 1);
            json.addProperty("fileName", fileName);
            json.addProperty("url", fileUrl);

            AmazonRekognitionService amazonRekognitionService = new AmazonRekognitionService();
            List<String> detectedUsers = amazonRekognitionService.searchFaceMatchingImageCollection(ConstantData.BUCKET_INLINE,fileName );
            log.info(detectedUsers+"");
            //json.addProperty("detectedUser",detectedUsers.toString()); //TODO detectedUser에 감지된 얼굴정보 들어있음.
            printWriter.println(json);
            //return detectedUsers.toString();
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

