<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

	$jfId = $_POST['jobfairId'];

    require_once 'connect.php';

   $sql = "SELECT DISTINCT jobvacancy_table.jv_id, jobvacancy_table.jv_title, employer_table.e_cname, jobvacancy_table.jv_location
    FROM jobvacancy_table
    LEFT JOIN employer_table
    ON jobvacancy_table.jv_e_id = employer_table.e_id
    WHERE jv_jf_id='$jfId'"; //all vacancy in the jobfair

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['vacancies'] = array();

    if (!empty((mysqli_num_rows($response)))) {
           
        while($row = mysqli_fetch_assoc($response)){
            $index['jv_id'] = $row['jv_id'];
            $index['jv_title'] = $row['jv_title'];
            $index['e_cname'] = $row['e_cname'];
            $index['jv_location'] = $row['jv_location'];
             array_push($result['vacancies'], $index);
                
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