package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class PostDTO {
    private int post_id;
    private String user_id;
<<<<<<< HEAD
    private int board_id;
    private int com_id;
    private String user_name;
=======
    private String user_name;
    private String board_id;
    private int company_id;
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
    private String post_title;
    private String post_content;
    private String post_register_time;

<<<<<<< HEAD
    @Override
    public String toString() {
        return "게시글 제목: " + this.post_title + "\n게시글 내용: " + this.post_content;
=======
    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBoard_id() {
        return board_id;
    }

    public void setBoard_id(String board_id) {
        this.board_id = board_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_register_time() {
        return post_register_time;
    }

    public void setPost_register_time(String post_register_time) {
        this.post_register_time = post_register_time;
>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
    }
}
