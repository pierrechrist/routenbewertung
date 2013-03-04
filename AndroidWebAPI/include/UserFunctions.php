<?php
 
class UserFunctions {
 
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
     * Neuen Benutzer anlegen
     * Gibt Benutzer Details zurück
     */
    public function storeUser($name, $email, $password) {
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // Encrypted Passwort
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO rb_user(user_name, user_email, encrypted_password, salt) VALUES('$name', '$email', '$encrypted_password', '$salt')");
        // Auf erfolgreiches speichern prüfen
        if ($result) {
            $uid = mysql_insert_id(); // Letzte Gespeicherte id
            $result = mysql_query("SELECT * FROM rb_user WHERE uid = $uid");
            // Benutzer Details zurückgeben
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
 
    /**
     * Benutzer durch Email und Passwort zurückgeben
     */
    public function getUserByNameAndPassword($name, $password) {
        $result = mysql_query("SELECT * from rb_user WHERE user_name = '$name'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // Passwort auf richtigkeit prüfen
            if ($encrypted_password == $hash) {
                // Benutzer autentifizierung erfolgreich
                return $result;
            }
        } else {
            // Benutzer nicht gefunden
            return false;
        }
    }
 
    /**
     * Testen ob es den gesuchten Benutzer gibt
     */
    public function isUserExisted($name) {
        $result = mysql_query("SELECT user_name from rb_user WHERE user_name = '$name'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // Benutzer existiert
            return true;
        } else {
            // Benutzer existiert nicht
            return false;
        }
    }
 
    /**
     * Passwort encrypten
     * Gibt salt und encrypted Passwort zurück
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Passwort decrypten
     * Gibt Hash String zurück
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        return $hash;
    }
 
}
 
?>