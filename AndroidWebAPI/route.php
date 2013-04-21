<?php 

//Admin Rating und Kategorie setzen wenn die POST Variablen vorhanden sind
if(isset($_POST["rating"]) && isset($_POST["categorie"])) {
	$dbF->setRatingAndCategorie($_POST["rating"], $_POST["categorie"], $_GET["id"]);
}

//Route aus der Datenbank laden
$result = $dbF->getRoute($_GET["id"]);

//Unix Timestamp in datum umwandeln
$date = gmdate("d.m.Y", $result["dateon"]);

//Wenn Admin Rating vorhanden, diese für das Image verweden, ansonsten das Rating mit dem die Route erstellt wurde
if($result["rating"] != null) {
	$rating = $result["rating"];
} else {
	$rating = $result["uiaa"];
}

//QR Code Text festlegen und zeichenen
$qr->text("DAV ".$result["uid"]); 
$qr->draw(180, "images/qr-code.png");

//Hintergrund und QR Code Image einlesen
$im = @imagecreatefrompng("images/background.png");
$grImg = @imagecreatefrompng("images/qr-code.png");

//QR Code Image auf den Hintergrund zeichenen
imagecopy($im, $grImg, 5, 5, 0, 0, 180, 180);

//Farben und Schriftart festlegen
$grey = imagecolorallocate($im, 128, 128, 128);
$black = imagecolorallocate($im, 0, 0, 0);
$font = 'fonts/arial.ttf';

//Text auf den Hintergrund zeichnen
imagettftext($im, 20, 0, 240, 50, $black, $font, "Farbe: ".$result["color"]);
imagettftext($im, 30, 0, 240, 100, $black, $font, "Grad: ".$rating);
imagettftext($im, 15, 0, 25, 210, $black, $font, "Schrauber: ".$result["createdby"]);
imagettftext($im, 15, 0, 25, 240, $black, $font, "Datum: ".$date);

//Image las tmp.png abspeichern um es auf der Routenseite darzustellen
imagepng($im, "images/tmp.png");
imagedestroy($im);
imagedestroy($qr);

echo "<table id='infoTable'>";
echo "<tr><th>Routen ID:</th><td>".$result["uid"]."</td><th>Anzahl Bewertungen:</th><td>".$result["rating_count"]."</td></tr>";
echo "<tr><th>BoltRow:</th><td>".$result["boltrow"]."</td><th>Anzahl Flash:</th><td id='flash'>".$result["flash_count"]."</td></tr>";
echo "<tr><th>Bewertung:</th><td>".$result["uiaa"]."</td><th>Anzahl Rotpunkt:</th><td id='redpoint'>".$result["redpoint_count"]."</td></tr>";
echo "<tr><th>Griffarbe:</th><td>".$result["color"]."</td><th>Anzahl Projekt:</th><td id='project'>".$result["project_count"]."</td></tr>";
echo "<tr><th>Routenschrauber</th><td>".$result["createdby"]."</td><th>Benutzer nicht bewertet:</th><td>".$result["not_climbed_count"]."</td></tr>";
echo "<tr><th>Datum:</th><td>".$date."</td><th>Festgelegte Bewertung:</th><td>".$result["rating"]."</td></tr>";
echo "<tr><th>Wand:</th><td>".$result["sektor"]."</td><th>Festgelegte Kategorie:</th><td>".$result["avarage_categorie"]."</td></tr>";
echo "<tr><th>TopRobe?</th><td>".$result["tr"]."</td><th>Durschnitts Bewertung:</th><td>".$dbF->getAvarageRouteRating($result["uid"])."</td></tr>";
echo "</table>";

echo '<div id="imgHolder"><div id="chart_div"></div>';
echo '<div id="print"><img src="images/tmp.png" id="printI" onclick="printImg()"></div></div>';
echo '<div id="routeForm"><form action="admin.php?page=route&id='.$result["uid"].'" method="post">';
?>
Bewertung: <select name="rating">
  <option>2+</option>
  <option>3-</option>
  <option>3</option>
  <option>3+</option>
  <option>4-</option>
  <option>4</option>
  <option>4+</option>
  <option>5-</option>
  <option>5</option>
  <option>5+</option>
  <option>6-</option>
  <option>6</option>
  <option>6+</option>
  <option>7-</option>
  <option>7</option>
  <option>7+</option>
  <option>8-</option>
  <option>8</option>
  <option>8+</option>
  <option>9-</option>
  <option>9</option>
  <option>9+</option>
  <option>10-</option>
  <option>10</option>
  <option>10+</option>
  <option>11-</option>
</select>
Kategorie: <select name="categorie">
  <option>Fingerkraft</option>
  <option>Ausdauer</option>
  <option>Technik</option>
  <option>Kraft</option>
</select>
<input type="submit" value="Submit">
</form></div>

<?php
$results = $dbF->getRouteRatings($_GET["id"]);
echo "<table id='myTable' class='tablesorter'><thead><tr><th>ID</th><th>Bewertung</th><th>wie geklettert</th><th>Kategorie</th><th>Datum</th><th>RoutenId</th><th>Benutzername</th><th>Löschen?</th></tr></thead><tbody>";
while($result=mysql_fetch_assoc($results)) {
		echo '<tr>';
	foreach($result as $key => $value){ 
		echo '<td>'.$value.'</td>'; 							
	}
	echo '<td><img src="images/cancle.png" onclick="deleteClick(\'deleteRating\','.$result["uid"].')"></img></td></tr>';
}
echo "</tbody></table>";
?>