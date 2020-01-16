package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String user_id;
    private int com_id;
<<<<<<< HEAD
    private String user_pwd;
    private String user_type;

=======
    private String user_password;
    private String user_type;

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public UserDTO(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getCom_id() {
        return com_id;
    }

    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }


>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
    @Override
    public String toString() {
        return "UserDTO [User_ID=" + user_id + ", Company_ID=" + com_id + ", User_Type=" + user_type;
    }
}
