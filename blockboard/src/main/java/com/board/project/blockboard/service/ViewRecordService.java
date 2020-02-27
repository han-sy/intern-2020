/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordService.java
 */

package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData.PageSize;
import com.board.project.blockboard.dto.ViewRecordDTO;
import com.board.project.blockboard.mapper.ViewRecordMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViewRecordService {

  @Autowired
  private ViewRecordMapper viewRecordMapper;

  public void readPostByUser(String userId, int postId) {
    ViewRecordDTO record = ViewRecordDTO.builder()
        .postId(postId)
        .userId(userId)
        .build();
    viewRecordMapper.insertViewRecord(record);
  }

  public boolean isReadPostByUser(String userId, int postId) {
    ViewRecordDTO record = ViewRecordDTO.builder()
        .postId(postId)
        .userId(userId)
        .build();
    return viewRecordMapper.selectRecordExist(record);
  }

  public List<ViewRecordDTO> getViewRecords(int postId, String userId, int startIndex) {
    Map<String, Object> recordData = getRecordMapData(postId, userId, startIndex);
    return viewRecordMapper.selectViewRecordsByPostId(recordData);
  }

  private Map<String, Object> getRecordMapData(int postId, String userId, int startIndex) {
    Map<String, Object> recordData = new HashMap<>();
    recordData.put("postId", postId);
    recordData.put("userId", userId);
    recordData.put("startIndex", startIndex);
    recordData.put("pageSize", PageSize.VIEW_RECORDS);
    return recordData;
  }
}
