function getCommentAllContents(postID, boardID, postContentObj) {
    console.log("conmment ajax 함수 호출");
    postContentObj.append("<div><span><strong>Comments</strong></span> <span id=commentCount></span></div>");
  $.ajax({
    type: 'GET',                 //get방식으로 통신
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",    //탭의 data-tab속성의 값으로 된 html파일로 통신
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {    //통신 성공시 탭 내용담는 div를 읽어들인 값으로 채운다.
      $.each(data, function (key, value) {
        postContentObj.append("<div><span style ='width:500px'><strong>ㄴ" + value.userName + " : </strong></span>   <span>"+value.commentContent+"</span></div>");
      });

      var commentHtml = "";
                  commentHtml += "<br><table class=commentHtml>";
                  commentHtml += "<tr><td>";
                  commentHtml += "<textarea style='width: 80% rows=3 cols=30' id=commentText name=commentTrxt placeholder='댓글을 입력하세요'></textarea>";
                  commentHtml += "<div></br><button id=btn_openComment>등록</button></div>";
                  commentHtml += "</td></tr></table>";
                  postContentObj.append(commentHtml);
    }
  });
}