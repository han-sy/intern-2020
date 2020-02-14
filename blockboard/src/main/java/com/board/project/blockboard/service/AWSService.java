package com.board.project.blockboard.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.key.Key;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file AWSService.java
 */

@Slf4j
@Service
public class AWSService {
  private AmazonS3 amazonS3;

  public AWSService(){
    AWSCredentials awsCredentials = new BasicAWSCredentials(Key.ACCESS_KEY,Key.SECRET_KEY);
    amazonS3 = new AmazonS3Client(awsCredentials);
  }

  public String upload(String fileName, InputStream inputStream, ObjectMetadata metadata,String fileDir){
    if(amazonS3!=null)
      try{
        String fullPath = fileDir+"/"+fileName;
        amazonS3.putObject(new PutObjectRequest(ConstantData.BUCKET_NAME, fullPath,inputStream,metadata));
        return amazonS3.generatePresignedUrl(new GeneratePresignedUrlRequest(ConstantData.BUCKET_NAME,fullPath)).toString();
      }catch(AmazonClientException ace){
        ace.printStackTrace();
      } finally {
        amazonS3 = null;
      }
    return null;
  }

  public S3ObjectInputStream download(String fileName) {
    if (amazonS3 != null) {
      try {
        S3Object o = amazonS3.getObject(ConstantData.BUCKET_NAME, fileName);
        S3ObjectInputStream s3is = o.getObjectContent();
        return s3is;
      } catch (AmazonServiceException e) {
        System.err.println(e.getErrorMessage());
        System.exit(1);
      } finally {
        amazonS3 = null;
      }
    }
    return null;
  }





}
