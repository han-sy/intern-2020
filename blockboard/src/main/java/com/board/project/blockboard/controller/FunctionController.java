/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file FunctionController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.common.validation.AuthorityValidation;
import com.board.project.blockboard.dto.FunctionDTO;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.FunctionService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/functions")
public class FunctionController {

  @Autowired
  private FunctionService functionService;

  /**
   * 기존 기능 on/off 정보
   *
   * @return 리스트 반환
   */
  @GetMapping(value = "")
  public List<FunctionDTO> getFunctionInfo(HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    List<FunctionDTO> functionInfoList = functionService
        .getFunctionInfoListByCompanyId(userData.getCompanyId());
    return functionInfoList;
  }

  /**
   * 기능 on/off 정보 업데이트
   */
  @PostMapping(value = "")
  public void insertNewFunctionData(@RequestBody List<FunctionDTO> functionDTOList,
      HttpServletRequest request) {
    UserDTO userData = new UserDTO(request);
    if (AuthorityValidation.isAdmin(userData)) {
      functionService.updateNewFunctionsInfo(userData.getCompanyId(), functionDTOList);
    }

  }

}
