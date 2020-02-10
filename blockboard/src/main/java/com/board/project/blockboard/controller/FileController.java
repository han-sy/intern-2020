/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileController.java
 */
package com.board.project.blockboard.controller;

import java.io.File;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Controller
@RequestMapping("")
public class FileController {

  @GetMapping(value = "/fileUpload")
  public String dragAndDrop(Model model) {

    return "fileUpload";

  }

  @PostMapping(value = "/fileUpload")
  public String uploadFile(MultipartHttpServletRequest multipartRequest) {
    Iterator<String> itr = multipartRequest.getFileNames();

    String filePath = "C:/test"; //설정파일로 뺀다.

    while (itr.hasNext()) { //받은 파일들을 모두 돌린다.




      MultipartFile mpf = multipartRequest.getFile(itr.next());

      String originFileName = mpf.getOriginalFilename(); //파일명

      log.info("FILE_INFO: " + originFileName); //받은 파일 리스트 출력'

      String fileFullPath = filePath + "/" + originFileName; //파일 전체 경로

      try {
        //파일 저장
        mpf.transferTo(new File(fileFullPath)); //파일저장 실제로는 service에서 처리

        System.out.println("originalFilename => " + originFileName);
        System.out.println("fileFullPath => " + fileFullPath);

      } catch (Exception e) {
        System.out.println("postTempFile_ERROR======>" + fileFullPath);
        e.printStackTrace();
      }

    }

    return "success";
  }
}
