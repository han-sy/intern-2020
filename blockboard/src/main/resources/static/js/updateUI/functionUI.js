/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionUI.js
 */
//기능 변경 업데이트
function updateNewFunctionInfoUI(data) {
  let source = $('#functionList-template').html();
  let template = Handlebars.compile(source);
  let functions = {functions: data};
  let itemList = template(functions);
  $('#functionListContainer').html(itemList);
  $('#postcontent').html("");
  alert("기능이 변경되었습니다.");
  getBoardList(updateTab);
  resetFunctionAble();
}

//기능변경 체크 UI
function getFunctionCheckList(data) {
  let source = $('#changeFunctionInfo-template').html();
  let template = Handlebars.compile(source);
  let functions = {functions: data};
  let itemList = template(functions);
  $('.modal-body-changeFunctions').html(itemList);
  $('.btn-group-toggle').first().addClass("first_function");
  ChangeFunctionListUI($(".first_function").find("._function-switch"));
}