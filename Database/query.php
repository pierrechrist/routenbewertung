<?php
	mysql_connect("localhost","dav_rb","dav5471");
	mysql_select_db("dav_rb");
	
	$sql=$_REQUEST['sql'];	//SQL String aus $_GET, $_POST oder $_COOKIE holen
	$q=mysql_query("$sql");	//SQL abfrage an die Datenbank
	
	while($e=mysql_fetch_assoc($q))
			$output[]=$e;	//Zeile nach Zeile in das output Array schreiben
	 
	print(json_encode($output));	//Das Array als Json ausgeben
	 
	mysql_close();
?>