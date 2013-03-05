<?php
/**
 * Datei API requests zu bearbeiten
 *
 * Jeder request wird anhand des Tags identifiziert
 * Rückgabe ist JSON
**/ 

// Überprüfen ob Tag gesetzt wurde
if (isset($_REQUEST['tag']) && $_REQUEST['tag'] != '') {
    // Tag auslesen
    $tag = $_REQUEST['tag'];
 
    // Includes
    require_once 'include/DBFunctions.php';
	require_once 'include/UserFunctions.php';
    $dbF = new DBFunctions();
	$userF = new UserFunctions();
 
    // Rückgabe Array erstellen
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // Zwichen Tags unterscheiden
    if ($tag == 'login') {
        $name = $_REQUEST['name'];
        $password = $_REQUEST['password'];
 
        // Name und Passwort in der Datenbank prüfen
        $user = $userF->getUserByNameAndPassword($name, $password);
        if ($user != false) {
            // Benutzer gefunden und Passwort richtig
            $response["success"] = 1;
			$response["user"]["uid"] = $user["uid"];
            $response["user"]["name"] = $user["user_name"];
            $response["user"]["email"] = $user["user_email"];
            $response["user"]["created_at"] = $user["crdate"];
            echo json_encode($response);
        } else {
            // Benutzer nicht gefunden oder Passwort falsch
            $response["error"] = 1;
            $response["error_msg"] = "Falscher Benutzername oder Passwort!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        $name = $_REQUEST['name'];
        $email = $_REQUEST['email'];
        $password = $_REQUEST['password'];
 
        // Prüfen ob der Benutzer schon existiert
        if ($userF->isUserExisted($name)) {
            // Benutzer existiert schon
            $response["error"] = 2;
            $response["error_msg"] = "Benutzer existiert schon";
            echo json_encode($response);
        } else {
            // Benutzer exsistiert noch nicht, also neuen anlegen
            $user = $userF->storeUser($name, $email, $password);
            if ($user) {
                // Benutzer erfolgreich angelegt
                $response["success"] = 1;
                $response["user"]["name"] = $user["user_name"];
                $response["user"]["email"] = $user["user_email"];
                $response["user"]["created_at"] = $user["crdate"];
                echo json_encode($response);
            } else {
                // Fehler beim speicher des Benutzers
                $response["error"] = 1;
                $response["error_msg"] = "Fehler beim speicher des Benutzers";
                echo json_encode($response);
            }
        }
    } else if ($tag == 'setrating') {
        $routeId = $_REQUEST['routeid'];
        $userId = $_REQUEST['userid'];
        $categorie = $_REQUEST['categorie'];
		$howClimbed = $_REQUEST['howclimbed'];
		$ratingId = $_REQUEST['rating'];
		
		// Neues Rating anlegen
		$rating = $dbF->storeRating($routeId, $userId, $categorie, $howClimbed, $ratingId);	
		if ($rating) {
			// Rating erfolgreich angelegt
			$response["success"] = 1;
			$response["rating"]["route_id"] = $rating["route_id"];
			$response["rating"]["user_id"] = $rating["user_id"];
			$response["rating"]["categorie"] = $rating["categorie"];
			$response["rating"]["howclimbed"] = $rating["howclimbed"];
			$response["rating"]["rating"] = $rating["rating"];
			$response["rating"]["created_at"] = $rating["crdate"];
			echo json_encode($response);
		} else {
                // Fehler beim speichern des Ratings
                $response["error"] = 1;
                $response["error_msg"] = "Fehler beim speichern des Ratings";
                echo json_encode($response);
        }
	} else if ($tag == 'getroutes') {
		// Routen aus der Datenbank laden
		$routes = $dbF->getRoutes();
		if ($routes) {
			// Routen erfolgreich geladen
			$response["success"] = 1;
			// Jede Route in den JSON Response schreiben
			while($route=mysql_fetch_assoc($routes))
				$response["route"][]=$route;				
			echo json_encode($response);
		} else {
			// Keine Routen gefunden
			$response["error"] = 1;
			$response["error_msg"] = "Keine Routen gefunden";
			echo json_encode($response);
		}
	} else {
        echo "Falsche Anfrage";
    }
} else {
    echo "Zugriff verweigert";
}
?>