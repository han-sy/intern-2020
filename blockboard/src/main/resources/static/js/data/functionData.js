/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    functionData.js
 */

function isFunctionTagValueOn(functionID){
  var functionTagID = "#functionAble"+ functionID;
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

  /*constructor(){
    this._comments = isFunctionTagValueOn(1);
    this._reply = isFunctionTagValueOn(2);
    this._postFileAttach = isFunctionTagValueOn(3);
    this._commentFileAttach= isFunctionTagValueOn(4);
    this._postInlineImage= isFunctionTagValueOn(5);
    this._commentInlineImage= isFunctionTagValueOn(6);
    this._postTempSave= isFunctionTagValueOn(7);
    this._postSticker= isFunctionTagValueOn(9);
    this._commentSticker= isFunctionTagValueOn(10);
    this._postAutoTag= isFunctionTagValueOn(11);
  }*/

  set comments(functionID) {
    this._comments = isFunctionTagValueOn(functionID);
  }

  set reply(functionID) {
    this._reply = isFunctionTagValueOn(functionID);
  }

  set postFileAttach(functionID) {
    this._postFileAttach = isFunctionTagValueOn(functionID);
  }

  set commentFileAttach(functionID) {
    this._commentFileAttach = isFunctionTagValueOn(functionID);
  }

  set postInlineImage(functionID) {
    this._postInlineImage = isFunctionTagValueOn(functionID);
  }

  set commentInlineImage(functionID) {
    this._commentInlineImage = isFunctionTagValueOn(functionID);
  }

  set postTempSave(functionID) {
    this._postTempSave = isFunctionTagValueOn(functionID);
  }

  set postSticker(functionID) {
    this._postSticker = isFunctionTagValueOn(functionID);
  }

  set commentSticker(functionID) {
    this._commentSticker = isFunctionTagValueOn(functionID);
  }

  set postAutoTag(functionID) {
    this._postAutoTag = isFunctionTagValueOn(functionID);
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
