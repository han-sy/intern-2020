CALL dummyPostInsert;
CALL dummyUserInsert;
CALL dummyAlarmInsert;
insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values(1,1,1,'첫게시글','<p>첫내용</p>', '첫내용', now());
CALL dummyCommentInsert;
CALL dummyCommentInsert2;
insert into comments values(20001,1,1,1,'첫 댓글', '첫 댓글',now(),null,null);
CALL dummyReplyInsert;
insert into view_records values(10001,"irene");
insert into view_records values(10001,"yeri");
insert into view_records values(10001,"2");
insert into view_records values(10001,"admin");
insert into view_records values(10001,"seulgi");
insert into view_records values(10001,"rkdgPdnjs");
insert into view_records values(10001,"rnjsdmsql");
insert into view_records values(10001,"rlacodnjs");
insert into view_records values(10001,"aldhkdizl");
insert into view_records values(10001,"dkqnzlskzh");
insert into view_records values(10001,"dksdbwls");
insert into view_records values(10001,"dlcodus");
insert into view_records values(10001,"wkddnjsdud");
insert into view_records values(10001,"whdbfl");
insert into view_records values(10001,"chldPsk");
insert into view_records values(10001,"ghsekglxhal");
insert into view_records values(10001,"rlaskawn");
insert into view_records values(10001,"eldh");
insert into view_records values(10001,"foqahstmxj");
insert into view_records values(10001,"fpdl");
insert into view_records values(10001,"fhwp");
insert into view_records values(10001,"fltk");
insert into view_records values(10001,"qorgus");
insert into view_records values(10001,"qnl");
insert into view_records values(10001,"tjgus");
insert into view_records values(10001,"tpgns");
insert into view_records values(10001,"thsskdms");
insert into view_records values(10001,"tndud");
insert into view_records values(10001,"tngh");
insert into view_records values(10001,"tbrk");
insert into view_records values(10001,"tldnals");

UPDATE posts
SET view_count = (
	select count(*)
	from view_records
	where post_id = 10001)
WHERE post_id = 10001;

UPDATE posts
SET comments_count = (
	select count(*)
	from comments
	where post_id = 10001 and comment_referenced_id = null)
WHERE post_id = 10001;

UPDATE comments c1
SET c1.replies_count = (
	select count(*)
	from (SELECT * FROM comments) c2
	where c2.comment_referenced_id = 20001)
WHERE c1.comment_id = 20001;
