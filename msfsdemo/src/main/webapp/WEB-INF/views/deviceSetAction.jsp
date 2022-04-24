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
<title>MSFS Set Device Action</title>

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
<body onload="hideElements()">

	<div class="text-center">
		<div class="container">
			<div class="title">
				<h1>Set Configuration Data for Device</h1>
				<h1>${deviceName}</h1>
			</div>

			<!-- Device Registration -->

			<div>
				<form:form method="POST"
					action="${pageContext.request.contextPath}/setAction"
					modelAttribute="actionForm">
					<form:input path="chosenDevice" type="hidden" value="${deviceId}" />
					<div class="form-group">
						<%
							int count = 0;
						%>
						<c:if test="${!entries.isEmpty()}">
							<div>
								<figure >
									<img src='<c:url value="/img/PLANT_POSITIONS.svg" ></c:url>'
										height="250px" style=" border: 5px #cccccc solid;"/>
									<figcaption>Sketch of farming site positions</figcaption>
								</figure>
							</div>
							<div class="row ">
								<form:label path="position">Choose a position</form:label>
							</div>
							<div class="row ">
								<form:select path="position" class="form-control">
									<c:forEach items="${entries}" var="entry">
										<option value="${entry.getNumber()}">
										${entry.getNumber()}
										</option>
									</c:forEach>
								</form:select>
							</div>
							<br>
							<div class="row ">
								<form:label path="chosenAction">Choose an action</form:label>
							</div>
							<div class="row ">
								<form:select id="action" path="chosenAction" class="form-control">
									<option value="Seeding">Seeding</option>
									<option value="Irrigating">Irrigating</option>
									<option value="Fertilizing">Fertilizing</option>
									<option value="Harvesting">Harvesting</option>
								</form:select>
							</div>
							<br>
							<div id="all" class="row">
							<form:label path="value">Value:</form:label>
							<form:input path="value" type="text"
								placeholder="e.g., 2.5" class="form-control"></form:input>
								<br>
							<form:label path="unit">Unit:</form:label>
								<form:select id="seedingUnits" path="unit" class="form-control">
									<option>cm</option>
									<option>mm</option>
								</form:select>	
								<form:select id="irrigationUnits" path="unit" class="form-control">
									<option>L</option>
								</form:select>
								<form:select id="fertilizerUnits" path="unit" class="form-control">
									<option>g</option>
									<option>TS</option>
								</form:select>	
					
							</div>
							<br>
							<div class="row justify-content-md-center">
								<button type="submit" class="btn btn-primary">Set
									action</button>
							</div>
						</c:if>

					</div>

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
	<script type="text/javascript">
	$("#action").change(function() {
		  if ($(this).val() == "Irrigating") {
			  $("#all").show();
			  $('#irrigationUnits').show();
			  $('#fertilizerUnits').hide();
			  $('#seedingUnits').hide();
		  } 
		  
		  else if ($(this).val() == "Fertilizing") {
			  $("#all").show();
			  $('#fertilizerUnits').show();
			  $('#seedingUnits').hide();
			  $('#irrigationUnits').hide();
			  }
		  
		  else if ($(this).val() == "Seeding") {
			  $("#all").show();
			    $('#seedingUnits').show();
			    $('#irrigationUnits').hide();
			    $('#fertilizerUnits').hide();
			  }
		  else {
			  $("#all").hide();
		  }
		});
		$("#action").trigger("change");
		
		function hideElements() {
		    $('#irrigationUnits').hide();
		    $('#fertilizerUnits').hide();
			}
	</script>
</body>
</html>