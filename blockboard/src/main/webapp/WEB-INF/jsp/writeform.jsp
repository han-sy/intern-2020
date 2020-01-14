<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/WEB-INF/SE2/js/service/HuskyEZCreator.js" charset="utf-8"></script>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>글쓰기</title>
</head>

<body>
    <h2>글쓰기 페이지</h2>


    <div id="editor-area">
        <textarea name="weditor" id="weditor" rows="10" cols="100">
            <script type="text/javascript">
                var oEditors = [];
                nhn.husky.EZCreator.createInIFrame({ 
                    oAppRef: editor, 
                    elPlaceHolder: 'txtContent', 
                    sSkinURI: '/WEB-INF/SE2/SmartEditor2Skin.html', 
                    fCreator: 'createSEditor2' 
                    });
            </script>
        </textarea>
    </div>
</body>

</html>