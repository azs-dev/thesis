<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jvId = $_POST["vacancyId"];
	$jsId = $_POST["jobseekerId"];
	$jfId = $_POST["jobfairId"];
	$eId = $_POST["employerId"];
	$hstatus = $_POST["hiringstatus"];
	$location = $_POST["location"];
	$gender = $_POST["gender"];
	$comments = $_POST["comments"];

	require_once 'connect.php';

	$sql_check = "SELECT * FROM employer_scanned_table WHERE vacancy_id = '$jvId' AND jobseeker_id = '$jsId' AND employer_id = '$eId' AND jobfair_id = '$jfId'";
	$check_response = mysqli_query($conn, $sql_check);

	if( mysqli_num_rows($check_response) != null){

		$result["success"] = "0";
		$result["message"] = "duplicate";
		echo json_encode($result);
		mysqli_close($conn);

	} else {

		$hiring_sql = "SELECT hs_id FROM hstatus_table WHERE hiring_status='$hstatus'";
			$hstatusInt = 0;
			$response = mysqli_query($conn, $hiring_sql);
			if ( mysqli_num_rows($response) != null ){
			    while($row = mysqli_fetch_assoc($response)){
			    	$hstatusInt = $row['hs_id'];
			    }
			}

		$sql = "INSERT INTO employer_scanned_table(vacancy_id,jobfair_id,jobseeker_id,employer_id,hstatus_id,jv_location,js_gender,comments) 
		VALUES ('$jvId','$jfId','$jsId','$eId','$hstatusInt','$location','$gender','$comments')";

		if(mysqli_query($conn, $sql)){
			$result["success"] = "1";
			$result["message"] = "success";
			echo json_encode($result);
			mysqli_close($conn);

		} else {
			$result["success"] = "0";
			$result["message"] = "error";
			echo json_encode($result);
			mysqli_close($conn);
		}
	}

}
?>