package com.board.project.blockboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("UserDTO")
@Data
public class UserDTO {
    private String userID;
    private int companyID;
    private String userName;
    private String userPassword;
    private String userType;
}
