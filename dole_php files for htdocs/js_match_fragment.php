<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    require_once 'connect.php';

   $sql = "SELECT * FROM jobfair_table WHERE jf_status!=2"; //upcoming and going

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['jobfairs'] = array();

    if (mysqli_num_rows($response)==null) {
            $result['success'] = "0";
    }

        while($row = mysqli_fetch_assoc($response)){
            $index['jf_name'] = $row['jf_name'];
            $index['jf_date'] = $row['jf_date'];
            $index['jf_location'] = $row['jf_location'];
            $index['jf_id'] = $row['jf_id'];
             array_push($result['jobfairs'], $index);
        }     
            $result['success'] = "1";
            echo json_encode($result);
            mysqli_close($conn);

}

?>      