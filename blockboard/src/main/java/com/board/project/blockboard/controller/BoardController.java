package com.board.project.blockboard.controller;


import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.AES256Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private UserService userService;

    private String key = "slgi3ibu5phi8euf";
    private String userID;
    private int companyID;
    AES256Util aes256;
    URLCodec codec;
    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("")
    public String getMainContent(HttpServletRequest request, HttpSession session,Model model) throws UnsupportedEncodingException {
        // 클라이언트의 쿠키를 가져옴

        Cookie[] getCookie = request.getCookies();
        aes256 = new AES256Util(key);
        codec = new URLCodec();
        String decode = null;
        // 클라이언트가 보낸 쿠키가 서버가 생성해준건지 검사 (복호화 과정)
        if(getCookie != null){
            for(int i=0; i<getCookie.length; i++){
                Cookie c = getCookie[i];
                String name = c.getName();
                String value = c.getValue();
                if(name.equals("sessionID")) {
                    try {
                        decode = aes256.aesDecode(codec.decode(value));
                        userID = decode.substring(0, decode.length()-6); // id 자르기
                        logger.info(decode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<BoardDTO> boardList = boardService.getBoardListByUserID(userID);

        logger.info("userID : "+userID);
        companyID = boardService.getCompanyIDByUserID(userID);

        System.out.println("list: "+boardList.size());
        model.addAttribute("list",boardList); //게시판 목록
        model.addAttribute("companyName",boardService.getCompanyNameByUserID(userID));//회사이름
        model.addAttribute("isadmin",boardService.checkAdmin(userID));

        System.out.println(model);
        return "board";
    }


    /**
     * boardid 받아와서 해당하는 게시판의 게시글목록들 리턴
     * @param boardID
     * @return
     */
    @RequestMapping(value = "/{boardid}/postlist",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getPostListByBoardID(@PathVariable("boardid") int boardID){


        //String boardID = request.getParameter("activeTab");
        List<PostDTO> postList = boardService.getPostListByBoardID(boardID);

        System.out.println(postList);

        List postDataList = new ArrayList<Object>();

        try{
            for(int i=0;i<postList.size();i++){
                Map<String,Object> postData = new HashMap<String, Object>();
                postData.put("postID",postList.get(i).getPostID());
                postData.put("postTitle",postList.get(i).getPostTitle());
                postData.put("userName",postList.get(i).getUserName());
                postData.put("postRegisterTime",postList.get(i).getPostRegisterTime());
                System.out.println(postData);
                postDataList.add(postData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return postDataList;
    }

    /**
     * 게시물 클릭시 게시물id 받아와서 게시물 데이터 전달
     * @param postID
     * @return
     */
    @RequestMapping(value = "/postlist/{postid}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getPostByPostID(@PathVariable("postid") int postID,HttpServletRequest request) {

        PostDTO post = boardService.getPostByPostID(postID);


        String userID = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie getCookie : cookies) {
            if (getCookie.getName().equals("userID")) {
                userID = getCookie.getValue();
            }
        }
        //System.out.println("ajax로 넘어온 data :  "+request);

        System.out.println(post);

        Map<String, Object> map = new HashMap<String, Object>();
        logger.info("userID = " + userID);
        logger.info("postUserID = " + post.getUserID());
        try {
            // 현재 로그인한 유저와 게시글 작성자가 같을 경우에 'canDelete' 를 true로 전달
            map.put("canDelete", userID.equals(post.getUserID()) ? true : false);
            map.put("postID", postID);
            map.put("postTitle", post.getPostTitle());
            map.put("postContent", post.getPostContent());
            map.put("userName", post.getUserName());
            map.put("postRegisterTime", post.getPostRegisterTime());
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }
    @RequestMapping(value = "/boardlist")
    @ResponseBody
    public List<Map<String,Object>> getBoardList(){

        List<BoardDTO> boardListByUserID = boardService.getBoardListByUserID(userID); // select로 받아오기
        List boardList = new ArrayList<Object>(); // 보내기위한 리스트맵
        try{
            for(int i=0;i<boardListByUserID.size();i++){
                Map<String,Object> boardData = new HashMap<String, Object>();
                boardData.put("boardID",boardListByUserID.get(i).getBoardID());
                boardData.put("companyID",boardListByUserID.get(i).getCompanyID());
                boardData.put("boardName",boardListByUserID.get(i).getBoardName());
                boardList.add(boardData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return boardList;
    }

    /**
     * 추가할 boardname 받아오고 삽입후 추가한 boardid와 boardname 리턴
     * @param newBoardName
     * @return
     */
    @RequestMapping(value = "/newboard",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> insertNewBoard(@RequestParam("boardName") String newBoardName){
        boardService.insertNewBoard(newBoardName,companyID);
        
        Map<String,Object> boardData = new HashMap<String, Object>();
        BoardDTO newBoard = boardService.getBoardByBoardName(newBoardName);
        boardData.put("boardID",newBoard.getBoardID());
        boardData.put("boardName",newBoard.getBoardName());
        return boardData;
    }


    /**
     *
     * @return 고객사별 기능사용정보
     */
    @RequestMapping(value = "/function-info")
    @ResponseBody
    public List<Map<String,Object>> getFunctionInfo(){

        List<FunctionDTO> functionInfoList = functionService.getFunctionInfoByCompanyID(companyID);
        List functionInfoDataList = new ArrayList<Object>();
        try{
            for(int i=0;i<functionInfoList.size();i++){
                Map<String,Object> functionInfoData = new HashMap<String, Object>();
                functionInfoData.put("functionID",functionInfoList.get(i).getFunctionID());
                functionInfoData.put("companyID",functionInfoList.get(i).getCompanyID());
                functionInfoData.put("functionName",functionInfoList.get(i).getFunctionName());
                functionInfoData.put("functionData",functionInfoList.get(i).getFunctionData());
                functionInfoDataList.add(functionInfoData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return functionInfoDataList;
    }


    /**
     *
     * @param functionDataJson
     * @return sql문 진행에 이상없으면 //TODO 현재는 에러가 안뜰시 true를 반환하는데 안전을 위해 값비교후 반환해도 될것같다.
     */
    @RequestMapping(value = "/function-change",method = RequestMethod.POST)
    @ResponseBody
    public Boolean insertNewFunctionData(@RequestParam("functionInfoData") String functionDataJson){
        List<FunctionDTO> functionInfoList = functionService.getFunctionInfoByCompanyID(companyID); //기존데이터

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gsonFunctionData = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, Integer>>>() {}.getType();
        ArrayList<Map<String,Integer>> functionListMap = gsonFunctionData.fromJson(functionDataJson,type); //새로운 데이터

        //logger.info("functionInfoList : "+ functionInfoList);
        //logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(functionListMap));

        try{
            for(int i=0;i<functionInfoList.size();i++){
                if(functionInfoList.get(i).getCompanyID()>0&&functionListMap.get(i).get("functionCheck")==0){//on->off
                    //insert문
                    functionService.changeFunctionOnToOff(functionInfoList.get(i).getFunctionID(),companyID);
                }
                else if(functionInfoList.get(i).getCompanyID()==0&&functionListMap.get(i).get("functionCheck")==1){//off->on
                    //delete문
                    functionService.changeFunctionOffToOn(functionInfoList.get(i).getFunctionID(),companyID);
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    @RequestMapping(value = "/newboardname",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>>  changeNewBoardName(@RequestParam("boardData") String boardDataJson){
        List<BoardDTO> oldboardList = boardService.getBoardListByUserID(userID); //기존데이터

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gsonNewBoardList = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String,String>> newBoardListMap = gsonNewBoardList.fromJson(boardDataJson,type); //새로운 데이터

        //logger.info("functionInfoList : "+ functionInfoList);
        logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(newBoardListMap));


        for(int i=0;i<oldboardList.size();i++){
            if(!oldboardList.get(i).getBoardName().equals(newBoardListMap.get(i).get("boardName"))) {//on->off
                boardService.changeBoardName(Integer.parseInt(newBoardListMap.get(i).get("boardID")), newBoardListMap.get(i).get("boardName"));
            }
        }


        List<BoardDTO> newBoardListByUserID = boardService.getBoardListByUserID(userID); // select로 받아오기
        List newBoardList = new ArrayList<Object>(); // 보내기위한 리스트맵
        try{
            for(int i=0;i<newBoardListByUserID.size();i++){
                Map<String,Object> boardData = new HashMap<String, Object>();
                boardData.put("boardID",newBoardListByUserID.get(i).getBoardID());
                boardData.put("companyID",newBoardListByUserID.get(i).getCompanyID());
                boardData.put("boardName",newBoardListByUserID.get(i).getBoardName());
                newBoardList.add(boardData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return newBoardList;
    }
}