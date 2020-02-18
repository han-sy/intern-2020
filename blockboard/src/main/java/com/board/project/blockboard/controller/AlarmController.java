/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file AlarmController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.AlarmDTO;
import com.board.project.blockboard.service.AlarmService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AlarmController {

  @Autowired
  private AlarmService alarmService;

  @GetMapping("/alarms")
  public List<AlarmDTO> getAlarms(HttpServletRequest request) {
    List<AlarmDTO> alarms = alarmService.selectAlarmsByUser(request);
    if (alarms.isEmpty()) {
      return null;
    }
    return alarms;
  }

  @DeleteMapping("/alarms/{alarmid}")
  public void deleteAlarm(@PathVariable("alarmid") int alarmID) {
    alarmService.deleteAlarm(alarmID);
  }
}
