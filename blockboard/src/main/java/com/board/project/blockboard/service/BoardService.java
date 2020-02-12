/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BoardService {

  @Autowired
  private BoardMapper boardMapper;

  public List<BoardDTO> getBoardListByCompanyID(int companyID) {
    List<BoardDTO> boardList = boardMapper.selectBoardsByCompanyID(companyID);
    return boardList;
  }

  public void insertNewBoard(String newBoardName, UserDTO userData) {
    BoardDTO newBoard = new BoardDTO();
    newBoard.setCompanyID(userData.getCompanyID());
    newBoard.setBoardName(newBoardName);
    boardMapper.insertBoard(newBoard);
  }

  public void changeBoardName(int boardID, String boardName) {
    Map<String, Object> boardAttributes = new HashMap<String, Object>();
    boardAttributes.put("boardID", boardID);
    boardAttributes.put("boardName", boardName);
    boardMapper.updateBoardName(boardAttributes);
  }


  public void deleteBoard(int boardID) {
    boardMapper.deleteBoard(boardID);
  }

  public void deleteBoardsByDeleteBoardList(String deleteBoardListJson) {

    //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
    Gson gson = new Gson();
    Type type = new TypeToken<ArrayList<Map<String, String>>>() {
    }.getType();
    ArrayList<Map<String, String>> deleteBoardListMap = gson
        .fromJson(deleteBoardListJson, type); //새로운 데이터

    for (int i = 0; i < deleteBoardListMap.size(); i++) { //삭제목록 조회
      int boardIDInteger = Integer.parseInt(deleteBoardListMap.get(i).get("boardID"));
      deleteBoard(boardIDInteger);
    }
  }

  public void updateChangedName(String newTItleList, int companyID) {

    //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
    Gson gsonNewBoardList = new Gson();
    Type type = new TypeToken<ArrayList<Map<String, String>>>() {
    }.getType();
    ArrayList<Map<String, String>> newBoardListMap = gsonNewBoardList
        .fromJson(newTItleList, type); //새로운 데이터

    for (int i = 0; i < newBoardListMap.size(); i++) {
      changeBoardName(Integer.parseInt(newBoardListMap.get(i).get("boardID")),
          newBoardListMap.get(i).get("boardName"));
    }

  }
}

