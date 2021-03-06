<?php
/**
 * Android App API
 * Jeder request wird anhand des Tags identifiziert
 * R�ckgabe ist JSON
 * Wenn kein Tag gesetzt wurde wird an die admin.php Seite weitergeleitet
**/ 

// �berpr�fen ob Tag gesetzt wurde
if (isset($_REQUEST['tag']) && $_REQUEST['tag'] != '') {
    // Tag auslesen
    $tag = $_REQUEST['tag'];
 
    // Includes
    require_once 'include/DBFunctions.php';
	require_once 'include/UserFunctions.php';
    $dbF = new DBFunctions();
	$userF = new UserFunctions();
 
    // R�ckgabe Array erstellen
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // Zwichen Tags unterscheiden
    if ($tag == 'login') {
        $name = $_REQUEST['name'];
        $password = $_REQUEST['password'];
 
        // Name und Passwort in der Datenbank pr�fen
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
 
        // Pr�fen ob der Benutzer schon existiert
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
	} else if ($tag == 'getratings') {
		$userId = $_REQUEST['userid'];
	
		// Ratings aus der Datenbank laden
		$ratings = $dbF->getRatings($userId);
		if ($ratings) {
			// Ratings erfolgreich geladen
			$response["success"] = 1;
			// Jedes Rating in den JSON Response schreiben
			while($rating=mysql_fetch_assoc($ratings))
				$response["rating"][]=$rating;				
			echo json_encode($response);
		} else {
			// Keine Ratings gefunden
			$response["error"] = 1;
			$response["error_msg"] = "Keine Ratings gefunden";
			echo json_encode($response);
		}
	} else if ($tag == 'getroutes') {
		$timestamp = $_REQUEST['timestamp'];
	
		// Routen aus der Datenbank laden
		$routes = $dbF->getRoutes();
		if ($routes) {
			// Routen erfolgreich geladen
			$response["success"] = 1;
			// Jede Route in den JSON Response schreiben
			while($route=mysql_fetch_assoc($routes)) {
				$ratingTimestamp = $dbF->getNewestRouteRatingTimestamp($route["uid"]);
				//echo strtotime($ratingTimestamp['crdate']);
				if($ratingTimestamp == false) {
					if($timestamp < $route["dateon"]){
						$route["avarage_rating"]=$dbF->getAvarageRouteRating($route["uid"]);
						$route["color"]=utf8_encode($route["color"]);
						$response["route"][]=$route;
					}
				} else {
					if($timestamp < strtotime($ratingTimestamp['crdate'])){
						$route["avarage_rating"]=$dbF->getAvarageRouteRating($route["uid"]);
						$route["color"]=utf8_encode($route["color"]);
						$response["route"][]=$route;
					}
				}
			}				
			echo json_encode($response);
		} else {
			// Keine Routen gefunden
			$response["error"] = 1;
			$response["error_msg"] = "Keine Routen gefunden";
			echo json_encode($response);
		}
	} else if ($tag == 'getuiaa') {
		$rating = $_REQUEST['rating'];
		// Routen aus der Datenbank laden
		$uiaa = $dbF->getUiaa($rating);
		if ($uiaa != false) {
			// Routen erfolgreich geladen
			$response["success"] = 1;
			// Uiaa in den JSON Response schreiben
			$response["uiaa"]=$uiaa["uid"];				
			echo json_encode($response);
		} else {
			// Keine Uiaa gefunden
			$response["error"] = 1;
			$response["error_msg"] = "Keine Uiaa gefunden";
			echo json_encode($response);
		}
	} else if ($tag == 'recoverpassword') {
		$name = $_REQUEST['name'];
		// Benutzer in der Datenbank suchen und random Passwort zusenden
		$user = $userF->recoverPassword($name);
		if ($user != false) {
			// Passwort erfolgreich zugesendet
			$response["success"] = 1;			
			echo json_encode($response);
		} else {
			// Benutzer nicht gefunden
			$response["error"] = 1;
			$response["error_msg"] = "Benutzer nicht gefunden";
			echo json_encode($response);
		}
	} else if ($tag == 'setuserpassword') {
		$name = $_REQUEST['name'];
		$password = $_REQUEST['password'];
		// Benutzer in der Datenbank suchen und neues Passwort setzten
		$user = $userF->setPassword($name, $password);
		if ($user != false) {
		// Passwort erfolgreich gesetzt
		$response["success"] = 1;	
		echo json_encode($response);
		} else {
		// Benutzer nicht gefunden
		$response["error"] = 1;
		$response["error_msg"] = "Fehler beim setzen des neuen Passworts";
		echo json_encode($response);
		}
	} else {
        echo "Falsche Anfrage";
    }
} else {
    header("location:admin.php");
}
?>