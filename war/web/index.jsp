<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="project.fantalk.model.*"%>
<%@ page import="project.fantalk.api.Utils"%>
<%
    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    Member m = Datastore.getInstance().getAndCacheMember(email);
    boolean fanfouOAuth = !Utils.isEmpty(m.getFanfouUserName());
    boolean twitter = !Utils.isEmpty(m.getTwitterPassword());
    boolean sina = !Utils.isEmpty(m.getSinaPassword());
    boolean netease = !Utils.isEmpty(m.getNeteasePassword());
    boolean tecent = !Utils.isEmpty(m.getTecentPassword());
    boolean douban = !Utils.isEmpty(m.getDoubanPassword());
    boolean digu = !Utils.isEmpty(m.getDiguPassword());
    String appid=Utils.getAppID();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="../static/main.css" />
		<title>FanTalk - 基于XMPP的多功能饭否机器人</title>
	</head>
	<body>
		<h1>
			FanTalk
		</h1>
		<p class="center">
			<strong>基于XMPP的多功能饭否机器人</strong>
		</p>
		<p class="center-large">
			使用方法：添加
			<strong><% out.print(appid); %>@appspot.com</strong> 为好友即可使用
		</p>
		<ul>
			<li>
				饭否OAuth帐号 <%
			    if (fanfouOAuth) {
			%>(已绑定)
				<%
			    }
			%>-->
				<a href="/oauth/fanfou"> 点击<% if(fanfouOAuth){ %>重新<% } %>绑定</a>
			</li>
			<li>
				推特帐号 <%
			    if (twitter) {
			%>(已绑定)
				<%
			    }
			%>-->
				<a href="/oauth/twitter"> 点击<% if(twitter){ %>重新<% } %>绑定</a>
			</li>
			<li>
				新浪微博 <%
			    if (sina) {
			%>(已绑定)
				<%
			    }
			%>-->
				<a href="/oauth/sina"> 点击<% if(sina){ %>重新<% } %>绑定</a>
			</li>
			<li>
				豆瓣帐号 <%
			    if (douban) {
			%>(已绑定)
				<%
			    }
			%>-->
				<a href="/oauth/douban"> 点击<% if(douban){ %>重新<% } %>绑定</a>
			</li>
			<li>
				腾讯微博 <%
			    if (tecent) {
			%>(已绑定)
				<%
			    }
			%>-->
				<a href="/oauth/qq"> 点击<% if(tecent){ %>重新<% } %>绑定</a>
			</li>
			<li>
				网易微博 <%
			    if (netease) {
			%>(已绑定)
				<%
			    }
			%>-->
				<a href="/oauth/nets"> 点击<% if(netease){ %>重新<% } %>绑定</a>
			</li>
			<li>
				嘀咕帐号 --><%
			    if (digu) {
			%>(已绑定)
				<%
			    }
			%>
				<a href="#"> 请在GTalk中绑定</a>
			</li>
			<li class="not">
				Follow5帐号 -->
				<a href="#"> 暂未开放</a>
			</li>
			<li class="not">
				Buzz帐号 -->
				<a href="#"> 暂未开放</a>
			</li>
		</ul>
		<div class="copyright">
			&copy;2010-2011
			<a href="http://www.wheremylife.com/">mcxiaoke</a>
		</div>
	</body>
</html>
