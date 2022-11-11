<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	$jfId = $_POST["jobfairId"];
	$eId = $_POST["employerId"];

	require_once 'connect.php';

	$sql_count = "SELECT COUNT(jobseeker_id) AS count
	FROM employer_scanned_table
	WHERE employer_scanned_table.employer_id = '$eId' AND employer_scanned_table.jobfair_id = '$jfId'";

	$sql_location = "SELECT jv_location,COUNT(jv_location)
	FROM employer_scanned_table
	WHERE employer_id = '$eId' AND jobfair_id = '$jfId'
	GROUP BY jv_location";

	$sql_gender = "SELECT js_gender,COUNT(js_gender)
	FROM employer_scanned_table
	WHERE employer_id = '$eId' AND jobfair_id = '$jfId'
	GROUP BY js_gender";

	$sql_hiring = "SELECT hstatus_table.hiring_status, COUNT(employer_scanned_table.hstatus_id) AS count
	FROM hstatus_table
    LEFT OUTER JOIN employer_scanned_table
    ON employer_scanned_table.hstatus_id = hstatus_table.hs_id AND employer_scanned_table.employer_id = '$eId'
    AND employer_scanned_table.jobfair_id = '$jfId'
	GROUP BY hstatus_table.hiring_status";
	

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
         
                 $result['headcount'] = $row4['count'];
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
                $index['count'] = $row['COUNT(jv_location)'];
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
                $index2['count'] = $row2['COUNT(js_gender)'];
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
                $index3['count'] = $row3['count'];
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
/*$sql_hiring = "SELECT hstatus_table.hiring_status,COUNT(hstatus_id) AS count
	FROM employer_scanned_table
    LEFT JOIN hstatus_table
    ON employer_scanned_table.hstatus_id = hstatus_table.hs_id
    WHERE employer_scanned_table.employer_id = '$eId' AND employer_scanned_table.jobfair_id = '$jfId'
	GROUP BY hstatus_id";*/
?>
<?php
/*	$result = array();
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
                $index4['headcount'] = $row4['count'];
                 array_push($result['headcount'], $index4);
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
                $index['count'] = $row['COUNT(jv_location)'];
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
                $index2['count'] = $row2['COUNT(js_gender)'];
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
                $index3['count'] = $row3['count'];
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
*/
?>