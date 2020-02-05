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
  } else if(xhr.status == 400) {
    alert("잘못 된 요청입니다.");

  }
}

// 문자열 byte 계산 함수
function getByteLength(s,b,i,c){
  for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?3:c>>7?2:1);
  return b;
}