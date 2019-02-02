package br.pucminas.gerenciadorfavoritos.api.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.pucminas.gerenciadorfavoritos.api.dtos.BookReviewDTO;

@Entity
public class BookReview {

	private Long id;
	private Book book;
	private User user;
	private Integer starsNumber;
	private String titleComment;
	private String comment;
	private Calendar creationDate;


	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "starsNumber", nullable = false)
	public Integer getStarsNumber() {
		return starsNumber;
	}
	public void setStarsNumber(Integer starsNumber) {
		this.starsNumber = starsNumber;
	}
	
	@Column(name = "titleComment", nullable = false)
	public String getTitleComment() {
		return titleComment;
	}
	public void setTitleComment(String titleComment) {
		this.titleComment = titleComment;
	}
	
	@Column(name = "comment", nullable = false)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column(name = "creationdate", nullable = false)
	public Calendar getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
	
	@Transient
	public List<BookReviewDTO> convertToDTO(List<BookReview> bookReviews) 
	{
		List<BookReviewDTO> bookReviewsDTO = new ArrayList<BookReviewDTO>();
		for (BookReview bookReview : bookReviews) 
		{
			BookReviewDTO bookReviewDTO = convertToDTO(bookReview);
			bookReviewsDTO.add(bookReviewDTO);
		}
		
		return bookReviewsDTO;
	}
	
	@Transient
	public BookReviewDTO convertToDTO(BookReview bookReview) 
	{
		BookReviewDTO bookReviewDTO = new BookReviewDTO();
		bookReviewDTO.setAvaliationNumber(bookReview.getBook().getBookReviews().size() + 1);
		bookReviewDTO.setStarsNumber(bookReview.getStarsNumber());
		bookReviewDTO.setUserName(bookReview.getUser().getName());
		bookReviewDTO.setTitleComment(bookReview.getTitleComment());
		bookReviewDTO.setComment(bookReview.getComment());
		bookReviewDTO.setCreationDate(bookReview.getCreationDate());
		
		return bookReviewDTO;
	}
}
