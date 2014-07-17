-- 创建表
create table categroys (
cateid               VARCHAR(255)                   not null,
cate_name            VARCHAR(36)                    not null,
is_show              INT                            not null default 1,
cate_order           INT                            not null default 0,
primary key (cateid, cate_name, is_show, cate_order)
);


create table blogs (
blogid               VARCHAR(255)                   not null,
cateid               VARCHAR(255),
title                VARCHAR(120)                   not null,
summary              VARCHAR(300)                   not null,
body                 TEXT                           not null,
author               VARCHAR(36)                    not null,
viewcout             INTEGER                        not null,
commentcount         INTEGER                        not null,
postdate             VARCHAR(36)                    not null,
primary key (blogid, title, summary, body, author, viewcout, commentcount, postdate),
foreign key (cateid)
      references categroys (cateid)
);

create table options (
dicid                INTEGER                        not null,
codename             VARCHAR(36)                    not null,
dic_key              VARCHAR(36)                    not null,
value_a              VARCHAR(2048),
value_b              VARCHAR(2048),
value_c              VARCHAR(2048),
primary key (dicid, codename, dic_key)
);


--插入分类数据
insert into categroys(cateid,cate_name)values('NET技术','108698');
insert into categroys(cateid,cate_name)values('NET新手区','beginner');
insert into categroys(cateid,cate_name)values('ASP.NET','aspnet');
insert into categroys(cateid,cate_name)values('C#','csharp');
insert into categroys(cateid,cate_name)values('WinForm','winform');
insert into categroys(cateid,cate_name)values('Silverlight','silverlight');
insert into categroys(cateid,cate_name)values('WCF','wcf');
insert into categroys(cateid,cate_name)values('WPF','wpf');
insert into categroys(cateid,cate_name)values('ASP.NET MVC','mvc');

insert into categroys(cateid,cate_name)values('控件开发','control');
insert into categroys(cateid,cate_name)values('Entity Framework','ef');
insert into categroys(cateid,cate_name)values('WinRT & Metro','winrt_metro');

insert into categroys(cateid,cate_name)values('编程语言','cate12');
insert into categroys(cateid,cate_name)values('Java','java');
insert into categroys(cateid,cate_name)values('C++','cpp');
insert into categroys(cateid,cate_name)values('PHP','php');
insert into categroys(cateid,cate_name)values('Python','python');
insert into categroys(cateid,cate_name)values('C','c');
insert into categroys(cateid,cate_name)values('delphi','delphi');
insert into categroys(cateid,cate_name)values('ruby','ruby');
insert into categroys(cateid,cate_name)values('erlang','erlang');
insert into categroys(cateid,cate_name)values('go','go');
insert into categroys(cateid,cate_name)values('verilog','verilog');

insert into categroys(cateid,cate_name)values('软件设计','108701');
insert into categroys(cateid,cate_name)values('架构设计','design');
insert into categroys(cateid,cate_name)values('面向对象','108702');

insert into categroys(cateid,cate_name)values('Web前端','108703');
insert into categroys(cateid,cate_name)values('Html/Css','web');
insert into categroys(cateid,cate_name)values('JavaScript','javascript');
insert into categroys(cateid,cate_name)values('JQuery','jquery');
insert into categroys(cateid,cate_name)values('HTML5','html5');

insert into categroys(cateid,cate_name)values('手机开发','108705');
insert into categroys(cateid,cate_name)values('Android开发','android');
insert into categroys(cateid,cate_name)values('IOS开发','ios');
insert into categroys(cateid,cate_name)values('Windows Phone','wp');
insert into categroys(cateid,cate_name)values('Windows Mobile','wm');

insert into categroys(cateid,cate_name)values('软件工程','108709');
insert into categroys(cateid,cate_name)values('敏捷开发','agile');
insert into categroys(cateid,cate_name)values('项目与团队管理','pm');
insert into categroys(cateid,cate_name)values('软件工程其他','pm');

insert into categroys(cateid,cate_name)values('数据库技术','108712');
insert into categroys(cateid,cate_name)values('SqlServer','sqlserver');
insert into categroys(cateid,cate_name)values('Oracle','oracle');
insert into categroys(cateid,cate_name)values('MySQL','mysql');
insert into categroys(cateid,cate_name)values('NoSQL','nosql');
insert into categroys(cateid,cate_name)values('其他数据库','database');
