/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.FileValidException;
import com.board.project.blockboard.service.FileService;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class FileValidation {

  @Autowired
  FileService fileService;

  @SneakyThrows
  public boolean isExistFileInDatabase(String fileName) {
    log.info("!!1");
    boolean isValid = fileService.isExistFile(fileName);
    if (!isValid) {
      throw new FileValidException("존재하지 않는 파일입니다.");
    }
    return true;
  }
}
