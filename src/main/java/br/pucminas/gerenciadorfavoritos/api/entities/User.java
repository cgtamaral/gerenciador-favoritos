package br.pucminas.gerenciadorfavoritos.api.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.pucminas.gerenciadorfavoritos.api.dtos.UserDTO;
import br.pucminas.gerenciadorfavoritos.api.enums.UserProfileEnum;

@Entity
@Table(name = "usuario")
public class User {

	private Long id;
	private String name;
	private String email;
	private String password;
	private UserProfileEnum userProfile;
	private Boolean active;
	private Calendar creationDate;
	private List<Favorite> favorites;
	private List<BookReview> bookReviews;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "password", nullable = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "profile", nullable = false)
	public UserProfileEnum getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(UserProfileEnum userProfile) {
		this.userProfile = userProfile;
	}
	
	@Column(name = "active", nullable = false)
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Column(name = "creationdate", nullable = false)
	public Calendar getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public List<Favorite> getFavorites() {
		return favorites;
	}
	public void setFavorites(List<Favorite> favorites) {
		this.favorites = favorites;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	public List<BookReview> getBookReviews() {
		return bookReviews;
	}
	public void setBookReviews(List<BookReview> bookReviews) {
		this.bookReviews = bookReviews;
	}
	
	@Transient
	public static List<UserDTO> convertToDTO(List<User> users) 
	{
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		for (User user : users) 
		{
			usersDTO.add(convertToDTO(user));
		}
		
		return usersDTO;
	}
	
	@Transient
	public static UserDTO convertToDTO(User user) 
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setName(user.getName());
		userDTO.setEmail(user.getEmail());
		userDTO.setUserProfile(user.getUserProfile().name());
		userDTO.setActive(user.getActive() ? "Sim" : "Não");
		userDTO.setCreationDate(simpleDateFormat.format(user.getCreationDate().getTime()));
	
		return userDTO;
	}
}
