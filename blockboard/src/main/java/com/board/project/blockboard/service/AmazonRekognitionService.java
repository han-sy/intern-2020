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
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
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
import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.key.Key;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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

  public void registerImageToCollection(String fileName, String bucket, String collectionID) {

    Image image = new Image()
        .withS3Object(new S3Object()
            .withBucket(bucket)
            .withName(fileName));
    //registerCollection();

    IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
        .withImage(image)
        .withQualityFilter(QualityFilter.AUTO)
        .withMaxFaces(5)
        .withCollectionId(collectionID)
        .withExternalImageId(fileName)
        .withDetectionAttributes("DEFAULT");

    log.info("IndexFacesRequest : " + indexFacesRequest);
    IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

  }

  public void registerCollection(String collectionID) {
    CreateCollectionRequest request = new CreateCollectionRequest()
        .withCollectionId(collectionID);

    CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request);
    log.info("CollectionArn : " +
        createCollectionResult.getCollectionArn());
    log.info("Status code : " +
        createCollectionResult.getStatusCode().toString());
  }

  public void deleteCollection(String collectionID) {

    DeleteCollectionRequest request = new DeleteCollectionRequest()
        .withCollectionId(collectionID);
    DeleteCollectionResult deleteCollectionResult = rekognitionClient.deleteCollection(request);

    log.info(collectionID + ": " + deleteCollectionResult.getStatusCode()
        .toString());
  }

  public boolean searchFaceMatchingImageCollection(String bucket, String fileName,
      String collectionID)
      throws IOException {
    // Get an image object from S3 bucket.
    Image image = new Image()
        .withS3Object(new S3Object()
            .withBucket(bucket)
            .withName(fileName));

    // Search collection for faces similar to the largest face in the image.
    SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
        .withCollectionId(collectionID)
        .withImage(image)
        .withFaceMatchThreshold(75F)
        .withMaxFaces(1);

    log.info("" + searchFacesByImageRequest);

    if (searchFacesByImageRequest != null) {
      SearchFacesByImageResult searchFacesByImageResult =
          rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
      List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();
      if (faceImageMatches.size() > 0) {
        log.info("감지 : "+fileName);
        return true;
      }
      return false;
    }
    return false;

  }

  public String parseExternalImageId(FaceMatch face) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(face);
    Map<String, Object> faceData = JsonParse.getMapFromJsonString(jsonString);
    String json = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(faceData.get("face"));
    Map<String, Object> imageID = JsonParse.getMapFromJsonString(json);

    return imageID.get("externalImageId").toString();
  }

}
