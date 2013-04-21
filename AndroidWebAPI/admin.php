<?php

require_once 'include/DBFunctions.php';
require_once 'include/UserFunctions.php';
require_once 'include/BarcodeQR.php';

// Klassen Objekte inizialisieren
$qr = new BarcodeQR();
$dbF = new DBFunctions();
$userF = new UserFunctions();

//Prüfen ob der Benutzer Admin eingeloggt ist
session_start();
if(!session_is_registered(myusername)){
	header("location:login.php");
}

if (isset($_GET["page"])) {
	if($_GET["page"] == "routes") {
		$pageHeader = "Routen Liste";
		$results = $dbF->getRoutesWithoutCount();
		$tableHeader = "<tr><th>ID</th><th>Bewertung</th><th>Griffarbe</th><th>Datum</th><th>Routenschrauber</th><th>Wand</th><th>TopRobe</th><th>BoltRow</th><th>AdminBewertung</th><th>Kategorie</th><th>Durschnittsbewertung</th></tr>";
	} else if($_GET["page"] == "users") {
		$pageHeader = "Benutzer Liste";
		$results = $userF->getUsers();
		$tableHeader = "<tr><th>ID</th><th>Benutzername</th><th>eMail</th><th>Datum</th><th>Löschen?</th></tr>";
	} else if($_GET["page"] == "ratings") {
		$pageHeader = "Bewertungen Liste";
		$results = $dbF->getAllRatings();
		$tableHeader = "<tr><th>ID</th><th>Bewertung</th><th>wie geklettert</th><th>Kategorie</th><th>Datum</th><th>RoutenId</th><th>Benutzername</th><th>Löschen?</th></tr>";
	} else if($_GET["page"] == "route" && isset($_GET["id"])) {
		$pageHeader = "Route Nr. ".$_GET["id"];
	} else if($_GET["page"] == "functions") {
		$pageHeader = "Admin Funktionen";
	}
} else {
	$_GET["page"] = "routes";
	$pageHeader = "Routen Liste";
	$results = $dbF->getRoutesWithoutCount();
	$tableHeader = "<tr><th>ID</th><th>Bewertung</th><th>Griffarbe</th><th>Datum</th><th>Routenschrauber</th><th>Wand</th><th>TopRobe</th><th>BoltRow</th><th>AdminBewertung</th><th>Kategorie</th><th>Durschnittsbewertung</th></tr>";
}

?>
<html>
<head>
<title>DAV App Admin</title><link rel="stylesheet" type="text/css" href="css/main.css" media="screen" />
<script type="text/javascript" src="js/jquery-latest.js"></script> 
<script type="text/javascript" src="js/jquery.tablesorter.js"></script>
<script type="text/javascript" src="js/jsapi.js"></script>
<script type="text/javascript" src="js/main.js"></script> 
</head>
<body>
	<div id ="head"><h1>DAV App Admin</h1></div>
	<div id="content">
		<div id="navigation">
		<div id="naviHead">Navigation</div>
			<ul>
				<li><a href="?page=routes">Routen</a></li>
				<li><a href="?page=users">Benutzer</a></li>
				<li><a href="?page=ratings">Bewertungen</a></li>
				<li><a href="?page=functions">Funktionen</a></li>
				<li><a href="login.php?logout=true">Logout</a></li>
			</ul> 
		</div>
		<div id="page">
			<div id="pageHead"><?php echo $pageHeader; ?></div>
			<div id="pageContent">
				<?php 
					if (isset($_GET["page"])) {
						if($_GET["page"] == "route" && isset($_GET["id"])) {
							include("route.php");
						} else if($_GET["page"] == "functions") {
							?>
							<div id="blueWrapper"><form method="post" action="adminFunctions.php">
							<table border="0">
							<tr>
							<td colspan="3"><strong>Passwort ändern </strong></td>
							</tr>
							<tr>
							<td>Altes Passwort&nbsp;&nbsp;</td>
							<td><input name="oldpassword" type="password" id="oldpassword"></td>
							</tr>
							<tr>
							<td>Neues Passwort&nbsp;&nbsp;</td>
							<td><input name="newpassword1" type="password" id="newpassword1"></td>
							</tr>
							<tr>
							<td>Neues Passwort wiederholen&nbsp;&nbsp;</td>
							<td><input name="newpassword2" type="password" id="newpassword1"></td>
							</tr>
							<tr>
							<td>&nbsp;</td>
							<td><input type="submit" name="Submit" value="senden"></td>
							</tr>
							</table></form></div>
							<div id="blueWrapper"><center>Alte Ratings zu nichtmehr vorhandenen Routen löschen <button type="button" onclick="cleanDB()">DB reinigen!</button></center></div><?php
							if(isset($_GET["message"])) {
								echo $_GET["message"];
							}
						} else {
							echo "<table id='myTable' class='tablesorter'><thead>";
							echo $tableHeader."</thead><tbody>";
							while($result=mysql_fetch_assoc($results)) {
								if($_GET["page"] == "routes") {
									$result["avarage_rating"]=$dbF->getAvarageRouteRating($result["uid"]);
									$result["dateon"]=$date = gmdate("d.m.Y", $result["dateon"]);
									echo '<tr onclick="tableClick(\'route\','.$result["uid"].')">';
								} else {
									echo '<tr>';
								}
								foreach($result as $key => $value){ 
									echo '<td>'.$value.'</td>'; 							
								}
								if($_GET["page"] == "users") {
									echo '<td><img src="images/cancle.png" onclick="deleteClick(\'deleteUser\','.$result["uid"].')"></img></td></tr>';
								} else if($_GET["page"] == "ratings") {
									echo '<td><img src="images/cancle.png" onclick="deleteClick(\'deleteRating\','.$result["uid"].')"></img></td></tr>';
								}
							}
							echo "</tbody></table>";
						}
					} else {
						echo "Web Admin Seite für die DAV Routenbewerter App";
					} 
				?>
			</div>
		<div>
	</div>
</body>
</html>