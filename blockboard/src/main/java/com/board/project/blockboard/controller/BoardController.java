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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;
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
        model.addAttribute("companyID",companyID);

        return "boards";
    }

    /**
     * boardid 받아와서 해당하는 게시판의 게시글목록들 리턴
     * @param boardID
     * @return
     */
    @GetMapping("/{boardid}/posts")
    @ResponseBody
    public JSONArray getPostListByBoardID(@PathVariable("boardid") int boardID){
        List<PostDTO> postList = boardService.getPostListByBoardID(boardID);
        JSONArray postlist = new JSONArray();

        for(PostDTO post : postList) {
            JSONObject object = new JSONObject();
            object.put("postTitle", post.getPostTitle());
            object.put("postID", post.getPostID());
            object.put("companyID", post.getCompanyID());
            object.put("boardID", post.getBoardID());
            object.put("postContent", post.getPostContent());
            object.put("userName", post.getUserName());
            object.put("userID", post.getUserID());
            object.put("postRegisterTime", post.getPostRegisterTime());
            postlist.add(object);
        }
        return postlist;
    }

    /**
     * 게시물 조회
     * @param postID
     * @param request
     * @return PostDTO + 유저일치여부 로 구성된 map
     * @throws Exception
     */
    @GetMapping(value = "/{boardid}/posts/{postid}")
    @ResponseBody
    public Map<String,Object> getPostByPostID(@PathVariable("postid") int postID, HttpServletRequest request) throws Exception{
        SessionTokenizer session = new SessionTokenizer(request);
        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        //postData는 PostDTO + 유저 일치여부
        Map<String, Object> postData  = boardService.getPostDataAboutSelected(postID,userID);
        return postData;
    }

    /**
     * 게시판 목록 가져오기
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public List<BoardDTO> getBoardList(HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);
        int companyID = session.getCompanyID();

        //게시판 목록
        List<BoardDTO> boardList = boardService.getBoardListByCompanyID(companyID); // select로 받아오기
        return boardList;
    }

    /**
     * 게시판 추가
     * @param newBoardName 새로입력받은 보드이름
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/{boardname}/newboard")
    @ResponseBody
    public List<BoardDTO> insertNewBoard(@PathVariable("boardname") String newBoardName, HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);
        int companyID = session.getCompanyID();

        //게시판 삽입
        boardService.insertNewBoard(newBoardName, companyID);

        //insert후 새로운 게시판목록
        List<BoardDTO> newBoardList = boardService.getBoardListByCompanyID(companyID);
        return newBoardList;
    }

    /**
     * 게시판 이름 변경
     * @param newTItleList 이름이 변경된 리스트
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/newtitle")
    @ResponseBody
    public List<BoardDTO>  changeNewBoardName(@RequestParam("newTItles") String newTItleList, HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);
        int companyID = session.getCompanyID();

        boardService.updateChangedName(newTItleList,companyID);
        //이름 수정후새로운 게시판 목록들 보내기
        List<BoardDTO> newBoardList = boardService.getBoardListByCompanyID(companyID);
        return newBoardList;
    }

    /**
     * 게시판 삭제
     * @param deleteBoards 삭제리스트
     * @param request
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/list")
    @ResponseBody
    public List<BoardDTO> deleteBoardbyBoardID(@RequestParam("deleteList") String deleteBoards, HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);
        int companyID = session.getCompanyID();

        log.info("deleteBoards : "+deleteBoards);
        boardService.deleteBoardsByDeleteBoardList(companyID,deleteBoards); //기존데이터

        List<BoardDTO> newBoardList = boardService.getBoardListByCompanyID(companyID);
        return newBoardList;
    }

}