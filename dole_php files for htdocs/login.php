<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $username = $_POST['username'];
    $password = $_POST['password'];

    require_once 'connect.php';

   $sql = "SELECT * FROM jobseeker_table WHERE js_username='$username' ";
   $sql2 = "SELECT * FROM employer_table WHERE e_username='$username' ";
   $sql3 = "SELECT * FROM partner_table WHERE p_username='$username' ";
   $sql4 = "SELECT * FROM admin_table WHERE a_username='$username' ";

    $response = mysqli_query($conn, $sql);
    $response2 = mysqli_query($conn, $sql2);
    $response3 = mysqli_query($conn, $sql3);
    $response4 = mysqli_query($conn, $sql4);

    $result = array();
    $result['login'] = array();
    //FOR JOBSEEKER
    if ( mysqli_num_rows($response) === 1 ) {
        
        $row = mysqli_fetch_assoc($response);

        if ( password_verify($password, $row['js_password']) ) {
            
            $index['js_first_name'] = $row['js_first_name'];
            $index['js_last_name'] = $row['js_last_name'];
            $index['js_email'] = $row['js_email'];
            $index['js_id'] = $row['js_id'];
            array_push($result['login'], $index);

            $result['success'] = "1";
            $result['message'] = "success";
            $result['user'] = "jobseeker";
            echo json_encode($result);

            mysqli_close($conn);

        } else {

            $result['success'] = "0";
            $result['message'] = "error";
            $result['user'] = "jobseeker";
            echo json_encode($result);

            mysqli_close($conn);

        }
        //FOR EMPLOYER
    } else if(mysqli_num_rows($response2) === 1 ) {

    	$row = mysqli_fetch_assoc($response2);

    	if (password_verify($password, $row['e_password'])) {
    		$index['e_cname'] = $row['e_cname'];
            $index['e_email'] = $row['e_email'];
            $index['e_id'] = $row['e_id'];
            $index['e_cnumber'] = $row['e_cnumber'];
            $index['e_validation'] = $row['e_validation'];

    		array_push($result['login'], $index);

    		$result['success'] = "1";
    		$result['message'] = "success";
    		$result['user'] = "employer";
    		echo json_encode($result);

    		mysqli_close($conn);
    	} else {
    		$result['success'] = "0";
            $result['message'] = "error";
            $result['user'] = "employer";
            echo json_encode($result);

            mysqli_close($conn);
    	}
    	//FOR PARTNER
    } else if(mysqli_num_rows($response3) === 1 ) {

    	$row = mysqli_fetch_assoc($response3);

    	if (password_verify($password, $row['p_password'])) {
    		$index['p_name'] = $row['p_name'];
    		$index['p_id'] =  $row['p_id'];

    		array_push($result['login'], $index);

    		$result['success'] = "1";
    		$result['message'] = "success";
    		$result['user'] = "partner";
    		echo json_encode($result);

    		mysqli_close($conn);
    	} else {
    		$result['success'] = "0";
            $result['message'] = "error";
            $result['user'] = "partner";
            echo json_encode($result);

            mysqli_close($conn);
    	}

    }else if (mysqli_num_rows($response4) ===1){

    	$row = mysqli_fetch_assoc($response4);

    	if (password_verify($password, $row['a_password'])) {
    		$index['a_name'] = $row['a_name'];

    		array_push($result['login'], $index);

    		$result['success'] = "1";
    		$result['message'] = "success";
    		$result['user'] = "admin";
    		echo json_encode($result);

    		mysqli_close($conn);
    	} else {
    		$result['success'] = "0";
            $result['message'] = "error";
            $result['user'] = "admin";
            echo json_encode($result);

            mysqli_close($conn);
    	}

    } else {
    	$result['success'] = "0";
    	$result['message'] = "error"; //num_rows = 0
    	echo json_encode($result);
    }

}

?>