<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jfId = $_POST["jobfairId"];
	$pId = $_POST["partnerId"];

	require_once 'connect.php';

	$sql_count = "SELECT COUNT(js_id) AS COUNT
    FROM attendees_table
    WHERE attendees_table.p_id = '$pId' AND attendees_table.jf_id = '$jfId'";

	$sql_location = "SELECT jv_location,COUNT(DISTINCT jobseeker_id) AS COUNT
    FROM employer_scanned_table
    LEFT JOIN attendees_table ON attendees_table.jf_id = employer_scanned_table.jobfair_id
    WHERE jobfair_id = '$jfId' AND attendees_table.p_id = '$pId'
    GROUP BY jv_location";

	$sql_gender = "SELECT
    js_gender,
    COUNT(DISTINCT jobseeker_id) AS COUNT
    FROM
    employer_scanned_table
    LEFT JOIN attendees_table ON attendees_table.jf_id = employer_scanned_table.jobfair_id
    WHERE
    jobfair_id = '$jfId' AND attendees_table.p_id = '$pId'
    GROUP BY
    js_gender";

	/*$sql_hiring = "SELECT hstatus_table.hiring_status, COUNT(employer_scanned_table.hstatus_id) AS COUNT 
    FROM attendees_table 
    LEFT JOIN employer_scanned_table ON employer_scanned_table.jobseeker_id = attendees_table.js_id
    LEFT OUTER JOIN hstatus_table ON hstatus_table.hs_id = employer_scanned_table.hstatus_id
    WHERE attendees_table.p_id = '$pId' AND attendees_table.jf_id = '$jfId' 
    GROUP BY hiring_status";*/

    $sql_hiring = "SELECT s.hiring_status,
    COUNT(t.jobseeker_id) as COUNT FROM hstatus_table s
    LEFT JOIN(SELECT jobseeker_id,MIN(hstatus_id) hstatus_id FROM
    employer_scanned_table LEFT JOIN attendees_table
              ON attendees_table.jf_id = employer_scanned_table.jobfair_id
              WHERE jobfair_id = '$jfId' AND attendees_table.p_id = '$pId' GROUP BY
    jobseeker_id) t ON t.hstatus_id = s.hs_id GROUP BY s.hs_id,
    s.hiring_status;";

	$result = array();
	$result['location'] = array();
	$result['gender'] = array();
	$result['hiring'] = array();
	$result['headcount'] = array();
	$count_response = mysqli_query($conn, $sql_count);
	$location_response = mysqli_query($conn,$sql_location);
	$gender_response = mysqli_query($conn, $sql_gender);
	$hiring_response = mysqli_query($conn, $sql_hiring);

	if (!empty(mysqli_num_rows($count_response))) {

            while($row4 = mysqli_fetch_assoc($count_response)){    
                 $result['headcount'] = $row4['COUNT'];
            }   
                $result['success'] = "1";
                $result['message'] = "success";
        } else {
        		$result['location-success'] = "0";
                $result['message'] = "error";
                echo json_encode($result);
                mysqli_close($conn); 
        }


		if (!empty(mysqli_num_rows($location_response))) {

            while($row = mysqli_fetch_assoc($location_response)){
                $index['location'] = $row['jv_location'];
                $index['count'] = $row['COUNT'];
                 array_push($result['location'], $index);
            }   
 
                $result['success'] = "1";
                $result['message'] = "success";

        } else {
        		$result['location-success'] = "0";
                $result['message'] = "error";
                echo json_encode($result);
                mysqli_close($conn); 
        }

        if (!empty(mysqli_num_rows($gender_response))) {

            while($row2 = mysqli_fetch_assoc($gender_response)){
                $index2['gender'] = $row2['js_gender'];
                $index2['count'] = $row2['COUNT'];
                 array_push($result['gender'], $index2);
            }   
 
                $result['success'] = "1";
                $result['message'] = "success";
                 

        } else {
        		$result['gender-success'] = "0";
                $result['message'] = "error";
                echo json_encode($result);
                mysqli_close($conn); 
        }

        if (!empty(mysqli_num_rows($hiring_response))) {

            while($row3 = mysqli_fetch_assoc($hiring_response)){
                $index3['status'] = $row3['hiring_status'];
                $index3['count'] = $row3['COUNT'];
                 array_push($result['hiring'], $index3);
            }   
 
                $result['success'] = "1";
                $result['message'] = "success";
                echo json_encode($result);
                mysqli_close($conn); 

        } else {
        		$result['gender-success'] = "0";
                $result['message'] = "error";
                echo json_encode($result);
                mysqli_close($conn); 
        }

}

?>