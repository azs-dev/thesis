<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    require_once 'connect.php';

   $sql = "SELECT * FROM jobfair_table WHERE jf_status ='0';"; //upcoming AND  jf_date>=CURRENT_DATE
   $sql2 = "SELECT * FROM jobfair_table WHERE jf_status = '1';";//ongoing AND jf_date>=CURRENT_DATE

    $response = mysqli_query($conn, $sql);
    $response2 = mysqli_query($conn, $sql2);
  
    $result = array();
    $result['upcoming'] = array();
    $result['ongoing'] = array();

    if (!empty((mysqli_num_rows($response))) || !empty(mysqli_num_rows($response2))) {
           
        while($row = mysqli_fetch_assoc($response)){
            $index['jf_name'] = $row['jf_name'];
            $index['jf_date'] = $row['jf_date'];
            $index['jf_location'] = $row['jf_location'];
            $index['jf_id'] = $row['jf_id'];
            $index['jf_description'] = $row['jf_description'];
             array_push($result['upcoming'], $index);
        }   

        while($row2 = mysqli_fetch_assoc($response2)){
            $index2['jf_name'] = $row2['jf_name'];
            $index2['jf_date'] = $row2['jf_date'];
            $index2['jf_location'] = $row2['jf_location'];
            $index2['jf_id'] = $row2['jf_id'];
            $index2['jf_description'] = $row2['jf_description'];
             array_push($result['ongoing'], $index2);
        }  
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