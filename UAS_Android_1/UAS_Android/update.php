<?php
	include("config.php");
	
	$email = $_POST['email'];
	$password = $_POST['password'];
	
	$sql = "UPDATE user SET Password = '$password' WHERE email = '$email'";
	
	$stmt = $conn->prepare($sql);
	
	if($stmt->execute())
		$result="true";
	else 
		$result="false";
	echo $result;
?>
