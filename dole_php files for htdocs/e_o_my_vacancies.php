<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jfID = $_POST["jobfairId"];
    $eID = $_POST["employerId"];

    require_once 'connect.php';

   $sql = "SELECT jobvacancy_table.jv_id, jobvacancy_table.jv_title, jobvacancy_table.jv_location, jobvacancy_table.jv_vacancy_count
    FROM jobvacancy_table
    WHERE jv_jf_id='$jfID' AND jv_e_id = '$eID';"; 

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['vacancies'] = array();

    if (!empty(mysqli_num_rows($response)) ) {
        
        while($row = mysqli_fetch_assoc($response)){
        	$index['jv_id'] = $row['jv_id'];
            $index['jv_title'] = $row['jv_title'];
            $index['jv_location'] = $row['jv_location'];
            $index['jv_vacancy_count'] = $row['jv_vacancy_count'];
            array_push($result['vacancies'], $index);
        }
        $result['success'] = "1";
        echo json_encode($result);
        mysqli_close($conn);
    } else {
        $result['success'] = "0";
        echo json_encode($result);
        mysqli_close($conn);
    }
}
/*SELECT jobvacancy_table.jv_id, jobvacancy_table.jv_title, jobfair_table.jf_status
    FROM jobvacancy_table
    LEFT JOIN jobfair_table
    ON jobvacancy_table.jv_jf_id = jobfair_table.jf_id
    WHERE jobfair_table.jf_status = '1'
    AND jv_jf_id='$jfID' AND jv_e_id = '$eID'*///ONLY GET FROM UPCOMING JOBFAIRS (0) WHICH JOBFAIR'S AND EMPLOYER'S ID ARE GIVEN
?>

