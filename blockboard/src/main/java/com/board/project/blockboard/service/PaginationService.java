/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file PaginationService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.dto.PaginationDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaginationService {
  @Autowired
  private PostService postService;

  public PaginationDTO getPageListByPageNumber(int pageNumber, int boardID) {
    int postCount = postService.getPostsCountByBoardID(boardID);
    PaginationDTO paginationInfo = new PaginationDTO(postCount,pageNumber, ConstantData.PAGE_SIZE,ConstantData.RANGE_SIZE);
    paginationInfo.rangeSetting(pageNumber);
    log.info("!!!!리스트:"+paginationInfo.toString());
    return paginationInfo;
  }
}
