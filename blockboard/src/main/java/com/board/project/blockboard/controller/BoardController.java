package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import com.board.project.blockboard.util.AES256Util;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

        List<BoardDTO> list = boardService.getBoardListByUserID(userID);

        logger.info("userID : "+userID);
        companyID = boardService.getCompanyIDByUserID(userID);


        System.out.println("list: "+list.size());
        model.addAttribute("list",list); //게시판 목록
        model.addAttribute("companyName",boardService.getCompanyNameByUserID(userID));//회사이름
        model.addAttribute("isadmin",boardService.checkAdmin(userID));
      
        System.out.println("list: "+list.size());
        model.addAttribute("list",list);
        model.addAttribute("companyName",boardService.getCompanyNameByUserID(userID));

        System.out.println(model);
        return "board";
    }

    /**
     *
     * @param request 클릭한 탭의 id
     * @return
     */
    @RequestMapping(value = "/tab",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getPostListByBoardID(HttpServletRequest request){


        String boardID = request.getParameter("activeTab");
        List<PostDTO> list = boardService.getPostListByBoardID(boardID);
        //System.out.println("ajax로 넘어온 data :  "+request);

        //model.addAttribute("post_list",list);
        System.out.println(list);

        List listSender = new ArrayList<Object>();

        try{
            for(int i=0;i<list.size();i++){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("postID",list.get(i).getPostID());
                map.put("postTitle",list.get(i).getPostTitle());
                map.put("userName",list.get(i).getUserName());
                map.put("postRegisterTime",list.get(i).getPostRegisterTime());
                System.out.println(map);
                listSender.add(map);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return listSender;
    }
    /**
     *
     * @param request 게시글 목록중 클릭한 게시글의 id
     * @return {key,value} 로 넘기기위해 map
     */
    @RequestMapping(value = "/post",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getPostByPostID(HttpServletRequest request){

        String postID = request.getParameter("postID");
        PostDTO post = boardService.getPostByPostID(postID);
        //System.out.println("ajax로 넘어온 data :  "+request);

        //model.addAttribute("post_list",list);
        System.out.println(post);
        Map<String,Object> map = new HashMap<String, Object>();
        try{

            map.put("postTitle",post.getPostTitle());
            map.put("postContent",post.getPostContent());
            map.put("userName",post.getUserName());
            map.put("postRegisterTime",post.getPostRegisterTime());
            System.out.println(map);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    /**
     *
     * @param request 여기서 board name 넘겨줄거임
     * @return {key,value} 로 넘기기위해 map
     */
    @RequestMapping(value = "/addboard",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> insertNewBoard(HttpServletRequest request){

        String newBoardName = request.getParameter("boardName");
        boardService.insertNewBoard(newBoardName,companyID);
        //System.out.println("ajax로 넘어온 data :  "+request);
        Map<String,Object> map = new HashMap<String, Object>();
        BoardDTO newBoard = boardService.getBoardByBoardName(newBoardName);
        map.put("boardID",newBoard.getBoardID());
        map.put("boardName",newBoard.getBoardName());
        return map;
    }


}