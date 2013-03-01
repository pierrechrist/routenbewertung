package dav.routenbewerter;

import java.util.List;

public class User {

	int userId;
	String eMail;
	String userName;
	String password;
	int allRouteCount;
	int flashRouteCount;
	int rotpunktRouteCount;
	int notClimbedRouteCount;
	List<Rating> ratings;
	
	
	public User(String eMail, String userName, int allRouteCount,
			int flashRouteCount, int rotpunktRouteCount,
			int notClimbedRouteCount, List<Rating> ratings) {
		super();
		this.eMail = eMail;
		this.userName = userName;
		this.allRouteCount = allRouteCount;
		this.flashRouteCount = flashRouteCount;
		this.rotpunktRouteCount = rotpunktRouteCount;
		this.notClimbedRouteCount = notClimbedRouteCount;
		this.ratings = ratings;
		this.userId = 0;
	}
	public User(String eMail, String userName, String password) {
		super();
		this.eMail = eMail;
		this.userName = userName;
		this.password = password;
		this.allRouteCount = 0;
		this.flashRouteCount = 0;
		this.rotpunktRouteCount = 0;
		this.notClimbedRouteCount = 0;
		this.ratings = null;
		this.userId = 0;
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public int getAllRouteCount() {
		return allRouteCount;
	}
	public void setAllRouteCount(int allRouteCount) {
		this.allRouteCount = allRouteCount;
	}
	public int getFlashRouteCount() {
		return flashRouteCount;
	}
	public void setFlashRouteCount(int flashRouteCount) {
		this.flashRouteCount = flashRouteCount;
	}
	public int getRotpunktRouteCount() {
		return rotpunktRouteCount;
	}
	public void setRotpunktRouteCount(int rotpunktRouteCount) {
		this.rotpunktRouteCount = rotpunktRouteCount;
	}
	public int getNotClimbedRouteCount() {
		return notClimbedRouteCount;
	}
	public void setNotClimbedRouteCount(int notClimbedRouteCount) {
		this.notClimbedRouteCount = notClimbedRouteCount;
	}
	
}
