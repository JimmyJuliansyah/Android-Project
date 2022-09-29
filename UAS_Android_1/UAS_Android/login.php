<?php
	include 'config.php';
	if(isset($_POST['email']) && isset($_POST['password'])) {
		$result = '';
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		$sql = 'SELECT * FROM user WHERE Email=:email AND Password=:password';
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':email'=>$email, ':password'=>$password));
		
		if($stmt->rowCount()){
			$result="true";
		} elseif(!$stmt->rowCount()){
			$result="false";
		}
		echo $result;
	}
?>