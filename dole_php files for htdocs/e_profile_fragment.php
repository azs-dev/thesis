<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $eID = $_POST["e_id"];
    
    require_once 'connect.php';

    $sql = "SELECT * FROM employer_table WHERE e_id='$eID' ";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result['employer'] = array();
	if ( mysqli_num_rows($response) != null) {

		    while($row = mysqli_fetch_assoc($response)){
            $index['e_name'] = $row['e_cname'];
            $index['e_email'] = $row['e_email'];
            $index['e_address'] = $row['e_caddress'];
            $index['e_number'] = $row['e_cnumber'];
            $index['e_person'] = $row['e_cperson'];
            $index['e_valid'] = $row['e_validation'];
            $index['e_username'] = $row['e_username'];
            array_push($result['employer'], $index);
        }
            $result['success'] = "1";
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