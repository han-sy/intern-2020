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
  private int pageSize; /**1페이지에 들어가는 게시물개수*/
  private int rangeSize; /**한 범위안에 들어가는 페이지개수*/
  private int currentPage; /**현재 페이지*/
  private int currentRange; /**현재 블록*/
  private int postsCount; /**게시물 개수*/
  private int pageCount; /** 페이지 개수*/
  private int rangeCount; /**범위 개수*/
  private int startPage; /**시작 페이지*/
  private int endPage; /**끝페이지*/
  private int prevPage; /**이전페이지*/
  private int nextPage; /**다음페이지*/
  private int startIndex; /** 페이지 첫번째 게시물 번호*/
  private int[] pageList;

  public PaginationDTO(int postsCount, int currentPage,int pageSize, int rangeSize){
    setCurrentPage(currentPage);
    setPostsCount(postsCount);
    setPageSize(pageSize);
    setRangeSize(rangeSize);

    setPageCount(postsCount);
    setRangeCount(pageCount);

  }

  /**
   * 현재 페이지를 재설정함과 동시에 시작페이지,끝페이지,이전페이지, 다음페이지 설정.
   * @param currentPage
   */
  public void rangeSetting(int currentPage){
    setCurrentRange(currentPage); //현재 페이지 재설정;
    this.startPage = (currentRange -1)*rangeSize +1;
    this.endPage = startPage + rangeSize-1;

    if(endPage>pageCount){
      this.endPage = pageCount;
    }
    this.prevPage = currentPage -1;
    this.nextPage = currentPage +1;
    setPageList(startPage,pageSize);
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
    this.currentRange = (int)((currentPage-1)/rangeSize)+1;
  }

  public void setPostsCount(int postsCount) {
    this.postsCount = (int) Math.ceil(postsCount*1.0/pageSize);
  }

  public void setPageCount(int postsCount) {
    this.pageCount = (int) Math.ceil(postsCount*1.0/pageSize);
  }

  public void setRangeCount(int rangeCount) {
    this.rangeCount = (int)Math.ceil(pageCount*1.0/rangeSize);
  }

  public void setStartPage(int startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(int endPage) {
    this.endPage = endPage;
  }

  public void setPrevPage(int prevPage) {
    this.prevPage = prevPage;
  }

  public void setNextPage(int nextPage) {
    this.nextPage = nextPage;
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = (currentPage-1)*pageSize;
  }

  public void setPageList(int startPage,int pageSize) {
    int[] pageList = new int[pageSize];
    for(int i=0;i<pageSize;i++){
      pageList[i] = startPage+i;
    }
    this.pageList = pageList;
  }
}
