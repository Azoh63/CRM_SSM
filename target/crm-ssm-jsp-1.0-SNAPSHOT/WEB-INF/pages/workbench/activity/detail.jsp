<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});

        $("#remarkDivList").on("mouseover",".remarkDiv",function () {
            $(this).children("div").children("div").show();
        });

        $("#remarkDivList").on("mouseout",".remarkDiv",function () {
            $(this).children("div").children("div").hide();
        });

        $("#remarkDivList").on("mouseover",".myHref",function () {
            $(this).children("span").css("color","red");
        });

        $("#remarkDivList").on("mouseout",".myHref",function () {
            $(this).children("span").css("color","#E6E6E6");
        });

        //填完了数据之后还要标签具有行为
       /* $(".remarkDiv").mouseover(function(){
            $(this).children("div").children("div").show();
        });*/

        /*$(".remarkDiv").mouseout(function(){
            $(this).children("div").children("div").hide();
        });*/

        /*$(".myHref").mouseover(function(){
            $(this).children("span").css("color","red");
        });*/

        /*$(".myHref").mouseout(function(){
            $(this).children("span").css("color","#E6E6E6");
        });*/
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});


		//新增一条备注的请求
		$("#saveRemarkBtn").click(function () {
			//具体的备注内容
			var noteContent = $("#remark").val();
			var activityId = "${activity.id}";

			//表单验证
			if(noteContent == ""){
				alert("备注信息不能为空");
				return;
			}

			$.ajax({
				url:"workbench/activity/saveActivityRemark.do",
				data:{
					noteContent:noteContent,
					activityId:activityId
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					var html = "";
					//添加了一条数据后只需要动态添加一条数据即可
                    if(data.code == 1){
						html += "<div id=\"div_"+data.retData.id+"\" class=\"remarkDiv\" style=\"height: 60px;\">";
						html += "<img title=\"zhangsan\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						html += "<div style=\"position: relative; top: -40px; left: 40px;\" >";
						html += "<h5>"+data.retData.noteContent+"</h5>";
						html += "<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font> <b>${activity.name}</b> <small style=\"color: gray;\"> "+data.retData.createTime+"由${sessionScope.sessionUser.name}创建</small>";
						html += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none\">";
						html += "<a class=\"myHref\" href=\"javascript:void(0);\"  name=\"editRemarkBtn\" remarkId=\""+data.retData.id+"\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						html += "&nbsp;&nbsp;&nbsp;&nbsp;";
						html += "<a class=\"myHref\" href=\"javascript:void(0);\" name=\"deleteRemarkBtn\" remarkId=\""+data.retData.id+"\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						html += "</div>";
						html += "</div>";
						html += "</div>";

						//并且还需要将文本框中的数据清空
						$("#remark").val("");
						//在这个div之前添加备注信息
						$("#remarkDiv").before(html);
                    }else{
                        alert(data.message);
                    }
				}
			});	//ajax
		});	//保存的单击事件

        //给备注的修改按钮绑定事件，之所以要这么写是因为这个按钮是动态生成的
        $("#remarkDivList").on("click","a[name=editRemarkBtn]",function () {
        	//拿到备注的内容
			var id = $(this).attr("remarkId");
			var noteContent = $("#div_"+id+" h5").text();
			$("#noteContent").val(noteContent);

			//把id写在模态窗口的模态窗口
			$("#remarkId").val(id);

            //打开修改备注的模态窗口
            $("#editRemarkModal").modal("show");
        });

        //修改的更新按钮
		$("#updateRemarkBtn").click(function () {
			//表单验证
			var noteContent = $("#noteContent").val();
			var id = $("#remarkId").val();

			if(noteContent == ""){
				alert("内容不能为空");
				return;
			}
			$.ajax({
				url:"workbench/activity/editActivityRemark.do",
				data:{
					noteContent:noteContent,
					id:id
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code == 1){
						//重新更新这一行备注
						$("#div_"+id+" h5").text(data.retData.noteContent);
						$("#div_"+id+" small").text(data.retData.editTime+"由${sessionScope.sessionUser.name}修改");

						//关闭模态窗口
						$("#editRemarkModal").modal("hide");
					}else{
						alert(data.message);
						//不关闭模态窗口
						$("#editRemarkModal").modal("show");
					}
				}
			})
		});


        //删除备注的按钮
        $("#remarkDivList").on("click","a[name=deleteRemarkBtn]",function () {
        	//获取备注的id，存放在两个图标的remarkId自定义属性当中
			var id = $(this).attr("remarkId");

            //直接删除，发出异步请求
			$.ajax({
				url:"workbench/activity/deleteActivityRemarkById",
				data:{
					id:id
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code == "1"){
						//删除相应的备注所在的标签
						$("#div_"+id).remove();
					}else{
						alert(data.message);
					}
				}
			})
        });
	});
	
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
	</div>
	
	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;" >所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;" ><b>${activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;" >名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;" ><b>${activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					<%--市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等--%>
					${activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div style="position: relative; top: 30px; left: 40px;" id="remarkDivList">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<%--动态备注--%>
		<c:forEach items="${remarkList}" var="remark">
		<div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>${remark.noteContent}</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> ${remark.editFlag == "0"?remark.createTime:remark.editTime}由${remark.editFlag == "0"?remark.createBy:remark.editBy}${remark.editFlag == "0"?"创建":"修改"}</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);" name="editRemarkBtn" remarkId="${remark.id}"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);" name="deleteRemarkBtn" remarkId="${remark.id}"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		</c:forEach>
		<!-- 备注1 -->
		<%--<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
		
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
                <input type="hidden" id="remarkId">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>