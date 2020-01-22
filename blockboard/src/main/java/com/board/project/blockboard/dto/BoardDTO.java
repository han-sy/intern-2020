package com.board.project.blockboard.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("BoardDTO")
@Data
@NoArgsConstructor
public class BoardDTO {
    private int BoardID;
    private int companyID;
    private String boardName;
}
