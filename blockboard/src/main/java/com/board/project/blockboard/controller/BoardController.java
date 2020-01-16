package com.board.project.blockboard.controller;


import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.util.AES256Util;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Controller
@RequestMapping("/board")
public class BoardController {
    private BoardService boardService;
    private String key = "slgi3ibu5phi8euf";
    private String userID;
    private int companyID;
    AES256Util aes256;
    URLCodec codec;
    @Autowired
    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping("")
    public String getMainContent(HttpServletRequest request, HttpSession session,Model model) throws UnsupportedEncodingException {


        // 클라이언트의 쿠키를 가져옴
        Logger logger = LoggerFactory.getLogger(getClass());
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
    public Map<String,Object> getPostByPostID(@PathVariable("postid") int postID){

        PostDTO post = boardService.getPostByPostID(postID);
        //System.out.println("ajax로 넘어온 data :  "+request);

        //model.addAttribute("post_list",list);
        System.out.println(post);
        Map<String,Object> postData = new HashMap<String, Object>();
        try{

            postData.put("postTitle",post.getPostTitle());
            postData.put("postContent",post.getPostContent());
            postData.put("userName",post.getUserName());
            postData.put("postRegisterTime",post.getPostRegisterTime());
            System.out.println(postData);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return postData;
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


}