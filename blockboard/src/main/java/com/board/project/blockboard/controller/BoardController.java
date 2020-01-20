package com.board.project.blockboard.controller;


import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.UserService;
import com.board.project.blockboard.util.SessionTokenizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Slf4j
@Data
@Controller
@RequestMapping("/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private UserService userService;


    /**
     * 메인 화면
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("")
    public String getMainContent(HttpServletRequest request, Model model) throws Exception {  // 일일이 예외처리 안해서 Exception으로 수정 (동욱)
        SessionTokenizer session = new SessionTokenizer(request);

        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        List<BoardDTO> boardList = boardService.getBoardListByCompanyID(companyID);

        model.addAttribute("boardList",boardList); //게시판 목록
        model.addAttribute("companyName",boardService.getCompanyNameByUserID(userID));//회사이름
        model.addAttribute("isadmin",boardService.checkAdmin(userID));

        return "boards";
    }


    /**
     * boardid 받아와서 해당하는 게시판의 게시글목록들 리턴
     * @param boardID
     * @return
     */
    @GetMapping("/{boardid}/posts")
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
    @RequestMapping(value = "/{boardid}/posts/{postid}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getPostByPostID(@PathVariable("postid") int postID, HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        PostDTO post = boardService.getPostByPostID(postID);
        SessionTokenizer session = new SessionTokenizer(request);
        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // 현재 로그인한 유저와 게시글 작성자가 같을 경우에 'canDelete' 를 true로 전달
            map.put("canDelete", userID.equals(post.getUserID()) ? true : false);
            map.put("postID", postID);
            map.put("postTitle", post.getPostTitle());
            map.put("postContent", post.getPostContent());
            map.put("userName", post.getUserName());
            map.put("postRegisterTime", post.getPostRegisterTime());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }

    /**
     * 게시판 목록 가져오기
     * @param request
     * @return
     * @throws Exception
     */
    //TODO map -> DTO로 수정
    @GetMapping(value = "/{boardid}")
    @ResponseBody
    public List<BoardDTO> getBoardList(HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);

        int companyID = session.getCompanyID();

        List<BoardDTO> boardList = boardService.getBoardListByCompanyID(companyID); // select로 받아오기

        return boardList;
    }

    /**
     * 추가할 boardname 받아오고 삽입후 추가한 boardid와 boardname 리턴
     * @param newBoardName
     * @return
     */
    //FIXME 작동안됨
    @PostMapping(value = "/{boardname}/newboard")
    @ResponseBody
    public List<Map<String,Object>> insertNewBoard(@PathVariable("boardName") String newBoardName, HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);

        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        boardService.insertNewBoard(newBoardName,companyID);

        //insert후 새로운 게시판목록
        //return getBoardList();
        return null; //TODO getBoardLIst()로 수정
    }


    /**
     *
     * @return 고객사별 기능사용정보
     */
    @RequestMapping(value = "/function-info")
    @ResponseBody
    public List<Map<String,Object>> getFunctionInfo(HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SessionTokenizer session = new SessionTokenizer(request);

        String userID = session.getUserID();
        int companyID = session.getCompanyID();

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
    public Boolean insertNewFunctionData(@RequestParam("functionInfoData") String functionDataJson, HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SessionTokenizer session = new SessionTokenizer(request);

        String userID = session.getUserID();
        int companyID = session.getCompanyID();

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

    /**
     *  게시판 이름 수정
     * @param boardDataJson
     * @return
     */
    @RequestMapping(value = "/changed/boardname",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>>  changeNewBoardName(@RequestParam("boardData") String boardDataJson, HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SessionTokenizer session = new SessionTokenizer(request);

        //String userID = session.getUserID();
        int companyID = session.getCompanyID();

        List<BoardDTO> oldboardList = boardService.getBoardListByCompanyID(companyID); //기존데이터

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gsonNewBoardList = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String,String>> newBoardListMap = gsonNewBoardList.fromJson(boardDataJson,type); //새로운 데이터

        //logger.info("functionInfoList : "+ functionInfoList);
        log.info(new GsonBuilder().setPrettyPrinting().create().toJson(newBoardListMap));



        for(int i=0;i<oldboardList.size();i++){
            if(!oldboardList.get(i).getBoardName().equals(newBoardListMap.get(i).get("boardName"))) {//전후 이름이 다를때만 수정한다
                boardService.changeBoardName(Integer.parseInt(newBoardListMap.get(i).get("boardID")), newBoardListMap.get(i).get("boardName"));
            }
        }

        //이름 수정후새로운 게시판 목록들 보내기
        //return getBoardList();
        return null;
    }

    @RequestMapping(value = "/deletion/board",method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> deleteBoardbyBoardID(@RequestParam("deleteBoardList") String deleteBoardListJson, HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SessionTokenizer session = new SessionTokenizer(request);

        //String userID = session.getUserID();
        int companyID = session.getCompanyID();

        List<BoardDTO> deleteboardList = boardService.getBoardListByCompanyID(companyID); //기존데이터

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gsonNewBoardList = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String,String>> deleteBoardListMap = gsonNewBoardList.fromJson(deleteBoardListJson,type); //새로운 데이터

        for(int i=0;i<deleteBoardListMap.size();i++){
            //logger.info("boardIDInteger : "+deleteBoardListMap.get(i).get("boardID"));
            int boardIDInteger = Integer.parseInt(deleteBoardListMap.get(i).get("boardID"));
            boardService.deleteAllCommentInBoard(boardIDInteger);
            boardService.deleteAllPostInBoard(boardIDInteger);
            boardService.deleteBoard(boardIDInteger);
        }

        //삭제후 새로운 게시판 목록들 보내기
        //return getBoardList();
        return null;
    }

}