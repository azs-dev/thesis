<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jsId = $_POST["jobseekerId"];
    $jfId = $_POST["jobfairId"];
    $pId = $_POST["partnerId"];

    require_once 'connect.php';

    $sql = "UPDATE attendees_table
    SET p_id = '$pId', at_attend = '1'
    WHERE js_id = '$jsId' AND jf_id = '$jfId';";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['jobseeker'] = array();
    
	if(mysqli_query($conn, $sql)){
        $result["success"] = "1";
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

