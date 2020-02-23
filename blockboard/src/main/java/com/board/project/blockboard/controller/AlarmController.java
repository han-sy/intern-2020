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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AlarmController {

  @Autowired
  private AlarmService alarmService;
  @Autowired
  private PostService postService;

  @GetMapping("/alarms")
  public List<AlarmDTO> getAlarms(HttpServletRequest request) {
    List<AlarmDTO> alarms = alarmService.selectAlarmsByUser(request);
    if (alarms.isEmpty()) {
      return null;
    }
    return alarms;
  }

  @DeleteMapping("/alarms/{alarm-id}")
  public void deleteAlarm(@PathVariable("alarm-id") int alarmId) {
    alarmService.deleteAlarm(alarmId);
  }

  @GetMapping("alarms/{alarm-id}")
  public AlarmDTO getAlarm(@PathVariable("alarm-id") int alarmId) {
    return alarmService.selectAlarmByAlarmId(alarmId);
  }

  @GetMapping("alarms/{alarm-id}/post")
  public PostDTO getAlarmContent(@PathVariable("alarm-id") int alarmId) {
    PostDTO post = postService.selectPostByAlarmId(alarmId);
    if (post == null) {
      throw new NullPointerException("원본 게시글이 삭제되었습니다.");
    }
    return post;
  }

  @PutMapping("alarms/{alarm-id}")
  public void readAlarm(@PathVariable("alarm-id") int alarmId) {
    alarmService.readAlarm(alarmId);
  }
}


