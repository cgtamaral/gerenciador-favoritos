package br.pucminas.gerenciadorfavoritos.api.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Book {
	
	private Long id;
	private String title;
	private String isbn;
	private List<Favorite> favorites = new ArrayList<Favorite>();
	private Integer starRating;
	private List<BookReview> bookReviews = new ArrayList<BookReview>();
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "isbn", nullable = false)
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
	@ManyToMany(mappedBy = "books")
	public List<Favorite> getFavorites() {
		return favorites;
	}
	public void setFavorites(List<Favorite> favorites) {
		this.favorites = favorites;
	}
	@Column(name = "starRating", nullable = false)
	public Integer getStarRating() {
		return starRating;
	}
	public void setStarRating(Integer starRating) {
		this.starRating = starRating;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
	public List<BookReview> getBookReviews() {
		return bookReviews;
	}
	public void setBookReviews(List<BookReview> bookReviews) {
		this.bookReviews = bookReviews;
	}

	@Transient
	public Integer countTotalStar()
	{
		Integer count = 0;
		if(getBookReviews()!=null && getBookReviews().size() > 0)
		for (BookReview review : getBookReviews()) 
		{
			count += review.getStarsNumber();
		}
		
		return count;
	}
}
