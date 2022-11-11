<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jfId = $_POST["jobfairId"];

    require_once 'connect.php';

    $sql = "SELECT * FROM jobfair_table WHERE jf_id ='$jfId'"; //upcoming

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['jobfair'] = array();

    if (mysqli_num_rows($response) != null) {
           
        while($row = mysqli_fetch_assoc($response)){
            $index['jf_name'] = $row['jf_name'];
            $index['jf_date'] = $row['jf_date'];
            $index['jf_location'] = $row['jf_location'];
            $index['jf_datestop'] = $row['jf_datestop'];
            $index['jf_organizer'] = $row['jf_organizer'];
            $index['jf_description'] = $row['jf_description'];
            $index['jf_pattendees'] = $row['jf_pattendees'];
            array_push($result['jobfair'], $index);
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