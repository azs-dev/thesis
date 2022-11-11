<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $eID = $_POST["e_id"];

    require_once 'connect.php';
    
   $sql = "SELECT * FROM jobfair_table WHERE jf_status = '2';"; //previous 

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['previous'] = array();

    if (!empty(mysqli_num_rows($response))) {

        while($row = mysqli_fetch_assoc($response)){
            $index['jf_name'] = $row['jf_name'];
            $index['jf_date'] = $row['jf_date'];
            $index['jf_location'] = $row['jf_location'];
            $index['jf_id'] = $row['jf_id'];
             array_push($result['previous'], $index);
        }   

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);
            mysqli_close($conn); 

    } 
    else {
        $result['success'] = "0";
        $result['message'] = "error";
        echo json_encode($result);
        mysqli_close($conn);
}

}

?>      