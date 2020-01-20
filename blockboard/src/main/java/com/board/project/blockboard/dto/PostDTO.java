package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class PostDTO {
    private int postID;
    private String userID;
    private String userName;
    private int boardID;
    private int companyID;
    private String postTitle;
    private String postContent;
    private String postRegisterTime;

}
