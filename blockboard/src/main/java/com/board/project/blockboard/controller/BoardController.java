package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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

}
