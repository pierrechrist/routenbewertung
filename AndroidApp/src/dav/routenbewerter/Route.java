package dav.routenbewerter;

public class Route {

	int routeNumber;
	String handleColor;
	String routeDriver;
	String wallName;
	String creationDate;
	int averageRating;
	char averageRatingAdd;
	int ratingCount;
	String avarageCategorie;
	Rating rating;
	
		
	public Route(int routeNumber, String handleColor, String routeDriver,
			String wallName, String creationDate, int averageRating,
			char averageRatingAdd, int ratingCount, String avarageCategorie,
			Rating rating) {
		super();
		this.routeNumber = routeNumber;
		this.handleColor = handleColor;
		this.routeDriver = routeDriver;
		this.wallName = wallName;
		this.creationDate = creationDate;
		this.averageRating = averageRating;
		this.averageRatingAdd = averageRatingAdd;
		this.ratingCount = ratingCount;
		this.avarageCategorie = avarageCategorie;
		this.rating = rating;
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
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public double getAverageRating() {
		return averageRating;
	}
	public char getAverageRatingAdd() {
		return averageRatingAdd;
	}
	public void setAverageRatingAdd(char averageRatingAdd) {
		this.averageRatingAdd = averageRatingAdd;
	}
	public void setAverageRating(int averageRating) {
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
