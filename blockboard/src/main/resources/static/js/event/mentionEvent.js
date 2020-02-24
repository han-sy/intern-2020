/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file metionEvent.java
 */
$(document).on('click', '.name', function () {
  let userID = $(this).attr("data-id");
  $.ajax({
    type: 'GET',
    url: `/users/${userID}`,
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      $("#modal_userID").html(data.userID);
      $("#modal_userName").html(data.userName);
      $("#modal_userType").html(data.userType);
      $("#modal_companyName").html(data.companyName);
      $("#modal_userImage").attr("src", data.thumbnailUrl);
      $("#tagUserInfoModal").modal();
    }
  });

});