/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file AWSService.java
 */
package com.board.project.blockboard.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.exception.FileValidException;
import com.board.project.blockboard.common.util.Thumbnail;
import com.board.project.blockboard.key.Key;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AmazonS3Service {

  private AmazonS3 amazonS3;


  public AmazonS3Service() {
    log.info("!!!AmazonS3Service");
    AWSCredentials awsCredentials = new BasicAWSCredentials(Key.ACCESS_KEY, Key.SECRET_KEY);
    amazonS3 = new AmazonS3Client(awsCredentials);
  }

  /**
   * 파일업로드
   */
  public String upload(String fileName, String bucket, InputStream inputStream,
      ObjectMetadata metadata) throws IOException {

    if (amazonS3 != null) {
      try {
        amazonS3.putObject(
            new PutObjectRequest(bucket, fileName, inputStream, metadata));

        return amazonS3.getUrl(bucket,fileName).toString();
      } catch (AmazonClientException ace) {
        ace.printStackTrace();
      } finally {
        //amazonS3 = null;
        inputStream.close();
        Thumbnail.deleteSubFile(fileName);
      }
    }
    return null;
  }

  /**
   * 파일 다운로드
   */
  public S3ObjectInputStream download(String fileName, String bucket,
      HttpServletResponse response) {
    if (amazonS3 != null) {

      try {
        if (!amazonS3.doesObjectExist(bucket, fileName)) { //버킷에 파일이 존재하지 않을때.
          throw new FileValidException();
        }
        S3Object o = amazonS3.getObject(ConstantData.BUCKET_FILE, fileName);
        S3ObjectInputStream s3is = o.getObjectContent();
        return s3is;
      } catch (AmazonServiceException e) {
        log.error(e.getErrorMessage());
      } catch (FileValidException fve) {
        fve.sendError(response, "존재하지 않는 파일입니다.");
        fve.printStackTrace();
      } /*finally {
        amazonS3 = null;
      }*/
    }
    return null;
  }

  public boolean deleteFile(String fileName, String bucket, HttpServletResponse response) {
    if (amazonS3 != null) {
      try {
        if (!amazonS3.doesObjectExist(bucket, fileName)) { //버킷에 파일이 존재하지 않을때.
          throw new FileValidException();
        }
        amazonS3.deleteObject(ConstantData.BUCKET_FILE, fileName);
        if (amazonS3.doesObjectExist(ConstantData.BUCKET_FILE, fileName)) {
          return false;
        }
        return true;
      } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
        System.exit(1);
        return false;
      } catch (FileValidException fve) {
        fve.sendError(response, "이미 존재하지 않는 파일입니다.");
        fve.printStackTrace();
      } /*finally {
        amazonS3 = null;
      }*/
    }
    return false;
  }


}
