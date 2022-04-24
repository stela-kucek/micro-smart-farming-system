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
<title>MSFS New Grow Program</title>

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
		<div class="container">
			<div class="title">
				<h1>Create a New Grow Program</h1>
			</div>

			<!-- Device Registration -->

			<div>
				<form:form method="POST"
					action="${pageContext.request.contextPath}/createGrowProgram"
					modelAttribute="gpForm">
					<div class="form-group">
						<div class="col-xs-15">
							<div>
								<!-- Check for registration error -->
								<c:if test="${error != null}">
									<div class="alert alert-danger col-xs-offset-1 col-xs-10">
										${error}</div>
								</c:if>
							</div>
						</div>

						<!-- Input fields -->
						<div class="row">
							<form:label path="name">Name your grow program:</form:label>
							<form:input path="name" type="text"
								placeholder="Enter program name" class="form-control"></form:input>
						</div>
						<div class="row">
							<form:label path="plantName">Which plant is this program for?</form:label>
							<form:input path="plantName" type="text"
								placeholder="Enter plant (species) name" class="form-control"></form:input>
						</div>
						<div class="row">
							<form:label path="price">Set a price for your program (in euro):</form:label>
							<form:input path="price" type="number" min="0.1" step="0.01"
								placeholder="Enter price" class="form-control"></form:input>
						</div>
						<div class="row">
							<form:label path="forSale">Should this program be put on the market for sale?</form:label>
							<form:select path="forSale" class="form-control">
								<option value="true">Yes</option>
								<option value="false">No</option>
							</form:select>
						</div>
						<br>
						<hr>
						<br>
						<div class="row justify-content-center">
							<b>Seed Treatment</b>
						</div>
						<div class="row">
							<div class="col">
								<form:label path="seedAt">Seed at depth of:</form:label>
								<form:input path="seedAt" type="number" min="0.1" step="0.01"
									placeholder="e.g., 2.5" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedUnit">Unit:</form:label>
								<form:select path="seedUnit" class="form-control">
									<option>cm</option>
									<option>mm</option>
								</form:select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="replantAfter">Replant after:</form:label>
								<form:input path="replantAfter" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="replantTimeUnit">Unit:</form:label>
								<form:select path="replantTimeUnit" class="form-control">
									<option>week(s)</option>
									<option>day(s)</option>
								</form:select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="seedIrrigateHowMuch">Water amount (in L):</form:label>
								<form:input path="seedIrrigateHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedIrrigateEvery">Water every...</form:label>
								<form:input path="seedIrrigateEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedIrrigateTimeUnit">Unit:</form:label>
								<form:select path="seedIrrigateTimeUnit" class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="seedFertilizer">Pick a fertilizer:</form:label>
								<form:select path="seedFertilizer" class="form-control">
									<option>Urea</option>
									<option>Ammonium Nitrate</option>
									<option>Ammonium Sulfate</option>
									<option>Calcium Nitrate</option>
									<option>Diammonium Phosphate</option>
									<option>Monoammonium Phosphate</option>
									<option>Triple Super Phosphate</option>
									<option>Potassium Nitrate</option>
									<option>Potassium Chloride</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="seedFertilizeHowMuch">Amount to apply:</form:label>
								<form:input path="seedFertilizeHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedFertilizerUnit">Unit:</form:label>
								<form:select path="seedFertilizerUnit" class="form-control">
									<option>TS</option>
									<option>g</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="seedFertilizeEvery">Apply every:</form:label>
								<form:input path="seedFertilizeEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedFertilizeTimeUnit">Unit:</form:label>
								<form:select path="seedFertilizeTimeUnit" class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
							<div class="col-2">
								<form:label path="seedFertilizerReps">Times to apply:</form:label>
								<form:input path="seedFertilizerReps" type="number" min="1"
									data-toggle="tooltip" data-placement="top"
									title="e.g., 2 if there should be a total of 2 applications of the fertilizer in this stage"
									class="form-control"></form:input>
							</div>
						</div>
						<br>
						<div class="row justify-content-center">
							<i>Ideal conditions</i>
						</div>

						<div class="row">

							<div class="col">
								<form:label path="seedHumidValue">Soil humidity (in %):</form:label>
								<form:input path="seedHumidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedAcidValue">Soil acidity (in pH):</form:label>
								<form:input path="seedAcidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
						</div>

						<br>
						<hr>
						<br>
						<div class="row justify-content-center">
							<b>Seedling Treatment</b>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="seedlingIrrigateHowMuch">Water amount (in L):</form:label>
								<form:input path="seedlingIrrigateHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedlingIrrigateEvery">Water every...</form:label>
								<form:input path="seedlingIrrigateEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedlingIrrigateTimeUnit">Unit:</form:label>
								<form:select path="seedlingIrrigateTimeUnit"
									class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="seedlingFertilizer">Pick a fertilizer:</form:label>
								<form:select path="seedlingFertilizer" class="form-control">
									<option>Urea</option>
									<option>Ammonium Nitrate</option>
									<option>Ammonium Sulfate</option>
									<option>Calcium Nitrate</option>
									<option>Diammonium Phosphate</option>
									<option>Monoammonium Phosphate</option>
									<option>Triple Super Phosphate</option>
									<option>Potassium Nitrate</option>
									<option>Potassium Chloride</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="seedlingFertilizeHowMuch">Amount to apply:</form:label>
								<form:input path="seedlingFertilizeHowMuch" type="number"
									min="1" step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedlingFertilizerUnit">Unit:</form:label>
								<form:select path="seedlingFertilizerUnit" class="form-control">
									<option>TS</option>
									<option>g</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="seedlingFertilizeEvery">Apply every:</form:label>
								<form:input path="seedlingFertilizeEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedlingFertilizeTimeUnit">Unit:</form:label>
								<form:select path="seedlingFertilizeTimeUnit"
									class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
							<div class="col-2">
								<form:label path="seedlingFertilizerReps">Times to apply:</form:label>
								<form:input path="seedlingFertilizerReps" type="number" min="1"
									data-toggle="tooltip" data-placement="top"
									title="e.g., 2 if there should be a total of 2 applications of the fertilizer in this stage"
									class="form-control"></form:input>
							</div>
						</div>
						<br>
						<div class="row justify-content-center">
							<i>Ideal conditions</i>
						</div>

						<div class="row">

							<div class="col">
								<form:label path="seedlingHumidValue">Soil humidity (in %):</form:label>
								<form:input path="seedlingHumidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="seedlingAcidValue">Soil acidity (in pH):</form:label>
								<form:input path="seedlingAcidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
						</div>
						<br>
						<hr>
						<br>
						<div class="row justify-content-center">
							<b>Young Treatment</b>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="youngIrrigateHowMuch">Water amount (in L):</form:label>
								<form:input path="youngIrrigateHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="youngIrrigateEvery">Water every...</form:label>
								<form:input path="youngIrrigateEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="youngIrrigateTimeUnit">Unit:</form:label>
								<form:select path="youngIrrigateTimeUnit" class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="youngFertilizer">Pick a fertilizer:</form:label>
								<form:select path="youngFertilizer" class="form-control">
									<option>Urea</option>
									<option>Ammonium Nitrate</option>
									<option>Ammonium Sulfate</option>
									<option>Calcium Nitrate</option>
									<option>Diammonium Phosphate</option>
									<option>Monoammonium Phosphate</option>
									<option>Triple Super Phosphate</option>
									<option>Potassium Nitrate</option>
									<option>Potassium Chloride</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="youngFertilizeHowMuch">Amount to apply:</form:label>
								<form:input path="youngFertilizeHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="youngFertilizerUnit">Unit:</form:label>
								<form:select path="youngFertilizerUnit" class="form-control">
									<option>TS</option>
									<option>g</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="youngFertilizeEvery">Apply every:</form:label>
								<form:input path="youngFertilizeEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="youngFertilizeTimeUnit">Unit:</form:label>
								<form:select path="youngFertilizeTimeUnit" class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
							<div class="col-2">
								<form:label path="youngFertilizerReps">Times to apply:</form:label>
								<form:input path="youngFertilizerReps" type="number" min="1"
									data-toggle="tooltip" data-placement="top"
									title="e.g., 2 if there should be a total of 2 applications of the fertilizer in this stage"
									class="form-control"></form:input>
							</div>
						</div>
						<br>
						<div class="row justify-content-center">
							<i>Ideal conditions</i>
						</div>

						<div class="row">

							<div class="col">
								<form:label path="youngHumidValue">Soil humidity (in %):</form:label>
								<form:input path="youngHumidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="youngAcidValue">Soil acidity (in pH):</form:label>
								<form:input path="youngAcidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
						</div>
						<br>
						<hr>
						<br>
						<div class="row justify-content-center">
							<b>Adult Treatment</b>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="adultIrrigateHowMuch">Water amount (in L):</form:label>
								<form:input path="adultIrrigateHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="adultIrrigateEvery">Water every...</form:label>
								<form:input path="adultIrrigateEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="adultIrrigateTimeUnit">Unit:</form:label>
								<form:select path="adultIrrigateTimeUnit" class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col">
								<form:label path="adultFertilizer">Pick a fertilizer:</form:label>
								<form:select path="adultFertilizer" class="form-control">
									<option>Urea</option>
									<option>Ammonium Nitrate</option>
									<option>Ammonium Sulfate</option>
									<option>Calcium Nitrate</option>
									<option>Diammonium Phosphate</option>
									<option>Monoammonium Phosphate</option>
									<option>Triple Super Phosphate</option>
									<option>Potassium Nitrate</option>
									<option>Potassium Chloride</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="adultFertilizeHowMuch">Amount to apply:</form:label>
								<form:input path="adultFertilizeHowMuch" type="number" min="1"
									step="0.01" placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="adultFertilizerUnit">Unit:</form:label>
								<form:select path="adultFertilizerUnit" class="form-control">
									<option>TS</option>
									<option>g</option>
								</form:select>
							</div>
							<div class="col">
								<form:label path="adultFertilizeEvery">Apply every:</form:label>
								<form:input path="adultFertilizeEvery" type="number" min="1"
									placeholder="e.g., 2" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="adultFertilizeTimeUnit">Unit:</form:label>
								<form:select path="adultFertilizeTimeUnit" class="form-control">
									<option>day(s)</option>
									<option>week(s)</option>
								</form:select>
							</div>
							<div class="col-2">
								<form:label path="adultFertilizerReps">Times to apply:</form:label>
								<form:input path="adultFertilizerReps" type="number" min="1"
									data-toggle="tooltip" data-placement="top"
									title="E.g., 2 if there should be a total of 2 applications of the fertilizer in this stage."
									class="form-control"></form:input>
							</div>
						</div>
						<br>
						<div class="row justify-content-center">
							<i>Ideal conditions</i>
						</div>

						<div class="row">

							<div class="col">
								<form:label path="adultHumidValue">Soil humidity (in %):</form:label>
								<form:input path="adultHumidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
							<div class="col">
								<form:label path="adultAcidValue">Soil acidity (in pH):</form:label>
								<form:input path="adultAcidValue" type="number" min="1.0"
									step="0.01" placeholder="e.g., 30" class="form-control"></form:input>
							</div>
						</div>

						<br>
						<hr>
						<br>
						<div class="row justify-content-center">
							<b>Fruit Treatment</b>
						</div>
						<div class="row justify-content-center">
							<i>When a plant is at this stage, it should be harvested.</i>
						</div>


						<br>
						<div class="row">
							<form:label path="harvestMode">Pick a harvest mode:</form:label>
							<form:select path="harvestMode" class="form-control">
								<option value="manual">I am harvesting myself, manually</option>
								<option value="onClick">The device should perform the
									harvest, but on-click</option>
								<option value="auto">The device should harvest
									automatically when it detects a fruit</option>
							</form:select>
						</div>




					</div>
					<br>
					<button type="submit" class="btn btn-primary">Create</button>

				</form:form>

				<br>
				<form:form method="GET"
					action="${pageContext.request.contextPath}/growPrograms">
					<button type="submit" class="btn btn-secondary">Back to
						grow programs</button>
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