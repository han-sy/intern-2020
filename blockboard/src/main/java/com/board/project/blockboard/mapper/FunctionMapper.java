package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.FunctionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FunctionMapper {
    List<FunctionDTO> selectFunctionCheckByCompanyID(int companyID);
}
