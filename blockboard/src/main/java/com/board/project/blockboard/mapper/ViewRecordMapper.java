/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.ViewRecordDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface ViewRecordMapper {

  List<ViewRecordDTO> selectViewRecordsByPostId(Map<String, Object> recordData);

  boolean selectRecordExist(ViewRecordDTO record);

  void insertViewRecord(ViewRecordDTO record);
}
