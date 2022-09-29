<?php
	include("config.php");
	
	$name = $_POST['name'];
	$email = $_POST['email'];
	$password = $_POST['password'];
	
	$sql = 'INSERT INTO user(Nama,Email,Password)VALUES(:name,:email,:password)';
	$stmt = $conn->prepare($sql);
	
	if($stmt->execute(array(':name'=>$name, ':email'=>$email,':password'=>$password)))
		$result="true";
	else 
		$result="false";
	echo $result;
?>