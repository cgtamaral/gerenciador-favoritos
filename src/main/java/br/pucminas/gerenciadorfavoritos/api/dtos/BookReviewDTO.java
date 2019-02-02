package br.pucminas.gerenciadorfavoritos.api.dtos;

import java.util.Calendar;

public class BookReviewDTO {

	private Integer avaliationNumber;
	private Calendar creationDate;
	private String userName;
	private Integer starsNumber;
	private String titleComment;
	private String comment;
	
	public Integer getAvaliationNumber() {
		return avaliationNumber;
	}
	public void setAvaliationNumber(Integer avaliationNumber) {
		this.avaliationNumber = avaliationNumber;
	}
	public Calendar getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getStarsNumber() {
		return starsNumber;
	}
	public void setStarsNumber(Integer starsNumber) {
		this.starsNumber = starsNumber;
	}
	public String getTitleComment() {
		return titleComment;
	}
	public void setTitleComment(String titleComment) {
		this.titleComment = titleComment;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
