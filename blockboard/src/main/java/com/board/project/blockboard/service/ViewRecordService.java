/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordService.java
 */

package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.ViewRecordDTO;
import com.board.project.blockboard.mapper.ViewRecordMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViewRecordService {
  @Autowired
  private ViewRecordMapper viewRecordMapper;

  public void readPostByUser(String userID, int postID){
    ViewRecordDTO record = new ViewRecordDTO(postID,userID);
    viewRecordMapper.insertViewRecord(record);
  }

  public boolean isReadPostByUser(String userID, int postID){
    ViewRecordDTO record = new ViewRecordDTO(postID,userID);
    return viewRecordMapper.selectRecordExist(record);
  }


  public List<ViewRecordDTO> getViewRecords(int postID, String userID) {
    ViewRecordDTO record = new ViewRecordDTO(postID,userID);
    return viewRecordMapper.selectViewRecordsByPostID(postID);
  }
}