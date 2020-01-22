//댓글 출력에 필요한 데이터 받아오기및 출력
function getCommentAllContents(postID, boardID, postContentObj) {
  console.log("conmment ajax 함수 호출");
  postContentObj.append("<div><span><strong>Comments</strong></span> <span id=commentCount></span></div>");
  $.ajax({
    type: 'GET',
    url: "/boards/" + boardID + "/posts/" + postID + "/comments",
    error: function () {  //통신 실패시
      alert('통신실패!');
    },
    success: function (data) {
     //TODO Template 적용예정
     $.each(data, function (key, value) {
        postContentObj.append("<div><span style ='width:500px'><strong>ㄴ" + value.userName + " : </strong></span>   <span>" + value.commentContent + "</span></div>");
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