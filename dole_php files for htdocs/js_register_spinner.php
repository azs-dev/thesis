<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    require_once 'connect.php';

   $sql = "SELECT * FROM skills_table";
   $sql2 = "SELECT * FROM education_table";

    $response = mysqli_query($conn, $sql);
    $response2 = mysqli_query($conn, $sql2);
  
    $result = array();
    $result['skills'] = array();
    $result['ed'] = array();
    if ( mysqli_num_rows($response) !=null ) {
        
        while($row = mysqli_fetch_assoc($response)){
            $index[$row['skills_id']] = $row['skills'];
        }

        while ($row = mysqli_fetch_assoc($response2)) {
            $index2[$row['ed_id']] = $row['ed_level'];
        }
            array_push($result['skills'], $index);
            array_push($result['ed'], $index2);
            $result['success'] = "1";
            echo json_encode($result);
            mysqli_close($conn);

    } 
}
?>      