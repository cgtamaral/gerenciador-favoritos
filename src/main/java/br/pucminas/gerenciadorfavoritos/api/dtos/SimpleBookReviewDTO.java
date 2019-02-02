package br.pucminas.gerenciadorfavoritos.api.dtos;

public class SimpleBookReviewDTO {
	
	private Long userId;
	private Integer starsNumber;
	private String titleComment;
	private String comment;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
