package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String user_id;
    private int com_id;
    private String user_pwd;
    private String user_type;

    @Override
    public String toString() {
        return "UserDTO [User_ID=" + user_id + ", Company_ID=" + com_id + ", User_Type=" + user_type;
    }
}
