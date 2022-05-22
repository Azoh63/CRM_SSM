<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>测试文件下载</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#fileDown").click(function () {
                //发送下载文件的请求
                window.location.href = "workbench/activity/fileDownload.do";
            })
        })
    </script>
</head>
<body>
    <button type="button" id="fileDown">获取文件</button>
</body>
</html>
