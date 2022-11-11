<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jfId = $_POST["jobfairId"];
	$eId = $_POST["employerId"];

	require_once 'connect.php';

	$sql_check = "SELECT jobfair_id,employer_id FROM jobfair_employers
	WHERE jobfair_id = '$jfId' AND employer_id = '$eId'";

	$response_check = mysqli_query($conn,$sql_check);

	if (!empty(mysqli_num_rows($response_check))) {
		$result['success'] = "0";
		$result['message'] = "duplicate";
		echo json_encode($result);
		mysqli_close($conn);
	} else {

			$sql = "INSERT INTO jobfair_employers (jobfair_id,employer_id) VALUES ('$jfId','$eId')";

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