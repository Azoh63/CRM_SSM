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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		$("#createActivityBtn").click(function () {
			//打开之前需要将里面的数据清空
			$("#create-activityForm")[0].reset();

			//打开创建市场活动的模态窗口
			$("#createActivityModal").modal("show");
		});
		$("#saveActivityBtn").click(function () {
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endTime").val();
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());

			//表单验证
			//所有者和名称不能为空
			if(owner == ""){
				alert("所有者不能为空");
				return;
			}
			if(name == ""){
				alert("市场活动名称不能为空");
				return;
			}

			//如果开始日期和结束日期都不为空,则结束日期不能比开始日期小
			if(startDate!=""&&endDate!=""){
				if(startDate > endDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}else{
				alert("日期不能为空");
				return;
			}

			//成本只能为非负整数
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			}

			//前端提交数据的按钮，发出ajax请求
			$.ajax({
				url:"workbench/activity/create.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code == 1){
						$("#createActivityModal").modal("hide");

						//还需要刷新市场活动列表，保留在这里，需要调用函数
						getActivity(1,$("#paginationBox").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//失败就不要关闭模态窗口
						alert(data.message);
					}
				}

			});
		});

		$(".myDate").datetimepicker({
			language:"zh-CN",   //日历语言是中文
			format:"yyyy-mm-dd",    //日历输入的格式
			autoclose:true, //默认是false，这里需要开启
			minView:"month",    //最小选择视图
			todayBtn:true,  //'今日按钮'，需要开启
			initialDate:new Date(),  //打开的时候是系统当前时间
			clearBtn:true,   //readonly的同时还能删除
			pickerPosition: "bottom-right"
		});

		//给查询按钮绑定事件
		$("#queryActivityBtn").click(function () {
			getActivity(1,$("#paginationBox").bs_pagination('getOption', 'rowsPerPage'));
		});

		//页面加载完毕就应该去请求市场活动列表信息
		getActivity(1,10);


		//给市场活动的CheckBox绑定全选和取消全选的事件
		$("#checkAll").click(function () {
			//this对象：是调用该函数的对象，这里指的就是checkAll标签对象
			//this.checked;

			//对表单中的复选框设置属性
			$("#activityTbody input[type=checkbox]").prop("checked",this.checked);
		});

		//单个复选框
		/*$("#activityTbody input[type=checkbox]").click(function () {
			//判断是否全选
			alert("test");
		})*/
		//使用父选择器（必须是固有元素）on("事件名",子选择器（也可以是动态的）,fun())
		$("#activityTbody").on("click",$("input[type=checkbox]"),function () {
			//获取表格中的复选框的总数
			if($("#activityTbody input[type=checkbox]").size() == $("#activityTbody input[type=checkbox]:checked").size()){
				$("#checkAll").prop("checked",true);
			}else{
				$("#checkAll").prop("checked",false);
			}
		});

		//删除按钮
		$("#deleteActivityBtn").click(function () {
			//每次必须删除一条市场活动，否则不发送请求
			if($("#activityTbody input[type=checkbox]:checked").size() == 0){
				alert("至少选择一条市场活动");
				return;
			}

			if(window.confirm("确认删除数据吗")){
				//准备传送到后台的数据
				var ids = "";
				var checkedInput = $("#activityTbody input[type=checkbox]:checked");
				$.each(checkedInput,function () {
					ids += "id=" + this.value+ "&";
				});
				//把字符串最后一个&去掉
				ids = ids.substring(0,ids.length - 1);

				//发出异步请求来完成请求
				$.ajax({
					url:"workbench/activity/deleteActivityByIds.do",
					type:"post",
					data:ids,
					dataType:"json",
					success:function (data) {
						if (data.code == 1){
							//删除成功，显示第一页的数据，保持显示页数不变
							getActivity(1,$("#paginationBox").bs_pagination('getOption', 'rowsPerPage'));
						} else{
							//删除失败，显示提示信息，并且不刷新列表
							alert(data.message);
						}
					}
				});	//ajax
			}	//confim
		});		//删除按钮绑定单击事件

		//修改市场活动按钮
		$("#editActivityBtn").click(function () {
			//$("#editActivityModal").modal("show");
			//没有选择市场活动不发请求
			if($("#activityTbody input[type=checkbox]:checked").size() == 0){
				alert("需要选择一个市场活动");
				return;
			}
			if($("#activityTbody input[type=checkbox]:checked").size() > 1){
				alert("最多选择一个市场活动");
				return;
			}

			//准备id
			var id = $("#activityTbody input[type=checkbox]:checked").val();

			//发送请求，获取到市场活动后并将数据写入后再打开模态窗口
			$.ajax({
				url:"workbench/activity/queryActivityById.do",
				data:{
					//去获取市场活动的id
					id:id
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					//alert(data.name + data.startDate);
					//首先应该保存id到影藏域
					$("#editActivityHiddenId").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description);

					//成功后打开模态窗口
					$("#editActivityModal").modal("show");
				}

			});
		});

		//修改市场活动模态窗口中的更新按钮
		$("#updateActivityBtn").click(function () {
			//首先要做的就是获取所有的数据
			var id = $("#editActivityHiddenId").val();
			var owner = $("#edit-marketActivityOwner").val();
			var name = $.trim($("#edit-marketActivityName").val());
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var cost = $.trim($("#edit-cost").val());
			var description = $.trim($("#edit-describe").val());

			//表单验证
			if (owner == ""){
				alert("请选择所有者");
				return;
			}
			if(name == ""){
				alert("请填写市场活动的名字");
				return;
			}
			if (startDate != "" && endDate != ""){
				if(startDate > endDate){
					alert("结束日期不能小于开始日期");
					return;
				}
			} else{
				alert("日期不能为空");
				return;
			}

			//成本只能为非负整数
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数");
				return;
			}

			$.ajax({
				url:"workbench/activity/editActivity.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if(data.code == 1){
						//关闭模态窗口
						$("#editActivityModal").modal("hide");
						//并且重新获取市场活动列表
						getActivity($("#paginationBox").bs_pagination('getOption', 'currentPage'),$("#paginationBox").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//不关闭模态窗口，并提示信息
						$("#editActivityModal").modal("show");
						alert(data.message);
					}
				}

			});
		});

		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/activityFileDownloadForAll.do";
		});


		$("#importActivityBtn").click(function () {

			//文件对象
			var formData = new FormData();

			//文件表单
			var file = $("#activityFile")[0].files[0];
			formData.append("excelFile",file);

			//用户点击了导入按钮，向后台发出请求
			$.ajax({
				url:"workbench/activity/activityListFileUpload.do",
				data:formData,
				type:"post",
				dataType:"json",
				processData:false,
				contentType:false,

				success:function (data) {
					if(data.code == 0){
						//失败了就不关闭，并提示信息
						$("importActivityModal").modal("show");
						alert(data.message);
					}else if(data.code == 1){
						//关闭模态窗口，刷新市场活动列表
						$("#importActivityModal").modal("hide");
						getActivity(1,$("#paginationBox").bs_pagination('getOption', 'rowsPerPage'));
						alert("总共导入了" + data.retData + "条数据");
					}
				}

			});
		});

		$("#exportActivityXzBtn").click(function () {
			if($("#activityTbody input[type=checkbox]:checked").size() == 0){
				alert("至少选择一条市场活动");
				return;
			}
			if(window.confirm("确认要选择这" + $("#activityTbody input[type=checkbox]:checked").size() + "条数据吗")){
				var ids = "";
				var inputChecked = $("#activityTbody input[type=checkbox]:checked");
				//判断是否选择了

				$.each(inputChecked,function (index, obj) {
					ids += "id=" + obj.value + "&";
				});
				if(ids.endsWith("&")){
					ids = ids.substring(0,ids.length-1);
				}
				window.location.href="workbench/activity/activityFileDownloadForChosen.do?" + ids;
			}
		})
	});		//页面加载完成





	//获取市场活动列表函数
	function getActivity(pageNo,pageSize) {
		//这里需获取前端的参数
		var name = $("#activityName").val();
		var owner = $("#activityOwner").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		//var pageNo = 1;		//默认是1
		//var pageSize = 10;	//默认是10

		$.ajax({
			url:"workbench/activity/queryActivityByConditionForPage.do",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:"post",
			dateType:"json",
			success:function (data) {
				//获取总页数
				var totalPages = parseInt(data.totalRows/pageSize);
				if(data.totalRows%pageSize > 0){
					//说明还需要加一
					totalPages += 1;
				}

				//拿到数据之后先分页再写
				//给分页插件绑定事件
				$("#paginationBox").bs_pagination({
					currentPage:pageNo,
					rowsPerPage:pageSize, //每一页显示的数据条数
					totalPages:totalPages, //需要计算，pagination内部没有提供计算,js是弱类型语言，除法得到的是小数，需要判断
					visiblePageLinks:5, //能够显示的按钮个数
					totalRows:data.totalRows,	//总记录数

					//显示跳转的按钮
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,

					onChangePage: function(event,pageObj) { // returns page_num and rows_per_page after a link has clicked
						getActivity(pageObj.currentPage,pageObj.rowsPerPage);
					}
				});

				//传回来两个数据，一个是市场活动列表，一个是总记录条数
				//把活动列表写入table的tbody中
				var htmlStr = "";

				//再重新写表格之前应该先把总复选框置为不选中
				$("#checkAll").prop("checked",false);

				//用拼串的方式来实现重新刷新表格
				//应该是使用循环的方式来拼串
				$.each(data.activityList,function (index,a) {
					htmlStr += "<tr class=\"active\">";
					htmlStr += "<td><input type=\"checkbox\" value=\""+a.id+"\"/></td>";
					htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/getActivityDetail.do?id="+a.id+"';\">"+a.name+"</a></td>";
					htmlStr += "<td>"+a.owner+"</td>";
					htmlStr += "<td>"+a.startDate+"</td>";
					htmlStr += "<td>"+a.endDate+"</td>";
					htmlStr += "</tr>";
				});

				//将所有的数据都写完了在展示html
				//用append方式需要手动清空html元素，而html方法就不需要手动清除，再每一次写入之前都会自动清除后再写入
				$("#activityTbody").html(htmlStr);

				//还要将总条数写进去
				//$("#totalRowsB").text(data.totalRows);
			}
		});
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="create-activityForm">
						<input type="hidden" id="editActivityHiddenId">
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
									<c:forEach items="${userList}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								  <option></option>
								  <c:forEach items="${userList}" var="u">
									  <option value="${u.id}">${u.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-startTime" value="2020-10-10" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-endTime" value="2020-10-20" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="activityName">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="activityOwner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control myDate" type="text" id="startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control myDate" type="text" id="endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityTbody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div id="paginationBox"></div>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>