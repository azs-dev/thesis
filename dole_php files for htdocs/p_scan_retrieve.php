<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jsID = $_POST["jobseekerId"];
    $jfID = $_POST["jobfairId"];

    require_once 'connect.php';

    $sql = "SELECT DISTINCT jobseeker_table.js_first_name, jobseeker_table.js_last_name, jobseeker_table.js_address,jobseeker_table.js_dateofbirth, 
        jobseeker_table.js_gender, attendees_table.at_attend
        
        FROM attendees_table
        LEFT JOIN jobseeker_table ON attendees_table.js_id = jobseeker_table.js_id
        WHERE attendees_table.js_id = '$jsID' AND attendees_table.jf_id = '$jfID';";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['jobseeker'] = array();
    
	if (!empty(mysqli_num_rows($response))) {
		    while($row = mysqli_fetch_assoc($response)){
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['js_address'] = $row['js_address'];
            $index['js_dateofbirth'] = $row['js_dateofbirth'];
            $index['js_gender'] = $row['js_gender'];
            array_push($result['jobseeker'], $index);
            $result['attend'] = $row['at_attend'];   
            }
            
	        $result['success'] = "1";
	        echo json_encode($result);
	        mysqli_close($conn);
	}
	else {
        $result['attend'] = "0";
	    $result['success'] = "0";  
        echo json_encode($result);
        mysqli_close($conn);
    }

}
?>

