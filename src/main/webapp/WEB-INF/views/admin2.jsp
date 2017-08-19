<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<LINK rel="Bookmark" href="/favicon.ico">
<LINK rel="Shortcut Icon" href="/favicon.ico" />
<!--[if lt IE 9]>
<script type="text/javascript" src="static/html5.js"></script>
<script type="text/javascript" src="static/respond.min.js"></script>
<script type="text/javascript" src="static/PIE_IE678.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css"
	href="static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css"
	href="static/Hui-iconfont/1.0.7/iconfont.css" />
<link rel="stylesheet" type="text/css" href="static/icheck/icheck.css" />
<link rel="stylesheet" type="text/css"
	href="static/h-ui/skin/red/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="static/h-ui/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>志愿者微信管理系统</title>
</head>
<body>
	<header class="navbar-wrapper">
		<div class="navbar navbar-fixed-top">
			<div class="container-fluid cl">
				<a class="logo navbar-logo f-l mr-10 hidden-xs" href="index.html">志愿者微信管理系统</a>
				<!-- 
			<nav class="nav navbar-nav">
				<ul class="cl">
					<li class="dropDown dropDown_hover"><a href="javascript:;" class="dropDown_A"><i class="Hui-iconfont">&#xe600;</i> 新增 <i class="Hui-iconfont">&#xe6d5;</i></a>
						<ul class="dropDown-menu menu radius box-shadow">
							<li><a href="javascript:;" onclick="article_add('添加资讯','article-add.html')"><i class="Hui-iconfont">&#xe616;</i> 资讯</a></li>
							<li><a href="javascript:;" onclick="picture_add('添加资讯','picture-add.html')"><i class="Hui-iconfont">&#xe613;</i> 图片</a></li>
							<li><a href="javascript:;" onclick="product_add('添加资讯','product-add.html')"><i class="Hui-iconfont">&#xe620;</i> 产品</a></li>
							<li><a href="javascript:;" onclick="member_add('添加用户','member-add.html','','510')"><i class="Hui-iconfont">&#xe60d;</i> 用户</a></li>
						</ul>
					</li>
				</ul>
			</nav>
		 -->
				<nav id="Hui-userbar"
					class="nav navbar-nav navbar-userbar hidden-xs">
					<ul class="cl">
						<li class="dropDown dropDown_hover"><a href="#"
							class="dropDown_A">系统管理员 <i class="Hui-iconfont">&#xe6d5;</i></a>
							<ul class="dropDown-menu menu radius box-shadow">
								<li><a href="#">个人信息</a></li>
								<li><a href="login/logout">退出</a></li>
							</ul></li>
					</ul>
				</nav>
			</div>
		</div>
	</header>
	<aside class="Hui-aside">
		<div class="menu_dropdown bk_2">
			<dl id="menu-article">
				<dt>
					<i class="Hui-iconfont">&#xe60d;</i> 用户模块<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a _href="company-shenhe.html" data-title="企业管理" href="javascript:void(0)">企业管理</a></li>
						<li><a _href="volunteers-list.html" data-title="志愿者管理" href="javascript:void(0)">志愿者管理</a></li>
					</ul>
				</dd>
			</dl>
			<dl id="menu-article">
				<dt>
					<i class="Hui-iconfont">&#xe60d;</i> 新闻模块<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a _href="about-us.html" data-title="关于我们"
							href="javascript:void(0)">关于我们</a></li>
						<li><a _href="events-list.html" data-title="活动管理"
							href="javascript:void(0)">活动管理</a></li>
						<li><a _href="news-list.html" data-title="公益脚印"
							href="javascript:void(0)">公益脚印</a></li>
					</ul>
				</dd>
			</dl>
			<dl id="menu-article">
				<dt>
					<i class="Hui-iconfont">&#xe60d;</i> 系统模块<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a _href="admin-list.html" data-title="管理员管理"
							href="javascript:void(0)">管理员管理</a></li>
					</ul>
				</dd>
			</dl>
			<!-- <dl id="menu-admin">
				<dt>
					<i class="Hui-iconfont">&#xe62d;</i> 管理员管理<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a _href="admin-role.html" data-title="角色管理"
							href="javascript:void(0)">角色管理</a></li>
						<li><a _href="admin-permission.html" data-title="权限管理"
							href="javascript:void(0)">权限管理</a></li>
						<li><a _href="admin-list.html" data-title="管理员列表"
							href="javascript:void(0)">管理员列表</a></li>
					</ul>
				</dd>
			</dl>
			<dl id="menu-system">
				<dt>
					<i class="Hui-iconfont">&#xe62e;</i> 系统管理<i
						class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
				</dt>
				<dd>
					<ul>
						<li><a _href="system-base.html" data-title="系统设置"
							href="javascript:void(0)">系统设置</a></li>
						<li><a _href="system-data.html" data-title="数据字典"
							href="javascript:void(0)">数据字典</a></li>
						<li><a _href="system-shielding.html" data-title="屏蔽词"
							href="javascript:void(0)">屏蔽词</a></li>
						<li><a _href="system-log.html" data-title="系统日志"
							href="javascript:void(0)">系统日志</a></li>
					</ul>
				</dd>
			</dl> -->
		</div>
	</aside>
	<div class="dislpayArrow hidden-xs">
		<a class="pngfix" href="javascript:void(0);"
			onClick="displaynavbar(this)"></a>
	</div>
	<section class="Hui-article-box">
		<div id="Hui-tabNav" class="Hui-tabNav hidden-xs">
			<div class="Hui-tabNav-wp">
				<ul id="min_title_list" class="acrossTab cl">
					<li class="active"><span title="我的桌面" data-href="welcome.html">我的桌面</span><em></em></li>
				</ul>
			</div>
			<div class="Hui-tabNav-more btn-group">
				<a id="js-tabNav-prev" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a
					id="js-tabNav-next" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a>
			</div>
		</div>
		<div id="iframe_box" class="Hui-article">
			<div class="show_iframe">
				<div style="display: none" class="loading"></div>
				<iframe scrolling="yes" frameborder="0" src="welcome.html"></iframe>
			</div>
		</div>
	</section>
	<script type="text/javascript" src="static/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="static/layer/2.1/layer.js"></script>
	<script type="text/javascript" src="static/h-ui/js/H-ui.js"></script>
	<script type="text/javascript" src="static/h-ui/js/H-ui.admin.js"></script>
	<script type="text/javascript">
		/*资讯-添加*/
		function article_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*图片-添加*/
		function picture_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*产品-添加*/
		function product_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*用户-添加*/
		function member_add(title, url, w, h) {
			layer_show(title, url, w, h);
		}
	</script>
</body>
</html>