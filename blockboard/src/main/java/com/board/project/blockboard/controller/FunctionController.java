package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.util.SessionTokenizer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/boards")
public class FunctionController {
    @Autowired
    private FunctionService functionService;
    @RequestMapping(value = "/function-info")
    @ResponseBody
    public List<Map<String,Object>> getFunctionInfo(HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SessionTokenizer session = new SessionTokenizer(request);

        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        List<FunctionDTO> functionInfoList = functionService.getFunctionInfoByCompanyID(companyID);
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



    @RequestMapping(value = "/function-change",method = RequestMethod.POST)
    @ResponseBody
    public Boolean insertNewFunctionData(@RequestParam("functionInfoData") String functionDataJson, HttpServletRequest request) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SessionTokenizer session = new SessionTokenizer(request);

        String userID = session.getUserID();
        int companyID = session.getCompanyID();

        List<FunctionDTO> functionInfoList = functionService.getFunctionInfoByCompanyID(companyID); //기존데이터

        //ajax를 통해 넘어온 json 형식의 string을 map 타입으로 변경
        Gson gsonFunctionData = new Gson();
        Type type = new TypeToken<ArrayList<Map<String, Integer>>>() {}.getType();
        ArrayList<Map<String,Integer>> functionListMap = gsonFunctionData.fromJson(functionDataJson,type); //새로운 데이터

        //logger.info("functionInfoList : "+ functionInfoList);
        //logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(functionListMap));

        try{
            for(int i=0;i<functionInfoList.size();i++){
                if(functionInfoList.get(i).getCompanyID()>0&&functionListMap.get(i).get("functionCheck")==0){//on->off
                    //insert문
                    functionService.changeFunctionOnToOff(functionInfoList.get(i).getFunctionID(),companyID);
                }
                else if(functionInfoList.get(i).getCompanyID()==0&&functionListMap.get(i).get("functionCheck")==1){//off->on
                    //delete문
                    functionService.changeFunctionOffToOn(functionInfoList.get(i).getFunctionID(),companyID);
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

}
