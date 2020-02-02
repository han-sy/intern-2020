/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file functionAjax.js
 */
//리스트 받아오기
function getFunctionList(companyID, successFunction) {
    $.ajax({
        type: 'GET',
        url: "/functions/" + companyID,
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function (data) {
            successFunction(data);
        }
    });
}

function updateNewFunctionInfoUI(data) {
    var source = $('#functionList-template').html();
    var template = Handlebars.compile(source);
    var functions = {functions: data};
    var itemList = template(functions);
    $('#fuctionListContainer').html(itemList);
    $('#postcontent').html("");
    alert("기능이 변경되었습니다.");
}


function getFunctionCheckList(data) {
    var source = $('#changeFunctionInfo-template').html();
    var template = Handlebars.compile(source);
    var functions = {functions: data};
    var itemList = template(functions);
    $('.modal-body-changeFunctions').html(itemList);
}

//기능변경후 새로운 사용중인기능목록 불러오기
function getNewFunctionInfo(companyID, jsonData) {
    $.ajax({
        type: 'POST',
        url: "/functions/" + companyID,
        data: {functionInfoData: jsonData},
        error: function () {  //통신 실패시
            alert('통신실패!' + error);
        },
        success: function (data) {
            getFunctionList(companyID, updateNewFunctionInfoUI);
        }
    });
}



