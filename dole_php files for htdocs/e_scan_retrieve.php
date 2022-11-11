<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jsID = $_POST["jobseekerId"];
    $jfID = $_POST["jobfairId"];
    $eID = $_POST["employerId"];
    
    require_once 'connect.php';

    $sql = "SELECT DISTINCT jobseeker_table.js_username, jobseeker_table.js_first_name, jobseeker_table.js_last_name, 
        jobseeker_table.js_email, jobseeker_table.js_contactno, jobseeker_table.js_address,jobseeker_table.js_dateofbirth, 
        jobseeker_table.js_gender, jobseeker_credentials.job_preferred, jobseeker_credentials.other_skill,
        education_table.ed_level
        
        FROM attendees_table
        LEFT JOIN jobseeker_table ON attendees_table.js_id = jobseeker_table.js_id
        LEFT JOIN jobseeker_credentials ON jobseeker_table.js_id = jobseeker_credentials.jobseeker_id
        LEFT JOIN education_table ON jobseeker_credentials.education_id = education_table.ed_id
        WHERE attendees_table.at_attend = '1' AND  attendees_table.js_id = '$jsID' AND  attendees_table.jf_id = '$jfID'";

    $skills_sql = "SELECT skills_table.skills
        FROM skills_table
        LEFT JOIN jobseeker_credentials ON skills_table.skills_id = jobseeker_credentials.jobseeker_skills_id
        WHERE jobseeker_id = '$jsID'";

    $hstatus_sql = "SELECT * FROM hstatus_table";

    $e_vacancies_sql = "SELECT jv_id, jv_title
    FROM jobvacancy_table
    WHERE jv_jf_id = '$jfID'
    AND jv_e_id = '$eID'";

    $skills_response = mysqli_query($conn, $skills_sql);
    $response = mysqli_query($conn, $sql);

    $hstatus_response = mysqli_query($conn, $hstatus_sql);
    $vacanies_response = mysqli_query($conn, $e_vacancies_sql);

    $result = array();
    $result['jobseeker'] = array();
    $result['skills'] = array();
    $result['hstatus'] = array();
    $result['vacancies'] = array();
    
	if (!empty(mysqli_num_rows($response))) {
            //jobseeker data
		    while($row = mysqli_fetch_assoc($response)){
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['js_email'] = $row['js_email'];
            $index['js_contactno'] = $row['js_contactno'];
            $index['js_address'] = $row['js_address'];
            $index['js_dateofbirth'] = $row['js_dateofbirth'];
            $index['js_gender'] = $row['js_gender'];
            $index['js_jobpreferred'] = $row['job_preferred'];
            $index['js_o_skill'] = $row['other_skill'];
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
           

            //hiring status
            if (mysqli_num_rows($hstatus_response) != null) {

                while($row = mysqli_fetch_assoc($hstatus_response)){
                    $index3[$row['hs_id']] = $row['hiring_status'];
                }
                array_push($result['hstatus'], $index3);

            }

            //for vacancies
            if (mysqli_num_rows($vacanies_response) != null) {

                while($row = mysqli_fetch_assoc($vacanies_response)){
                $index4['jv_id'] = $row['jv_id'];
                $index4['jv_title'] = $row['jv_title'];
                array_push($result['vacancies'], $index4);
                }
                $result['vacancy-success'] = "1";
            } else {
                $result['vacancy-success'] = "0";  
            }
	        $result['success'] = "1";
	        echo json_encode($result);
	        mysqli_close($conn);
	}
	else {
    	    $result['success'] = "0";
    	    $result['skill-success'] = "0";
    	    $result['vacancy-success'] = "0";  
            echo json_encode($result);
            mysqli_close($conn);
    }


   

}
/*SELECT DISTINCT jobseeker_table.js_username, jobseeker_table.js_first_name, jobseeker_table.js_last_name, 
        jobseeker_table.js_email, jobseeker_table.js_contactno, jobseeker_table.js_address,jobseeker_table.js_dateofbirth, jobseeker_credentials.job_preferred, jobseeker_credentials.other_skill, jobseeker_table.js_gender,
        education_table.ed_level
        
        FROM jobseeker_table
        LEFT JOIN jobseeker_credentials ON jobseeker_table.js_id = jobseeker_credentials.jobseeker_id
        LEFT JOIN education_table ON jobseeker_credentials.education_id = education_table.ed_id*/

?>

