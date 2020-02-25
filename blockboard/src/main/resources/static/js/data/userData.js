/**
 * @author  @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    userData.js
 */

class User {
  constructor() {
    this.companyId = $("#companyInfo").attr("value");
    this.userId = $('#current_user_info').attr("data-id");
    this.userType = $('#current_user_info').attr("data-type");
    this.userName = $('#current_user_info').html();
  }

  getUserId() {
    return this.userId;
  }

  getUserType() {
    return this.userType;
  }

  getCompanyId() {
    return this.companyId;
  }

  getUserName() {
    return this.userName;
  }

  getJsonString() {
    return JSON.stringify(this);
  }
}
