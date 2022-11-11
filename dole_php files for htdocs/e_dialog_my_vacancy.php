<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jvID = $_POST["jobvacancyId"];

    require_once 'connect.php';

   $sql = "SELECT jobvacancy_table.jv_title, jobvacancy_table.jv_location, education_table.ed_level, jobvacancy_table.jv_vacancy_count,
    jobvacancy_table.jv_description
    FROM jobvacancy_table
    LEFT JOIN education_table
    ON  jobvacancy_table.jv_education = education_table.ed_id
    WHERE jv_id ='$jvID';";

    $response = mysqli_query($conn, $sql);
  
    $result = array();
    $result['vacancies'] = array();

    if (!empty(mysqli_num_rows($response)) ) {
        
        while($row = mysqli_fetch_assoc($response)){
            $index['jv_title'] = $row['jv_title'];
            $index['jv_location'] = $row['jv_location'];
            $index['jv_education'] = $row['ed_level'];
            $index['jv_vacancy_count'] = $row['jv_vacancy_count'];
            $index['jv_description'] = $row['jv_description'];
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
?>      