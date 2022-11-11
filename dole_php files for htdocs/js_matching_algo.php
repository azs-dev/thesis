<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {
	
	$jsID = $_POST["js_id"];
	$jfID = $_POST["jf_id"];

	require_once 'connect.php';

	$finalskill = "";
	$finalstring = "";
	$jv_stringskills = "";
	//getting skiklls
	$js_skills_sql = "SELECT skills_table.skills, skills_table.skills_id
	FROM skills_table
	LEFT JOIN jobseeker_credentials ON skills_table.skills_id = jobseeker_credentials.jobseeker_skills_id
	WHERE jobseeker_id = '$jsID'";
	
	//getting job preferred, other skill, education
	$education_sql = "SELECT DISTINCT jobseeker_credentials.job_preferred, jobseeker_credentials.other_skill, jobseeker_credentials.education_id
	FROM jobseeker_credentials
	WHERE jobseeker_id = $jsID";

	$skills_response = mysqli_query($conn, $js_skills_sql);
	$education_response = mysqli_query($conn, $education_sql);
	/*$match_response = mysqli_query($conn, $match_sql);*/

	$result = array();
	$result['vacancies'] = array();

    if (mysqli_num_rows($education_response)) {
    	while ($row2 = mysqli_fetch_assoc($education_response)) {
    			$jobpreferred = $row2['job_preferred'];//data needed for matching
    			$otherskill = $row2['other_skill'];
    			$edlevel = $row2['education_id'];
    	   	} 
    	   	$result['userdata-success'] = "1";
    	   	

    	   	if ($otherskill != null and $jobpreferred != null) {
    	   		
    	   	//getting of skills
				if (mysqli_num_rows($skills_response) != null) {
					while ($row = mysqli_fetch_assoc($skills_response)) {
			           $finalskill = $finalskill.$row['skills_id'].","; //putting skill id to string
			           $finalstring = $finalstring.$row['skills'].",";
			       } 
			       $finalskill = substr_replace($finalskill, "", -1);
			       $finalstring = substr_replace($finalstring, "", -1);

			       $match_sql = "SELECT DISTINCT jobvacancy_table.jv_id, jobfair_table.jf_id, jobvacancy_table.jv_title, employer_table.e_cname, jobvacancy_table.jv_location, jobvacancy_skills_table.skills_id,
					skills_table.skills, jobvacancy_table.jv_skill_o
					FROM jobvacancy_table
					LEFT JOIN employer_table ON jobvacancy_table.jv_e_id = employer_table.e_id
					LEFT JOIN jobvacancy_skills_table ON  jobvacancy_skills_table.jobvacancy_id = jobvacancy_table.jv_id
				    LEFT JOIN skills_table ON jobvacancy_skills_table.skills_id = skills_table.skills_id
				    LEFT JOIN jobfair_table ON jobfair_table.jf_id = jobvacancy_table.jv_jf_id
				    WHERE jobfair_table.jf_id = '$jfID' AND jobvacancy_table.jv_education <= '$edlevel' AND jobvacancy_skills_table.skills_id IN ($finalskill) OR jobvacancy_table.jv_skill_o LIKE '%$otherskill%'
				    OR jobvacancy_table.jv_title LIKE '%$jobpreferred%'
				    GROUP BY jobvacancy_table.jv_title";

			       $match_response = mysqli_query($conn, $match_sql);

			       if (mysqli_num_rows($match_response) != null) {
			       		while ($row = mysqli_fetch_assoc($match_response)) {
			       			$index['jv_id'] = $row['jv_id'];
			       			$index['jv_title'] = $row['jv_title'];
			       			$index['e_cname'] = $row['e_cname'];
			       			$index['jv_location'] = $row['jv_location'];
			       			$index['js_skill_string'] = $finalstring;
			       			array_push($result['vacancies'], $index);
			       		}
			       		$result['matchedby'] = "skills";
			       } else{
					       	$result['result'] = "No vacancies found";
			       }
				} else { //IF DID NOT CHECK ANY IN SKILLS
			        $match_sql2 = "SELECT DISTINCT jobvacancy_table.jv_id, jobfair_table.jf_id, jobvacancy_table.jv_title, employer_table.e_cname, jobvacancy_table.jv_location,
					jobvacancy_table.jv_skill_o
					FROM jobvacancy_table
					LEFT JOIN employer_table ON jobvacancy_table.jv_e_id = employer_table.e_id
				    LEFT JOIN jobfair_table ON jobfair_table.jf_id = jobvacancy_table.jv_jf_id
				    WHERE jobfair_table.jf_id = '$jfID' AND jobvacancy_table.jv_education <= '$edlevel' AND jobvacancy_table.jv_skill_o LIKE '%$otherskill%'
				    OR jobvacancy_table.jv_title LIKE '%$jobpreferred%'
				    GROUP BY jobvacancy_table.jv_title";

				    $match_response2 = mysqli_query($conn, $match_sql2);

				    if (mysqli_num_rows($match_response2) != null) {
			       		while ($row = mysqli_fetch_assoc($match_response2)) {
			       			$index['jv_id'] = $row['jv_id'];
			       			$index['jv_title'] = $row['jv_title'];
			       			$index['e_cname'] = $row['e_cname'];
			       			$index['jv_location'] = $row['jv_location'];
			       			array_push($result['vacancies'], $index);
			       		}
			       		$result['matchedby'] = "otherskills";
			       } else {
			       		$result['result'] = "No vacancies found";
			       }

			    }
			} else{
				$match_sql3 = "SELECT DISTINCT jobvacancy_table.jv_id, jobfair_table.jf_id, jobvacancy_table.jv_title, employer_table.e_cname, jobvacancy_table.jv_location
					FROM jobvacancy_table
					LEFT JOIN employer_table ON jobvacancy_table.jv_e_id = employer_table.e_id
				    LEFT JOIN jobfair_table ON jobfair_table.jf_id = jobvacancy_table.jv_jf_id
				    WHERE jobfair_table.jf_id = '$jfID' AND jobvacancy_table.jv_education <= '$edlevel'
				    AND jobvacancy_table.jv_title LIKE '%$jobpreferred%'
				    GROUP BY jobvacancy_table.jv_title";

				$match_response3 = mysqli_query($conn, $match_sql3);

				if (mysqli_num_rows($match_response3) != null) {
					while ($row = mysqli_fetch_assoc($match_response3)) {
							$index['jv_id'] = $row['jv_id'];
			       			$index['jv_title'] = $row['jv_title'];
			       			$index['e_cname'] = $row['e_cname'];
			       			$index['jv_location'] = $row['jv_location'];
			       			array_push($result['vacancies'], $index);
		       		}
		       		$result['matchedby'] = "jobpreferred";
				} else {
					$result['result'] = "No vacancies found";
				}
			}

    	   	echo json_encode($result);
    } else{
    	$result['userdata-success'] = "0";
    	echo json_encode($result);
    	mysqli_close($conn);
    }

}
/*
			       $jv_skill_sql = "SELECT jobvacancy_table.jv_title, jobvacancy_skills_table.skills_id, skills_table.skills
					FROM jobvacancy_table
					LEFT JOIN jobvacancy_skills_table ON jobvacancy_table.jv_id = jobvacancy_skills_table.jobvacancy_id
					LEFT JOIN skills_table ON skills_table.skills_id = jobvacancy_skills_table.skills_id
					//WHERE jv_id = '$jv'";

					$jv_skill_response = mysqli_query($conn, $jv_skill_sql);

					if (mysqli_num_rows($jv_skill_response) != null) {
						while ($row3 = mysqli_fetch_assoc($jv_skill_response)) {
							$jv_stringskills = $jv_stringskills.$row3['skills'].",";
						} $jv_stringskills = substr_replace($jv_stringskills, "", -1);
					}*/
?>
