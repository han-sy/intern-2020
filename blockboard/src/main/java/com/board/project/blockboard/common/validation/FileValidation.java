/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FileValidation.java
 */
package com.board.project.blockboard.common.validation;

import com.board.project.blockboard.common.exception.FileValidException;
import com.board.project.blockboard.service.FileService;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class FileValidation {

  @Autowired
  FileService fileService;

  public boolean isExistFileInDatabase(String fileName, HttpServletResponse response) {
    boolean isValid = fileService.isExistFile(fileName);
    try {
      if (!isValid) {
        throw new FileValidException("존재하지 않는 파일입니다.");
      }
    } catch (FileValidException fve) {
      fve.sendError(response, "존재하지 않는 파일입니다. ");
    } finally {
      return isValid;
    }
  }

  @SneakyThrows
  public void validateUploadImageFile(MultipartFile file) {
    if (file == null) {
      throw new FileValidException("파일이 없습니다.");
    }
    if (file.getSize() == 0) {
      throw new FileValidException("파일 크기가 올바르지 않습니다.");
    }
    if (StringUtil.isBlank(file.getName())) {
      throw new FileValidException("파일명 오류");
    }
    if (!file.getContentType().toLowerCase().startsWith("image/")) {
      throw new FileValidException("이미지 파일이 아닙니다.");
    }
  }
}
