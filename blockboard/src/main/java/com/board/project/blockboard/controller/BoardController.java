/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardController.java
 */
package com.board.project.blockboard.controller;


import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.UserService;


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
    @Autowired
    private FunctionService functionService;
    @Autowired
    private JwtService jwtService;
    /**
     * 메인 화면
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("")
    public String getMainContent(HttpServletRequest request, Model model){  // 일일이 예외처리 안해서 Exception으로 수정 (동욱)
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();

        List<BoardDTO> boardList = boardService.getBoardListByCompanyID(companyID);

        model.addAttribute("boardList",boardList); //게시판 목록
        model.addAttribute("companyName",boardService.getCompanyNameByUserID(userID));//회사이름
        model.addAttribute("isadmin",boardService.checkAdmin(userID));
        model.addAttribute("companyID",companyID);
        model.addAttribute("userID",userID);
        model.addAttribute("userName",userService.getUserNameByUserID(userID));
        model.addAttribute("functionInfoList",functionService.getfunctionInfoListByCompanyID(companyID));

        return "boards";
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
    public Map<String,Object> getPostByPostID(@PathVariable("postid") int postID, HttpServletRequest request) {
        String userID = jwtService.getUserId();
        int companyID = jwtService.getCompanyId();

        //postData는 PostDTO + 유저 일치여부
        Map<String, Object> postData  = boardService.getPostDataAboutSelected(postID,userID);
        return postData;
    }

    /**
     * 게시판 목록 가져오기
     * @param request
     * @return
     * @throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public List<BoardDTO> getBoardList(HttpServletRequest request) {
        int companyID = jwtService.getCompanyId();

        //게시판 목록
        List<BoardDTO> boardList = boardService.getBoardListByCompanyID(companyID); // select로 받아오기
        return boardList;
    }

    /**
     * 게시판 추가
     * @param newBoardName 새로입력받은 보드이름
     * @param request
     * @return
     * @throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
     */
    @PostMapping(value = "/{boardname}")
    @ResponseBody
    public List<BoardDTO> insertNewBoard(@PathVariable("boardname") String newBoardName, HttpServletRequest request){
        int companyID = jwtService.getCompanyId();
        //게시판 삽입
        boardService.insertNewBoard(newBoardName, companyID);

        //insert후 새로운 게시판목록
        List<BoardDTO> newBoardList = boardService.getBoardListByCompanyID(companyID);
        return newBoardList;
    }

    /**
     * 게시판 이름 변경 변경된 리스트를 받아와서 수정한다.
     * @param newTItleList 이름이 변경된 리스트
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping(value = "")
    @ResponseBody
    public List<BoardDTO>  changeNewBoardName(@RequestParam("newTitles") String newTItleList, HttpServletRequest request) {
        int companyID = jwtService.getCompanyId();

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
     * @throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
     */
    @DeleteMapping(value = "/list")
    @ResponseBody
    public List<BoardDTO> deleteBoardbyBoardID(@RequestParam("deleteList") String deleteBoards, HttpServletRequest request) {
        int companyID = jwtService.getCompanyId();

        log.info("deleteBoards : "+deleteBoards);
        boardService.deleteBoardsByDeleteBoardList(companyID,deleteBoards); //기존데이터

        List<BoardDTO> newBoardList = boardService.getBoardListByCompanyID(companyID);
        return newBoardList;
    }

}