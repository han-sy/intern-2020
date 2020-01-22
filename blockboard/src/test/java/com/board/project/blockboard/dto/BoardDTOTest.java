package com.board.project.blockboard.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardDTOTest {

    @Test
    void getBoardID() {
        BoardDTO board = new BoardDTO();
        board.setBoardID(1);
        board.setBoardName("공지사항");
        board.setCompanyID(1);
        assertEquals("공지사항",board.getBoardName());
    }
}