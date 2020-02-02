/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file CompanyService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.CompanyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CompanyService {
    @Autowired
    CompanyMapper companyMapper;

    public String getCompanyNameByUserID(String userID) {
        return companyMapper.selectCompanyNameByUserID(userID);
    }

    public int getCompanyIDByUserID(String userID) {
        log.info("boardMapper userid: "+ userID);
        return companyMapper.selectCompanyIDByUserID(userID);
    }
}
