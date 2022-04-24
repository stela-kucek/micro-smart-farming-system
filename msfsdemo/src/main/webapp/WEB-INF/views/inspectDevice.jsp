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
	height: 150%;
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
textarea {
  resize: none;
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
					<c:if test="${infoMessage != null}">
							<div class="alert alert-secondary col-xs-offset-1 col-xs-10">
								${infoMessage}</div>
					</c:if>
					<h1>
						${entry.getName()} Maintenance Data: 
						<br>
					</h1>
					
					<table class="table">
					    <tr>
					      <th scope="row">Device Name:</th>
					      <td> ${entry.getName()}</td>
					    </tr>
					     <tr>
					     <th scope="row">Owner Name:</th>
					      <td> ${entry.getOwner()}</td>
					    </tr>
					     <tr>
					    <th scope="row">Is Active: </th>
					      <td> ${entry.isActive()}</td>
					    </tr>
					    <tr>
					      <th scope="row">Battery Level: </th>
					      <td> ${entry.getMaintenanceData().getBatteryLevel()}</td>
					    </tr>
					    <tr>
					    <th scope="row">Operation Hours: </th>
					      <td> ${entry.getMaintenanceData().getOperationHours()}</td>
					    </tr>
					    <tr>
					    <th scope="row">Firmware Version: </th>
					      <td> ${entry.getMaintenanceData().getFirmwareVersion()}</td>  
					    </tr>
					    <tr>
					    <th scope="row">State: </th>
					      <td> ${entry.getMaintenanceData().getState()}</td>
					    </tr>
					</table>
					<form:form method="POST" 
					action="${pageContext.request.contextPath}/firmwareUpdate" modelAttribute = "deviceForm" >
					<button type="submit" class="btn btn-warning">Update Firmware</button>
					<form:input type="hidden" path = "chosenDevice"  value="${entry.getId()}"/>
					</form:form>
					<form:form name= "myForm" method = "POST" action = "notifyCustomer" modelAttribute = "notification" onsubmit="return validateForm()">
 						Send a message to ${entry.getOwner()} :
 						<br>
 						<td><form:textarea path = "notificationMsg" rows = "5" cols = "100" resize="none" /></td><br>
 						<form:input type="hidden" path = "deviceName"  value="${entry.getName()}"/>
 						<form:input type="hidden" path = "ownerName"  value="${entry.getOwner()}"/>
  						<input type = "submit" class="btn btn-primary"  value = "Send message"/>
 					</form:form>
						<p id="warning"></p>			
					<br>
					<form:form method="GET"
					action="${pageContext.request.contextPath}/customerDevices">
					<button type="submit" class="btn btn-secondary">Back to
						device list</button>
			      	</form:form>
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
	function validateForm() {
		  var x = document.forms["myForm"]["notificationMsg"].value;
		  var warning;
		  if (x == "") {
		    warning = "Message Body required!";
		    document.getElementById("warning").innerHTML = warning;
		    return false;
		  }
		  
		}
	</script>
</body>
</html>