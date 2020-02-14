/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file userAjax.js
 */

function addUser() {
  var form = $("#userForm")[0];
  var formData = new FormData(form);
  var userImageFile = $("#userImageFile")[0].files;

  $.ajax({
    type: "POST",
    url: "/users",
    cache: false,
    data: {
      userID: formData.get("userID"),
      userName: formData.get("userName"),
      userPassword: formData.get("userPassword")
    },
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      // TODO 'userImageFile' 파일업로드하여 data(=fileID) 받아오기
      // TODO userID <-> fileID 매핑하기
      console.log(JSON.stringify(data));
      alert("회원이 추가되었습니다.");
      $('.modal').modal("hide");
    }
  });
}