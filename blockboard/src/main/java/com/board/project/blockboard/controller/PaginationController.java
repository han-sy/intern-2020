package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.PaginationDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.PaginationService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file PaginationController.java
 */

@Slf4j
@Controller
@RequestMapping("/pages")
public class PaginationController {

  @Autowired
  private PaginationService paginationService;

  @GetMapping(value = "")
  @ResponseBody
  public PaginationDTO getPageList(@RequestParam int boardID, @RequestParam int postID,
      @RequestParam int pageNumber, HttpServletRequest request) {
    UserDTO user = new UserDTO(request);

    //게시판 목록

    PaginationDTO pageInfo = paginationService.getPageList(pageNumber,boardID,postID,user);
    /*PaginationDTO pageInfo = paginationService
        .getPostPageListByPageNumberAboutBoard(pageNumber, boardID, user); // select로 받아오기*/
    return pageInfo;
  }
}
