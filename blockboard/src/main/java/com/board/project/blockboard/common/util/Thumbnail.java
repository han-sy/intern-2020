/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file Thumbnail.java
 */
package com.board.project.blockboard.common.util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class Thumbnail {

  public static InputStream makeThumbnail(MultipartFile originalFile, String fileName,
      String fileExt) throws Exception {
    File convertFile = new File(originalFile.getOriginalFilename());
    convertFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convertFile);
    fos.write(originalFile.getBytes());
    BufferedImage srcImg = ImageIO.read(convertFile);

    fos.close();
    int dest_width = 100, dest_height = 100;
    int origin_width = srcImg.getWidth();
    int origin_height = srcImg.getHeight();

    // 원본 너비를 기준으로 하여 썸네일의 비율로 높이를 계산
    int n_width = origin_width;
    int n_height = (origin_width * dest_height) / dest_width;

    //계산된 높이가 원본보다 높으면 crop이 안되므로
    //원본 높이를 기준으로 썸네일의 비율로 너비를 계산합니다.
    if (n_height > origin_height) {
      n_width = (origin_height * dest_width) / dest_height;
      n_height = origin_height;
    }

    BufferedImage cropImg = Scalr
        .crop(srcImg, (origin_width - n_width) / 2, (origin_height - n_height) / 2, n_width,
            n_height);


    BufferedImage destImg = Scalr.resize(cropImg, dest_width, dest_height);

    File thumbFile = new File(fileName);
    ImageIO.write(destImg, fileExt.toUpperCase(), thumbFile);
    InputStream inputStream = new FileInputStream(thumbFile);

    return inputStream;
  }

  public static void deleteSubFile(String fileName) {
    File file = new File(fileName);
    if (file.exists()) {
      if (file.delete()) {
      }
    }
  }
}
