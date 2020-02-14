/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file userAjax.js
 */

function addUser() {
  var form = $("#userForm")[0];
  var formData = new FormData(form);
  var userImageFile = $("#userImageFile")[0].files;
  sendUserImageToServer(formData.get("userID"));
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

/**
 * 유저 이미지 전송
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function sendUserImageToServer(userID){
  var form = $('#userForm')[0];
  var formData = new FormData(form);
  console.log("fd : "+formData);
  formData.append( "file", $("#userImageFile")[0].files[0]);

  $.ajax({
    type: "PUT",
    url: `/users/${userID}/Image`,
    data: formData,
    contentType: false,
    processData: false,
    cache: false,
    success: function () {
      console.log("이미지 업로드 성공");
    },
    error: function (e) {
      console.log(e.responseText);
    }
  });
}