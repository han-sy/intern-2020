package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String user_id;
    private int com_id;
    private String user_pwd;
    private String user_type;

<<<<<<< HEAD
    public UserDTO(String user_id) {
        this.user_id = user_id;
    }

=======
>>>>>>> 3b3e51fb97ab85c937552f8c52d1503097d4169c
    @Override
    public String toString() {
        return "UserDTO [User_ID=" + user_id + ", Company_ID=" + com_id + ", User_Type=" + user_type;
    }
}
