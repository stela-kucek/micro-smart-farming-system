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
<title>MSFS Login</title>

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
				<h1>Login</h1>
			</div>

			<!-- Login -->

			<div>
				<form:form method="POST"
					action="${pageContext.request.contextPath}/authenticateTheUser">
					<div class="form-group">
						<div class="col-xs-15">
							<div>
								<!-- Check for login error -->
								<c:if test="${param.error != null}">
									<div class="alert alert-danger col-xs-offset-1 col-xs-10">
										Invalid username and password.
									</div>
								</c:if>

								<!-- Check for logout -->
								<c:if test="${param.logout != null}">
									<div class="alert alert-success col-xs-offset-1 col-xs-10">
										You have been logged out.
									</div>
								</c:if>
							</div>
						</div>

						<div class="row">
							<label for="inputUsername">Username</label> <input type="text"
								name="username" class="form-control" id="inputUsername"
								placeholder="Enter username">
						</div>
						<div class="row">
							<label for="inputPassword">Password</label> <input
								type="password" name="password" class="form-control"
								id="inputPassword" placeholder="Enter password">
						</div>
					</div>
					<button type="submit" class="btn btn-primary">Log in</button>
				</form:form>
				
				<form:form method="GET" action="${pageContext.request.contextPath}/register">
					<p><i> Not registered yet? </i></p>
					
					<button type="submit" class="btn btn-primary">Register</button>
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