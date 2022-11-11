<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jfId = $_POST["jobfairId"];
	$pId = $_POST["partnerId"];

	require_once 'connect.php';

    	$sql = "SELECT DISTINCT jobseeker_table.js_id, jobseeker_table.js_first_name, jobseeker_table.js_last_name, jobseeker_table.js_gender
        FROM attendees_table
        LEFT JOIN jobseeker_table ON attendees_table.js_id = jobseeker_table.js_id
        WHERE attendees_table.jf_id = '$jfId' AND attendees_table.p_id = '$pId'";	

        $sql_count = "SELECT COUNT(DISTINCT js_id) AS COUNT
        FROM attendees_table WHERE p_id = '$pId' AND jf_id = '$jfId'";

	$result = array();
	$result['jobseeker'] = array();
	$response = mysqli_query($conn, $sql);
    $response_check = mysqli_query($conn, $sql_count);
    $totalcount = 0;

    if (!empty(mysqli_num_rows($response_check))) {
        while ($row = mysqli_fetch_assoc($response_check)) {
            $index['p_scanned'] = $row['COUNT'];
            $totalcount = $index['p_scanned'];
        }
        $result['p_scanned'] = $totalcount;
    }

    	if (!empty(mysqli_num_rows($response))) {

        while($row = mysqli_fetch_assoc($response)){
            $index['js_id'] = $row['js_id'];
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['js_gender'] = $row['js_gender'];
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