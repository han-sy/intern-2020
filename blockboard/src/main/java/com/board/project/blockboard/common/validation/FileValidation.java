/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.FileValidException;
import com.board.project.blockboard.service.FileService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class FileValidation {
  @Autowired
  FileService fileService;
  
  public boolean isExistFileInDatabase(String fileName, HttpServletResponse response){
    boolean isValid = fileService.isExistFile(fileName);
    try{
      if(!isValid){
        throw new FileValidException("존재하지 않는 파일입니다.");
      }
    }catch (FileValidException fve){
      fve.sendError(response,"존재하지 않는 파일입니다. 로그인 화면으로 돌아갑니다.");
    }finally {
      return isValid;
    }
  }
}
