<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jvId = $_POST["jv_id"];

    require_once 'connect.php';

   $sql = "SELECT jobvacancy_table.jv_title, jobvacancy_table.jv_location, education_table.ed_level,
    jobvacancy_table.jv_skill_o, jobvacancy_table.jv_vacancy_count, jobvacancy_table.jv_description
    FROM jobvacancy_table
    LEFT JOIN education_table
    ON jobvacancy_table.jv_education = education_table.ed_id
    WHERE jv_id='$jvId';";

    $skills_sql ="SELECT skills_table.skills 
    FROM skills_table 
    LEFT JOIN jobvacancy_skills_table 
    ON skills_table.skills_id = jobvacancy_skills_table.skills_id 
    WHERE jobvacancy_skills_table.jobvacancy_id = '$jvId'";

    $response = mysqli_query($conn, $sql);
    $skills_response = mysqli_query($conn, $skills_sql);
  
    $result = array();
    $result['vacancy'] = array();
    $result['skills'] = array();

    if (mysqli_num_rows($response) != null) {

            while($row = mysqli_fetch_assoc($response)){
            $index['jv_title'] = $row['jv_title'];
            $index['jv_location'] = $row['jv_location'];
            $index['ed_level'] = $row['ed_level'];
            $index['jv_skill_o'] = $row['jv_skill_o'];
            $index['jv_vacancy_count'] = $row['jv_vacancy_count'];
            $index['jv_description'] = $row['jv_description'];
            array_push($result['vacancy'], $index);   
            }
            $result['success'] = "1";
            if (mysqli_num_rows($skills_response) != null) {
               while ($row2 = mysqli_fetch_assoc($skills_response)) {
                   $index2['jv_skills'] = $row2['skills'];
                    array_push($result['skills'], $index2);
               }
               $result['skill-success'] = "1";
            } else {
                $result['skill-success'] = "no skill";
            }

        echo json_encode($result);
        mysqli_close($conn);

    } else {
        $result['success'] = "0";
        echo json_encode($result);
        mysqli_close($conn);
    }
}
?>      