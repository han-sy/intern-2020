/**
 * @author  Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file    common.js
 */

function returnToLoginPage() {
  window.location.href = getContextPath();
  alert("세션만료! 로그인 화면으로 돌아갑니다.");
}

function getContextPath() {
  var hostIndex = location.href.indexOf(location.host) + location.host.length;
  return location.href.substring(hostIndex,
      location.href.indexOf('/', hostIndex + 1));
}

function errorFunction(xhr) {
  if (xhr.status == 401) { // Token 만료 에러
    returnToLoginPage();
  } else if(xhr.status == 400) {
    alert(JSON.parse(xhr.responseText).message);
  } else if(xhr.status == 409) {
    alert(JSON.parse(xhr.responseText).message);
  }
}

// 문자열 byte 계산 함수
function getByteLength(s,b,i,c){
  for(b=i=0;c=s.charCodeAt(i++);b+=c>>11?3:c>>7?2:1);
  return b;
}