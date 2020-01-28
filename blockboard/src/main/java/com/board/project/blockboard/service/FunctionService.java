package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.mapper.FunctionMapper;
import com.board.project.blockboard.mapper.PostMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

    public List<FunctionDTO> getfunctionInfoListByCompanyID(int companyID) {
        List<FunctionDTO> functionInfoList = getFunctionInfoByCompanyID(companyID);
        List functionInfoDataList = new ArrayList<Object>();
        try{
            for(int i=0;i<functionInfoList.size();i++){
                Map<String,Object> functionInfoData = new HashMap<String, Object>();
                functionInfoData.put("functionID",functionInfoList.get(i).getFunctionID());
                functionInfoData.put("companyID",functionInfoList.get(i).getCompanyID());
                functionInfoData.put("functionName",functionInfoList.get(i).getFunctionName());
                functionInfoData.put("functionData",functionInfoList.get(i).getFunctionData());
                functionInfoDataList.add(functionInfoData);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return functionInfoDataList;
    }

    /**
     * @param companyID
     * @param functionInfoData functionID, functionCheck(ON or OFF)  두가지 키를 가지는 Jsonlist
     *                         /js/functionEvent.js 에 있는 clickSaveFunctionChange()에서 json 생성
     */
    public void updateNewFunctionsInfo(int companyID, String functionInfoData) {
        List<FunctionDTO> functionInfoList = getFunctionInfoByCompanyID(companyID); //기존데이터
        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        log.info("functionInfoData : "+functionInfoData);
        ArrayList<Map<String,String>> functionListMap = gson.fromJson(functionInfoData,type); //새로운 데이터
        log.info("functionListMap : "+functionListMap);
        try{
            for(int i=0;i<functionInfoList.size();i++){
                if(functionInfoList.get(i).getCompanyID()>0&&functionListMap.get(i).get("functionCheck").equals("OFF")){//on->off
                    //insert문
                    changeFunctionOnToOff(functionInfoList.get(i).getFunctionID(),companyID);
                }
                else if(functionInfoList.get(i).getCompanyID()==0&&functionListMap.get(i).get("functionCheck").equals("ON")){//off->on
                    //delete문
                    changeFunctionOffToOn(functionInfoList.get(i).getFunctionID(),companyID);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
