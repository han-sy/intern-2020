/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    common.js
 */


function returnToLoginPage() {
  redirectLogout();
  alert("세션만료! 로그인 화면으로 돌아갑니다.");
}

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function redirectLogout() {
  window.location.href = "/logout";
}

function changedDataError() {
  alert("페이지가 임의로 변경되었습니다.");
  redirectLogout();
}

function getContextPath() {
  let hostIndex = location.href.indexOf(location.host) + location.host.length;
  return location.href.substring(hostIndex,
      location.href.indexOf('/', hostIndex + 1));
}

function errorFunction(xhr) {
  if (xhr.status == HTTP_STATUS.UNAUTHORIZED) { // Token 만료 에러
    alert(JSON.parse(xhr.responseText).message);
    redirectLogout();
  } else if (xhr.status == HTTP_STATUS.BAD_REQUEST) {
    alert(JSON.parse(xhr.responseText).message);
  } else if (xhr.status == HTTP_STATUS.CONFLICT) {
    alert(JSON.parse(xhr.responseText).message);
  } else if (xhr.status == HTTP_STATUS.FORBIDDEN) {
    let jsonResponse = JSON.parse(xhr.responseText);
    alert(JSON.parse(xhr.responseText).message);
    redirectLogout();
  }
}

/**
 * text의 byte길이와 글자수를 반환하는 함수입니다.
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function getByteLength(text, maxLength) {
  let charLength, byte, i, unicodeValue;
  for (byte = i = 0; uniCodeValue = text.charCodeAt(i++);
      byte += unicodeValue >> UNICODE_3_BYTE ? BYTE_SIZE_3 : unicodeValue
      >> UNICODE_2_BYTE ? BYTE_SIZE_2 : BYTE_SIZE_1) {
    if (byte < maxLength) {
      charLength = i;
    }
  }
  return [byte, charLength - 2];
}

/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function bytesHandler(obj, selector, maxLength) {
  let text = $(obj).val();
  let textLength = getByteLength(text, maxLength);
  let byteLength = textLength[0];
  let charLength = textLength[1];
  $(selector).text(byteLength + "/" + maxLength);
  if (byteLength > maxLength) {
    $(selector).text("입력글자수 제한(" + byteLength + "/" + maxLength + ")");
    $(selector).css('color', 'red');
    alert("입력 글자수 초과입니다. 초과된 문자들은 삭제됩니다.");
    $(obj).val(text.substr(0, charLength).trim());
    $(selector).text("다시 입력하세요.");
    $(selector).css('color', 'black');
  } else {
    $(selector).text(byteLength + "/" + maxLength);
    $(selector).css('color', 'black');
  }
  $(selector).focus();
}

/**
 * Data의 값유무를 리턴하는 함수
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function isNullData(data) {
  if (data == "" || data == null || data == undefined) {
    return true;
  }
  return false;
}

function loadImage(value) {
  if (value.files && value.files[0]) {
    let reader = new FileReader();
    reader.onload = function (e) {
      $("#load_user_image").attr('src', e.target.result);
    };
    reader.readAsDataURL(value.files[0]);
  }
}
