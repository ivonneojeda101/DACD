package org.example.model;

import java.util.Date;

public class Session {
	Movie movie;
	Theater theater;
	Date date;

	public Session(Movie movie, Date date, Theater theater){
		this.movie = movie;
		this.date = date;
		this.theater = theater;
	}
}
