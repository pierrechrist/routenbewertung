<?php
require_once 'include/DBFunctions.php';
require_once 'include/UserFunctions.php';

// Klassen Objekte inizialisieren
$dbF = new DBFunctions();
$userF = new UserFunctions();

//Pr�fen ob der Benutzer Admin eingeloggt ist
session_start();
if(!session_is_registered(myusername)){
	header("location:login.php");
}

//Benutzer und zugeh�rige Ratings l�schen
if($_GET["function"] == "deleteUser") {
	$dbF->deleteUser($_GET["id"]);
	header("location:admin.php?page=users");
//Rating l�schen
} else if($_GET["function"] == "deleteRating") {
	$dbF->deleteRating($_GET["id"]);
	header("location:admin.php?page=ratings");
//Datenbank von alten Ratings und Route Details reinigen
} else if($_GET["function"] == "cleanDB") {
	$rows = $dbF->cleanDB();
	header("location:admin.php?page=functions&message=$rows Zeilen gel�scht");
//Admin Passwort �ndern
} else if(isset($_POST["oldpassword"])) {
	if($_POST['oldpassword'] != "" && $_POST['newpassword1'] != "" &&  $_POST['newpassword2'] != "") {
		if($userF->getUserByNameAndPassword("admin", $_POST['oldpassword']) != false) {
			if($_POST['newpassword1'] == $_POST['newpassword2']) {
				$userF->setPassword("admin", $_POST['newpassword1']);
				header("location:admin.php?page=functions&message=Passwort ge�ndert");
			} else {
				header("location:admin.php?page=functions&message=Neues Passwort stimmt nicht �berein");
			}
		} else {
			header("location:admin.php?page=functions&message=Altes Passwort falsch");
		}
	} else {
		header("location:admin.php?page=functions&message=Nicht alle Felder ausgef�llt");
	}
} else {
	header("location:admin.php");
}

?>