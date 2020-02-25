use block_board;

drop table view_records;
drop table alarms;
drop table files;
drop table comments;
drop table posts;
drop table users;
drop table functions_check;
drop table functions;
drop table boards;
drop table companies;

create table companies(
	company_id int(9) not null,
    company_name varchar(20) not null,
    primary key(company_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table users(
	user_id varchar(20) not null,
    company_id int(9) not null,
    user_name varchar(30) not null,
    user_password varchar(100) not null,
    user_type varchar(20) not null,
    image_url text,
    image_file_name text,
    thumbnail_url text,
    thumbnail_file_name text,
    foreign key(company_id) references companies(company_id) ON DELETE CASCADE ,
    primary key(user_id,company_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table functions(
	function_id int(9) not null AUTO_INCREMENT,
    function_name varchar(150) not null,
    primary key(function_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table functions_check(
	company_id int(9) not null,
    function_id int(9) not null,
    function_data varchar(300),
    foreign key(company_id) references companies(company_id) ON DELETE CASCADE ,
    foreign key(function_id) references functions(function_id) ON DELETE CASCADE ,
    primary key(company_id,function_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table boards(
	board_id int(9) not null AUTO_INCREMENT,
    company_id int(9) not null,
    board_name varchar(150) not null,
	foreign key(company_id) references companies(company_id) ON DELETE CASCADE ,
    primary key(board_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table posts(
	post_id int(9) not null AUTO_INCREMENT,
    user_id varchar(20) not null,
    board_id int(9) not null,
    company_id int(9) not null,
    post_title varchar(150) not null,
    post_content longtext character set utf8mb4 collate utf8mb4_unicode_ci not null,
	post_content_except_htmltag longtext character set utf8mb4 collate utf8mb4_unicode_ci not null,
    post_register_time datetime not null,
    post_last_update_time datetime null,
    post_status varchar(50) not null default "normal",
    view_count int(9) default 0,
    comments_count int(9) default 0,
	foreign key(user_id) references users(user_id) ON DELETE CASCADE,
	foreign key(board_id) references boards(board_id) ON DELETE CASCADE,
	foreign key(company_id) references companies(company_id) ON DELETE CASCADE,
	primary key(post_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table comments(
	comment_id int(9) not null AUTO_INCREMENT,
	post_id int(9) not null,
	user_id varchar(20) not null,
	company_id int(9) not null,
    comment_content longtext character set utf8mb4 collate utf8mb4_unicode_ci not null,
	comment_content_except_htmltag longtext character set utf8mb4 collate utf8mb4_unicode_ci not null,
    comment_register_time timestamp not null,
    comment_referenced_id int(9),
    replies_count int(9) default 0,
	foreign key(post_id) references posts(post_id) ON DELETE CASCADE ,
	foreign key(user_id) references users(user_id) ON DELETE CASCADE,
	foreign key(company_id) references companies(company_id) ON DELETE CASCADE,
    primary key(comment_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table files(
	file_id int(9) not null auto_increment,
    post_id int(9) ,
    comment_id int(9),
    resource_url varchar(500) not null,
    origin_file_name varchar(200) not null,
    stored_file_name varchar(200) not null,
    file_size int(11) NULL default null,
    upload_time timestamp not null,
    primary key(file_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table alarms(
	alarm_id int(9) not null auto_increment,
    tagged_user_id varchar(20) not null,
    post_id int(9),
    comment_id int(9),
    is_read boolean default false,
	foreign key(post_id) references posts(post_id) ON DELETE CASCADE ,
	foreign key(comment_id) references comments(comment_id) ON DELETE CASCADE,
    primary key(alarm_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table view_records(
	post_id int(9) not null,
    user_id varchar(20) not null,
    foreign key(post_id) references posts(post_id) ON DELETE CASCADE ,
    foreign key(user_id) references users(user_id) ON DELETE CASCADE ,
    primary key(post_id,user_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

alter table boards auto_increment=1;
alter table posts auto_increment=1;
alter table comments auto_increment=1;
alter table alarms auto_increment=1;

insert into companies values(1,'WORKS MOBILE');
insert into companies values(2,'naver');

insert into boards (company_id,board_name) values(1,"공지사항");
insert into boards (company_id,board_name) values(2,"건의사항");
insert into boards (company_id,board_name) values(2,"공지사항");
insert into boards (company_id,board_name) values(2,"건의사항");
insert into boards (company_id,board_name) values(1,"자유게시판");

insert into users values('admin', '1', '관리자', '123', '관리자', 'https://block-board-user.s3.amazonaws.com/admin.png', 'admin.png', 'https://block-board-user-thumbnail.s3.amazonaws.com/admin.png', 'admin.png');
insert into users values(1, '1', '김동욱', '123', '관리자', 'https://block-board-user.s3.amazonaws.com/dongwook.jpg', 'dongwook.jpg', 'https://block-board-user-thumbnail.s3.amazonaws.com/dongwook.jpg', 'dongwook.jpg');
insert into users values(2, '1', '전우혁', '123', '관리자', 'https://block-board-user.s3.amazonaws.com/woohyuk.jpg', 'woohyuk.jpg', 'https://block-board-user-thumbnail.s3.amazonaws.com/woohyuk.jpg', 'woohyuk.jpg');
insert into users values('irene', '1', '아이린', '123', '일반', 'https://block-board-user.s3.amazonaws.com/irene.jpg', 'irene.jpg', 'https://block-board-user-thumbnail.s3.amazonaws.com/irene.jpg', 'irene.jpg');
insert into users values('joy', '1', '조이', '123', '일반', 'https://block-board-user.s3.amazonaws.com/joy.jpg', 'joy.jpg', 'https://block-board-user-thumbnail.s3.amazonaws.com/joy.jpg', 'joy.jpg');
insert into users values('seulgi', '1', '슬기', '123', '일반', 'https://block-board-user.s3.amazonaws.com/seulgi.jpg', 'seulgi.jpg', 'https://block-board-user-thumbnail.s3.amazonaws.com/seulgi.jpg', 'seulgi.jpg');
insert into users values('wendy', '1', '웬디', '123', '일반', 'https://block-board-user.s3.amazonaws.com/wendy.png', 'wendy.png', 'https://block-board-user-thumbnail.s3.amazonaws.com/wendy.png', 'wendy.png');
insert into users values('yeri', '1', '예리', '123', '일반', 'https://block-board-user.s3.amazonaws.com/yeri.jpg', 'yeri.jpg', 'https://block-board-user-thumbnail.s3.amazonaws.com/yeri.jpg', 'yeri.jpg');

insert into functions values(1,'댓글');
insert into functions values(2,'대댓글');
insert into functions values(3,'게시물 파일첨부');
insert into functions values(4,'댓글/답글 파일첨부');
insert into functions values(5,'게시물 inline 이미지');
insert into functions values(6,'댓글/답글 inline 이미지');
insert into functions values(7,'임시저장');
insert into functions values(9,'게시물 스티커');
insert into functions values(10,'댓글/답글 스티커');
insert into functions values(11,'게시물 자동태그');
insert into functions values(12,'댓글/답글 자동태그');

insert into view_records values(1,"irene");
insert into view_records values(1,"yeri");
insert into view_records values(1,"2");
insert into view_records values(1,"admin");
insert into view_records values(1,"seulgi");

insert into view_records values(1,"rkdgPdnjs");
insert into view_records values(1,"rnjsdmsql");
insert into view_records values(1,"rlaalswn");
insert into view_records values(1,"rlacodnjs");
insert into view_records values(1,"aldhkdizl");
insert into view_records values(1,"dkqnzlskzh");
insert into view_records values(1,"dksdbwls");
insert into view_records values(1,"dlcodus");
insert into view_records values(1,"wkddnjsdud");
insert into view_records values(1,"whdbfl");
insert into view_records values(1,"chldPsk");
insert into view_records values(1,"ghsekglxhal");
insert into view_records values(1,"rlaskawn");
insert into view_records values(1,"eldh");
insert into view_records values(1,"foqahstmxj");
insert into view_records values(1,"fpdl");
insert into view_records values(1,"fhwp");
insert into view_records values(1,"fltk");
insert into view_records values(1,"qorgus");
insert into view_records values(1,"qnl");
insert into view_records values(1,"tjgus");
insert into view_records values(1,"tpgns");
insert into view_records values(1,"thsskdms");
insert into view_records values(1,"tndud");
insert into view_records values(1,"tngh");
insert into view_records values(1,"tbrk");
insert into view_records values(1,"tldnals");
insert into view_records values(1,"Tjsl");
insert into view_records values(1,"dhgkdud");
insert into view_records values(1,"dbfl");
insert into view_records values(1,"dbsdk");
insert into view_records values(1,"dlcodus");
insert into view_records values(1,"wkddnsdud");
insert into view_records values(1,"wjdrnr");
insert into view_records values(1,"wjddmswl");
insert into view_records values(1,"wpsl");
insert into view_records values(1,"wptlzk");
insert into view_records values(1,"wpdlghq");
insert into view_records values(1,"wlals");
insert into view_records values(1,"wltn");
insert into view_records values(1,"wls");
insert into view_records values(1,"cksduf");
insert into view_records values(1,"cps");
insert into view_records values(1,"zkdl");
insert into view_records values(1,"xlvksl");
insert into view_records values(1,"gydus");
