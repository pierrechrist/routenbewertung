package dav.routenbewerter;

import java.util.Date;

public class Rating {
	String rating;
	String howClimbed;
	String categorie;
	Date date;
	User user;
	Route route;
	boolean sent;
	
	
	public Rating(String rating, String howClimbed, String categorie,
			Date date, User user, Route route, boolean sent) {
		super();
		this.rating = rating;
		this.howClimbed = howClimbed;
		this.categorie = categorie;
		this.date = date;
		this.user = user;
		this.route = route;
		this.sent = sent;
	}
	
	public Rating(Route route, User user) {
		super();
		this.rating = null;
		this.howClimbed = null;
		this.categorie = null;
		this.date = null;
		this.user = user;
		this.route = route;
		this.sent = false;
	}
	
	public Rating(User user) {
		super();
		this.rating = null;
		this.howClimbed = null;
		this.categorie = null;
		this.date = null;
		this.user = user;
		this.route = null;
		this.sent = false;
	}
	
	public Rating(String howClimbed, User user) {
		super();
		this.rating = null;
		this.howClimbed = howClimbed;
		this.categorie = null;
		this.date = null;
		this.user = user;
		this.route = null;
		this.sent = false;
	}
	
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getHowClimbed() {
		return howClimbed;
	}
	public void setHowClimbed(String howClimbed) {
		this.howClimbed = howClimbed;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
}
