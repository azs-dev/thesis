<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $eId = $_POST["employerId"];
    $jfId = $_POST["jobfairId"];
    require_once 'connect.php';

    $sql = "SELECT jobfair_id, employer_id FROM jobfair_employers
    WHERE employer_id ='$eId' AND jobfair_id ='$jfId' ";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['employer'] = array();
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