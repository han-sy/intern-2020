/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file AlarmController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.AlarmDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.AlarmService;
import com.board.project.blockboard.service.PostService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AlarmController {

  @Autowired
  private AlarmService alarmService;
  @Autowired
  private PostService postService;

  @GetMapping("/alarms")
  public List<AlarmDTO> getAlarmsByUser(HttpServletRequest request,
      @RequestParam("pageNum") int pageNum) {
    List<AlarmDTO> alarms = alarmService.selectAlarmsByUser(request, pageNum);
    if (alarms.isEmpty()) {
      return null;
    }
    return alarms;
  }

  @DeleteMapping("/alarms/{alarm-id}")
  public void deleteAlarm(@PathVariable("alarm-id") int alarmId) {
    AlarmDTO alarm = alarmService.selectAlarmByAlarmId(alarmId);
    existAlarmValidation(alarm);
    alarmService.deleteAlarm(alarmId);
  }

  @GetMapping("/alarms/{alarm-id}")
  public AlarmDTO getAlarm(@PathVariable("alarm-id") int alarmId) {
    AlarmDTO alarm = alarmService.selectAlarmByAlarmId(alarmId);
    existAlarmValidation(alarm);
    return alarm;
  }

  @PutMapping("/alarms/{alarm-id}")
  public void readMarkToAlarm(@PathVariable("alarm-id") int alarmId) {
    AlarmDTO alarm = alarmService.selectAlarmByAlarmId(alarmId);
    existAlarmValidation(alarm);
    alarmService.readMarkToAlarm(alarmId);
  }

  @GetMapping("/alarms/count")
  public int getUnreadAlarmCountByUser(HttpServletRequest request) {
    return alarmService.getUnreadAlarmCountByUser(request);
  }

  @GetMapping("/alarms/{alarm-id}/post")
  public PostDTO getAlarmContent(@PathVariable("alarm-id") int alarmId) {
    return postService.selectPostByAlarmId(alarmId);
  }

  private void existAlarmValidation(AlarmDTO alarm) {
    if (alarm == null) {
      throw new NullPointerException("존재하지 않는 알람입니다.");
    }
  }
}


