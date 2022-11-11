<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

  $jfId = $_POST['jfId'];
  $jsId = $_POST['jsId'];

  require_once 'connect.php';

    $sql = "SELECT * FROM attendees_table WHERE jf_id='$jfId' AND js_id='$jsId';";
    $response = mysqli_query($conn, $sql);


    if (empty(mysqli_num_rows($response))) {
      $result['message'] = "not going";
      $result['success'] = "1";
      echo json_encode($result);
      mysqli_close($conn);

    } else {

      $result['message'] = "going";
      $result['success'] = "1";
       echo json_encode($result);
       mysqli_close($conn);

    }
    
}
?>

