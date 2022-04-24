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
<title>MSFS Sensor Data</title>
<style>
.failed {
	color: red;
}

body {
	height: auto;
	overflow: scroll;
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
		<div class="container-fluid " style="overflow-y: auto;">
			<!-- TODO: replace title-->
			<sec:authorize access="isAuthenticated()">
				<c:if test="${entries.isEmpty()}">
					<div class="title position-sticky">
						<h1>
							The device has no logged sensor data yet,
							<sec:authentication property="name" />
							. <br>
						</h1>
						<h3>
							<i>Tip: This means that the device has not yet been active.</i>
						</h3>
					</div>
				</c:if>

				<c:if test="${!entries.isEmpty()}">
					<div class="title position-sticky">
						<h1>
							Here is a list of sensor logs,
							<sec:authentication property="name" />
							: <br>
						</h1>
					</div>
					<div class="container" style="height: 500px; overflow-y: scroll;">
						<table class="table">
							<thead>
								<tr>
									<th scope="col">#</th>
									<th scope="col">Time stamp</th>
									<th scope="col">Position</th>
									<th scope="col">Measured aspect</th>
									<th scope="col">Value</th>
								</tr>
							</thead>
							<%
								int count = 0;
							%>
							<c:forEach items="${entries}" var="entry">

								<tbody>
									<tr>
										<th scope="row">
											<%
												out.println(++count);
											%>
										</th>
										<td>${entry.getTimestamp()}</td>
										<td>${entry.getPosition().getNumber()}</td>
										<td>${entry.getAspect()}</td>

										<td><c:if test="${entry.getAspect().equals(\"Plant Growth\")}">${entry.getPlantGrowth().getStage()}</c:if>
											<c:if test="${!entry.getAspect().equals(\"Plant Growth\")}">
										${entry.getLoggedAspect().getSpec().getValue()}
											${entry.getLoggedAspect().getSpec().getUnit()}
										</c:if></td>

									</tr>
								</tbody>

							</c:forEach>
						</table>
					</div>
					<br>

				</c:if>

				<br>
				<form:form method="GET"
					action="${pageContext.request.contextPath}/devices">
					<button type="submit" class="btn btn-secondary">Back to
						device list</button>
				</form:form>
			</sec:authorize>

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
		
	</script>
</body>
</html>