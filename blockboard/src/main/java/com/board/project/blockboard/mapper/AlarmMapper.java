/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file AlarmMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.AlarmDTO;
import com.board.project.blockboard.dto.UserDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AlarmMapper {

  List<AlarmDTO> selectAlarmsByUser(UserDTO user);

  void insertAlarm(AlarmDTO alarm);

  void deleteAlarm(int alarmID);

  void readAlarm(int alarmId);
}
