/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file Data.js
 */
class User{
  constructor(){
    this.companyID = $("#companyInfo").attr("value");
    this.userID = $('#current_user_info').attr("data-id");
    this.userType = $('#current_user_info').attr("data-type");
    this.userName = $('#current_user_info').html();
  }
  getUserID(){
    return this.userID;
  }
  getUserType(){
    return this.userType;
  }
  getCompanyID() {
    return this.companyID;
  }
  getUserName() {
    return this.userName;
  }
  getJsonString(){
    return JSON.stringify(this);
  }
}

