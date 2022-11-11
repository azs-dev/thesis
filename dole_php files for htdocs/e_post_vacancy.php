<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jv_jf_id = $_POST["jv_jf_id"];
	$jv_e_id = $_POST["jv_e_id"];
	$jv_title = $_POST["jv_title"];
	$jv_skill_o = $_POST["jv_skill_o"];
	$jv_education = $_POST["jv_education"];
    $jv_location = $_POST["jv_location"];
	$jv_vacancy_count = $_POST["jv_vacancy_count"];
	$jv_description = $_POST["jv_description"];
    $jv_skill = $_POST["jv_skill"]; //divide string by comma

    require_once 'connect.php';

    if (empty($jv_title) || empty($jv_skill_o) || empty($jv_education) || empty($jv_vacancy_count) || empty($jv_description) || empty($jv_skill)) {

        $result['success'] = "0";
        $result['message'] = "Fields must not be empty!";
        echo json_encode($result);
        mysqli_close($conn);

    } else {
        $job_title_check = "SELECT * FROM jobvacancy_table WHERE jv_jf_id ='$jv_jf_id' AND jv_e_id = '$jv_e_id' AND jv_title = '$jv_title'";
        $job_title_response = mysqli_query($conn,$job_title_check);
        if (mysqli_num_rows($job_title_response) != null) {

            $result['success'] = "0";
            $result['message'] = "job title already exists";
            echo json_encode($result);
            mysqli_close($conn);

        } else {

            $education_sql = "SELECT ed_id FROM education_table WHERE ed_level='$jv_education'";
            $jv_edattainInt = 0;
            $response = mysqli_query($conn, $education_sql);
            if ( mysqli_num_rows($response) != null ){
                while($row = mysqli_fetch_assoc($response)){
                    $jv_edattainInt = $row['ed_id'];
                }
            }

            $sql = "INSERT INTO jobvacancy_table(jv_jf_id,jv_e_id,jv_title,jv_skill_o,jv_education,jv_location,jv_vacancy_count,jv_description) VALUES ('$jv_jf_id','$jv_e_id','$jv_title','$jv_skill_o','$jv_edattainInt','$jv_location','$jv_vacancy_count','$jv_description');";

            if(mysqli_query($conn, $sql)){
            	$result["success"] = "1";
            	$result["message"] = "success";
                echo json_encode($result);

                $skills = explode(",", $jv_skill);
                for($i = 0 ; $i < sizeof($skills);$i++){
                $skills_sql = "INSERT into jobvacancy_skills_table (jobvacancy_id,skills_id)
                    select jv_id, skills_id
                    from jobvacancy_table, skills_table
                    where jobvacancy_table.jv_title = '$jv_title'
                    and skills_table.skills = '$skills[$i]'"; //use job title job title mcannot be duplicated

                        if(mysqli_query($conn, $skills_sql)){
                            $result2["skill-message"] = "success";
                            echo json_encode($result2);
                        } else {
                            $result2["skill-message"] = "error";
                            echo json_encode($result2);
                        }
                }
                mysqli_close($conn);	
            } else {
            	$result["success"] = "0";
            	$result["message"] = "error";

            	echo json_encode($result);
            }
        }
    }
}
?>