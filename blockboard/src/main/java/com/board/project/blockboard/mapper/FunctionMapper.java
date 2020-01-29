/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.FunctionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FunctionMapper {
    List<FunctionDTO> selectFunctionCheckByCompanyID(int companyID);
    void insertFunctionCheckData(Map<String, Object> functionPrimaryKey);
    void deleteFunctionCheckData(Map<String, Object> functionPrimaryKey);

}
