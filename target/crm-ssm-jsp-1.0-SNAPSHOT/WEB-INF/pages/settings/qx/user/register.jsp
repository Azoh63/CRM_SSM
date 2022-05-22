<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //把登录页面设置为顶层页面
            if(window.top!=window){
                window.top.location=window.location;
            }
            //按回车键实现登录
            $(window).keydown(function (event) {
                //alert(event.keyCode);
                if(event.keyCode == 13){
                    $("#registerBtn").click();
                }
            });

            //js中获取属性值有两种方式，prop和attr；attr用于值不是true/false；prop只用于值是true/false的属性
            $("#registerBtn").click(function () {
                var registerAct = $.trim($("#registerAct").val());
                var registerPwd = $.trim($("#registerPwd").val());


                if(registerAct == "") {
                    alert("用户名不能为空");
                    return;
                }
                if(registerPwd == ""){
                    alert("密码不能为空");
                    return;
                }

                $("#msg").css({"color":"red"});
                //$("#msg").html("正在努力加载.....");

                //发出ajax请求进去判断
                $.ajax({
                    url:"settings/qx/user/register.do",
                    data:{
                        loginAct:registerAct,
                        loginPwd:registerPwd
                    },
                    type:"post",
                    dataType:"json",
                    success:function (data) {
                        if(data.code=="1"){
                            //注册成功
                            window.location.href="settings/qx/user/toLogin.do"
                        }else{
                            //注册失败，显示错误信息
                            $("#msg").html(data.message);
                        }
                    },
                    beforeSend:function () {
                        //在ajax请求发出之前；只有当返回值是true时才能发出，当返回值是false时是不能发送ajax请求的
                        $("#msg").html("正在注册中，请稍后.....");
                        return true;
                    }
                });

            });
        });
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>用户注册</h1>
        </div>
        <form class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" id="registerAct" placeholder="用户名" value="${cookie.registerAct.value}">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" id="registerPwd" placeholder="密码" value="${cookie.registerPwd.value}">
                </div>
                <div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
<%--                    <label>--%>
<%--                        <c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">--%>
<%--                            <input type="checkbox" id="isRemember" checked>--%>
<%--                        </c:if>--%>
<%--                        <c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">--%>
<%--                            <input type="checkbox" id="isRemember">--%>
<%--                        </c:if>--%>
<%--                        十天内免登录--%>
<%--                    </label>--%>
                    &nbsp;&nbsp;
                    <span id="msg"></span>
                </div>
                <button type="button" id="registerBtn" class="btn btn-primary btn-lg btn-block" style="width: 350px; position: relative;top: 45px;">注册</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>