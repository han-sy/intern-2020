function getSticker() {

  $.ajax({
    type: 'GET',
    url: '/sticker',
    error: function (xhr) {
      errorFunction(xhr);
    },
    success: function (data) {
      let groupName = [];
      $.each(data, function (key, val) {
        groupName.push(key);
      })
    }
  });
}
