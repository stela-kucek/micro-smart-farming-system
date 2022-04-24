<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
<title>MSFS Home</title>
<style>
.failed {
	color: red;
}

body {
	height: 100%;
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
						<c:if test="${infoMessage != null}">
							<div class="alert alert-secondary col-xs-offset-1 col-xs-10">
								${infoMessage}</div>
						</c:if>
						
						<c:if test="${success != null}">
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
							Here is a list of devices,
							<sec:authentication property="name" />
							: <br>
						</h1>
						
						<form:form name="DeviceMaint" method="POST"
							action="${pageContext.request.contextPath}/inspectDevice"
							modelAttribute="deviceForm">
							<table class="table">
								<thead>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Device Name</th>
										<th scope="col">Owner Name</th>
										<th scope="col">Battery level</th>
										<th scope="col">Hours of operation</th>
										<th scope="col">Firmware version</th>
										<th scope="col">Status</th>
										<th scope="col">Message</th>
										<th scope="col">Select</th>
									</tr>
								</thead>
								<% int count = 0; %>
								<c:set var="idx" value="0"></c:set>
								<c:forEach items="${entries}" var="entry">

									<tbody>
										<tr>
											<th scope="row"><%out.println(++count); %></th>
											<td>${entry.getName()}</td>
					      					<td>${entry.getOwner()}</td>
											<td>${entry.getMaintenanceData().getBatteryLevel()}</td>
											<td>${entry.getMaintenanceData().getOperationHours()}</td>
											<td>${entry.getMaintenanceData().getFirmwareVersion()}</td>
											<td>${entry.getMaintenanceData().getState()}</td>
											<td>${states.get(idx)}&emsp;</td>
											<c:set var="idx" value="${idx+1}"></c:set>
											<td style="text-align: center !important;"><form:radiobutton path="chosenDevice"
													class="form-check-input" value="${entry.getId()}"></form:radiobutton>
											</td>
										</tr>
									</tbody>
									
									
									
								</c:forEach>
							</table>
							<button type="submit" class="btn btn-warning"
									formmethod="GET">
										Inspect the Device</button>
						</form:form>
					</c:if>
						
						<form:form method="GET" action="${pageContext.request.contextPath}/">
							<button type="submit" class="btn btn-primary">Initiate replacement shipping</button>
						</form:form>
						<br>
					<form:form method="POST" action="${pageContext.request.contextPath}/logout">
						<button type="submit" class="btn btn-secondary">Log out</button>
					</form:form>
				</sec:authorize>
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
		// Add javascript funtions here if needed
	</script>
</body>
</html>