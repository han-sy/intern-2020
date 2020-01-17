package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.mapper.FunctionMapper;
import com.board.project.blockboard.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionService {
    @Autowired
    private FunctionMapper functionMapper;

    public List<FunctionDTO> getFunctionInfoByCompanyID(int companyID){
        System.out.println("companyID:"+companyID);
        return functionMapper.selectFunctionCheckByCompanyID(companyID);
    }

    public void changeFunctionOnToOff(int functionID,int companyID) {
        FunctionDTO addfunction = new FunctionDTO();
        addfunction.setFunctionID(functionID);
        addfunction.setCompanyID(companyID);
        functionMapper.insertFunctionCheckData(addfunction);
    }
    public void changeFunctionOffToOn(int functionID,int companyID){
        FunctionDTO addfunction = new FunctionDTO();
        addfunction.setFunctionID(functionID);
        addfunction.setCompanyID(companyID);
        functionMapper.deleteFunctionCheckData(addfunction);
    }
}
