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
<title>MSFS Registration</title>

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
				<h1>Register</h1>
			</div>

			<!-- Registration -->

			<div>
				<form:form method="POST"
					action="${pageContext.request.contextPath}/register"
					modelAttribute="user">
					<div class="form-group">
						<div class="col-xs-15">
							<div>
								<!-- Check for registration error -->
								<c:if test="${registrationError != null}">
									<div class="alert alert-danger col-xs-offset-1 col-xs-10">
										${registrationError}</div>
								</c:if>
							</div>
						</div>

						<!-- Input fields -->
						<div class="row">
							<form:label path="firstname">First name</form:label>
							<form:input path="firstname" type="text"
								placeholder="Enter first name" class="form-control"></form:input>
						</div>
						
						<div class="row">
							<form:label path="lastname">Last name</form:label>
							<form:input path="lastname" type="text"
								placeholder="Enter last name" class="form-control"></form:input>
						</div>
						
						<div class="row">
							<form:label path="company">Company</form:label>
							<form:input path="company" type="text"
								placeholder="Enter company name" class="form-control"></form:input>
						</div>
						
						<div class="row">
							<form:label path="contact">Contact</form:label>
							<form:input path="contact" type="text"
								placeholder="Enter email or phone number" class="form-control"></form:input>
						</div>

						<div class="row">
							<form:label path="username">Username</form:label>
							<form:input path="username" type="text"
								placeholder="Enter username" class="form-control"></form:input>
						</div>

						<div class="row">
							<form:label path="password">Password</form:label>
							<form:input path="password" type="password"
								placeholder="Enter password" class="form-control"></form:input>
						</div>

						<div class="row">
							<form:label path="type">I am a...</form:label>
							<form:select path="type" class="form-control">
								<option>Consumer</option>
								<option>Device Supplier</option>
								<option>Micro Farmer</option>
								<option>Seed Supplier</option>
							</form:select>
						</div>
					</div>
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