<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jfId = $_POST['jobfairId'];

    require_once 'connect.php';

   $sql = "SELECT DISTINCT jobfair_employers.employer_id, employer_table.e_cname, employer_table.e_email, employer_table.e_cnumber
            FROM jobfair_employers
            LEFT JOIN employer_table
            ON jobfair_employers.employer_id = employer_table.e_id
            WHERE jobfair_employers.jobfair_id = '$jfId'";

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['employers'] = array();

    if (!empty((mysqli_num_rows($response)))) {
           
        while($row = mysqli_fetch_assoc($response)){
            $index['employer_id'] = $row['employer_id'];
            $eId = $row['employer_id'];
            $index['e_cname'] = $row['e_cname'];

            $sql_count = "SELECT COUNT(DISTINCT employer_scanned_table.jobseeker_id) AS COUNT
                            FROM employer_scanned_table WHERE jobfair_id = '$jfId' AND employer_id = '$eId'";
            $count_response = mysqli_query($conn,$sql_count);
            if (!empty(mysqli_num_rows($count_response))) {
                while ($row2 = mysqli_fetch_assoc($count_response)) {
                    $index['jobseeker_count'] = $row2['COUNT'];
                }
            }
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