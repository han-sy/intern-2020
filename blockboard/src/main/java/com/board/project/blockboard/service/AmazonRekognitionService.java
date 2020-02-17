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
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.ListCollectionsRequest;
import com.amazonaws.services.rekognition.model.ListCollectionsResult;
import com.amazonaws.services.rekognition.model.QualityFilter;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.UnindexedFace;
import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.key.Key;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.classfile.Constant;
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

  public void registerImageToCollection(String fileName, String bucket, String userID) {
    log.info("!!!!registerImageToCollection");
    Image image = new Image()
        .withS3Object(new S3Object()
            .withBucket(bucket)
            .withName(fileName));
    //registerCollection();
    log.info("Image : " + image);

    IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
        .withImage(image)
        .withQualityFilter(QualityFilter.AUTO)
        .withMaxFaces(5)
        .withCollectionId(ConstantData.COLLECTION_ID)
        .withExternalImageId(userID)
        .withDetectionAttributes("DEFAULT");

    log.info("IndexFacesRequest : " + indexFacesRequest);
    IndexFacesResult indexFacesResult = rekognitionClient.indexFaces(indexFacesRequest);

    System.out.println("Results for " + userID);
    System.out.println("Faces indexed:");
    List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
    for (FaceRecord faceRecord : faceRecords) {
      System.out.println("  Face ID: " + faceRecord.getFace().getFaceId());
      System.out.println("  Location:" + faceRecord.getFaceDetail().getBoundingBox().toString());
    }

    List<UnindexedFace> unindexedFaces = indexFacesResult.getUnindexedFaces();
    System.out.println("Faces not indexed:");
    for (UnindexedFace unindexedFace : unindexedFaces) {
      System.out.println("  Location:" + unindexedFace.getFaceDetail().getBoundingBox().toString());
      System.out.println("  Reasons:");
      for (String reason : unindexedFace.getReasons()) {
        System.out.println("   " + reason);
      }
    }
    rekognitionClient = null;
  }

  public void registerCollection() {
    CreateCollectionRequest request = new CreateCollectionRequest()
        .withCollectionId(ConstantData.COLLECTION_ID);
    System.out.println("Deleting collections");

    DeleteCollectionRequest request2 = new DeleteCollectionRequest()
        .withCollectionId(ConstantData.COLLECTION_ID);
    DeleteCollectionResult deleteCollectionResult = rekognitionClient.deleteCollection(request2);

    System.out.println(ConstantData.COLLECTION_ID + ": " + deleteCollectionResult.getStatusCode()
        .toString());

    CreateCollectionResult createCollectionResult = rekognitionClient.createCollection(request);
    System.out.println("CollectionArn : " +
        createCollectionResult.getCollectionArn());
    System.out.println("Status code : " +
        createCollectionResult.getStatusCode().toString());
    rekognitionClient = null;
  }

  public List<String> searchFaceMatchingImageCollection(String bucket, String fileName)
      throws IOException {

    // Get an image object from S3 bucket.
    Image image=new Image()
        .withS3Object(new S3Object()
            .withBucket(bucket)
            .withName(fileName));
    log.info(""+image);

    // Search collection for faces similar to the largest face in the image.
    SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
        .withCollectionId(ConstantData.COLLECTION_ID)
        .withImage(image)
        .withFaceMatchThreshold(70F)
        .withMaxFaces(3);

    log.info(""+searchFacesByImageRequest);

    SearchFacesByImageResult searchFacesByImageResult =
        rekognitionClient.searchFacesByImage(searchFacesByImageRequest);
    log.info(""+searchFacesByImageResult);
    System.out.println("Faces matching largest face in image from" + fileName);
    List <FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();
    List<String> detectedUser = new ArrayList<String>();
    for (FaceMatch face: faceImageMatches) {
      log.info(parseExternalImageId(face));
      detectedUser.add(parseExternalImageId(face));
    }
    log.info("detectedUser : "+ detectedUser);
    return detectedUser;
  }

  public String parseExternalImageId(FaceMatch face) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString= objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(face);
    Map<String,Object> faceData = JsonParse.getMapFromJsonString(jsonString);
    String json = objectMapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(faceData.get("face"));
    Map<String,Object> imageID = JsonParse.getMapFromJsonString(json);


    return imageID.get("externalImageId").toString();
  }

}
