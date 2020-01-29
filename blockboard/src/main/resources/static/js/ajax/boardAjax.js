/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file boardAjax.js
 */

//새로운 탭 내용으로 교체
function updateTab(data) {
    var source = $('#boards-template').html();
    var template = Handlebars.compile(source);
    var board = {boards: data};
    var itemList = template(board);
    $('#tab_id').html(itemList);
}

//게시글 내용
function loadPostContent(data) {
    var source = $('#postcontent-template').html();
    var template = Handlebars.compile(source);
    var post = {post: data};
    var item = template(post);
    $('#postcontent').html(item);
}

//게시글 목록
function loadPostList(data) {
    var source = $('#posts-template').html();
    var template = Handlebars.compile(source);
    var post = {posts: data};
    var itemList = template(post);
    $('#postlist').html(itemList);

}

//삭제를 위한 UI
function getBoardListToDelete(data) {
    var source = $('#deleteboards-template').html();
    var template = Handlebars.compile(source);
    var boardList = {boards: data};
    var itemList = template(boardList);
    $('#config_container').html(itemList);
}

//이름변경을 위한 UI
function getBoardListToChangeName(data) {
    var source = $('#changeBoardName-template').html();
    var template = Handlebars.compile(source);
    var boardList = {boards: data};
    var itemList = template(boardList);
    $('#config_container').html(itemList);
}


//탭 업데이트 새로운 게시판 목록으로
function updateTabByNewBoardListAfterAddBoard(boardName) {
    $.ajax({
        type: 'POST',
        url: "/boards",
        data: {boardName: boardName},
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function (data) {    //들어오는 data는 boardDTOlist
            getBoardList(updateTab);//새로운 탭 내용으로 교체
        }
    });
}

//게시판 삭제후 탭업데이트
function updateTabByNewBoardListAfterDeleteBoard(jsonData) {
    $.ajax({
        type: 'DELETE',
        url: "/boards",
        data: {deleteList: jsonData},
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function () {
            getBoardList(updateTab);//새로운 탭 내용으로 교체
        }
    });
    $('#config_container').html("");
}

//게시판 이름변경후 탭업데이트
function updateTabByNewBoardListAfterUpdateBoardName(jsonData) {
    $.ajax({
        type: 'PUT',
        url: "/boards",
        data: {newTitles: jsonData},
        error: function (error) {  //통신 실패시
            alert(error);
        },
        success: function () {
            getBoardList(updateTab);//새로운 탭 내용으로 교체
        }
    });
}

//리스트 받아오기
function getBoardList(successFunction) {
    $.ajax({
        type: 'GET',
        url: '/boards',
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function (data) {
            successFunction(data);
        }
    });
}

//게시물 클릭후 게시물 데이터 받아오기
function getPostDataAfterPostClick(postID, boardID) {
    var postContentObj = $('#postcontent');
    postContentObj.html("");
    $.ajax({
        type: 'GET',
        url: "/boards/" + boardID + "/posts/" + postID,
        error: function (error) {  //통신 실패시
            alert('통신실패!' + error);
        },
        success: function (data) {
            $('#writecontent').hide();
            $('#btn_write').show();

            //게시글 내용 출력
            loadPostContent(data);

            // 작성글의 userID와 현재 로그인한 userID가 같으면 삭제버튼 표시
            var commentAbleObj = $('#functionAble1');
            if (data.canDelete == true) {
                postContentObj.append(
                    "</br><button id=btn_updatePost>수정</button>" +
                    "</br><button id=btn_deletePost>삭제</button><br>"
                );
            }
            var postContentHtml = "";

            if (commentAbleObj.attr("value") == "on") {

                $(function () {
                    postContentObj.append("<br><br><div class= comment_section <div><span><strong>댓글</strong></span> <span id=commentCount></span></div>");
                    postContentHtml += "<div class = comment_list_container></div>";
                    postContentHtml += "<div class = comment_input_container></div>";
                    postContentObj.append(postContentHtml);

                    getCommentList(boardID, postID, getCommentAllContents); //삭제이후 tab에 게시판목록 업데이트 //CommentAjax.js 에 있음
                });
            }
        }
    });
}


//탭클릭후 게시판 목록 불러오기
function getPostsAfterTabClick(boardID) {
    $.ajax({
        type: 'GET',
        url: '/boards/' + boardID + "/posts",
        error: function () {  //통신 실패시
            alert('통신실패!');
        },
        success: function (data) {
            loadPostList(data);
        }
    });
}

