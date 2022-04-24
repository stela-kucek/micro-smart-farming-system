<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>MSFS Device Registration</title>

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
		<div class="container">
			<div class="title">
				<h1>Change Registration Settings for Device</h1>
				<h1>${deviceName}</h1>
			</div>

			<!-- Device Registration -->

			<div>
				<h2>Currently assigned to: 
				<c:if test="${currentSite != null}">
				${currentSite}</c:if>
				<c:if test="${currentSite == null}">
				none</c:if>
				</h2>
				<form:form method="POST"
					action="${pageContext.request.contextPath}/reassignDevice"
					modelAttribute="newAssignment">
					<form:input path="name" type="hidden" value="${deviceName}" />
					<form:input path="deviceId" type="hidden" value="${deviceId}" />
					<div class="form-group">
						<div class="row justify-content-md-center">
							<b>Farming Site Assignment</b>
						</div>
						
							<c:if test="${!entries.isEmpty()}">
							<div class="row ">
								<form:label path="site">Assign your device to a site:</form:label>
								</div>
								<div class="row ">
								<form:select path="site" class="form-control">
									<c:forEach items="${entries}" var="entry">
										<option value="${entry.getId()}">${entry.getName()}</option>
									</c:forEach>
								</form:select>
								</div>
								<br>
								<div class="row justify-content-md-center">
									<button type="submit" class="btn btn-primary">Apply
										changes</button>
								</div>
								<div class="row justify-content-md-center">
									<br>or<br>
								</div>
							</c:if>
							<c:if test="${entries.isEmpty()}">
							It seems all of your farming sites have a farmbot assigned to them.<br>
							Remove assignment from one of your farming sites to assign this device.
							</c:if>
						
					</div>
					<c:if test="${currentSite != null}">				
						<button type="submit" class="btn btn-warning"
						formaction="/removeAssignment">Remove assignment</button>
					</c:if>
				</form:form>
				<br>
				<form:form method="GET"
					action="${pageContext.request.contextPath}/devices">
					<button type="submit" class="btn btn-secondary">Back to
						device list</button>
				</form:form>

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