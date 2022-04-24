<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<% String dataPoints = (String) request.getAttribute("dataPoints"); %>

<html>

	<!-- Head -->
	<head>
		<script type="text/javascript">
			window.onload = function() {
				var chart = new CanvasJS.Chart("chartContainer", {
					theme: "light2",
					title: {
						text: "Monthly profit in year 2020"
					},
					axisX: {
						title: "Month"
					},
					axisY: {
						title: "Profit"
					},
					data: [{
						type: "line",
						yValueFormatString: "#,##0 €",                  
						dataPoints : <%out.print(dataPoints);%>
					}]
				 });
				chart.render();
			}
		</script>
	
		<!-- Required meta tags -->
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<!-- Bootstrap CSS -->
		<link rel="stylesheet"
			href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
		
		<!-- Page title -->
		<title>MSFS Farmer Finances</title>
		
		<style>
		
		.failed {
			color: red;
		}
		
		body {
			height: auto;
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
		
		
		.google-visualization-table-td {
		text-align: center !important;
		}
		
		</style>
	</head>


	<!-- Body -->
	<body>
		<div class="text-center">
			<div class="container-fluid">
				<div class="title">
					<sec:authorize access="isAuthenticated()">
						<h1 style="color:blue;">
							Here is your financial report,
							<sec:authentication property="name" />
						</h1>
						<br>
						<br>
						
						<!-- Representation of the report tables -->
						<h2>Revenue</h2>
						<br>
						<div id="table_div1" style="width: 900px"></div>
						<br>
						
						<h2>Expenses</h2>
						<br>
						<div id="table_div2" style="width: 900px"></div>
						<br>
						
						<h2>Operational Cost</h2>
						<br>
						<div id="table_div3" style="width: 900px"></div>
						<br>
						
						<form:form method="POST" action="${pageContext.request.contextPath}/logout">
						<button type="submit" class="btn btn-outline-danger">Log out</button>
						</form:form>
						<button onclick="myFunction()" class="btn btn-outline-primary">Visualize data</button>
						<br>
						<div id="myDIV" style="visibility:hidden;">
							<br>
						 	<div id="chartContainer" style="height: 370px; width: 100%;"></div>
						</div>
					
					</sec:authorize>
				</div>
			</div>
		</div>
	

		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
		<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
		
		<script>
		function myFunction() {
			  var x = document.getElementById("myDIV");
			  if (x.style.visibility === "hidden") {
				  x.style.visibility = "visible";
			  } else {
				  x.style.visibility = "hidden";
			  }
			}
		</script>
		
		<!-- start of report tables -->
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
		<script type='text/javascript' src="https://www.google.com/jsapi"></script>
	   	<script type='text/javascript'>
	
	   	
	   		<!-- Tables for financial report -->
	        google.load("visualization", "1", {packages:["table"]});
	        google.setOnLoadCallback(drawChart);
	
	        function drawChart() {
	        	
	        	
	        	<!-- Table for Revenue report -->
                var data1 = google.visualization.arrayToDataTable([
	                  ['Amount', 'Description', 'Currency', 'Date'],
	                   <c:forEach  var="revenues" items="${Revenues}" >
	                        [${revenues.amount}, '${revenues.description}', '${revenues.currency}', '${revenues.date}'], 
	                    </c:forEach>
	                ]);   
                
                var table1 = new google.visualization.Table(document.getElementById('table_div1'));
	            table1.draw(data1, {showRowNumber: true, width: '100%'});  
	            
	            
	        	<!--table for Expenses report-->
	        	var data2 = google.visualization.arrayToDataTable([
	                  ['Amount', 'Description', 'Currency', 'Date'],
	                   <c:forEach var="expensesofuser" items="${ExpensesOfUser}">
	                        [${expensesofuser.amount}, '${expensesofuser.description}', '${expensesofuser.currency}', '${expensesofuser.date}'], 
	                    </c:forEach>
	                ]);
	        	
	        	var table2 = new google.visualization.Table(document.getElementById('table_div2'));
	            table2.draw(data2, {showRowNumber: true, width: '100%'}); 
	            
	            
	        	<!-- Table for OperationalCost report -->
                var data3 = google.visualization.arrayToDataTable([
	                  ['Amount', 'Description', 'Currency', 'Date'],
	                   <c:forEach  var="operationalcost" items="${OperationalCost}" >
	                        [${operationalcost.amount}, '${operationalcost.description}', '${operationalcost.currency}', '${operationalcost.date}'], 
	                    </c:forEach>
	                ]);   
                
                var table3 = new google.visualization.Table(document.getElementById('table_div3'));
	            table3.draw(data3, {showRowNumber: true, width: '100%'}); 
	                     
	        }
	          
	   	</script>
	</body>
</html>