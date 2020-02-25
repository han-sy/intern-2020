/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file userAjax.js
 */

function addUser() {
  let formData = new FormData($("#userForm")[0]);
  sendUserImageToServer(formData.get("userId"));
  $.ajax({
    type: "POST",
    url: "/users",
    cache: false,
    data: {
      userId: formData.get("userId"),
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
function sendUserImageToServer(userId){
  let formData = new FormData($("#userForm")[0]);
  formData.append("file", $("#userImageFile")[0].files[0]);

  $.ajax({
    type: "PUT",
    url: `/users/${userId}/image`,
    data: formData,
    contentType: false,
    processData: false,
    cache: false,
    error: function (e) {
      errorFunction(e);
    }
  });
}