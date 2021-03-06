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
    * Benutzer auslesen
    */
    public function getUsers() {
		$result = mysql_query("SELECT uid, user_name, user_email, crdate FROM rb_user");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
			// Benutzer zur�ckgeben
            return $result;
        } else {
            // Keine Benutzer gefunden
            return false;
        }
    }
 
    /**
    * Neuen Benutzer anlegen
    * Gibt Benutzer Details zur�ck
    */
    public function storeUser($name, $email, $password) {
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // Encrypted Passwort
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO rb_user(user_name, user_email, encrypted_password, salt) VALUES('$name', '$email', '$encrypted_password', '$salt')");
        // Auf erfolgreiches speichern pr�fen
        if ($result) {
            $uid = mysql_insert_id(); // Letzte Gespeicherte id
            $result = mysql_query("SELECT * FROM rb_user WHERE uid = $uid");
            // Benutzer Details zur�ckgeben
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
 
    /**
    * Benutzer durch Benutzername und Passwort zur�ckgeben
	* Gibt true zur�ck wenn der Benutzer existiert und das Psswort �bereinstimmt
    */
    public function getUserByNameAndPassword($name, $password) {
        $result = mysql_query("SELECT * from rb_user WHERE user_name = '$name'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // Passwort auf richtigkeit pr�fen
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
    * Verlorenes Passwort an Email Adresse senden
    */
	public function recoverPassword($name) {
        $result = mysql_query("SELECT * FROM rb_user WHERE user_name = '$name'") or die(mysql_error());
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
			$result = mysql_fetch_array($result);
			$password = substr(md5(uniqid()), 0, 8);
            $hash = $this->hashSSHA($password);
			$encrypted_password = $hash["encrypted"]; // Encrypted Passwort
			$salt = $hash["salt"]; // salt
			mysql_query("UPDATE rb_user SET encrypted_password='$encrypted_password', salt='$salt' WHERE user_name = '$name'");
			
			//Password zusenden
			$to = $result['user_email'];
			$subject = "DAV Routenbewerter Passwort";
			$message = "Mit dieser Mail erhalten sie ein vorr�bergehendes Passwort f�r die DAV Routenbewerter App\n Beim n�chsten Login werden sie dazu aufgefordert ein eigenes neues Passwort zu vergeben.\n\nIhr vorr�bergehndes Passwort lautet: $password";
			$from = "dav@soret-corp.tk";
			$headers = "From:" . $from;
			mail($to,$subject,$message,$headers);
			return true;
		} else {
            // Benutzer existiert nicht
            return false;
        }
    }
	
    /**
    * Neues Passwort setzten
    */
	public function setPassword($name, $password) {
		$hash = $this->hashSSHA($password);
		$encrypted_password = $hash["encrypted"]; // Encrypted Passwort
		$salt = $hash["salt"]; // salt
		mysql_query("UPDATE rb_user SET encrypted_password='$encrypted_password', salt='$salt' WHERE user_name = '$name'");
		return true;
    }
 
    /**
    * Passwort encrypten
    * Gibt salt und encrypted Passwort zur�ck
    */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
    * Passwort encrypten mit vorhandenem salt Wert
    * Gibt Hash String zur�ck
    */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
        return $hash;
    }
 
}
 
?>