<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

	$jfId = $_POST['jobfairId'];

    require_once 'connect.php';

   $sql = "SELECT DISTINCT jobfair_employers.employer_id, employer_table.e_cname, employer_table.e_email, employer_table.e_cnumber
            FROM jobfair_employers
            LEFT JOIN employer_table 
            ON employer_table.e_id = jobfair_employers.employer_id
            WHERE jobfair_employers.jobfair_id = '$jfId'"; //all employer's that posted a vacancy  

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['employers'] = array();

    if (!empty((mysqli_num_rows($response)))) {
           
        while($row = mysqli_fetch_assoc($response)){
            $index['employer_id'] = $row['employer_id'];
            $index['e_cname'] = $row['e_cname'];
            $index['e_email'] = $row['e_email'];
            $index['e_cnumber'] = $row['e_cnumber'];
             array_push($result['employers'], $index);
                
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