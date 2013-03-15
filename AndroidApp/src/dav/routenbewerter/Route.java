package dav.routenbewerter;

public class Route {

	int routeNumber;
	String handleColor;
	String routeDriver;
	String wallName;
	int creationDate;
	int topRobe;
	int boltRow;
	String averageRating;
	String avarageCategorie;
	Rating personalRating;
	int ratingCount;
	int flashCount;
	int redpointCount;
	int projectCount;
	int notClimbedCount;
	
		
	public Route(int routeNumber) {
		super();
		this.routeNumber = routeNumber;
		this.handleColor = null;
		this.routeDriver = null;
		this.wallName = null;
		this.creationDate = 0;
		this.topRobe = 0;
		this.averageRating = null;
		this.ratingCount = 0;
		this.avarageCategorie = null;
		this.personalRating = null;
		this.flashCount = 0;
		this.redpointCount = 0;
		this.boltRow = 0;
	}

	public Route(int routeNumber, String handleColor, String routeDriver,
			String wallName, int creationDate, int topRobe, int boltRow, String averageRating, int ratingCount, String avarageCategorie, int flashCount, int redpointCount, int projectCount, int notClimbed) {
		super();
		this.routeNumber = routeNumber;
		this.handleColor = handleColor;
		this.routeDriver = routeDriver;
		this.wallName = wallName;
		this.creationDate = creationDate;
		this.topRobe = topRobe;
		this.averageRating = averageRating;
		this.ratingCount = ratingCount;
		this.avarageCategorie = avarageCategorie;
		this.personalRating = null;
		this.flashCount = flashCount;
		this.redpointCount = redpointCount;
		this.projectCount = projectCount;
		this.notClimbedCount = notClimbed;
		this.boltRow = boltRow;
	}
	
	public int getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(int routeNumber) {
		this.routeNumber = routeNumber;
	}
	public String getHandleColor() {
		return handleColor;
	}
	public void setHandleColor(String handleColor) {
		this.handleColor = handleColor;
	}
	public String getRouteDriver() {
		return routeDriver;
	}
	public void setRouteDriver(String routeDriver) {
		this.routeDriver = routeDriver;
	}
	public String getWallName() {
		return wallName;
	}
	public void setWallName(String wallName) {
		this.wallName = wallName;
	}
	public int getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(int creationDate) {
		this.creationDate = creationDate;
	}
	public String getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(String averageRating) {
		this.averageRating = averageRating;
	}
	public int getRatingCount() {
		return ratingCount;
	}
	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}
	public String getAvarageCategorie() {
		return avarageCategorie;
	}
	public void setAvarageCategorie(String avarageCategorie) {
		this.avarageCategorie = avarageCategorie;
	}

	public int getTopRobe() {
		return topRobe;
	}

	public void setTopRobe(int topRobe) {
		this.topRobe = topRobe;
	}

	public Rating getPersonalRating() {
		return personalRating;
	}

	public void setPersonalRating(Rating personalRating) {
		this.personalRating = personalRating;
	}

	public int getFlashCount() {
		return flashCount;
	}

	public void setFlashCount(int flashCount) {
		this.flashCount = flashCount;
	}

	public int getRedpointCount() {
		return redpointCount;
	}

	public void setRedpointCount(int redpointCount) {
		this.redpointCount = redpointCount;
	}

	public int getNotClimbedCount() {
		return notClimbedCount;
	}

	public void setNotClimbedCount(int notClimbedCount) {
		this.notClimbedCount = notClimbedCount;
	}	
	
	public int getBoltRow() {
		return boltRow;
	}

	public void setBoltRow(int boltRow) {
		this.boltRow = boltRow;
	}

	public int getProjectCount() {
		return projectCount;
	}

	public void setProjectCount(int projectCount) {
		this.projectCount = projectCount;
	}
	
	
	
}
