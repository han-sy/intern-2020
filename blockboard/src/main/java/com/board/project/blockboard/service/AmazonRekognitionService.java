/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file AmazonRekognitionService.java
 */
package com.board.project.blockboard.service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.QualityFilter;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.board.project.blockboard.key.Key;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AmazonRekognitionService {

  private AmazonRekognition rekognitionClient;

  public AmazonRekognitionService() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(Key.ACCESS_KEY, Key.SECRET_KEY);
    rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
  }

  /**
   * 이미지를 collection에 등록
   * @param fileName 등록할 이미지 파일이름
   * @param bucket 이미지버킷
   * @param collectionID collecion ID 분석할 사진단위로 생성된 collection
   */
  public void registerImageToCollection(String fileName, String bucket, String collectionID) {

    Image image = getImageInstance(bucket, fileName);
    IndexFacesRequest indexFacesRequest = getIndexFacesRequestInstance(fileName, collectionID,
        image);

    IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

  }

  /**
   * collection 생성시 조건들
   * @param fileName 파일이름
   * @param collectionID collectionID 유니크함
   * @param image 이미지 객체
   * @return
   */
  private IndexFacesRequest getIndexFacesRequestInstance(String fileName, String collectionID,
      Image image) {
    return new IndexFacesRequest()
        .withImage(image)
        .withQualityFilter(QualityFilter.AUTO)
        .withMaxFaces(5)
        .withCollectionId(collectionID)
        .withExternalImageId(fileName)
        .withDetectionAttributes("DEFAULT");
  }

  /**
   * collection 등록
   * @param collectionID 유니크하게 생성한 collectionID를 가지고
   */
  public void registerCollection(String collectionID) {
    CreateCollectionRequest request = new CreateCollectionRequest()
        .withCollectionId(collectionID);
  }

  /**
   * collection 분석후 collection 삭제
   * @param collectionID
   */
  public void deleteCollection(String collectionID) {

    DeleteCollectionRequest request = new DeleteCollectionRequest()
        .withCollectionId(collectionID);
    DeleteCollectionResult deleteCollectionResult = rekognitionClient.deleteCollection(request);

    log.info(collectionID + ": " + deleteCollectionResult.getStatusCode()
        .toString());
  }

  /**
   * collection에서 해당 얼굴이 있는지 찾는다.
   * @param bucket 버킷이름
   * @param fileName 파일이름
   * @param collectionID collection이름
   * @return 얼굴이 있으면 true 반환 아니면 false반환
   */
  public boolean searchFaceMatchingImageCollection(String bucket, String fileName,
      String collectionID)
      throws IOException {
    Image image = getImageInstance(bucket, fileName);

    SearchFacesByImageRequest searchFacesByImageRequest = getSearchFacesByImageRequestInstance(
        collectionID, image);

    return getResultOfUserExistInCollection(fileName, searchFacesByImageRequest);

  }

  /**
   * 유저가 collection에 존재하는지 여부 반환
   */
  private boolean getResultOfUserExistInCollection(String fileName,
      SearchFacesByImageRequest searchFacesByImageRequest) {
    if (searchFacesByImageRequest != null) {
      SearchFacesByImageResult searchFacesByImageResult =
          rekognitionClient.searchFacesByImage(searchFacesByImageRequest);

      List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();
      if (faceImageMatches.size() > 0) {
        log.info("감지 : " + fileName);
        return true;
      }
      return false;
    }
    return false;
  }

  /**
   * 얼굴찾기
   */
  private SearchFacesByImageRequest getSearchFacesByImageRequestInstance(String collectionID,
      Image image) {
    return new SearchFacesByImageRequest()
        .withCollectionId(collectionID)
        .withImage(image)
        .withFaceMatchThreshold(70F)
        .withMaxFaces(1);
  }

  /**
   * 이미지 인스턴스생성
   */
  private Image getImageInstance(String bucket, String fileName) {
    return new Image()
        .withS3Object(new S3Object()
            .withBucket(bucket)
            .withName(fileName));
  }

}
