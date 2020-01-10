package com.board.project.blockboard.dto;

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

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }
}
