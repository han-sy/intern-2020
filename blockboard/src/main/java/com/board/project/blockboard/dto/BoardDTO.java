package com.board.project.blockboard.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("BoardDTO")
@Data
public class BoardDTO {
    private int BoardID;
    private int companyID;
    private String boardName;
}
