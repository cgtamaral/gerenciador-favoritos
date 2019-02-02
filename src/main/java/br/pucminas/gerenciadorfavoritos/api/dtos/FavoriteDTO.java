package br.pucminas.gerenciadorfavoritos.api.dtos;

import java.util.List;

public class FavoriteDTO {
	
	private Long id;
	private Long userId;
	private String title;
	private String description;
	private List<BookDTO> BooksDTO;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<BookDTO> getBooksDTO() {
		return BooksDTO;
	}
	public void setBooksDTO(List<BookDTO> booksDTO) {
		BooksDTO = booksDTO;
	}
}
