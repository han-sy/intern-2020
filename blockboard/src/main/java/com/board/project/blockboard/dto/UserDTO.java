package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userID;
    private int companyID;
    private String userPassword;
    private String userType;
}
