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

  /**
   * companyID를 가지고 functionInfo 리턴
   */
  public List<FunctionDTO> getFunctionInfoByCompanyId(int companyId) {
    return functionMapper.selectFunctionCheckByCompanyId(companyId);
  }

  /**
   * on->off
   */
  public void changeFunctionOnToOff(int functionId, int companyId) {
    Map<String, Object> map_functionData = getMapAboutFunctionData(functionId, companyId);
    functionMapper.deleteFunctionCheckData(map_functionData);
  }

  /**
   * off->ON
   */
  public void changeFunctionOffToOn(int functionId, int companyId) {
    Map<String, Object> functionPrimaryKey = getMapAboutFunctionData(functionId, companyId);
    functionMapper.insertFunctionCheckData(functionPrimaryKey);
  }

  private Map<String, Object> getMapAboutFunctionData(int functionId, int companyId) {
    Map<String, Object> functionPrimaryKey = new HashMap<String, Object>();
    functionPrimaryKey.put("functionId", functionId);
    functionPrimaryKey.put("companyId", companyId);
    return functionPrimaryKey;
  }

  public List<FunctionDTO> getFunctionInfoListByCompanyId(int companyId) {
    List<FunctionDTO> functionInfoList = getFunctionInfoByCompanyId(companyId);
    return functionInfoList;
  }

  /**
   * 기능이 사용중인지 여부 반환
   */
  public boolean isUseFunction(int companyId, int functionId) {
    Map<String, Object> functionPrimaryKey = getMapAboutFunctionData(functionId, companyId);
    boolean result = functionMapper.selectFunctionCheckByCompanyIdAndFunctionId(functionPrimaryKey);
    return result;
  }

  /**
   * 기능 업데이트
   */
  public void updateNewFunctionsInfo(int companyId, List<FunctionDTO> functionNewList) {
    List<FunctionDTO> functionOldList = getFunctionInfoByCompanyId(companyId); //기존데이터
    //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
    for (FunctionDTO oldFunction : functionOldList) {
      changeFunction(companyId, functionNewList, functionOldList, oldFunction);
    }
  }

  /**
   * 변화가있는 기능을 변경
   */
  private void changeFunction(int companyId, List<FunctionDTO> functionNewList,
      List<FunctionDTO> functionOldList, FunctionDTO oldFunction) {
    int sameIndex = functionOldList.indexOf(oldFunction);
    FunctionDTO newFunction = functionNewList.get(sameIndex);
    int changeInfo = CompareData.compareFunctionOnOff(oldFunction, newFunction);

    if (changeInfo == FunctionAble.OFF_TO_ON) {
      changeFunctionOffToOn(newFunction.getFunctionId(), companyId);//delete문
    } else if (changeInfo == FunctionAble.ON_TO_OFF) {
      changeFunctionOnToOff(newFunction.getFunctionId(), companyId);//insert문
    }
  }


}
