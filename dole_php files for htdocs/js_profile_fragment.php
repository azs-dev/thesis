<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jsID = $_POST["js_id"];
    
    require_once 'connect.php';

    $sql = "SELECT DISTINCT jobseeker_table.js_username, jobseeker_table.js_first_name, jobseeker_table.js_last_name, 
        jobseeker_table.js_email, jobseeker_table.js_contactno, jobseeker_table.js_address,jobseeker_table.js_dateofbirth, jobseeker_credentials.job_preferred, jobseeker_credentials.other_skill,
        education_table.ed_level
        
        FROM jobseeker_table
        LEFT JOIN jobseeker_credentials ON jobseeker_table.js_id = jobseeker_credentials.jobseeker_id
        LEFT JOIN education_table ON jobseeker_credentials.education_id = education_table.ed_id
        WHERE js_id ='$jsID'";

    $skills_sql = "SELECT skills_table.skills
        FROM skills_table
        LEFT JOIN jobseeker_credentials ON skills_table.skills_id = jobseeker_credentials.jobseeker_skills_id
        WHERE jobseeker_id = '$jsID'";

    $skills_response = mysqli_query($conn, $skills_sql);
    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['jobseeker'] = array();
    $result['skills'] = array();
	if (mysqli_num_rows($response) != null) {

		    while($row = mysqli_fetch_assoc($response)){
            $index['js_username'] = $row['js_username'];
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['js_email'] = $row['js_email'];
            $index['js_contactno'] = $row['js_contactno'];
            $index['js_address'] = $row['js_address'];
            $index['js_dateofbirth'] = $row['js_dateofbirth'];
            $index['js_jobpreferred'] = $row['job_preferred'];
            $index['js_o_skill'] = $row['other_skill'];
            $index['ed_level'] = $row['ed_level'];
            array_push($result['jobseeker'], $index);   
            }
            $result['success'] = "1";
            if (mysqli_num_rows($skills_response) != null) {
               while ($row2 = mysqli_fetch_assoc($skills_response)) {
                   $index2['js_skills'] = $row2['skills'];
                    array_push($result['skills'], $index2);
               }
               $result['skill-success'] = "1";
            } else {
                $result['skill-success'] = "no skill";
            }

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