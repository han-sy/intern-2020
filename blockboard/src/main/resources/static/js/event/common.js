/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    common.js
 */
const UNICODE_3_BYTE = 11;
const UNICODE_2_BYTE = 7;
const BYTE_SIZE_3 = 3;
const BYTE_SIZE_2 = 2;
const BYTE_SIZE_1 = 1;

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
function changedDataError(){
  alert("페이지가 임의로 변경되었습니다.");
  redirectLogout();
}

function getContextPath() {
  var hostIndex = location.href.indexOf(location.host) + location.host.length;
  return location.href.substring(hostIndex,
      location.href.indexOf('/', hostIndex + 1));
}

function errorFunction(xhr) {
  if (xhr.status == 401) { // Token 만료 에러
    returnToLoginPage();
  } else if (xhr.status == 400) {
    alert(JSON.parse(xhr.responseText).message);
  } else if (xhr.status == 409) {
    alert(JSON.parse(xhr.responseText).message);
  } else if (xhr.status == 403) {
    var jsonResponse = JSON.parse(xhr.responseText);
    alert(jsonResponse.message);
    redirectLogout();
  }
}

/**
 * text의 byte길이와 글자수를 반환하는 함수입니다.
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 */
function getByteLength(text, maxLength) {
  var charLength, byte, i, unicodeValue;
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
  var text = $(obj).val();
  var textLength = getByteLength(text, maxLength);
  var byteLength = textLength[0];
  var charLength = textLength[1];
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