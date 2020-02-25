/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file csrf.js
 */

var generateCsrfToken = function () {
  function generateRandomString(length) {
    let text = "";
    let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (let i = 0; i < length; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
  }

  return btoa(generateRandomString(32));
};

var setCookie = function (cname, cvalue) {
  document.cookie = `${cname}=${cvalue};path=/`;
};

$.ajaxSetup({
  beforeSend: function (xhr, settings) {
    if (!(/^http:.*/.test(settings.url) || /^https:.*/.test(settings.url))) {
      let csrfToken = generateCsrfToken();
      setCookie('CSRF_TOKEN', encodeURIComponent(csrfToken));
      xhr.setRequestHeader("_csrf", csrfToken);
    }
  }
});