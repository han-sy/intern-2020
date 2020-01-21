function getNewFunctionInfo(companyID, jsonData){
    $.ajax({
          type: 'POST',                 //get방식으로 통신
          url: "/functions/" + companyID + "/new-info",    //탭의 data-tab속성의 값으로 된 html파일로 통신
          data: { functionInfoData: jsonData },
          error: function () {  //통신 실패시
            alert('통신실패!');
          },
          success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
            console.log("success" + data);
            var containerObj = $('#fuctionListContainer');
            containerObj.html("현재 사용중인 기능 : ");
            $.each(data, function (key, value) {
              console.log(value.functionName + " : " + value.companyID);
              if (value.companyID == 0) {
                containerObj.append("<span id = functionAble" + value.functionID + " style=display:none value=off>" + value.functionName + "</span>");
              }
              else {
                containerObj.append("<span id = functionAble" + value.functionID + " value=on>" + value.functionName + " </span>");
              }

            });
            alert("기능이 변경되었습니다.");
             $('#postcontent').html("");
          }
        });
}