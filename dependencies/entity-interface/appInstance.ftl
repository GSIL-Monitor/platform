<html>
<head>
<title>首页</title>

</head>
<body>
	<div class="row">
		<div class="col-lg-12 col-md-12">
			<div class="card">
				<div class="card-header card-header-primary">
					<h4 class="card-title">
						<i class="ace-icon fa fa-star orange"></i> 应用实例列表
					</h4>
					<botton style="float:right" class="btn btn-danger"
						data-toggle="modal" data-target="#myModal">新增</botton>
				</div>

				<div class="dropdown">
					<button class="btn btn-default dropdown-toggle" type="button"
						id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="true">
						<span id="my-show"> 开发团队选择 </span> <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
						<#list groupList as group>
						<li><a id="groupName">${group.groupName!}</a></li>
						</#list>
					</ul>
				</div>

				<!-- Modal -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
								<h4 class="modal-title" id="myModalLabel">Modal title</h4>
							</div>
							<div class="modal-body">
								<form id="form-inline">
									<div class="dropdown">
										<button class="btn btn-default dropdown-toggle" type="button"
											id="dropdownMenu1" data-toggle="dropdown"
											aria-haspopup="true" aria-expanded="true">
											<span id="my-show1"> 开发团队选择 </span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
											<#list groupList as group>
											<li><a id="groupName1">${group.groupName!}</a></li>
											</#list>
										</ul>
									</div>
									<div class="form-group">
										<label for="loginname" class="control-label">实例名称</label> <input
											type="text" class="form-control" id="insName"
											name="loginname">
									</div>
									<div class="form-group">
										<label for="email" class="control-label">IP</label> <input
											type="text" class="form-control" id="insIp" name="email">
									</div>
									<div class="form-group">
										<label for="phone" class="control-label">端口</label> <input
											type="text" class="form-control" id="insPost" name="phone">
									</div>
									<div class="form-group">
										<label for="address" class="control-label">创建者</label>
										<textarea class="form-control" id="createUser" name="address"></textarea>
									</div>
									<div class="text-right">
										<span id="returnMessage" class="glyphicon"> </span>
										<button type="button" class="btn btn-default right"
											data-dismiss="modal">关闭</button>
										<button id="insert111" type="button" class="btn btn-info">提交</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>





				<div class="modal fade" id="updateInsDialog" tabindex="-1"
					role="dialog" aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">×</span>
								</button>
								<h4 class="modal-title">修改实例</h4>
							</div>
							<div class="modal-body">
								<form id="form-inline">
									<div class="dropdown">
										<button class="btn btn-default dropdown-toggle" type="button"
											id="dropdownMenu1" data-toggle="dropdown"
											aria-haspopup="true" aria-expanded="true">
											<span id="my-show2"> 开发团队选择 </span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
											<#list groupList as group>
											<li><a id="groupName">${group.groupName!}</a></li>
											</#list>
										</ul>
									</div>
									<div class="form-group">
										<input type="hidden" id="insId1"> <label
											for="loginname" class="control-label">实例名称</label> <input
											type="text" class="form-control" id="insName1"
											name="loginname">
									</div>
									<div class="form-group">
										<label for="email" class="control-label">IP</label> <input
											type="text" class="form-control" id="insIp1">
									</div>
									<div class="form-group">
										<label for="phone" class="control-label">端口</label> <input
											type="text" class="form-control" id="insPort1">
									</div>
									<div class="form-group">
										<label for="address" class="control-label">创建者</label>
										<textarea class="form-control" id="createUser1"></textarea>
									</div>
									<div class="text-right">
										<span id="returnMessage" class="glyphicon"> </span>
										<button type="button" class="btn btn-default right"
											data-dismiss="modal">关闭</button>
										<button id="insert112" type="button" class="btn btn-info">提交</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>






				<div class="card-body table-responsive">
					<table class="table table-bordered table-striped table-hover">
						<thead class="text-warning">
							<tr>
								<th>应用</th>
								<th>实例名称</th>
								<th class="hidden-480">IP：端口</th>
								<th>创建者</th>
								<th>开发团队</th>
								<th>操作</th>
							</tr>
						</thead>

						<tbody id="instanceShowAll">
							<#list appInsList as ins>
							<tr>
								<input id="insIp" type="hidden" value="${ins.insIp!}"></input>
								<input id="insPort" type="hidden" value="${ins.insPort!}"></input>
								<input id="groupId" type="hidden" value="${ins.groupId!}"></input>
								<td id="insName">${ins.insName!}</td>
								<td id="insId" class="real"><small>${ins.insId!}</small></td>
								<td id="idAndPort"><b class="green"><a
										href="${contextPath}/log?appInsId=${ins.insId!}">${ins.insIp!}:${ins.insPort!?c}</a></b></td>
								<td id="createUser">${ins.createUser!}</td>
								<td id="groupName">${ins.groupName!}</td>
								<td><button class="btn btn-primary">删除</button>
									<button class="btn btn-danger" data-toggle="modal"
										data-target="#updateInsDialog" data-insName=${ins.insName!}
										data-insId=${ins.insId!} data-insIp=${ins.insIp!}
										data-insPort=${ins.insPort!}
										data-createUser=${ins.createUser!}
										data-groupName=${ins.groupName!}>修改</button></td>
							</tr>
							</#list>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>
	<script type="text/javascript">
		/*将值ins信息放入修改模态框*/
		$('#updateInsDialog').on('show.bs.modal', function(event) {
			var target = $(event.relatedTarget)[0]; // relatedTarget 事件属性返回与事件的目标节点相关的节点。
			var modal = $(this);
			modal.find('#insName1').val(target.dataset['insname']);
			modal.find('#insId1').val(target.dataset['insid']);
			console.dir("1112325")
			modal.find('#insIp1').val(target.dataset['insip']);
			modal.find('#insPort1').val(target.dataset['insport']);
			modal.find('#createUser1').val(target.dataset['createuser']);
			modal.find('#my-show2').val(target.dataset['groupname']);
		});

		/*将选中的分组显示在顶部*/
		$(function() {
			$(".dropdown .dropdown-menu li").click(function() {
				var $this = $(this);
				$("#my-show").html($this.find("a").html());
				$("#my-show1").html($this.find("a").html());
			});
		});

		/*新增实例*/

		$("#insert111").click(
				function() {

					var appGroup = $("#my-show1").text();
					var insName = $("#insName").val();
					var insIp = $("#insIp").val();
					var insPort = $("#insPost").val();
					var createUser = $("#createUser").val();
					if ((appGroup.trim() == "开发团队选择") || (insName.trim() == "")
							|| (insIp.trim() == "") || (insPort.trim() == "")
							|| (createUser.trim() == "")) {
						alert("任意一项不能为空！！！")
						return 0;
					}

					$.ajax({
						type : "post",
						url : "insertInstanceByAjax",
						datatype : "text",
						data : {
							appGroup : appGroup,
							insName : insName,
							insIp : insIp,
							insPort : insPort,
							createUser : createUser
						},
						success : function(data) {

							console.dir(data)
							if (data.result) {
								showAllInstance();
							}
						},
						errrror : function() {
							alert("新增失败")
						}
					});
				});

		$(AA()) //将删除按钮加上点击事件

		function AA() {

			/*删除实例*/
			var k2 = document.getElementsByClassName('btn btn-primary');
			$(k2).click(function() {

				console.log(1111111)
				var mythis = $(this)
				var mytr = mythis.closest("tr");
				var insId = mytr.find("#insId").text();

				if (!confirm("是否真的要删除？")) {
					return 0;
				}

				$.ajax({
					type : 'post',
					url : 'deleteInstanceByAjax',
					datatype : 'Json',
					data : {
						insId : insId
					},
					success : function(res) {
						console.dir(res)
						if (res.result) {
							showAllInstance();

						}
					}
				})
			})

		}
		/* 修改事件 */
		$("#insert112").click(function() {

			var groupName1 = $("#my-show2").text();
			var insId1 = $("#insId1").val();
			var insName1 = $("#insName1").val();
			var insIp1 = $("#insIp1").val();
			var insPort1 = $("#insPort").val();
			var createUser1 = $("#createUser1").val();

			insPort2 = insPort1;
			insPort1 = insPort1.replace(",", "");

			$.ajax({
				url : "updataInsByAjax",
				type : "post",
				dataType : "Json",
				data : {
					groupName1 : groupName1,
					insId1 : insId1,
					insName1 : insName1,
					insIp1 : insIp1,
					insPort1 : insPort1,
					createUser1 : createUser1
				},
				success : function(res) {
					if (res.result) {
						showAllInstance();

					} else {
						alert(res.error);
					}
				},
				error : function() {
					alert("修改失败！！！")
				}
			})
		})

		/*通过Ajax显示*/
		function showAllInstance() {

			$
					.ajax({
						url : "showAllInstanceByAjax",
						type : "get",
						dataType : "Json",
						data : {},
						success : function(data) {
							console.dir(data)
							$('#instanceShowAll').html("");
							var html = "";
							for (i = 0; i < data.length; i++) {
								html += "<tr><input id='insIp' type='hidden' value="+data[i].insIp+"></input><input id='insPort' type='hidden' value="+data[i].insPort+"></input><td id='insName'>"
										+ data[i].insName
										+ "</td><td id='insId' class='real'><small>"
										+ data[i].insId
										+ "</small></td><td id='idAndPort'><b class='green'><a>"
										+ data[i].insIp
										+ ":"
										+ data[i].insPort
										+ "</a></b></td><td id='createUser'>"
										+ data[i].createUser
										+ "</td><td id='groupName'>"
										+ data[i].groupName
										+ "</td><td><button class='btn btn-primary'>删除</button><button class='btn btn-danger' data-toggle='modal' data-target='#updateInsDialog' data-insName="+data[i].insName+" data-insId="+data[i].insId+" data-insIp="+data[i].insIp+" data-insPort="+data[i].insPort+" data-createUser="+data[i].createUser+" data-groupName="+data[i].groupName+">修改</button></td></tr>"

							}
							$('#instanceShowAll').html(html)
							AA();
						}
					})
		}
	</script>
</body>

</html>