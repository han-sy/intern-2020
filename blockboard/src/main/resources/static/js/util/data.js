/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file Data.js
 */
class User {
  constructor() {
    this.companyID = $("#companyInfo").attr("value");
    this.userID = $('#current_user_info').attr("data-id");
    this.userType = $('#current_user_info').attr("data-type");
    this.userName = $('#current_user_info').html();
  }

  getUserID() {
    return this.userID;
  }

  getUserType() {
    return this.userType;
  }

  getCompanyID() {
    return this.companyID;
  }

  getUserName() {
    return this.userName;
  }

  getJsonString() {
    return JSON.stringify(this);
  }
}

class FunctionOn{
  constructor(){
    if($('#functionAble1').attr("value") == "on"){
      this.comment = true;
    } else if($('#functionAble1').attr("value") == "off"){
      this.comment = false;
    }else{
      changedDataError();
    }

    if($('#functionAble2').attr("value") == "on"){
      this.reply = true;
    } else if($('#functionAble2').attr("value") == "off"){
      this.reply =false;
    }else{
      changedDataError();
    }

    if($('#functionAble3').attr("value") == "on"){
      this.fileAttach = true;
    }else if($('#functionAble3').attr("value") == "off"){
      this.fileAttach =false;
    }else{
      changedDataError();
    }

    if($('#functionAble4').attr("value") == "on"){
      this.inlineImage=true;
    }else if($('#functionAble4').attr("value") == "off"){
      this.inlineImage=false;
    }else{
      changedDataError();
    }

    if($('#functionAble5').attr("value") == "on"){
      this.tempSave = true;
    }else if($('#functionAble5').attr("value") == "off"){
      this.tempSave = false;
    }else{
      changedDataError();
    }

    if($('#functionAble6').attr("value") == "on"){
      this.sticker = true;
    }else if($('#functionAble6').attr("value") == "off"){
      this.sticker = false;
    }else{
      changedDataError();
    }
  }

  isCommentOn(){
    return this.comment;
  }
  isReplyOn(){
    return this.reply;
  }
  isFileAttachOn(){
    return this.fileAttach;
  }
  isInlineImageOn(){
    return this.inlineImage;
  }
  isTempSaveOn(){
    return this.tempSave;
  }
  isStickerOn(){
    return this.sticker;
  }
}

