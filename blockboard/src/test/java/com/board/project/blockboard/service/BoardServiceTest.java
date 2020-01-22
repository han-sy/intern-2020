package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.BoardMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
class BoardServiceTest {
    @Autowired
    private BoardMapper boardMapper;

    @Test
    void getCompanyNameByUserID() throws Exception{
        BoardService boardService = new BoardService();
        String companyName = boardService.getCompanyNameByUserID("1");
        assertEquals("wm",companyName);
    }

}