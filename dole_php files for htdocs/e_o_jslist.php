<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jfId = $_POST["jobfairId"];
	$eId = $_POST["employerId"];

	require_once 'connect.php';

	$sql = "SELECT DISTINCT jobseeker_table.js_id, jobseeker_table.js_first_name, jobseeker_table.js_last_name, hstatus_table.hiring_status, employer_scanned_table.jv_location
    FROM employer_scanned_table
    LEFT JOIN jobseeker_table ON employer_scanned_table.jobseeker_id = jobseeker_table.js_id
    LEFT JOIN hstatus_table ON hstatus_table.hs_id = employer_scanned_table.hstatus_id WHERE
    employer_scanned_table.jobfair_id = '$jfId' AND employer_scanned_table.employer_id = '$eId'";	

	$result = array();
	$result['jobseeker'] = array();
	$response = mysqli_query($conn, $sql);


    	if (!empty(mysqli_num_rows($response))) {

        while($row = mysqli_fetch_assoc($response)){
            $index['js_id'] = $row['js_id'];
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['hiring_status'] = $row['hiring_status'];
            $index['jv_location'] = $row['jv_location'];
            array_push($result['jobseeker'], $index);
            }   
 
                $result['success'] = "1";
                $result['message'] = "success";
                echo json_encode($result);
                mysqli_close($conn);    

        } else {
        		$result['success'] = "0";
                $result['message'] = "error";
                echo json_encode($result);
                mysqli_close($conn); 
        }

}
/*$sql_hiring = "SELECT hstatus_table.hiring_status,COUNT(hstatus_id) AS count
	FROM employer_scanned_table
    LEFT JOIN hstatus_table
    ON employer_scanned_table.hstatus_id = hstatus_table.hs_id
    WHERE employer_scanned_table.employer_id = '$eId' AND employer_scanned_table.jobfair_id = '$jfId'
	GROUP BY hstatus_id";*/
?>