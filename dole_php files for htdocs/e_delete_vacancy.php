<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {

    $jvID = $_POST["jobvacancyId"];

    require_once 'connect.php';

   $sql = "DELETE FROM jobvacancy_skills_table WHERE jobvacancy_id = '$jvID';";
   $sql2 = "DELETE FROM jobvacancy_table WHERE jv_id = '$jvID';";

   if (mysqli_query($conn, $sql)) {
        mysqli_query($conn, $sql2);
        $result['success'] = "1";
        echo json_encode($result);
        mysqli_close($conn);
    } else {
        $result['success'] = "0";
        echo json_encode($result);
        mysqli_close($conn);
    }
}
?>
