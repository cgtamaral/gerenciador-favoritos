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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import br.pucminas.gerenciadorfavoritos.api.dtos.BookDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.FavoriteDTO;
import br.pucminas.gerenciadorfavoritos.api.dtos.SimpleFavoriteDTO;

@Entity
public class Favorite {
	
	private Long id;
	private User user;
	private String title;
	private String description;
	private List<Book> books;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "title", nullable = false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "description", nullable = false)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "Favorite_Book", joinColumns = {@JoinColumn(name = "favorite_id") }, inverseJoinColumns = {@JoinColumn(name = "book_id") })
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}

	@Transient
	public static FavoriteDTO convertToDTOWithBooks(Favorite favorite) 
	{
		FavoriteDTO favoriteDTO = new FavoriteDTO();
		favoriteDTO.setId(favorite.getId());
		favoriteDTO.setTitle(favorite.getTitle());
		favoriteDTO.setDescription(favorite.getDescription());
		favoriteDTO.setUserId(favorite.getUser().getId());
		
		List<Book> books = favorite.getBooks();
		if(books!=null && books.size()>0)
		{
			List<BookDTO> booksDTO = new ArrayList<BookDTO>();
			for (Book book : books)
			{
				BookDTO bookDTO = new BookDTO();
				//bookDTO.setId(book.getId());
				bookDTO.setTitle(book.getTitle());
				bookDTO.setIsbn_10(book.getIsbn());
				
				booksDTO.add(bookDTO);
			}
			
			favoriteDTO.setBooksDTO(booksDTO);
		}
		return favoriteDTO;
	}
	
	@Transient
	public static SimpleFavoriteDTO convertToDTOWithoutBooks(Favorite favorite) 
	{
		SimpleFavoriteDTO simpleFavoriteDTO = new SimpleFavoriteDTO();
		simpleFavoriteDTO.setId(favorite.getId());
		simpleFavoriteDTO.setTitle(favorite.getTitle());
		simpleFavoriteDTO.setDescription(favorite.getDescription());
		simpleFavoriteDTO.setUserId(favorite.getUser().getId());
		
		return simpleFavoriteDTO;
	}
	
	@Transient
	public static List<FavoriteDTO> convertToDTO(List<Favorite> favorites) 
	{
		List<FavoriteDTO> favoritesDTO = new ArrayList<FavoriteDTO>();
		if(favorites.isEmpty())
		{
			for (Favorite favorite : favorites) 
			{
				FavoriteDTO favoriteDTO = convertToDTOWithBooks(favorite);
				
				favoritesDTO.add(favoriteDTO);
			}
		}
		
		return favoritesDTO;
	}

	@Transient
	public static List<SimpleFavoriteDTO> convertToDTOWithoutBooks(List<Favorite> favorites) 
	{
		List<SimpleFavoriteDTO> favoritesDTO = new ArrayList<SimpleFavoriteDTO>();
		if(favorites!=null && favorites.size() > 0)
		{
			for (Favorite favorite : favorites) 
			{
				SimpleFavoriteDTO simpleFavoriteDTO = convertToDTOWithoutBooks(favorite);
				
				favoritesDTO.add(simpleFavoriteDTO);
			}
		}
		
		return favoritesDTO;
	}
}
