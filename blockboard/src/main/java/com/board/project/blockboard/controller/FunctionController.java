/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.common.util.JsonParse;
import com.board.project.blockboard.common.validation.AuthorityValidation;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.FunctionService;
import com.board.project.blockboard.service.JwtService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
   * @return 리스트 반환
   */
  @GetMapping(value = "/{companyid}")
  public List<FunctionDTO> getFunctionInfo() {
    int companyID = jwtService.getCompanyId();
    List<FunctionDTO> functionInfoList = functionService.getfunctionInfoListByCompanyID(companyID);
    return functionInfoList;
  }

  /**
   * 기능 on/off 정보 업데이트
   */
  @PostMapping(value = "/{companyid}")
  public void insertNewFunctionData(@RequestParam("functionInfoData") String newfunctionInfoData,
      @RequestParam("userData") String userJsonData, HttpServletResponse response)
      throws IOException {
    UserDTO userData = JsonParse.jsonToDTO(userJsonData,UserDTO.class);
    if (AuthorityValidation.isAdmin(userData, response)) {
      functionService.updateNewFunctionsInfo(userData.getCompanyID(), newfunctionInfoData);
    }

  }

}
