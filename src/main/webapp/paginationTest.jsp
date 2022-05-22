<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <title>测试前端分页插件</title>
    <base href="<%=basePath%>">
    <!--  JQUERY -->
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

    <!--  BOOTSTRAP -->
    <link rel="stylesheet" type="text/css" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <!--  PAGINATION plugin -->
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">
        $(function() {

            $("#demo_pag1").bs_pagination({
                currentPage:1,
                rowsPerPage:10, //每一页显示的数据条数
                totalPages:100, //需要计算，pagination内部没有提供计算
                visiblePageLinks:5, //能够显示的按钮个数

                //显示跳转的按钮
                showGoToPage: true,
                showRowsPerPage: true,
                showRowsInfo: true,

                onChangePage: function() { // returns page_num and rows_per_page after a link has clicked
                    alert("testPagination")
                }
            });

        });
    </script>
</head>
<body>
    <div id="demo_pag1"></div>
</body>
</html>
