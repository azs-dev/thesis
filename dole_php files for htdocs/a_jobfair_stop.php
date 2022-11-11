<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jfId = $_POST["jobfairId"];
    $time = $_POST["time"];

    require_once 'connect.php';

   $sql = "UPDATE jobfair_table
    SET jf_status = '2'
    WHERE jf_id = '$jfId';";

    $sql_time = "UPDATE jobfair_table
    SET stop_time = '$time'
    WHERE jf_id = '$jfId'";

    $response = mysqli_query($conn, $sql);
    $time_response = mysqli_query($conn, $sql_time);
  
    $result = array();

    if(mysqli_query($conn, $sql)){
        if (mysqli_query($conn,$sql_time)) {
        
            $result["success"] = "1";
            echo json_encode($result);
            mysqli_close($conn);
        }
    }
    else {
            $result['success'] = "0";  
            echo json_encode($result);
            mysqli_close($conn);
    }

}

?>      