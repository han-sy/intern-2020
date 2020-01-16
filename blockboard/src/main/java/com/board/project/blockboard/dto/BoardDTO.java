package com.board.project.blockboard.dto;

public class BoardDTO {
    private int board_id;
    private int company_id;
    private String board_name;

    public BoardDTO(){
    }

    public BoardDTO(int board_id, int company_id, String board_name) {
        this.board_id = board_id;
        this.company_id = company_id;
        this.board_name = board_name;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "board_id=" + board_id +
                ", company_id=" + company_id +
                ", board_name='" + board_name + '\'' +
                '}';
    }
}
