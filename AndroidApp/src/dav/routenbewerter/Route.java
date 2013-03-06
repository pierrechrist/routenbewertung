package dav.routenbewerter;

public class Route {

	int routeNumber;
	String handleColor;
	String routeDriver;
	String wallName;
	int creationDate;
	String averageRating;
	int ratingCount;
	String avarageCategorie;
	Rating rating;
	
		
	public Route(int routeNumber, String handleColor, String routeDriver,
			String wallName, int creationDate, String averageRating, int ratingCount, String avarageCategorie,
			Rating rating) {
		super();
		this.routeNumber = routeNumber;
		this.handleColor = handleColor;
		this.routeDriver = routeDriver;
		this.wallName = wallName;
		this.creationDate = creationDate;
		this.averageRating = averageRating;
		this.ratingCount = ratingCount;
		this.avarageCategorie = avarageCategorie;
		this.rating = rating;
	}
	
	public Route(int routeNumber, String handleColor, String routeDriver,
			String wallName, String averageRating, int creationDate) {
		super();
		this.routeNumber = routeNumber;
		this.handleColor = handleColor;
		this.routeDriver = routeDriver;
		this.wallName = wallName;
		this.averageRating = averageRating;
		this.creationDate = creationDate;
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
	
}
