/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.constant.ConstantData.FunctionAble;
import com.board.project.blockboard.common.exception.FunctionValidException;
import com.board.project.blockboard.common.util.CompareData;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.mapper.FunctionMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FunctionService {

  @Autowired
  private FunctionMapper functionMapper;

  public List<FunctionDTO> getFunctionInfoByCompanyId(int companyId) {
    return functionMapper.selectFunctionCheckByCompanyId(companyId);
  }

  public void changeFunctionOnToOff(int functionID, int companyId) {
    Map<String, Object> map_functionData = new HashMap<String, Object>();
    map_functionData.put("functionID", functionID);
    map_functionData.put("companyId", companyId);
    functionMapper.deleteFunctionCheckData(map_functionData);
  }

  public void changeFunctionOffToOn(int functionID, int companyId) {
    Map<String, Object> functionPrimaryKey = new HashMap<String, Object>();
    functionPrimaryKey.put("functionID", functionID);
    functionPrimaryKey.put("companyId", companyId);
    functionMapper.insertFunctionCheckData(functionPrimaryKey);

  }

  public List<FunctionDTO> getFunctionInfoListByCompanyId(int companyId) {
    List<FunctionDTO> functionInfoList = getFunctionInfoByCompanyId(companyId);
    return functionInfoList;
  }

  public boolean isUseFunction(int companyId, int functionID) {
    Map<String, Object> functionPrimaryKey = new HashMap<String, Object>();
    functionPrimaryKey.put("functionID", functionID);
    functionPrimaryKey.put("companyId", companyId);
    boolean result = functionMapper.selectFunctionCheckByCompanyIdAndFunctionID(functionPrimaryKey);
    return result;
  }

  public void updateNewFunctionsInfo(int companyId, List<FunctionDTO> functionNewList) {
    List<FunctionDTO> functionOldList = getFunctionInfoByCompanyId(companyId); //기존데이터
    //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경

    try {
      for (FunctionDTO oldFunction : functionOldList) {
        int sameIndex = functionOldList.indexOf(oldFunction);
        FunctionDTO newFunction = functionNewList.get(sameIndex);
        int changeInfo = CompareData.compareFunctionOnOff(oldFunction, newFunction);

        if (changeInfo == FunctionAble.OFF_TO_ON) {
          changeFunctionOffToOn(newFunction.getFunctionID(), companyId);//delete문
        } else if (changeInfo == FunctionAble.ON_TO_OFF) {
          changeFunctionOnToOff(newFunction.getFunctionID(), companyId);//insert문
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
