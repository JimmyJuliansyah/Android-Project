<?php
	include 'config.php';
	
	if(isset($_POST['email']) && isset($_POST['kd_buku'])) {
		$email = $_POST['email'];
		$kd_buku = $_POST['kd_buku'];
		
		$id_user = $conn->query("SELECT id FROM user WHERE Email='$email'")->fetchColumn();
		
		$sql = "DELETE FROM history WHERE Id_user = '$id_user' AND Kd_buku IN($kd_buku)";
		
		$stmt = $conn->prepare($sql);
		if($stmt->execute())
				$result="true";
			else 
				$result="false";
			echo $result;
	}
?>