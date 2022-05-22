<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <title>测试日历控件</title>
    <base href="<%=basePath%>">
    <!--引入datetimepicker相关文件-->
    <!--都依赖于jQuery-->
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <!--datetimepicker又依赖于bootstrap-->
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css"/>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <!--这才是开始导入datetimepicker控件-->
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css"/>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript">
        $(function () {
            //页面加载完毕后
            //给容器添加日历控件
            $("#dateInput").datetimepicker({
                language:"zh-CN",   //日历语言是中文
                format:"yyyy-mm-dd",    //日历输入的格式
                autoclose:true, //默认是false，这里需要开启
                minView:"month",    //最小选择视图
                todayBtn:true,  //'今日按钮'，需要开启
                initialDate:new Date(),  //打开的时候是系统当前时间
                clearBtn:true   //readonly的同时还能删除
            });
        });
    </script>
</head>
<body>
    <input type="text" id="dateInput" readonly>
</body>
</html>
