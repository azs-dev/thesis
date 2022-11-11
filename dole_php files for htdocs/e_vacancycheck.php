<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $eId = $_POST["employerId"];
    $jfId = $_POST["jobfairId"];
    require_once 'connect.php';

    $sql = "SELECT * FROM jobvacancy_table
    WHERE jv_jf_id = '$jfId' AND jv_e_id = '$eId'";

    $response = mysqli_query($conn, $sql);

    $result = array();
	if (mysqli_num_rows($response) != null) {
        
            $result['success'] = "1";
            echo json_encode($result);
            mysqli_close($conn);
	}
	else {
    	    $result['success'] = "0";
            echo json_encode($result);
            mysqli_close($conn);
    }
}

?>