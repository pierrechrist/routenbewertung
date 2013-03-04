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
     * Routen auslesen
     */
    public function getRoutes() {
		$result = mysql_query("SELECT r.uid, u.uiaa, r.color, r.dateon, r.createdby, s.sektor FROM tx_dihlroutes_routelist r LEFT JOIN tx_dihlroutes_uiaa u ON r.uiaa = u.uid LEFT JOIN tx_dihlroutes_sektor s ON r.sektor = s.uid WHERE r.deleted = '0'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
			// Routen zurückgeben
            return $result;
        } else {
            // Keine Routen gefunden
            return false;
        }
    }

 
}
 
?>