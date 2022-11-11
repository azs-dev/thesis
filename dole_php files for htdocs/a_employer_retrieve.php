<?php
	if ($_SERVER['REQUEST_METHOD'] == 'POST') {

	require_once 'connect.php';

	$sql = "SELECT DISTINCT employer_table.e_id, employer_table.e_cname
			FROM employer_table";

	$response = mysqli_query($conn, $sql);

	$result['employers'] = array();

	if (!empty(mysqli_num_rows($response))) {
		while ($row = mysqli_fetch_assoc($response)) {
			$index['e_id'] = $row['e_id'];
            $index['e_cname'] = $row['e_cname'];
            array_push($result['employers'], $index);   
		}
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