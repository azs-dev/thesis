<?php
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$e_username = $_POST["e_username"];
	$e_password = $_POST["e_password"];
	$e_email = $_POST["e_email"];
	$e_cname = $_POST["e_cname"];
	$e_caddress = $_POST["e_caddress"];
	$e_cnumber = $_POST["e_cnumber"];
	$e_cperson = $_POST["e_cperson"];

	$e_password = password_hash($e_password, PASSWORD_DEFAULT);

	require_once 'connect.php';

	//if statement, new SQL. if mysqli_num_rows(reponse for queue of SQL statement to check if USERNAME is duplicated){ $result[" success"]=0 message"duplicate"}
	//in android. if duplicate = 1. PUT ERROR USERNAME.
	$username_check = "SELECT * FROM employer_table WHERE e_username = '$e_username'";
	$username_check2 = "SELECT * FROM jobseeker_table WHERE js_username = '$e_username'";
	$username_response = mysqli_query($conn, $username_check);
	$username_response2 = mysqli_query($conn, $username_check2);
	
	if (mysqli_num_rows($username_response) != null) {

		$result['success'] = "0";
		$result['message'] = "username already exists";
		echo json_encode($result);
		mysqli_close($conn);

	} else {
		if (mysqli_num_rows($username_response2) !=null) {
			$result['success'] = "0";
			$result['message'] = "username already exists";
			echo json_encode($result);
			mysqli_close($conn);
		} else {
		
			$sql = "INSERT INTO employer_table(e_username,e_password,e_email,e_cname,e_caddress,e_cnumber,e_cperson) VALUES ('$e_username','$e_password','$e_email','$e_cname','$e_caddress','$e_cnumber','$e_cperson');";

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

}
?>