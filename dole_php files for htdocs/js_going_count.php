<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

	$jfId = $_POST['jfId'];
	$decision = $_POST['decision'];
	$jsId = $_POST['jsId'];
	require_once 'connect.php';

		if ($decision == "plus") {
			$sql = "UPDATE jobfair_table SET jf_pattendees = jf_pattendees+1 WHERE jf_id = '$jfId'; ";
			$sql2 = "INSERT INTO attendees_table (jf_id,js_id,p_id) VALUES ('$jfId','$jsId', NULL);";
				if(mysqli_query($conn, $sql)){
					if (mysqli_query($conn,$sql2)) {
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

		} elseif ($decision == "minus") {
			$sql = "UPDATE jobfair_table SET jf_pattendees = jf_pattendees-1 WHERE jf_id = '$jfId' AND jf_pattendees!='0';";
			$sql2 = "DELETE FROM attendees_table WHERE jf_id ='$jfId' AND js_id='$jsId'";
				if(mysqli_query($conn, $sql)){
					if (mysqli_query($conn,$sql2)) {
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
		} else {
			$result["success"] = "0";
			$result["message"] = "error";

			echo json_encode($result);
			mysqli_close($conn);
		}
		
	}
?>

