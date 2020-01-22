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
@Controller
@RequestMapping("/functions")
public class FunctionController {
    @Autowired
    private FunctionService functionService;

    /**
     * 기존 기능 on/off 정보
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/{companyid}")
    @ResponseBody
    public List<FunctionDTO> getFunctionInfo(HttpServletRequest request) throws Exception {
        SessionTokenizer session = new SessionTokenizer(request);
        int companyID = session.getCompanyID();

        List<FunctionDTO> functionInfoList = functionService.getfunctionInfoListByCompanyID(companyID);
        return functionInfoList;
    }

    /**
     * 기능 on/off 정보 업데이트
     * @param functionInfoData
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/{companyid}")
    @ResponseBody
    public List<FunctionDTO> insertNewFunctionData(@RequestParam("functionInfoData") String functionInfoData, HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SessionTokenizer session = new SessionTokenizer(request);
        int companyID = session.getCompanyID();

        functionService.updateNewFunctionsInfo(companyID,functionInfoData);

        List<FunctionDTO> functionInfoList = functionService.getfunctionInfoListByCompanyID(companyID);
        return functionInfoList;
    }

}
