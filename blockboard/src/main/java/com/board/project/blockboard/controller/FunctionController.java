package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * @return
     * @throws Exception
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
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/{companyid}")
    public List<FunctionDTO> insertNewFunctionData(@RequestParam("functionInfoData") String functionInfoData, HttpServletRequest request) {
        int companyID = jwtService.getCompanyId();

        functionService.updateNewFunctionsInfo(companyID,functionInfoData);

        List<FunctionDTO> functionInfoList = functionService.getfunctionInfoListByCompanyID(companyID);
        return functionInfoList;
    }

}
