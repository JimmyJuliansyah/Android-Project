<?php
	include 'config.php';
	
	$email = $_POST['email'];
	$id_user = $conn->query("SELECT id FROM user WHERE Email='$email'")->fetchColumn();
	
	$sql = "SELECT buku.*,Tanggal FROM buku JOIN history ON history.Kd_buku = buku.Kd_buku WHERE history.Id_user = '$id_user' ORDER BY Tanggal DESC";
	
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row['buku'] = $stmt ->fetchAll(PDO::FETCH_ASSOC);
	
	echo(json_encode($row));
	
?>