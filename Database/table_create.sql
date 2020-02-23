use block_board;
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
    foreign key(company_id) references companies(company_id),
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
    foreign key(company_id) references companies(company_id),
    foreign key(function_id) references functions(function_id),
    primary key(company_id,function_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table boards(
	board_id int(9) not null AUTO_INCREMENT,
    company_id int(9) not null,
    board_name varchar(150) not null,
	foreign key(company_id) references companies(company_id),
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

insert into users (user_id,company_id,user_name,user_password,user_type) values(1,1,'김동욱','123','관리자');
insert into users (user_id,company_id,user_name,user_password,user_type) values(2,1,'전우혁','123','사원');
insert into users (user_id,company_id,user_name,user_password,user_type) values(3,2,'곽대훈','123','관리자');

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

insert into functions_check values(1,1,null);
insert into functions_check values(2,4,null);

insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values(1,1,1,'첫게시글','<p>첫내용</p>', '첫내용', now());
insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values (1,1,1,'두번째 게시글','<p>두번째 게시글내용</p>','두번째 게시글내용',now());
insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values (2,1,1,'건의사항 게시판 첫글','<p>공지사항이네</p>', '공지사항이네',now());
insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values (2,1,1,'공지사항 게시판 ','<p>ㅎㅎㅎㅎ테스트</p>','ㅎㅎㅎㅎ테스트',now());
insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values (1,5,1,'자유1','<p>1111</p>','1111',now());
insert into posts (user_id, board_id, company_id, post_title, post_content, post_content_except_htmltag, post_register_time) values (1,5,1,'자유2','<p>22222</p>','22222',now());

select * from posts;
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (1,1,2,"Suspendisse","vel nisl. Quisque fringilla euismod enim. Etiam gravida molestie","mauris eu","2019-12-09 00:37:22"),(1,1,1,"sapien, cursus","dictum. Proin eget odio. Aliquam vulputate ullamcorper magna.","Nullam nisl. Maecenas malesuada fringilla est. Mauris","2018-12-27 01:33:59"),(2,1,2,"turpis nec","lacus. Nulla","sapien. Nunc pulvinar arcu et pede. Nunc sed","2018-04-29 06:34:13"),(1,1,2,"adipiscing lobortis risus. In","molestie dapibus ligula.","cursus luctus, ipsum leo elementum","2019-05-02 07:01:31"),(2,1,2,"dictum mi, ac mattis","dictum eu, eleifend nec, malesuada ut,","aliquet. Phasellus fermentum convallis ligula. Donec","2018-05-01 08:46:10"),(1,1,1,"dignissim","pede et risus. Quisque libero lacus, varius et, euismod","feugiat metus sit","2019-07-20 20:42:00"),(2,1,1,"penatibus et","amet ultricies sem magna nec quam. Curabitur","sagittis. Duis gravida. Praesent eu nulla at sem molestie sodales.","2019-06-06 13:10:15"),(2,1,2,"mollis. Duis sit","a nunc. In at pede. Cras vulputate velit","ultrices","2019-08-07 17:11:57"),(1,1,2,"Nunc mauris.","habitant morbi tristique senectus et netus et malesuada fames ac","ipsum","2019-08-15 14:51:38"),(1,1,2,"tempor","ornare,","Fusce dolor quam, elementum at, egestas a,","2018-08-26 11:07:15");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (2,1,1,"sem, consequat","Sed eget lacus. Mauris non","laoreet lectus quis massa.","2019-10-18 02:16:51"),(1,1,1,"gravida. Praesent","Proin vel nisl.","dolor, tempus non, lacinia at, iaculis quis, pede. Praesent","2018-10-15 12:44:08"),(2,1,1,"justo nec","a, aliquet","a mi fringilla mi lacinia mattis. Integer eu lacus. Quisque","2018-05-16 01:16:10"),(1,1,1,"orci,","molestie in, tempus eu, ligula. Aenean euismod","vel lectus. Cum sociis natoque penatibus et","2019-04-30 11:35:28"),(1,1,2,"ipsum","taciti sociosqu ad litora torquent per conubia nostra, per","tincidunt, nunc ac mattis ornare, lectus","2019-07-13 01:33:16"),(1,1,2,"Donec tincidunt. Donec vitae","nec, cursus a, enim. Suspendisse aliquet, sem","Curabitur consequat, lectus sit amet luctus vulputate,","2018-08-18 12:09:11"),(2,1,1,"id, erat.","dui. Fusce aliquam, enim nec tempus scelerisque,","convallis convallis dolor. Quisque tincidunt","2019-04-13 05:40:45"),(2,1,1,"libero","blandit at, nisi. Cum","purus ac tellus. Suspendisse sed dolor. Fusce mi lorem,","2018-09-15 16:00:21"),(2,1,2,"metus. Aliquam erat volutpat.","risus. Morbi metus. Vivamus euismod urna. Nullam lobortis quam a","nulla magna, malesuada vel, convallis","2019-01-04 09:27:17"),(2,1,2,"Aliquam nisl. Nulla","purus gravida sagittis.","Mauris magna. Duis dignissim tempor arcu. Vestibulum ut eros non","2019-03-04 06:15:57");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (2,1,1,"est. Mauris","mollis. Duis sit amet diam eu dolor egestas rhoncus. Proin","elit, dictum eu, eleifend nec, malesuada ut, sem.","2019-10-01 17:58:20"),(2,1,2,"ipsum. Phasellus","risus.","orci luctus et ultrices posuere cubilia Curae; Phasellus ornare.","2018-06-02 23:24:45"),(1,1,2,"eu, euismod ac,","sed leo. Cras vehicula aliquet libero.","natoque penatibus","2019-10-13 12:51:02"),(2,1,2,"diam dictum sapien. Aenean","felis.","lorem","2018-10-16 13:44:57"),(1,1,2,"ut quam vel sapien","Proin vel arcu eu odio tristique pharetra. Quisque ac","Curabitur vel lectus. Cum sociis natoque penatibus et magnis dis","2020-01-14 23:28:52"),(1,1,2,"penatibus","ullamcorper. Duis","egestas a, dui. Cras pellentesque. Sed dictum.","2019-06-16 13:06:59"),(2,1,1,"aliquet. Phasellus fermentum","tristique senectus et netus et malesuada fames ac turpis egestas.","Cras dolor dolor,","2019-08-25 09:21:33"),(2,1,1,"Nam ac nulla. In","ipsum nunc id","Mauris blandit enim consequat purus. Maecenas libero","2018-11-08 00:41:57"),(1,1,1,"tellus id nunc","orci tincidunt adipiscing. Mauris molestie pharetra","mauris sapien, cursus in, hendrerit consectetuer,","2018-12-10 04:31:24"),(1,1,1,"blandit mattis. Cras eget","ridiculus mus. Aenean","luctus,","2018-08-07 14:00:08");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (2,1,1,"eu, odio. Phasellus at","Nullam vitae diam. Proin","nisl elementum purus, accumsan interdum libero dui nec","2018-03-27 00:37:12"),(1,1,1,"odio vel est tempor","consectetuer rhoncus. Nullam velit dui, semper et, lacinia vitae, sodales","ac libero","2018-02-14 22:04:04"),(2,1,1,"quis diam.","tristique ac, eleifend vitae, erat. Vivamus nisi. Mauris nulla.","nec, mollis vitae, posuere at, velit.","2019-04-08 01:57:40"),(2,1,2,"semper tellus","netus et malesuada","aliquet odio. Etiam ligula tortor, dictum eu, placerat","2018-05-31 09:20:27"),(1,1,2,"at,","accumsan sed, facilisis vitae,","fermentum","2019-07-16 07:16:37"),(1,1,1,"sit amet diam","pede blandit","nunc ac mattis","2018-07-22 00:43:35"),(1,1,1,"nisl arcu","lorem vitae odio sagittis semper. Nam tempor diam dictum","iaculis","2018-06-03 19:56:18"),(1,1,2,"mauris, aliquam","laoreet,","eget varius ultrices, mauris ipsum porta elit, a feugiat tellus","2019-09-11 17:39:17"),(1,1,1,"sagittis. Nullam vitae diam.","Nunc lectus","scelerisque scelerisque dui. Suspendisse ac metus vitae","2018-12-23 05:13:49"),(2,1,2,"est","gravida non, sollicitudin","euismod enim. Etiam gravida molestie arcu.","2018-03-23 00:13:39");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (1,1,2,"massa lobortis ultrices.","orci. Ut semper pretium neque. Morbi quis urna.","purus. Duis elementum, dui quis accumsan","2018-06-02 02:29:26"),(2,1,2,"turpis. Aliquam adipiscing lobortis","adipiscing lacus. Ut nec","quis urna. Nunc quis arcu","2018-11-12 23:35:16"),(1,1,1,"mi felis, adipiscing","facilisis facilisis, magna","elit. Aliquam auctor, velit eget","2018-02-25 05:29:20"),(2,1,1,"a tortor. Nunc","auctor. Mauris","sit amet ultricies sem magna nec quam.","2019-08-18 21:57:29"),(2,1,2,"commodo auctor velit.","et, commodo at, libero. Morbi accumsan laoreet ipsum.","Sed nulla ante, iaculis nec, eleifend non, dapibus rutrum, justo.","2018-12-17 02:05:01"),(2,1,1,"sit amet","enim, condimentum eget, volutpat ornare, facilisis eget, ipsum.","nulla. Integer vulputate, risus a ultricies adipiscing, enim mi tempor","2018-12-05 18:06:05"),(2,1,1,"ultricies dignissim lacus. Aliquam","amet, consectetuer adipiscing elit. Etiam laoreet, libero et tristique pellentesque,","orci lacus vestibulum lorem, sit amet ultricies sem","2019-07-26 09:44:00"),(2,1,2,"neque.","tempus risus. Donec egestas. Duis ac arcu. Nunc","Quisque libero lacus, varius et, euismod et, commodo at, libero.","2019-09-08 23:35:03"),(1,1,1,"luctus","magnis dis parturient montes, nascetur ridiculus mus. Aenean eget","magna. Ut tincidunt orci quis lectus. Nullam","2019-06-03 02:36:46"),(2,1,2,"Phasellus dapibus","consectetuer adipiscing elit. Etiam laoreet, libero et tristique pellentesque, tellus","at, iaculis quis, pede. Praesent eu","2018-06-13 15:08:29");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (2,1,1,"arcu.","ultricies ligula. Nullam enim. Sed nulla ante, iaculis nec,","Mauris molestie pharetra","2018-11-17 00:53:33"),(2,1,2,"amet ornare","purus mauris","fringilla mi lacinia","2020-01-21 18:57:03"),(2,1,2,"aliquam iaculis,","sed, facilisis vitae, orci. Phasellus dapibus quam quis diam.","et risus. Quisque","2018-05-30 07:19:57"),(1,1,1,"massa rutrum","scelerisque dui. Suspendisse ac","Proin velit. Sed","2019-11-27 16:28:47"),(1,1,2,"Cras dolor dolor, tempus","pellentesque. Sed dictum. Proin eget odio. Aliquam","Curabitur","2019-02-12 20:20:01"),(2,1,1,"facilisis non, bibendum sed,","nunc interdum feugiat.","parturient montes, nascetur ridiculus mus. Aenean","2018-07-30 01:57:18"),(2,1,2,"vitae velit egestas","nunc sed pede. Cum sociis natoque penatibus et","condimentum. Donec","2019-02-23 11:20:32"),(2,1,2,"hendrerit.","eu erat semper rutrum. Fusce","ac","2018-07-21 22:03:40"),(1,1,2,"suscipit","nec urna","vestibulum. Mauris magna. Duis dignissim tempor","2019-03-20 04:15:35"),(2,1,1,"tristique senectus et netus","Morbi non sapien molestie orci tincidunt adipiscing. Mauris","semper erat, in consectetuer ipsum nunc id enim. Curabitur massa.","2018-02-14 11:53:42");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (2,1,2,"et, eros. Proin ultrices.","at, egestas a, scelerisque","Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit","2020-01-03 20:06:20"),(2,1,1,"nulla. Cras","Suspendisse eleifend. Cras sed leo.","tellus","2018-10-25 21:45:45"),(2,1,2,"Morbi vehicula.","nulla. Integer vulputate, risus a ultricies adipiscing, enim mi tempor","id enim. Curabitur massa. Vestibulum accumsan neque et nunc.","2019-01-24 07:49:07"),(2,1,2,"eget metus.","mi lorem, vehicula et, rutrum eu, ultrices","id, blandit at, nisi. Cum sociis","2018-07-12 14:58:13"),(2,1,1,"montes, nascetur ridiculus mus.","Mauris non dui nec","neque. Nullam nisl. Maecenas","2020-01-08 02:04:18"),(1,1,1,"Donec egestas. Aliquam","ut erat.","massa lobortis ultrices. Vivamus rhoncus. Donec est. Nunc ullamcorper,","2019-11-26 09:52:16"),(1,1,2,"porttitor","luctus lobortis. Class aptent taciti sociosqu ad","interdum. Sed auctor odio a","2018-12-22 16:51:14"),(2,1,1,"Ut nec","tellus eu augue porttitor","odio a purus. Duis elementum, dui quis accumsan convallis,","2020-02-02 19:33:57"),(2,1,1,"Fusce","tellus non magna.","orci luctus et ultrices posuere cubilia Curae; Donec tincidunt.","2019-01-31 15:25:04"),(1,1,2,"consequat dolor vitae dolor.","neque","Proin mi. Aliquam gravida mauris ut mi.","2019-05-31 18:57:38");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (1,1,2,"felis purus","sagittis. Duis gravida. Praesent eu nulla","et libero. Proin mi. Aliquam","2018-04-18 11:43:25"),(2,1,1,"sed","nec","amet,","2018-03-14 23:56:10"),(1,1,2,"adipiscing, enim mi","eros turpis","quam dignissim pharetra. Nam ac nulla. In","2020-01-23 00:29:20"),(1,1,1,"in, dolor.","per inceptos hymenaeos. Mauris ut quam vel sapien imperdiet ornare.","Suspendisse","2019-09-06 17:05:20"),(2,1,2,"sagittis","sed, est. Nunc laoreet lectus quis massa. Mauris","ligula. Nullam feugiat placerat velit.","2018-06-06 05:39:51"),(1,1,2,"Aliquam ultrices iaculis","a ultricies adipiscing,","condimentum eget, volutpat ornare, facilisis eget, ipsum. Donec sollicitudin adipiscing","2019-11-06 23:33:21"),(2,1,1,"Praesent luctus.","non, lacinia at, iaculis","et magnis dis parturient montes, nascetur ridiculus","2019-04-18 11:04:05"),(2,1,1,"Nunc","tortor. Integer aliquam adipiscing lacus. Ut nec urna et","metus facilisis lorem tristique aliquet. Phasellus fermentum","2019-10-29 12:46:43"),(1,1,2,"vel arcu.","vel sapien imperdiet ornare. In faucibus. Morbi","amet metus. Aliquam erat volutpat. Nulla","2019-04-09 11:01:48"),(1,1,2,"est","orci lobortis augue scelerisque mollis. Phasellus libero mauris, aliquam","fringilla, porttitor vulputate, posuere vulputate,","2019-11-17 05:27:32");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (1,1,2,"penatibus et","sollicitudin adipiscing","orci tincidunt adipiscing. Mauris molestie pharetra nibh.","2020-01-27 02:56:05"),(1,1,1,"lorem ut aliquam iaculis,","et malesuada fames ac turpis","malesuada vel, convallis in,","2019-10-12 00:23:52"),(1,1,2,"Morbi","dictum eu, eleifend","mauris erat eget ipsum. Suspendisse sagittis.","2019-08-22 02:26:57"),(2,1,2,"posuere cubilia Curae; Donec","vel sapien","eget nisi dictum augue malesuada malesuada.","2019-05-10 11:54:27"),(1,1,1,"arcu eu odio","sagittis augue, eu tempor erat neque non","blandit enim consequat purus. Maecenas libero est, congue a, aliquet","2018-09-02 23:02:48"),(1,1,1,"urna. Vivamus","luctus sit amet, faucibus ut,","Aenean gravida nunc sed pede. Cum sociis natoque penatibus","2019-01-10 22:44:44"),(2,1,2,"sodales","Proin","at arcu. Vestibulum ante ipsum primis in","2019-02-02 06:13:41"),(2,1,1,"auctor quis, tristique ac,","ornare tortor at risus. Nunc","sem magna nec quam. Curabitur vel lectus. Cum","2018-06-13 06:50:41"),(2,1,1,"blandit viverra. Donec","Proin ultrices. Duis volutpat nunc sit","Donec tempus, lorem fringilla ornare","2019-10-31 07:24:25"),(1,1,1,"eu","diam dictum sapien.","placerat, augue. Sed molestie. Sed","2019-10-31 12:26:15");
INSERT INTO `Posts` (`user_id`,`board_id`,`company_id`,`post_title`,`post_content`,`post_content_except_htmltag`,`post_register_time`) VALUES (1,1,1,"cursus a, enim. Suspendisse","erat. Sed nunc est,","ac mi","2019-08-17 01:47:19"),(1,1,1,"Proin non massa non","Proin eget odio. Aliquam vulputate ullamcorper magna. Sed eu eros.","eu, accumsan sed, facilisis vitae, orci.","2019-04-27 11:58:44"),(1,1,2,"eleifend","arcu. Vivamus sit amet risus.","rhoncus id, mollis nec, cursus a, enim.","2018-10-11 14:01:36"),(1,1,2,"amet","risus. In mi pede, nonummy ut, molestie","malesuada fames ac turpis egestas. Fusce aliquet magna a","2020-01-06 19:58:52"),(1,1,2,"posuere","odio sagittis","interdum enim non nisi. Aenean","2018-04-17 17:47:55"),(2,1,2,"Aenean eget metus.","hendrerit. Donec porttitor tellus non","aptent taciti","2018-05-06 04:41:02"),(1,1,2,"Donec est","scelerisque dui. Suspendisse ac","aliquet libero. Integer in magna. Phasellus dolor","2018-11-15 04:08:41"),(2,1,1,"Cras vehicula aliquet","id magna et ipsum cursus","magna, malesuada","2019-08-03 00:02:07"),(2,1,2,"vel","eros. Nam consequat dolor vitae","aliquet odio. Etiam ligula tortor, dictum","2019-11-20 18:38:32"),(2,1,2,"mi lorem, vehicula","ligula tortor, dictum","felis eget varius","2019-08-24 12:39:21");

update posts
set company_id = 1
where company_id = 2;

update users
set image_url = "aaa" ,image_file_name = "aaa"
where user_id = "1";

select * from posts;

select * from functions;