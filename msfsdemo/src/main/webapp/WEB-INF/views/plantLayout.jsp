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
<title>MSFS Plant Layout</title>
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
</style>
</head>

<!-- Body -->
<body>

	<div class="text-center">

		<div class="container-fluid">
			<div class="title">
				<!-- TODO: replace title-->
				<sec:authorize access="isAuthenticated()">
					<div class="title">
						<h1>Plant Layout of Farming Site</h1>
						<h1>${siteName}</h1>
					</div>
					<br>
					<br>
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

					<form:form name="Site" method="POST"
						action="${pageContext.request.contextPath}/changePlantLayout"
						modelAttribute="site">
						<table class="table table-bordered border-dark">
							<tbody>
								<form:hidden path="chosenSite" value="${chosenSite}" />

								<tr style="height: 50px">
									<c:forEach items="${site.plantLayout}" var="entry">
										<c:if test="${entry.getPosition() < 12}">
											<td><form:hidden
													path="plantLayout[${entry.getPosition()-1}].position"
													value="${entry.getPosition() }" /> <form:select
													path="plantLayout[${entry.getPosition()-1}].plant"
													class="form-control">
													<option value="${entry.getPlant()}">${entry.getPlant()}</option>
													<c:forEach items="${plants}" var="plant">
														<c:if test="${!plant.equals(entry.getPlant())}">
															<option value="${plant}">${plant}</option>
														</c:if>
													</c:forEach>
													<c:if test="${!entry.getPlant().equals('')}">
														<option></option>
													</c:if>
												</form:select></td>
										</c:if>
									</c:forEach>
								</tr>


								<tr style="height: 50px">
									<c:forEach items="${site.plantLayout}" var="entry">
										<c:if
											test="${entry.getPosition() >= 12 && entry.getPosition() < 23}">

											<td><form:hidden
													path="plantLayout[${entry.getPosition()-1}].position"
													value="${entry.getPosition() }" /> <form:select
													path="plantLayout[${entry.getPosition()-1}].plant"
													class="form-control">
													<option value="${entry.getPlant()}">${entry.getPlant()}</option>
													<c:forEach items="${plants}" var="plant">
														<c:if test="${!plant.equals(entry.getPlant())}">
															<option value="${plant}">${plant}</option>
														</c:if>
													</c:forEach>
													<c:if test="${!entry.getPlant().equals('')}">
														<option></option>
													</c:if>
												</form:select></td>
										</c:if>
									</c:forEach>
								</tr>

								<tr style="height: 50px">
									<c:forEach items="${site.plantLayout}" var="entry">
										<c:if
											test="${entry.getPosition() >= 23 && entry.getPosition() < 34}">
											<td><form:hidden
													path="plantLayout[${entry.getPosition()-1}].position"
													value="${entry.getPosition() }" /> <form:select
													path="plantLayout[${entry.getPosition()-1}].plant"
													class="form-control">
													<option value="${entry.getPlant()}">${entry.getPlant()}</option>
													<c:forEach items="${plants}" var="plant">
														<c:if test="${!plant.equals(entry.getPlant())}">
															<option value="${plant}">${plant}</option>
														</c:if>
													</c:forEach>
													<c:if test="${!entry.getPlant().equals('')}">
														<option></option>
													</c:if>
												</form:select></td>
										</c:if>
									</c:forEach>
								</tr>

								<tr style="height: 50px">
									<c:forEach items="${site.plantLayout}" var="entry">
										<c:if
											test="${entry.getPosition() >= 34 && entry.getPosition() < 45}">
											<td><form:hidden
													path="plantLayout[${entry.getPosition()-1}].position"
													value="${entry.getPosition() }" /> <form:select
													path="plantLayout[${entry.getPosition()-1}].plant"
													class="form-control">
													<option value="${entry.getPlant()}">${entry.getPlant()}</option>
													<c:forEach items="${plants}" var="plant">
														<c:if test="${!plant.equals(entry.getPlant())}">
															<option value="${plant}">${plant}</option>
														</c:if>
													</c:forEach>
													<c:if test="${!entry.getPlant().equals('')}">
														<option></option>
													</c:if>
												</form:select></td>
										</c:if>
									</c:forEach>
								</tr>

								<tr style="height: 50px">
									<c:forEach items="${site.plantLayout}" var="entry">
										<c:if
											test="${entry.getPosition() >= 45 && entry.getPosition() < 56}">
											<td><form:hidden
													path="plantLayout[${entry.getPosition()-1}].position"
													value="${entry.getPosition() }" /> <form:select
													path="plantLayout[${entry.getPosition()-1}].plant"
													class="form-control">
													<option value="${entry.getPlant()}">${entry.getPlant()}</option>
													<c:forEach items="${plants}" var="plant">
														<c:if test="${!plant.equals(entry.getPlant())}">
															<option value="${plant}">${plant}</option>
														</c:if>
													</c:forEach>
													<c:if test="${!entry.getPlant().equals('')}">
														<option></option>
													</c:if>													
												</form:select></td>
										</c:if>
									</c:forEach>
								</tr>

							</tbody>

						</table>
						<br>
						<div class="row">

							<div class="col">
								<button type="submit" class="btn btn-info">Save changes</button>
							</div>

						</div>
						<br>
						<br>
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