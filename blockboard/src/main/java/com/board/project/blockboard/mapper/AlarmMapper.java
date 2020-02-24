/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file AlarmMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.AlarmDTO;
import com.board.project.blockboard.dto.UserDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AlarmMapper {

  List<AlarmDTO> selectAlarmsByUser(Map<String, Object> attributes);

  AlarmDTO selectAlarmByAlarmId(int alarmId);

  int getUnreadAlarmCountByUser(UserDTO userID);

  void insertAlarm(AlarmDTO alarm);

  void deleteAlarm(int alarmID);

  void readAlarm(int alarmId);
}
