package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class BoardDTO {
    private int board_id;
    private int com_id;
    private String board_name;

    public BoardDTO(){
    }
    public BoardDTO(int board_id, int com_id, String board_name) {
        this.board_id = board_id;
        this.com_id = com_id;
        this.board_name = board_name;
    }
}
