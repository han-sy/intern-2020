/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file userAjax.js
 */

function addUser() {
  let formData = new FormData($("#userForm")[0]);
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
    success: function () {
      alert("회원이 추가되었습니다.");
      resetAddUserForm();
    }
  });
}

/**
 * 유저 이미지 전송
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function sendUserImageToServer(userID){
  let formData = new FormData($("#userForm")[0]);
  formData.append("file", $("#userImageFile")[0].files[0]);

  $.ajax({
    type: "PUT",
    url: `/users/${userID}/Image`,
    data: formData,
    contentType: false,
    processData: false,
    cache: false,
    error: function (e) {
      errorFunction(e);
    }
  });
}