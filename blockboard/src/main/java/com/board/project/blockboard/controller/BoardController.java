package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Controller
public class BoardController {
    private BoardService boardService;

    @Autowired
    BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @RequestMapping("/board")
    public String printBoardbyComp(HttpServletRequest request, HttpSession session,Model model) {

        List<BoardDTO> list = boardService.printBoardbyComp(request,session);
        System.out.println("request: "+request);
        System.out.println("session: "+session.getAttribute("USER")+","+session.getAttribute("COMPANY"));
        System.out.println("list: "+list.size());

        model.addAttribute("list",list);
        //model.addAttribute("board_id",list.get(0).getBoard_id());
       // model.addAttribute("com_id",list.get(0).getCom_id());
        //model.addAttribute("board_name",list.get(0).getBoard_name());
        System.out.println(model);
        return "board";
    }
    @RequestMapping(value = "/board/tab",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> printPostList(HttpServletRequest request, Model model){

        String board_id = request.getParameter("activeTab");
        List<PostDTO> list = boardService.printPostbyBoard(board_id);
        System.out.println("ajax로 넘어온 data :  "+request);

        //model.addAttribute("post_list",list);
        System.out.println(list);

        List listSender = new ArrayList<Object>();

        try{
            for(int i=0;i<list.size();i++){
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("post_id",list.get(i).getPost_id());
                map.put("post_title",list.get(i).getPost_title());
                map.put("user_name",list.get(i).getUser_name());
                map.put("post_reg_time",list.get(i).getPost_reg_time());
                System.out.println(map);
                listSender.add(map);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return listSender;
    }
    @RequestMapping(value = "/board/post",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> printPost(HttpServletRequest request){

        String post_id = request.getParameter("post_id");
        PostDTO post = boardService.printPostContnet(post_id);
        System.out.println("ajax로 넘어온 data :  "+request);

        //model.addAttribute("post_list",list);
        System.out.println(post);
        Map<String,Object> map = new HashMap<String, Object>();
        try{

            map.put("post_title",post.getPost_title());
            map.put("post_content",post.getPost_content());
            map.put("user_name",post.getUser_name());
            map.put("post_reg_time",post.getPost_reg_time());
            System.out.println(map);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

}
