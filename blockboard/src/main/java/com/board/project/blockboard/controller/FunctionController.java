package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.JwtService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/functions")
public class FunctionController {
    @Autowired
    private FunctionService functionService;
    @Autowired
    private JwtService jwtService;
    /**
     * 기존 기능 on/off 정보
     * @param request
     * @return 리스트 반환
     */
    @GetMapping(value = "/{companyid}")
    public List<FunctionDTO> getFunctionInfo(HttpServletRequest request) {
        int companyID = jwtService.getCompanyId();
        List<FunctionDTO> functionInfoList = functionService.getfunctionInfoListByCompanyID(companyID);
        return functionInfoList;
    }

    /**
     * 기능 on/off 정보 업데이트
     * @param functionInfoData
     * @param request
     */
    @PostMapping(value = "/{companyid}")
    public void insertNewFunctionData(@RequestParam("functionInfoData") String functionInfoData, HttpServletRequest request) {
        int companyID = jwtService.getCompanyId();
        functionService.updateNewFunctionsInfo(companyID,functionInfoData);
}

}
