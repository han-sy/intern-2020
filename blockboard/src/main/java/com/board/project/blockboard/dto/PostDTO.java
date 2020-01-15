package com.board.project.blockboard.dto;

import lombok.Data;

@Data
public class PostDTO {
    private int post_id;
    private String user_id;
    private int board_id;
    private int com_id;
    private String user_name;
    private String post_title;
    private String post_content;
    private String post_reg_time;

    @Override
    public String toString() {
        return "게시글 제목: " + this.post_title + "\n게시글 내용: " + this.post_content;
    }
}
