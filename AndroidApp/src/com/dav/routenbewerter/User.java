package com.dav.routenbewerter;

public class User {

	String eMail;
	String UserName;
	int allRouteCount;
	int flashRouteCount;
	int rotpunktRouteCount;
	int notClimbedRouteCount;
	
	public String geteMail() {
		return eMail;
	}
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
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
