## site category page

GET/POST: http://www.cnblogs.com/mvc/AggSite/PostList.aspx

>请求参数：

| 参数名称           | 参数说明      |
| -------------      | ------------- |
| CategoryType       | TopSiteCategory，取值参考如下 |
| ParentCategoryId   | 父级，如：.NET技术、编程语言 |
| CategoryId         | 子分类, |
| PageIndex          | 页码，取值：1、2、3... |
| ItemListActionName | PostList |

>CategoryType取值

| 参数名称       | 参数说明      |
|MyDigged	     |我赞过的
|MyCommented     |我评论过的
|News	         |新闻
|MyFollowing     |我关注的
|HomeCandidate   |首页候选区
|Picked	         |精选
|AllPosts	     |所有随笔
|SiteHome	     |首页
|SiteCategory    |首页分类
|TopSiteCategory |首页分类

>分类取值

```sql
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('首页','SiteHome',0,808);

-- 一级分类
DELETE FROM APP_BLOGS;
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('.NET技术','TopSiteCategory',0,108698);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('编程语言','TopSiteCategory',0,2);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('软件设计','TopSiteCategory',0,108701);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Web前端','TopSiteCategory',0,108703);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('企业信息化','TopSiteCategory',0,108704);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('手机开发','TopSiteCategory',0,108705);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('软件工程','TopSiteCategory',0,108709);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('数据库技术','TopSiteCategory',0,108712);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('操作系统','TopSiteCategory',0,108724);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('其他分类','TopSiteCategory',0,4);

INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('.NET新手区','TopSiteCategory',108698,/cate/beginner/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('ASP.NET','TopSiteCategory',108698,/cate/aspnet/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('C#','TopSiteCategory',108698,/cate/csharp/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('.NET Core','TopSiteCategory',108698,/cate/dotnetcore/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('WinForm','TopSiteCategory',108698,/cate/winform/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Silverlight','TopSiteCategory',108698,/cate/silverlight/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('WCF','TopSiteCategory',108698,/cate/wcf/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('CLR','TopSiteCategory',108698,/cate/clr/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('WPF','TopSiteCategory',108698,/cate/wpf/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('XNA','TopSiteCategory',108698,/cate/xna/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Visual Studio','TopSiteCategory',108698,/cate/vs2010/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('ASP.NET MVC','TopSiteCategory',108698,/cate/mvc/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('控件开发','TopSiteCategory',108698,/cate/control/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Entity Framework','TopSiteCategory',108698,/cate/ef/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('NHibernate','TopSiteCategory',108698,/cate/nhibernate/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('WinRT/Metro','TopSiteCategory',108698,/cate/winrt_metro/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Java','TopSiteCategory',2,/cate/java/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('C++','TopSiteCategory',2,/cate/cpp/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('PHP','TopSiteCategory',2,/cate/php/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Delphi','TopSiteCategory',2,/cate/delphi/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Python','TopSiteCategory',2,/cate/python/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Ruby','TopSiteCategory',2,/cate/ruby/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('C语言','TopSiteCategory',2,/cate/c/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Erlang','TopSiteCategory',2,/cate/erlang/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Go','TopSiteCategory',2,/cate/go/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Swift','TopSiteCategory',2,/cate/swift/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Scala','TopSiteCategory',2,/cate/scala/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('R语言','TopSiteCategory',2,/cate/r/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Verilog','TopSiteCategory',2,/cate/verilog/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('其它语言','TopSiteCategory',2,/cate/otherlang/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('架构设计','TopSiteCategory',108701,/cate/design/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('面向对象','TopSiteCategory',108701,/cate/108702/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('设计模式','TopSiteCategory',108701,/cate/dp/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('领域驱动设计','TopSiteCategory',108701,/cate/ddd/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Html/Css','TopSiteCategory',108703,/cate/web/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('JavaScript','TopSiteCategory',108703,/cate/javascript/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('jQuery','TopSiteCategory',108703,/cate/jquery/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('HTML5','TopSiteCategory',108703,/cate/html5/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('SharePoint','TopSiteCategory',108704,/cate/sharepoint/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('GIS技术','TopSiteCategory',108704,/cate/gis/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('SAP','TopSiteCategory',108704,/cate/sap/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Oracle ERP','TopSiteCategory',108704,/cate/OracleERP/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Dynamics CRM','TopSiteCategory',108704,/cate/dynamics/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('K2 BPM','TopSiteCategory',108704,/cate/k2/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('信息安全','TopSiteCategory',108704,/cate/infosec/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('企业信息化其他','TopSiteCategory',108704,/cate/3/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Android开发','TopSiteCategory',108705,/cate/android/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('iOS开发','TopSiteCategory',108705,/cate/ios/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Windows Phone','TopSiteCategory',108705,/cate/wp/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Windows Mobile','TopSiteCategory',108705,/cate/wm/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('其他手机开发','TopSiteCategory',108705,/cate/mobile/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('敏捷开发','TopSiteCategory',108709,/cate/agile/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('项目与团队管理','TopSiteCategory',108709,/cate/pm/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('软件工程其他','TopSiteCategory',108709,/cate/Engineering/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('SQL Server','TopSiteCategory',108712,/cate/sqlserver/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Oracle','TopSiteCategory',108712,/cate/oracle/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('MySQL','TopSiteCategory',108712,/cate/mysql/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('NoSQL','TopSiteCategory',108712,/cate/nosql/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('大数据','TopSiteCategory',108712,/cate/bigdata/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('其它数据库','TopSiteCategory',108712,/cate/database/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Windows','TopSiteCategory',108724,/cate/win7/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Windows Server','TopSiteCategory',108724,/cate/winserver/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Linux','TopSiteCategory',108724,/cate/linux/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('OS X','TopSiteCategory',108724,/cate/osx/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('嵌入式','TopSiteCategory',108724,/cate/eos/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('非技术区','TopSiteCategory',4,/cate/life/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('软件测试','TopSiteCategory',4,/cate/testing/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('代码与软件发布','TopSiteCategory',4,/cate/software/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('计算机图形学','TopSiteCategory',4,/cate/cg/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Google开发','TopSiteCategory',4,/cate/google/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('游戏开发','TopSiteCategory',4,/cate/gamedev/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('程序人生','TopSiteCategory',4,/cate/codelife/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('求职面试','TopSiteCategory',4,/cate/job/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('读书区','TopSiteCategory',4,/cate/book/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('转载区','TopSiteCategory',4,/cate/quoted/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Windows CE','TopSiteCategory',4,/cate/wince/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('翻译区','TopSiteCategory',4,/cate/translate/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('开源研究','TopSiteCategory',4,/cate/opensource/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('Flex','TopSiteCategory',4,/cate/flex/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('云计算','TopSiteCategory',4,/cate/cloud/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('算法与数据结构','TopSiteCategory',4,/cate/algorithm/);
INSERT INTO APP_BLOGS(NAME,categoryType,ParentCategoryId,categoryId)values('其他技术区','TopSiteCategory',4,/cate/misc/);
```


