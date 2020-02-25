/**
 * @author  @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file    functionData.js
 */

function isFunctionTagValueOn(functionId){
  var functionTagID = "#functionAble"+ functionId;
  if($(functionTagID).attr("value") == "on"){
    return true;
  }else if($(functionTagID).attr("value") == "off"){
    return false;
  }
  else{
    changedDataError();
  }
}

class FunctionOn{

  set comments(functionId) {
    this._comments = isFunctionTagValueOn(functionId);
  }

  set reply(functionId) {
    this._reply = isFunctionTagValueOn(functionId);
  }

  set postFileAttach(functionId) {
    this._postFileAttach = isFunctionTagValueOn(functionId);
  }

  set commentFileAttach(functionId) {
    this._commentFileAttach = isFunctionTagValueOn(functionId);
  }

  set postInlineImage(functionId) {
    this._postInlineImage = isFunctionTagValueOn(functionId);
  }

  set commentInlineImage(functionId) {
    this._commentInlineImage = isFunctionTagValueOn(functionId);
  }

  set postTempSave(functionId) {
    this._postTempSave = isFunctionTagValueOn(functionId);
  }

  set postSticker(functionId) {
    this._postSticker = isFunctionTagValueOn(functionId);
  }

  set commentSticker(functionId) {
    this._commentSticker = isFunctionTagValueOn(functionId);
  }

  set postAutoTag(functionId) {
    this._postAutoTag = isFunctionTagValueOn(functionId);
  }

  get comments() {
    return this._comments;
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

function resetFunctionAble(){
  functionOn.comments = 1;
  functionOn.reply=2;
  functionOn.postFileAttach =3;
  functionOn.commentFileAttach= 4;
  functionOn.postInlineImage=5;
  functionOn.commentInlineImage= 6;
  functionOn.postTempSave= 7;
  functionOn.postSticker= 9;
  functionOn.commentSticker= 10;
  functionOn.postAutoTag= 11;
}



var functionOn = new FunctionOn();
