/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file PaginationController.java
 */

package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PaginationDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.PaginationService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/pages")
public class PaginationController {

  @Autowired
  private PaginationService paginationService;

  @GetMapping(value = "")
  public PaginationDTO getPageList(@RequestParam int boardId, @RequestParam int postId,
      @RequestParam int pageNumber, HttpServletRequest request) {
    UserDTO user = new UserDTO(request);
    //게시판 목록
    PaginationDTO pageInfo = paginationService.getPageList(pageNumber, boardId, postId, user);
    return pageInfo;
  }

  @GetMapping("/search")
  public PaginationDTO getSearchPageList(@RequestParam int pageNumber, @RequestParam String keyword,
      @RequestParam String option, HttpServletRequest request) {
    return paginationService.getSearchPageList(pageNumber, keyword, option, request);
  }
}
