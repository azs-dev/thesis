<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jsId = $_POST["jobseekerId"];
    $jfId = $_POST["jobfairId"];
    $eId = $_POST["employerId"];

    require_once 'connect.php';

   $sql = "SELECT DISTINCT jobvacancy_table.jv_title, hstatus_table.hiring_status, employer_scanned_table.jv_location, employer_scanned_table.comments,
        jobseeker_table.js_first_name, jobseeker_table.js_last_name, 
        jobseeker_table.js_email, jobseeker_table.js_contactno, jobseeker_table.js_address,jobseeker_table.js_dateofbirth, 
        jobseeker_table.js_gender, jobseeker_credentials.job_preferred, jobseeker_credentials.other_skill,
        education_table.ed_level
        
        FROM jobseeker_table
        LEFT JOIN employer_scanned_table ON employer_scanned_table.jobseeker_id = jobseeker_table.js_id
        LEFT JOIN jobvacancy_table ON jobvacancy_table.jv_id = employer_scanned_table.vacancy_id
        LEFT JOIN hstatus_table ON employer_scanned_table.hstatus_id = hstatus_table.hs_id
        LEFT JOIN jobseeker_credentials ON jobseeker_table.js_id = jobseeker_credentials.jobseeker_id
        LEFT JOIN education_table ON jobseeker_credentials.education_id = education_table.ed_id
        WHERE employer_scanned_table.jobseeker_id = '$jsId' AND employer_scanned_table.employer_id = '$eId'
        AND employer_scanned_table.jobfair_id = '$jfId'";

    $skills_sql = "SELECT skills_table.skills
    FROM skills_table
    LEFT JOIN jobseeker_credentials ON skills_table.skills_id = jobseeker_credentials.jobseeker_skills_id
    WHERE jobseeker_id = '$jsId'";

    $response = mysqli_query($conn, $sql);
    $skills_response = mysqli_query($conn, $skills_sql);
  
    $result = array();
    $result['jobseeker'] = array();
    $result['skills'] = array();

    if (!empty(mysqli_num_rows($response)) ) {
        
        while($row = mysqli_fetch_assoc($response)){
            $index['jv_title'] = $row['jv_title'];
            $index['hiring_status'] = $row['hiring_status'];
            $index['jv_location'] = $row['jv_location'];
            $index['comments'] = $row['comments'];
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['js_email'] = $row['js_email'];
            $index['js_contactno'] = $row['js_contactno'];
            $index['js_address'] = $row['js_address'];
            $index['js_dateofbirth'] = $row['js_dateofbirth'];
            $index['js_gender'] = $row['js_gender'];
            $index['job_preferred'] = $row['job_preferred'];
            $index['other_skill'] = $row['other_skill'];    
            $index['ed_level'] = $row['ed_level'];

            array_push($result['jobseeker'], $index);
        }

        if (mysqli_num_rows($skills_response) != null) {
               while ($row2 = mysqli_fetch_assoc($skills_response)) {
                   $index2['js_skills'] = $row2['skills'];
                    array_push($result['skills'], $index2);
               }
               $result['skill-success'] = "1";
        } else {
            $result['skill-success'] = "no skill";
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