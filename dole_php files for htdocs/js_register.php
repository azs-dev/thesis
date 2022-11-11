<?php
		if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$js_username = $_POST["js_username"];
	$js_password = $_POST["js_password"];
	$js_email = $_POST["js_email"];
	$js_first_name = $_POST["js_first_name"];
	$js_last_name = $_POST["js_last_name"];
	$js_contactno = $_POST["js_contactno"];
	$js_address = $_POST["js_address"];
	$js_gender = $_POST["js_gender"];
	$js_dateofbirth = $_POST["js_dateofbirth"];
	$js_edattain = $_POST["js_edattain"];
	$js_course = $_POST["js_course"];
	$js_school = $_POST["js_school"];
	$js_o_skill = $_POST["js_o_skill"];
	$js_joblocation = $_POST["js_joblocation"];
	$js_jobpreferred = $_POST["js_jobpreferred"];
	$js_skill = $_POST["js_skill"]; //divide string by comma

	$js_password = password_hash($js_password, PASSWORD_DEFAULT);

	require_once 'connect.php';

	$username_check = "SELECT * FROM jobseeker_table WHERE js_username = '$js_username'";
	$username_check2 = "SELECT * FROM employer_table WHERE e_username = '$js_username'";
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
		} else{
	
			$education_sql = "SELECT ed_id FROM education_table WHERE ed_level='$js_edattain'";
			$js_edattainInt = 0;
			$response = mysqli_query($conn, $education_sql);
			if ( mysqli_num_rows($response) != null ){
			    while($row = mysqli_fetch_assoc($response)){
			    	$js_edattainInt = $row['ed_id'];
			    }
			}

			$sql = "INSERT INTO jobseeker_table(js_username,js_password,js_email,js_first_name,js_last_name,js_contactno,
			js_address,js_gender,js_dateofbirth,js_course,js_school,js_joblocation) VALUES ('$js_username','$js_password','$js_email',
			'$js_first_name','$js_last_name','$js_contactno','$js_address','$js_gender','$js_dateofbirth','$js_course','$js_school','$js_joblocation');";

			if(mysqli_query($conn, $sql)){
				$result["success"] = "1";
				$result["message"] = "success";
				echo json_encode($result);

				$skills = explode(",", $js_skill);
				for($i = 0 ; $i< sizeof($skills);$i++){
				$skills_sql = "INSERT into jobseeker_credentials (jobseeker_id,education_id,job_preferred,other_skill,jobseeker_skills_id)
					select js_id, '$js_edattainInt','$js_jobpreferred','$js_o_skill',skills_id
					from jobseeker_table, skills_table
					where jobseeker_table.js_username = '$js_username'
					and skills_table.skills = '$skills[$i]'";

						if(mysqli_query($conn, $skills_sql)){
							$result2["skill-message"] = "success";
							echo json_encode($result2);
						} else {
							$result2["skill-message"] = "error";
							echo json_encode($result2);
						}
				}
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