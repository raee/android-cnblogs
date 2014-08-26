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
isreaded				INTEGER						not null default 0,
primary key (blogid, title, summary, body, author, viewcout, commentcount, postdate,isreaded),
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
insert into categroys(cate_name,cateid)values('NET技术','108698');
insert into categroys(cate_name,cateid)values('NET新手区','beginner');
insert into categroys(cate_name,cateid)values('ASP.NET','aspnet');
insert into categroys(cate_name,cateid)values('C#','csharp');
insert into categroys(cate_name,cateid)values('WinForm','winform');
insert into categroys(cate_name,cateid)values('Silverlight','silverlight');
insert into categroys(cate_name,cateid)values('WCF','wcf');
insert into categroys(cate_name,cateid)values('WPF','wpf');
insert into categroys(cate_name,cateid)values('ASP.NET MVC','mvc');
insert into categroys(cate_name,cateid)values('控件开发','control');
insert into categroys(cate_name,cateid)values('Entity Framework','ef');
insert into categroys(cate_name,cateid)values('WinRT & Metro','winrt_metro');
insert into categroys(cate_name,cateid)values('编程语言','cate12');
insert into categroys(cate_name,cateid)values('Java','java');
insert into categroys(cate_name,cateid)values('C++','cpp');
insert into categroys(cate_name,cateid)values('PHP','php');
insert into categroys(cate_name,cateid)values('Python','python');
insert into categroys(cate_name,cateid)values('C','c');
insert into categroys(cate_name,cateid)values('delphi','delphi');
insert into categroys(cate_name,cateid)values('ruby','ruby');
insert into categroys(cate_name,cateid)values('erlang','erlang');
insert into categroys(cate_name,cateid)values('go','go');
insert into categroys(cate_name,cateid)values('verilog','verilog');
insert into categroys(cate_name,cateid)values('软件设计','108701');
insert into categroys(cate_name,cateid)values('架构设计','design');
insert into categroys(cate_name,cateid)values('面向对象','108702');
insert into categroys(cate_name,cateid)values('Web前端','108703');
insert into categroys(cate_name,cateid)values('Html/Css','web');
insert into categroys(cate_name,cateid)values('JavaScript','javascript');
insert into categroys(cate_name,cateid)values('JQuery','jquery');
insert into categroys(cate_name,cateid)values('HTML5','html5');
insert into categroys(cate_name,cateid)values('手机开发','108705');
insert into categroys(cate_name,cateid)values('Android开发','android');
insert into categroys(cate_name,cateid)values('IOS开发','ios');
insert into categroys(cate_name,cateid)values('Windows Phone','wp');
insert into categroys(cate_name,cateid)values('Windows Mobile','wm');
insert into categroys(cate_name,cateid)values('软件工程','108709');
insert into categroys(cate_name,cateid)values('敏捷开发','agile');
insert into categroys(cate_name,cateid)values('项目与团队管理','pm');
insert into categroys(cate_name,cateid)values('软件工程其他','pm');
insert into categroys(cate_name,cateid)values('数据库技术','108712');
insert into categroys(cate_name,cateid)values('SqlServer','sqlserver');
insert into categroys(cate_name,cateid)values('Oracle','oracle');
insert into categroys(cate_name,cateid)values('MySQL','mysql');
insert into categroys(cate_name,cateid)values('NoSQL','nosql');
insert into categroys(cate_name,cateid)values('其他数据库','database');
