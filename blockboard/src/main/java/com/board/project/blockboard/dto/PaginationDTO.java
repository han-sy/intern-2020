/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file PagingDTO.java
 */
package com.board.project.blockboard.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PaginationDTO {
  private String pageType;
  private int pageSize; /**1페이지에 들어가는 게시물개수*/
  private int rangeSize; /**한 범위안에 들어가는 페이지개수*/
  private int currentPage; /**현재 페이지*/
  private int currentRange; /**현재 블록*/
  private int dataCount; /**게시물 개수*/
  private int pageCount; /** 페이지 개수*/
  private int rangeCount; /**범위 개수*/
  private int startPage; /**시작 페이지*/
  private int endPage; /**끝페이지*/
  private int prevPage; /**이전범위*/
  private int nextPage; /**다음페이지*/
  private int startIndex; /** 페이지 첫번째 게시물 번호*/
  private int[] pageList;

  public PaginationDTO(String pageType,int dataCount, int currentPage, int pageSize, int rangeSize) {
    setPageType(pageType);
    setCurrentPage(currentPage);
    setDataCount(dataCount);
    setPageSize(pageSize);
    setRangeSize(rangeSize);

    setPageCount(dataCount);
    setRangeCount(pageCount);
    setStartIndex(currentPage, pageSize);

  }

  /**
   * 현재 페이지를 재설정함과 동시에 시작페이지,끝페이지,이전페이지, 다음페이지 설정.
   *
   * @param currentPage
   */
  public void rangeSetting(int currentPage) {
    setCurrentRange(currentPage); //현재 페이지 재설정;
    this.startPage = (currentRange - 1) * rangeSize + 1;
    this.endPage = startPage + rangeSize - 1;

    if (endPage > pageCount) {
      this.endPage = pageCount;
    }
    if (currentPage > rangeSize) {
      this.prevPage = (currentRange - 1) * rangeSize;
    } else {
      this.prevPage = 1;
    }

    if ((currentPage / rangeSize) + 1 < rangeCount) {
      this.nextPage = currentRange * rangeSize + 1;
    } else {
      this.nextPage = pageCount;
    }
    setPageList(startPage, endPage);
  }

  public void setPageType(String pageType) {
    this.pageType = pageType;
  }

  public void setCurrentPage(int currentPage) {//그대로
    this.currentPage = currentPage;
  }

  public void setPageSize(int pageSize) {//그대로
    this.pageSize = pageSize;
  }

  public void setRangeSize(int rangeSize) {//그대로
    this.rangeSize = rangeSize;
  }

  public void setCurrentRange(int currentPage) {
    this.currentRange = (int) ((currentPage - 1) / rangeSize) + 1;
  }

  public void setDataCount(int dataCount) {
    this.dataCount = (int) Math.ceil(dataCount * 1.0 / pageSize);
  }

  public void setPageCount(int dataCount) {
    this.pageCount = (int) Math.ceil(dataCount * 1.0 / pageSize);
  }

  public void setRangeCount(int rangeCount) {
    this.rangeCount = (int) Math.ceil(pageCount * 1.0 / rangeSize);
  }

  public void setStartPage(int startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(int endPage) {
    this.endPage = endPage;
  }

  public void setStartIndex(int currentPage, int pageSize) {
    this.startIndex = (currentPage - 1) * pageSize;
  }

  public void setPageList(int startPage, int endPage) {
    int size = endPage - startPage + 1;
    int[] pageList = new int[size];
    for (int i = 0; i < size; i++) {
      pageList[i] = startPage + i;
    }
    this.pageList = pageList;
  }
}
