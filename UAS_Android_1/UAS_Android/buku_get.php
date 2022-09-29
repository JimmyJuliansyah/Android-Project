<?php
	include 'config.php';
	
	$kd_buku = $_POST['kd_buku'];
	
	$sql = "SELECT * FROM buku WHERE Kd_buku = '$kd_buku'";
	
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row['buku'] = $stmt ->fetchAll(PDO::FETCH_ASSOC);
	
	echo(json_encode($row));
	
?>