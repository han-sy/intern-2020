package com.board.project.blockboard.dto;

public class UserDTO {
    private String user_id;
    private int com_id;
    private String user_type;

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


    @Override
    public String toString() {
        return "UserDTO [User_ID=" + user_id + ", Company_ID=" + com_id + ", User_Type=" + user_type;
    }
}
