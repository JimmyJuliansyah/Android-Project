<?php
	include 'config.php';
	
	$sql = "select * from buku";
	
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row['buku'] = $stmt ->fetchAll(PDO::FETCH_ASSOC);
	
	echo(json_encode($row));
	
?>