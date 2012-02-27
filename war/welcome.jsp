<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="project.fantalk.model.*"%>
<%@ page import="project.fantalk.api.Utils"%>
<%@ page import="java.util.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String appid=Utils.getAppID();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>FanTalk - 基于XMPP的多功能饭否机器人</title>
		<meta http-equiv="keywords"
			content="FanTalk,Fanfou,饭否,Twitter,GTalk,XMPP" />
		<meta http-equiv="description" content="FanTalk,Fanfou Tool" />
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="refresh" content="15;url=http://<% out.print(appid); %>.appspot.com/index.jsp" />
		<link rel="stylesheet" type="text/css" href="static/main.css" />
		<title>FanTalk - 基于XMPP的多功能饭否机器人</title>
	</head>
	<body>
		<h1>
			FanTalk
		</h1>
		<div class="space"></div>
		<div class="line">
			<div class="space"></div>
		</div>
		<p>
			<a id="login" href="web/index.jsp">进入FanTalk首页</a>
		</p>
		<div class="copyright">
			&copy;2010-2011
			<a href="http://www.wheremylife.com/">wheremylife.com</a>.
		</div>
	</body>
</html>
