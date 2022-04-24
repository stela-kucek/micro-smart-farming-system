<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html>
<!-- Head -->
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<!-- 
	Helpful documentation where you can lookup what you need for the page: 
	https://getbootstrap.com/docs/4.3/getting-started/introduction/ 
-->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

<!-- Page title -->
<title>MSFS Farmer Devices</title>
<style>
.failed {
	color: red;
}

body {
	height: auto;
	overflow: auto;
}

body {
	display: -ms-flexbox;
	display: -webkit-box;
	display: flex;
	-ms-flex-align: center;
	-ms-flex-pack: center;
	-webkit-box-align: center;
	align-items: center;
	-webkit-box-pack: center;
	justify-content: center;
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
}

.form-signin {
	width: 100%;
	max-width: 330px;
	padding: 15px;
	margin: 0 auto;
}

.form-signin .checkbox {
	font-weight: 400;
}

.form-signin .form-control {
	position: relative;
	box-sizing: border-box;
	height: auto;
	padding: 10px;
	font-size: 16px;
}

.form-signin .form-control:focus {
	z-index: 2;
}

.form-signin input[type="email"] {
	margin-bottom: -1px;
	border-bottom-right-radius: 0;
	border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
	margin-bottom: 10px;
	border-top-left-radius: 0;
	border-top-right-radius: 0;
}

#myProgress {
	width: 100%;
	background-color: grey;
}

#myBar {
	width: 1%;
	height: 30px;
	background-color: green;
}
</style>
</head>

<!-- Body -->
<body>

	<div class="text-center">

		<div class="container-fluid">
			<div class="title">
				<!-- TODO: replace title-->
				<sec:authorize access="isAuthenticated()">

					<div>
						<!-- Check for registration error -->
						<c:if test="${infoMessage != null && !infoMessage.isEmpty()}">
							<div class="alert alert-secondary col-xs-offset-1 col-xs-10">
								${infoMessage}</div>
						</c:if>

						<c:if test="${success != null && !success.isEmpty()}">
							<div class="alert alert-success col-xs-offset-1 col-xs-10">
								${success}</div>
						</c:if>
					</div>

					<c:if test="${entries.isEmpty()}">
						<h1>
							You have no registered devices at the moment,
							<sec:authentication property="name" />
							. <br>
						</h1>

					</c:if>
					<c:if test="${!entries.isEmpty()}">
						<h1>
							Here is a list of your devices,
							<sec:authentication property="name" />
							: <br>
						</h1>
						<form:form name="DeviceMgmt" method="POST"
							action="${pageContext.request.contextPath}/reassignDevice"
							modelAttribute="deviceForm">
							<table id="element" class="table">
								<thead>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Name</th>
										<th scope="col">Brand</th>
										<th scope="col">Assigned farming site</th>
										<th scope="col">Active</th>
										<th scope="col">Battery level</th>
										<th scope="col">Hours of operation</th>
										<th scope="col">Operational status</th>
										<th scope="col">News</th>
										<th scope="col">Select</th>
									</tr>
								</thead>
								<%
									int count = 0;
									
								%>
								<c:set var="idx" value="0"></c:set>
								<c:forEach items="${entries}" var="entry">
									<tbody id="rbutton">
										<tr>
											<th scope="row">
												<%
													out.println(++count);
												%>
											</th>
											<td>${entry.getName()}</td>
											<td>${entry.getSupplier()}</td>
											<td><c:if test="${entry.getAssignedSite() == null}"> none </c:if>
												<c:if test="${entry.getAssignedSite() != null}"> ${entry.getAssignedSite()}</c:if>
											</td>
											<td>${entry.isActive()}</td>
											<td>${entry.getMaintenanceData().getBatteryLevel()}</td>
											<td>${entry.getMaintenanceData().getOperationHours()}</td>
											<td>${entry.getMaintenanceData().getState()}</td>
											<td>${messages.get(idx)}&emsp;</td>
											<c:set var="idx" value="${idx+1}"></c:set>
											<td  style="text-align: center !important;"><form:radiobutton id="radiobutton"
													path="chosenDevice" class="form-check-input"
													value="${entry.getId()}"></form:radiobutton></td>
										</tr>
									</tbody>
								</c:forEach>
							</table>
							<br>
							<div class="row">
								<div class="col">
									<button type="submit" class="btn btn-info" formmethod="POST"
										formaction="${pageContext.request.contextPath}/toggleActive">Toggle
										active state</button>
								</div>
								<div class="col">
									<button type="submit" class="btn btn-info" formmethod="GET"
										formaction="${pageContext.request.contextPath}/showSensorData">Get
										sensor data</button>
								</div>
								<div class="col">
									<button type="submit" class="btn btn-info" formmethod="GET"
										formaction="${pageContext.request.contextPath}/setAction">Set
										action</button>
								</div>
								<div class="col">
									<button type="submit" class="btn btn-info" formmethod="GET"
										formaction="${pageContext.request.contextPath}/setMeasurement">Set
										measurement</button>
								</div>
								<div class="col">
									<button type="submit" class="btn btn-info"  formmethod="POST"
									formaction="${pageContext.request.contextPath}/chargeDevice" 
									data-toggle="modal"
										data-target="#exampleModal">Charge device</button>
								</div>
							</div>
							<br>
							<br>
							<div class="row">
								<div class="col">
									<button type="submit" class="btn btn-danger"
										formaction="/deleteDevice">Delete device</button>
								</div>

								<div class="col">
									<button type="submit" class="btn btn-warning" formmethod="GET">
										Reassign device</button>
								</div>
							</div>

						</form:form>
					</c:if>
					<br>

					<form:form method="GET"
						action="${pageContext.request.contextPath}/registerDevice">
						<button type="submit" class="btn btn-primary">Register
							new device</button>
					</form:form>
					<br>
					<br>
					<div class="row">
						<div class="col align-self-start">
							<form:form method="GET"
								action="${pageContext.request.contextPath}/">
								<button type="submit" class="btn btn-secondary">Home</button>
							</form:form>
						</div>
						<div class="col align-self-end">
							<form:form method="POST"
								action="${pageContext.request.contextPath}/logout">
								<button type="submit" class="btn btn-secondary">Log out</button>
							</form:form>
						</div>
					</div>
				</sec:authorize>
			</div>
		</div>
	</div>



	<!-- Modal -->
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true" onfocus="move()">
		<div class="modal-dialog" role="document" >
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Charging...</h5>

				</div>
				<div class="modal-body">
					<div id="myProgress">
						<div id="myBar"></div>
					</div>
					<div id="info-text">
					
					</div>
				</div>
	
			</div>
		</div>
	</div>


	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
	<script>
		var i = 0;
		
		function move() {
				if (i == 0) {
					i = 1;
					var elem = document.getElementById("myBar");
					var width = 1;
					var id = setInterval(frame, 10);
					function frame() {
						if (width >= 100) {
							clearInterval(id);
							i = 0;
						} else {
							width++;
							elem.style.width = width + "%";
						}
					}
				}

		}

	</script>
</body>
</html>