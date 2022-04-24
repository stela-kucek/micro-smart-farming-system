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
	
<!--  added by azar -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<!-- FontAwesome -->
<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">

<!-- Page title -->
<title>MSFS My Plants</title>
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
<!-- -------------------------------------------------------------------------------------- -->
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

					<c:if test="${plants.isEmpty()}">
						<h1>
							You don't have any plants at the moment,
							<sec:authentication property="name" />
							 <br>
						</h1>
					</c:if>
					
						<h1>
							Your Plants
						</h1>
						<form:form name="PlantMgmt" method="GET"
							action="${pageContext.request.contextPath}/editPlant"
							modelAttribute="plantForm">
							<table id="element" class="table">
								<thead>
									<tr>
										<th scope="col">Plants</th>
									</tr>
									<tr>
										<th scope="col">#</th>
										<th scope="col">Name</th>
										<th scope="col">Scientific Name</th>
										<th scope="col">Stage</th>
										<th scope="col">Price</th>
										<th scope="col">Available Quantity</th>
										<th scope="col">For Sale</th>
									</tr>
								</thead>
								<% int count = 0; %>
								<c:forEach items="${plants}" var="plant">

									<tbody>
										<tr>
											<th scope="row"><% out.println(++count); %></th>
											<td>${plant.getSpecies().getName()}</td>
											<td>${plant.getSpecies().getScientificName()}</td>
											<td>${plant.getCurrentStage()}</td>
											<td>${plant.getPrice()}</td>
											
											<td>${plant.getQuantity()}</td>
											<td>${plant.isForSale()}</td>
											<td><form:radiobutton path="chosenItem"
												 	class="form-check-input" value="${plant.getId()}"></form:radiobutton>
											</td>
											
										</tr>
									</tbody>

								</c:forEach>
							</table>
							<br>

							<div class="row">
							

								<div class="col">
									<button type="submit" class="btn btn-warning">
										Edit plant</button>
								</div>
							</div>
						</form:form>					
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
<!-- -------------------------------------------------------------------------------------- -->
	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>