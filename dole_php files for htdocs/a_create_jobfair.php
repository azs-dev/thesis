<?php
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jf_name = $_POST["jf_name"];
	$jf_date = $_POST["jf_date"];
	$jf_datestop = $_POST["jf_date"];
	$jf_location = $_POST["jf_location"];
	$jf_organizer = $_POST["jf_organizer"];
	$jf_description = $_POST["jf_description"];

	require_once 'connect.php';

	$sql_check = "SELECT * FROM jobfair_table WHERE jf_name = '$jf_name'";

	$check_response = mysqli_query($conn, $sql_check);
	if (mysqli_num_rows($check_response) != null) {
		$result["message"] = "duplicate";
		$result['success'] = "0";

		echo json_encode($result);
		mysqli_close($conn);
	} else {
			$sql = "INSERT INTO jobfair_table(jf_name,jf_date,jf_datestop,jf_location,jf_organizer,jf_description) VALUES ('$jf_name','$jf_date','$jf_datestop','$jf_location','$jf_organizer','$jf_description');";

				if(mysqli_query($conn, $sql)){

					$sql_getId = "SELECT jf_id FROM jobfair_table WHERE jf_name ='$jf_name' AND jf_date = '$jf_date' AND jf_location = '$jf_location' AND jf_organizer = '$jf_organizer'";
					$getId_response = mysqli_query($conn, $sql_getId);
					if (mysqli_num_rows($getId_response) != null) {
						while ($row = mysqli_fetch_assoc($getId_response)) {
							$result['jobfairId'] = $row['jf_id'];
							$result['jobfairName'] = $jf_name;
						}
						$result["success"] = "1";
						$result["message"] = "success";
						echo json_encode($result);
						mysqli_close($conn);
					}
				
			} else {
				$result["success"] = "0";
				$result["message"] = "error";

				echo json_encode($result);
				mysqli_close($conn);
			}
	}

}
?>