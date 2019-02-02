package br.pucminas.gerenciadorfavoritos.api.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.pucminas.gerenciadorfavoritos.api.entities.User;
import br.pucminas.gerenciadorfavoritos.api.enums.UserProfileEnum;
import br.pucminas.gerenciadorfavoritos.api.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTests 
{	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	private static final String BUSCAR_USER_URL = "/v1/public/users";
	private static final Long ID = Long.valueOf(1);
	private static final String USER_NAME = "Fake User";
	private static final String USER_EMAIL = "fakeuser@gmail.com";
	
	@Test
	public void testFindAllUsers() throws Exception
	{
		BDDMockito.given(this.userService.findAll()).willReturn(this.getFakeUserList());
		
		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_USER_URL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data[0].id").value(ID))
				.andExpect(jsonPath("$.data[0].name", equalTo(USER_NAME)))
				.andExpect(jsonPath("$.data[0].email", equalTo(USER_EMAIL)))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	private List<User> getFakeUserList()
	{
		List<User> retorno = new ArrayList<>();
		User user = new User();
		user.setId(Long.valueOf(1));
		user.setName(USER_NAME);
		user.setEmail(USER_EMAIL);
		user.setUserProfile(UserProfileEnum.ROLE_USUARIO);
		user.setActive(true);
		user.setCreationDate(Calendar.getInstance());
		
		retorno.add(user);
		
		return retorno;
	}
}
