<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="renderer" content="webkit">
		<link rel="shortcut icon" href="" type="image/x-icon">
		<title>后台管理</title>
	</head>
	<body>
		<div class="container">
			<div class="head clearFix">
				<div class="h-l clearFix">
					<div class="h-l-l">
						<img src="../../../imgs/admin/shape.png">
					</div>
					<div class="h-l-r">
						<p>管理系统后台</p>
						<span>The background management system</span>
					</div>
				</div>
				<div class="h-r">
					<div class="h-r-up">
						<a href="#" class="first">
							<div></div>首页</a>
						<span>|</span>
						<a href="#">帮助</a>
						<span>|</span>
						<a href="#">关于</a>
						<span>|</span>
						<a href="#">退出</a>
					</div>
					<div class="h-r-down clearFix">
						<div class="img">
							<img src="#">
						</div>
						<div class="name">
							<p>admin</p>
						</div>
						<div class="info">
							消息<i>8</i>
						</div>
						<div class="password">
							<a href="#">修改密码</a>
						</div>
					</div>
				</div>
			</div>
			<div class="content clearFix">
				<div class="c-side">
					<div class="p">
						<div class="p-icon-1"></div>
						<div>人员机构</div>
						<p class="p-icon-2"></p>
					</div>
					<ul>
						<li class="on">
							<div></div>
							<a href="javascript:;">用户管理</a>
						</li>
						<li>
							<div></div>
							<a href="javascript:;">角色管理</a>
						</li>
						<li>
							<div></div>
							<a href="javascript:;">权限管理</a>
						</li>
						<li>
							<div></div>
							<a href="javascript:;">日志管理</a>
						</li>
						<li>
							<div></div>
							<a href="javascript:;">在线管理</a>
						</li>
					</ul>
					<div class="p">
						<div class="p-icon-1 p1"></div>
						<div>权限管理</div>
						<p></p>
					</div>
					<ul>
						<li>
							<div></div>
							<a href="javascript:;">所有模板</a>
						</li>
					</ul>
					<div class="p">
						<div class="p-icon-1 p2"></div>
						<div>资源中心</div>
						<p></p>
					</div>
					<ul>
						<li>
							<div></div>
							<a href="javascript:;">添加资源</a>
						</li>
					</ul>
					<div class="p">
						<div class="p-icon-1 p3"></div>
						<div>日志管理</div>
						<p></p>
					</div>
					<ul>
						<li>
							<div></div>
							<a href="javascript:;">所有课程</a>
						</li>
					</ul>
				</div>
				<div class="c-iframe">
					<iframe src="../html/notice.html" id="myiframe" onLoad="reinitIframe()" scrolling="auto" frameborder="0"
                    marginheight=0 marginwidth=0></iframe>
				</div>
			</div>
		</div>
		<div class="foot">
			<i>沪ICP备13008591号-3</i>&nbsp;&nbsp;
			<span>Copyright 1970-2017</span>
		</div>
	</body>
	<script type="text/javascript" src="../publicJs/admin.js"></script>
</html>