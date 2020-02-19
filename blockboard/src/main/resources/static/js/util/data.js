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
      this._comment = true;
    } else if($('#functionAble1').attr("value") == "off"){
      this._comment = false;
    }else{
      changedDataError();
    }

    if($('#functionAble2').attr("value") == "on"){
      this._reply = true;
    } else if($('#functionAble2').attr("value") == "off"){
      this._reply =false;
    }else{
      changedDataError();
    }

    if($('#functionAble3').attr("value") == "on"){
      this._postFileAttach = true;
    }else if($('#functionAble3').attr("value") == "off"){
      this._postFileAttach =false;
    }else{
      changedDataError();
    }
    if($('#functionAble4').attr("value") == "on"){
      this._commentFileAttach = true;
    }else if($('#functionAble4').attr("value") == "off"){
      this._commentFileAttach =false;
    }else{
      changedDataError();
    }

    if($('#functionAble5').attr("value") == "on"){
      this._postInlineImage=true;
    }else if($('#functionAble5').attr("value") == "off"){
      this._postInlineImage=false;
    }else{
      changedDataError();
    }
    if($('#functionAble6').attr("value") == "on"){
      this._commentInlineImage=true;
    }else if($('#functionAble6').attr("value") == "off"){
      this._commentInlineImage=false;
    }else{
      changedDataError();
    }

    if($('#functionAble7').attr("value") == "on"){
      this._postTempSave = true;
    }else if($('#functionAble7').attr("value") == "off"){
      this._postTempSave = false;
    }else{
      changedDataError();
    }

    if($('#functionAble9').attr("value") == "on"){
      this._postSticker = true;
    }else if($('#functionAble9').attr("value") == "off"){
      this._postSticker = false;
    }else{
      changedDataError();
    }
    if($('#functionAble10').attr("value") == "on"){
      this._commentSticker = true;
    }else if($('#functionAble10').attr("value") == "off"){
      this._commentSticker = false;
    }else{
      changedDataError();
    }
    if($('#functionAble11').attr("value") == "on"){
      this._postAutoTag = true;
    }else if($('#functionAble11').attr("value") == "off"){
      this._postAutoTag = false;
    }else{
      changedDataError();
    }
    if($('#functionAble12').attr("value") == "on"){
      this._commentAutoTag = true;
    }else if($('#functionAble12').attr("value") == "off"){
      this._commentAutoTag = false;
    }else{
      changedDataError();
    }
  }

  get comment() {
    return this._comment;
  }

  get reply() {
    return this._reply;
  }

  get postFileAttach() {
    return this._postFileAttach;
  }

  get commentFileAttach() {
    return this._commentFileAttach;
  }

  get postInlineImage() {
    return this._postInlineImage;
  }

  get commentInlineImage() {
    return this._commentInlineImage;
  }

  get postTempSave() {
    return this._postTempSave;
  }

  get postSticker() {
    return this._postSticker;
  }

  get commentSticker() {
    return this._commentSticker;
  }

  get postAutoTag() {
    return this._postAutoTag;
  }

  get commentAutoTag() {
    return this._commentAutoTag;
  }

}

