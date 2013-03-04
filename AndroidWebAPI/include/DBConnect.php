<?php
 
class DBConnect {
 
    // Konnstruktor
    function __construct() {
 
    }
 
    // Destruktor
    function __destruct() {
        // $this->close();
    }
 
    // Mit der MySQL Datenbank verbinden
    public function connect() {
        require_once 'config.php';
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
        mysql_select_db(DB_DATABASE);

        return $con;
    }
 
    // Datenbankverbindung schließen
    public function close() {
        mysql_close();
    }
 
}
 
?>