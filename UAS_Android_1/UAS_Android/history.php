<?php
	include("config.php");
	
	if(isset($_POST['email']) && isset($_POST['kd_buku'])) {
		$kd_buku = $_POST['kd_buku'];
		$email = $_POST['email'];
		
		$id_user = $conn->query("SELECT id FROM user WHERE Email='$email'")->fetchColumn();
	
		$sql = "SELECT * FROM history WHERE Id_user = '$id_user' AND Kd_buku = '$kd_buku'";
		$stmt = $conn->prepare($sql);
		$stmt->execute();
		
		if($stmt->rowCount()){
			$sql ="UPDATE history SET Tanggal = now() WHERE Id_user = '$id_user' AND Kd_buku = '$kd_buku'";
		}else{
			$sql = "INSERT INTO history(Id_user, Kd_buku, Tanggal)VALUES('$id_user','$kd_buku',now())";
		}
		
		$stmt = $conn->prepare($sql);
		if($stmt->execute())
			$result="true";
		else 
			$result="false";
		echo $result;
	}
	
?>