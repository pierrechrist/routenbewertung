package dav.routenbewerter;

import java.util.List;

public class User {

	int userId;
	String eMail;
	String userName;
	List<Rating> ratings;
	
	
	public User(String eMail, String userName, List<Rating> ratings) {
		super();
		this.eMail = eMail;
		this.userName = userName;
		this.ratings = ratings;
		this.userId = 0;
	}
	
	public User(int userId, String eMail, String userName) {
		super();
		this.userId = userId;
		this.eMail = eMail;
		this.userName = userName;
		this.ratings = null;
	}
	
	public User(int userId) {
		super();
		this.userId = userId;
		this.eMail = null;
		this.userName = null;
		this.ratings = null;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
