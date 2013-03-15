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
			(SELECT u.uiaa FROM rb_route_details LEFT JOIN tx_dihlroutes_uiaa u ON avarage_rating = u.uid  WHERE route_id = r.uid) as avarage_rating,
			(SELECT avarage_categorie FROM rb_route_details WHERE route_id = r.uid) as avarage_categorie
			FROM tx_dihlroutes_routelist r 
			LEFT JOIN tx_dihlroutes_uiaa u ON r.uiaa = u.uid 
			LEFT JOIN tx_dihlroutes_sektor s ON r.sektor = s.uid WHERE r.deleted = '0'");
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
 
}
 
?>