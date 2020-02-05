/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.constant.ConstantData;
import com.board.project.blockboard.common.util.CompareData;
import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.mapper.FunctionMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FunctionService {

  @Autowired
  private FunctionMapper functionMapper;

  public List<FunctionDTO> getFunctionInfoByCompanyID(int companyID) {
    System.out.println("companyID:" + companyID);
    return functionMapper.selectFunctionCheckByCompanyID(companyID);
  }

  public void changeFunctionOnToOff(int functionID, int companyID) {
    Map<String, Object> map_functionData = new HashMap<String, Object>();
    map_functionData.put("functionID", functionID);
    map_functionData.put("companyID", companyID);
    functionMapper.deleteFunctionCheckData(map_functionData);
  }

  public void changeFunctionOffToOn(int functionID, int companyID) {
    Map<String, Object> functionPrimaryKey = new HashMap<String, Object>();
    functionPrimaryKey.put("functionID", functionID);
    functionPrimaryKey.put("companyID", companyID);
    functionMapper.insertFunctionCheckData(functionPrimaryKey);

  }

  public List<FunctionDTO> getfunctionInfoListByCompanyID(int companyID) {
    List<FunctionDTO> functionInfoList = getFunctionInfoByCompanyID(companyID);
    return functionInfoList;
  }

  /**
   * @param companyID
   * @param functionInfoData functionID, functionCheck(ON or OFF)  두가지 키를 가지는 Jsonlist
   *                         /js/functionEvent.js 에 있는 clickSaveFunctionChange()에서 json 생성
   */
  public void updateNewFunctionsInfo(int companyID, String functionInfoData) {
    List<FunctionDTO> functionOldList = getFunctionInfoByCompanyID(companyID); //기존데이터
    //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
    List<FunctionDTO> functionNewList = JsonParse.jsonToDTOList(functionInfoData,FunctionDTO[].class);

    try {
      for (FunctionDTO oldFunction : functionOldList) {
        int sameIndex = functionOldList.indexOf(oldFunction);
        FunctionDTO newFunction = functionNewList.get(sameIndex);
        int changeInfo = CompareData.compareFunctionOnOff(oldFunction,newFunction);

        if (changeInfo== ConstantData.OFF_TO_ON)
          changeFunctionOffToOn(newFunction.getFunctionID(), companyID);//delete문
        else if (changeInfo ==ConstantData.ON_TO_OFF) {
          changeFunctionOnToOff(newFunction.getFunctionID(), companyID);//insert문
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
