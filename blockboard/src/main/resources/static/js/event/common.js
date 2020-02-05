/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    common.js
 */

function returnToLoginPage() {
  redirectLogout();
  alert("세션만료! 로그인 화면으로 돌아갑니다.");
}

function redirectLogout() {
  window.location.href = "/logout";
}

function getContextPath() {
  var hostIndex = location.href.indexOf(location.host) + location.host.length;
  return location.href.substring(hostIndex,
      location.href.indexOf('/', hostIndex + 1));
}

function errorFunction(xhr) {
  if (xhr.status == 401) { // Token 만료 에러
    returnToLoginPage();
  } else if (xhr.status == 403) {
    var jsonResponse = JSON.parse(xhr.responseText);
    alert(jsonResponse.message);
    redirectLogout();
  }
}