package com.board.project.blockboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDTO {
    private int commmentID;
    private int boardID;
    private int postID;
    private int companyID;
    private String userID;
    private String userName;
    private String commentContent;
    private String commentRegisterTime;
    private int commentReferencedID;
}
