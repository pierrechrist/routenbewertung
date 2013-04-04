<?php

class DBFunctions {
 
    private $db;
 
    // Konnstruktor
    function __construct() {
        require_once 'DBConnect.php';
        $this->db = new DBConnect();
        $this->db->connect();
    }
 
    // Destruktor
    function __destruct() {
 
    }
 
    /**
     * Neues Rating anlegen
     * Gibt Rating zurück
     */
    public function storeRating($routeId, $userId, $categorie, $howClimbed, $rating) {
		$result = mysql_query("INSERT INTO rb_ratings (route_id, user_id, categorie, howclimbed, rating) VALUES('$routeId', '$userId', '$categorie', '$howClimbed', '$rating')");
        if ($result) {
            $uid = mysql_insert_id(); // Letzte Gespeicherte id
            $result = mysql_query("SELECT * FROM rb_ratings WHERE uid = $uid");
            // Rating zurückgeben
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
	
	/**
     * Ratings auslesen
     */
    public function getRatings($userId) {
		$result = mysql_query("SELECT u.uiaa, a.howclimbed, a.categorie, a.crdate, a.route_id, a.user_id FROM rb_ratings a LEFT JOIN tx_dihlroutes_uiaa u ON a.rating = u.uid LEFT JOIN tx_dihlroutes_routelist r ON a.route_id = r.uid WHERE a.user_id = $userId AND r.deleted = 0");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
			// Ratings zurückgeben
            return $result;
        } else {
            // Keine Ratings gefunden
            return false;
        }
    }
	
	/**
     * Routen auslesen
     */
    public function getRoutes() {
		$result = mysql_query("SELECT r.uid, u.uiaa, r.color, r.dateon, r.createdby, s.sektor, r.tr, r.boltrow,
			(SELECT COUNT(*) FROM rb_ratings WHERE route_id = r.uid) as rating_count,
			(SELECT COUNT(*) FROM rb_ratings WHERE howclimbed = 'Flash' AND route_id = r.uid) as flash_count,
			(SELECT COUNT(*) FROM rb_ratings WHERE howclimbed = 'Rotpunkt' AND route_id = r.uid) as redpoint_count,
			(SELECT COUNT(*) FROM rb_ratings WHERE howclimbed = 'Projekt' AND route_id = r.uid) as project_count,
			(SELECT COUNT(*)-rating_count FROM rb_user) as not_climbed_count,
			(SELECT u.uiaa FROM rb_route_details LEFT JOIN tx_dihlroutes_uiaa u ON avarage_rating = u.uid  WHERE route_id = r.uid) as rating,
			(SELECT avarage_categorie FROM rb_route_details WHERE route_id = r.uid) as avarage_categorie
			FROM tx_dihlroutes_routelist r 
			LEFT JOIN tx_dihlroutes_uiaa u ON r.uiaa = u.uid 
			LEFT JOIN tx_dihlroutes_sektor s ON r.sektor = s.uid WHERE r.deleted = '0' AND r.pid = '853' AND r.boltrow != '0'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
			// Routen zurückgeben
            return $result;
        } else {
            // Keine Routen gefunden
            return false;
        }
    }
	
	/**
     * Das durschnitts Rating der User berechnen
     */
    public function getAvarageRouteRating($routeId) {
		$routerating = mysql_query("SELECT u.uiaa FROM tx_dihlroutes_routelist r LEFT JOIN tx_dihlroutes_uiaa u ON r.uiaa = u.uid WHERE r.uid = $routeId");
		$result = mysql_query("SELECT u.uiaa FROM rb_ratings r LEFT JOIN tx_dihlroutes_uiaa u ON r.rating = u.uid WHERE r.route_id = $routeId");
		$ratingArray = array();
		while($rating=mysql_fetch_assoc($result)) {
			array_push($ratingArray, $this->ratingStringToInt($rating));			
		}
		array_push($ratingArray, $this->ratingStringToInt(mysql_fetch_assoc($routerating)));
		foreach($ratingArray as $val) {
			$avarageRating+=$val;
		}
		$avarageRating = $avarageRating/count($ratingArray);
		return $this->roundRating($avarageRating);
	}
	
	/**
     * UiaaId auslesen
     */
    public function getUiaa($rating) {
		$result = mysql_query("SELECT uid FROM tx_dihlroutes_uiaa WHERE uiaa = '$rating'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
			// Uiaa zurückgeben
			$result = mysql_fetch_array($result);
            return $result;
        } else {
            // Keine Routen gefunden
            return false;
        }
    }
	
	/**
    * Rundet das AvarageRating
	* Gibt wieder einen Uiaa String zurück (Bsp. 2.4 wird zu 2+)
    */
	public function roundRating($x) {
		$y=$x-floor($x);
		if(($y>0)&&($y<=0.15))
			$x=$x-$y;
		else if(($y>0.15)&&($y<=0.3))
			$x=($x-$y)."+";
		else if(($y>0.3)&&($y<=0.5))
			$x=($x-$y)."+";
		else if(($y>0.5)&&($y<=0.7))
			$x=($x-$y+1)."-";
		else if(($y>0.7)&&($y<=0.85))
			$x=($x-$y+1)."-";
		else if(($y>0.85)&&($y<=1))
			$x=$x-$y+1;
		return $x;
	}
 
 	/**
    * Wandelt den Rating String in einen Int um
	* Gibt ein Int Rating zurück (Bsp. 2+ wird zu 2.3)
    */
	public function ratingStringToInt($rating) {
		if(strlen($rating["uiaa"]) == 2) {
			$number = substr($rating["uiaa"], 0, 1); 
			$add = substr($rating["uiaa"], 1, 1);
			if($add == "+")
				$number+=0.3;
			else
				$number-=0.3;
		} else if(strlen($rating["uiaa"]) == 3) {
			$number = substr($rating["uiaa"], 0, 2); 
			$add = substr($rating["uiaa"], 2, 1);
			if($add == "+")
				$number+=0.3;
			else
				$number-=0.3;
		} else {
			$number = $rating["uiaa"];
		}
		return $number;
	}
}
 
?>