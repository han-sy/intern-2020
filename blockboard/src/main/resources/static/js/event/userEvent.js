/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file userEvent.js
 */

function clickAddUser() {
  let formData = new FormData($("#userForm")[0]);
  if (!isValidPasswordPattern(formData.get("userPassword"))) {
    alert("비밀번호는 8 ~ 20자 이내 문자, 숫자, 특수문자로 구성하여야 합니다.");
    $("#form-userPassword").val('');
    return;
  }
  addUser(formData);
}

// 비밀번호 패턴 체크 (8 ~ 20 자, 문자, 숫자, 특수문자 포함여부 체크)
function isValidPasswordPattern(str) {
  let pattern1 = /[0-9]/; // 숫자
  let pattern2 = /[a-zA-Z]/; // 문자
  let pattern3 = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자

  return !(!pattern1.test(str) || !pattern2.test(str) || !pattern3.test(str)
      || str.length < 8 || str.length > 20);

}

// userId 에 한글입력 제한
$("#form-userId").on('keyup blur keydown', function (event) {
  if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37
      || event.KeyCode == 39 || event.keyCode == 46) {
    return;
  }
  let korean_pattern = /[\ㄱ-ㅎㅏ-ㅣ가-힣]/g;
  let str_userId = $(this).val();
  if (korean_pattern.test(str_userId)) {
    alert("한글은 입력할 수 없습니다.");
    $(this).val(str_userId.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, ''));
  }
});

// 회원추가 Modal 창 닫힐 때 form 초기화
$("#addUserModal").on('hide.bs.modal', function () {
  resetAddUserForm();
});

function resetAddUserForm() {
  $("#userForm")[0].reset();
  $("#load_user_image").attr("src", '');
}

$('.btn_logout').on('click', function () {
  $.ajax({
    type: 'POST',
    url: '/logout',
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (url) {
      window.location.href = url;
    }
  })
});