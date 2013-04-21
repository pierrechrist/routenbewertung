<?php
require_once 'include/UserFunctions.php';

if(isset($_POST['myusername']) && isset($_POST['mypassword'])) {
	$userF = new UserFunctions();
	//Wenn POST Variablen vorhanden, Session anlegen und auf admin.php Seite weiterleiten
	if($_POST['myusername'] == "admin" && $userF->getUserByNameAndPassword($_POST['myusername'], $_POST['mypassword']) != false){
		session_register("myusername");
		session_register("mypassword");
		header("location:admin.php");
	}
	else {
		echo "Falscher Benutzername oder Passwort";
	}
//Prüfen ob der Benutzer Admin eingeloggt ist
} else if(isset($_GET['logout'])) {
	session_start();
	session_destroy();
}
?>
<html>
<head><title>DAV App Admin</title><link rel="stylesheet" type="text/css" href="css/main.css" media="screen" /></head>
<body>
<div id="login">
<form name="form1" method="post" action="login.php">

<table border="0">
<tr>
<td colspan="3"><strong>Admin Login </strong></td>
</tr>
<tr>
<td>Username&nbsp;&nbsp;</td>
<td><input name="myusername" type="text" id="myusername"></td>
</tr>
<tr>
<td>Password&nbsp;&nbsp;</td>
<td><input name="mypassword" type="password" id="mypassword"></td>
</tr>
<tr>
<td>&nbsp;</td>
<td><input type="submit" name="Submit" value="Login"></td>
</tr>
</table>

</form>
</div>
</body>