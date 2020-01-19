package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.mapper.FunctionMapper;
import com.board.project.blockboard.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FunctionService {
    @Autowired
    private FunctionMapper functionMapper;

    public List<FunctionDTO> getFunctionInfoByCompanyID(int companyID){
        System.out.println("companyID:"+companyID);
        return functionMapper.selectFunctionCheckByCompanyID(companyID);
    }

    public void changeFunctionOnToOff(int functionID,int companyID) {
        Map<String, Object> map_functionData = new HashMap<String, Object>();
        map_functionData.put("functionID",functionID);
        map_functionData.put("companyID",companyID);
        functionMapper.deleteFunctionCheckData(map_functionData);
    }
    public void changeFunctionOffToOn(int functionID,int companyID){
        Map<String, Object> functionPrimaryKey = new HashMap<String, Object>();
        functionPrimaryKey.put("functionID",functionID);
        functionPrimaryKey.put("companyID",companyID);
        functionMapper.insertFunctionCheckData(functionPrimaryKey);

    }
}
