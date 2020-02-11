/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file file.js
 */

function handleFileUpload(files)
{
  for (var i = 0; i < files.length; i++)
  {
    var fd = new FormData();
    fd.append('file', files[i]);
    var status = new createStatusbar($(".dragAndDropDiv"));

    status.setFileNameSize(files[i].name,files[i].size);
    //console.log("status : "+status.filename.attr("data-filename"));
    sendFileToServer(fd,status);

  }
}

var rowCount=0;
function createStatusbar(obj){

  rowCount++;
  var row="odd";
  if(rowCount %2 ==0) row ="even";


  this.statusbar = $("<div class='statusbar "+row+"'></div>");
  this.filename = $("<div class='filename' ></div>").appendTo(this.statusbar);
  this.size = $("<div class='filesize'></div>").appendTo(this.statusbar);
  this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(this.statusbar);
  this.abort = $("<div class='abort'>중지</div>").appendTo(this.statusbar);

  obj.after(this.statusbar);

  this.setFileNameSize = function(name,size){
    var sizeStr="";
    var sizeKB = size/1024;
    if(parseInt(sizeKB) > 1024){
      var sizeMB = sizeKB/1024;
      sizeStr = sizeMB.toFixed(2)+" MB";
    }else{
      sizeStr = sizeKB.toFixed(2)+" KB";
    }

    this.filename.html(name);
    this.size.html(sizeStr);
  }

  this.setProgress = function(progress){
    var progressBarWidth =progress*this.progressBar.width()/ 100;
    this.progressBar.find('div').animate({ width: progressBarWidth }, 10).html(progress + "% ");
    if(parseInt(progress) >= 100)
    {
      this.abort.hide();
    }
  }

  this.setAbort = function(jqxhr){
    var sb = this.statusbar;
    this.abort.click(function()
    {
      jqxhr.abort();
      sb.hide();
    });
  }
}

