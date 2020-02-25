/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file ViewRecordController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.dto.ViewRecordDTO;
import com.board.project.blockboard.service.ViewRecordService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/boards/{boardId}/posts/{postId}/view-records")
public class ViewRecordController {

  @Autowired
  private ViewRecordService viewRecordService;

  //select 필요
  @GetMapping("")
  public List<ViewRecordDTO> getViewRecords(@PathVariable int boardId,
      @PathVariable int postId, @RequestParam int startIndex, HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    return viewRecordService.getViewRecords(postId, userData.getUserId(), startIndex);
  }
}
