package com.board.project.blockboard.dto;

public class BoardDTO {
    private int BoardID;
    private int companyID;
    private String boardName;

    public BoardDTO(){
    }

    public BoardDTO(int boardID, int companyID, String boardName) {
        BoardID = boardID;
        this.companyID = companyID;
        this.boardName = boardName;
    }

    public int getBoardID() {
        return BoardID;
    }

    public void setBoardID(int boardID) {
        BoardID = boardID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "BoardID=" + BoardID +
                ", companyID=" + companyID +
                ", boardName='" + boardName + '\'' +
                '}';
    }
}
