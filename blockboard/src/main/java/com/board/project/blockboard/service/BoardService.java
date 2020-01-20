package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    public String getCompanyNameByUserID(String userID) {
        return boardMapper.selectCompanyNameByUserID(userID);
    }

    public List<BoardDTO> getBoardListByCompanyID(int companyID) {
        //System.out.println("companyID (session): " + session.getAttribute("COMPANY"));
        System.out.println("companyID : "+companyID);
        List<BoardDTO> boardList= boardMapper.selectBoardsByCompanyID(companyID);
        return boardList;
    }

    public List<PostDTO> getPostListByBoardID(int boardID) {
        List<PostDTO> postlist = boardMapper.selectPostByBoardID(boardID);
        return postlist;
    }

    public PostDTO getPostByPostID(int postID) {
        PostDTO post = boardMapper.selectPostByPostID(postID);
        return post;
    }

    public boolean checkAdmin(String userID) {
        String admin = boardMapper.selectUserTypeByUserID(userID);
        return admin.equals("관리자");
    }

    public void insertNewBoard(String newBoardName,int companyID){
        BoardDTO newBoard = new BoardDTO();
        newBoard.setCompanyID(companyID);
        newBoard.setBoardName(newBoardName);
        logger.info("newBoard : "+newBoard);
        boardMapper.insertBoard(newBoard);
    }

    public int getCompanyIDByUserID(String userID) {
        logger.info("boardMapper userid: "+ userID);
        return boardMapper.selectCompanyIDByUserID(userID);
    }
    
    public BoardDTO getBoardByBoardName(String boardName) {
        return boardMapper.selectBoardByBoardName(boardName);
    }

    public void changeBoardName(int boardID, String boardName) {
        Map<String, Object> boardAttributes = new HashMap<String, Object>();
        boardAttributes.put("boardID",boardID);
        boardAttributes.put("boardName",boardName);
        boardMapper.updateBoardName(boardAttributes);
    }

    public void deleteAllPostInBoard(int boardID) {
        boardMapper.deleteAllPostInBoard(boardID);
    }

    public void deleteBoard(int boardID) {
        boardMapper.deleteBoard(boardID);
    }

    public void deleteAllCommentInBoard(int boardID) {
        boardMapper.deleteAllCommentInBoard(boardID);
    }

    public void deleteBoardsByDeleteBoardList(int companyID, String deleteBoardListJson) {

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String,String>> deleteBoardListMap = gson.fromJson(deleteBoardListJson,type); //새로운 데이터

        for(int i=0;i<deleteBoardListMap.size();i++){ //삭제목록 조회
            //logger.info("boardIDInteger : "+deleteBoardListMap.get(i).get("boardID"));
            int boardIDInteger = Integer.parseInt(deleteBoardListMap.get(i).get("boardID"));
            deleteAllCommentInBoard(boardIDInteger);
            deleteAllPostInBoard(boardIDInteger);
            deleteBoard(boardIDInteger);
        }

    }

    public Map<String, Object> getPostDataAboutSelected(int postID, String userID) {
        Map<String, Object> postMapData = new HashMap<String, Object>();
        PostDTO postData = getPostByPostID(postID);
        try {
            // 현재 로그인한 유저와 게시글 작성자가 같을 경우에 'canDelete' 를 true로 전달
            postMapData.put("canDelete", userID.equals(postData.getUserID()) ? true : false);
            postMapData.put("postID", postID);
            postMapData.put("postTitle", postData.getPostTitle());
            postMapData.put("postContent", postData.getPostContent());
            postMapData.put("userName", postData.getUserName());
            postMapData.put("postRegisterTime", postData.getPostRegisterTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postMapData;
    }

    public void updateChangedName(String newTItleList, int companyID) {

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gsonNewBoardList = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String,String>> newBoardListMap = gsonNewBoardList.fromJson(newTItleList,type); //새로운 데이터

        for(int i=0;i<newBoardListMap.size();i++){
            changeBoardName(Integer.parseInt(newBoardListMap.get(i).get("boardID")), newBoardListMap.get(i).get("boardName"));
        }

    }
}

