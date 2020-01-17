package com.board.project.blockboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDTO {
    private int postID;
    private String userID;
    private String userName;
    private int boardID;
    private int companyID;
    private String postTitle;
    private String postContent;
    private String postRegisterTime;

    public PostDTO(PostDTO post) {
        this.postID = post.getPostID();
        this.userID = post.getUserID();
        this.userName = post.getUserName();
        this.boardID = post.getBoardID();
        this.companyID = post.getCompanyID();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.postRegisterTime = post.getPostRegisterTime();
    }
}
