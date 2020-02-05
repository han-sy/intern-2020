/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.JwtService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
   *
   * @param request
   * @return 리스트 반환
   */
  @GetMapping(value = "/{companyid}")
  public List<FunctionDTO> getFunctionInfo(HttpServletRequest request) {
    Map<String, Object> userInfo = jwtService.getBody();
    int companyID = Integer.parseInt(userInfo.get("companyID").toString());
    List<FunctionDTO> functionInfoList = functionService.getfunctionInfoListByCompanyID(companyID);
    return functionInfoList;
  }

  /**
   * 기능 on/off 정보 업데이트
   *
   * @param functionInfoData
   * @param request
   */
  @PostMapping(value = "/{companyid}")
  public void insertNewFunctionData(@RequestParam("functionInfoData") String functionInfoData,
      HttpServletRequest request) {
    Map<String, Object> userInfo = jwtService.getBody();
    int companyID = Integer.parseInt(userInfo.get("companyID").toString());
    functionService.updateNewFunctionsInfo(companyID, functionInfoData);
  }

}
