package com.simplon.tests;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import com.jayway.jsonpath.JsonPath;
import com.simplon.tests.entities.UserEntity;
import com.simplon.tests.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
class TestsApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockitoBean
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// on vérifie u'un utilisateur avec le role USER peut récupérer les livres
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_USER" })
	public void shouldGetBooks() throws Exception {
		this.mvc.perform(get("/api/books")).andDo(print()).andExpect(status().isOk());
	}

	// on vérifie qu'un utilisateur avec le role USER ne peut pas supprimer un livre
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_USER" })
	public void shouldNotDeleteBooks() throws Exception {
		var mockId = "er45-e484f-ferf8r-rrefrt";
		StringBuilder mockUrl = new StringBuilder();
		mockUrl.append("/api/books/").append(mockId);
		this.mvc.perform(delete(mockUrl.toString())).andDo(print()).andExpect(status().isForbidden());
	}

	// on vérifie qu'un utilisateur avec le role ADMIN peut créer un livre
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_ADMIN" })
	public void shouldCreateBook() throws Exception {
		String bookItemJson = """
					{
						"title": "La bible",
						"author": "Jesus & co",
						"category": "sciences fictions",
						"yearOfPublication": "0",
						"copiesAvailable": "1"
					}
				""";
		this.mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookItemJson))
				.andDo(print()).andExpect(status().isOk());
	}

	// on vérifie qu'un utilisateur avec le role ADMIN peut mettre à jour un livre
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_ADMIN" })
	public void shouldUpdateBook() throws Exception {
		String bookItemJson = """
					{
						"title": "La bible2",
						"author": "Jesus & co",
						"category": "sciences fictions",
						"yearOfPublication": "0",
						"copiesAvailable": "1"
					}
				""";
		var response = this.mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookItemJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String id = JsonPath.read(response.getResponse().getContentAsString(), "$.id");

		StringBuilder mockUrl = new StringBuilder();
		mockUrl.append("/api/books/").append(id);

		String bookToUpdateItemJson = """
					{
						"title": "La bible2 modifié"
					}
				""";

		this.mvc.perform(put(mockUrl.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookToUpdateItemJson))
				.andDo(print()).andExpect(status().isOk());
	}

	// on vérifie qu'un utilisateur avec le role ADMIN peut supprimer un livre
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_ADMIN" })
	public void shouldDeleteBook() throws Exception {
		String bookItemJson = """
					{
						"title": "Livre à supprimer",
						"author": "supprime tout",
						"category": "oups",
						"yearOfPublication": "1950",
						"copiesAvailable": "1"
					}
				""";
		var response = this.mvc.perform(post("/api/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(bookItemJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String id = JsonPath.read(response.getResponse().getContentAsString(), "$.id");

		StringBuilder mockUrl = new StringBuilder();
		mockUrl.append("/api/books/").append(id);

		this.mvc.perform(delete(mockUrl.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	// on vérifie qu'un utilisateur peut s'enregistrer
	@Test
	public void shouldRegister() throws Exception {

		var password = "password";
		var user = """
				{
				"name" : "chrisUser",
				"password": "%s",
				"email" : "chrisUser@simplon.co",
				"authorities": [{
					"authority": "ROLE_USER"
				}]
				}
				""".formatted(password);

		this.mvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(user))
				.andDo(print())
				.andExpect(status().isOk());

		/*
		 * le password ne devrais pas être retourner par la réponse, même hashé
		 * du coup on passe par Argumentcaptor pour capturer l'utilisateur enregistré et
		 * récupérer son password
		 * grâce à userCaptor.capture(), Mockito "attrape au vol" l'intance de User qui
		 * a été passée en paramètre à la méthode .save
		 */
		ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
		verify(this.userRepository).save(userCaptor.capture());

		// on récupère ici l'utilisateur créé
		UserEntity savedUser = userCaptor.getValue();

		// on vérifie ici que le password donnée lors de l'inscription ne soit pas le
		// même que celui qui a été sauvegardé
		// ce qui sous entend qu'il a bien été hashé
		assertThat(savedUser.getPassword()).isNotEqualTo(password);

		// mais pour aller plus loin,
		// on vérifie ici que le password et le password
		// hashé sont bien les mêmes
		// via passwordEncoder
		assertThat(this.passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
	}

	@Test
	public void shouldLogin() throws Exception {

		// var email = "chrisUser2@simplon.co";
		// var password = "password";

		var user = """
				{
				"name" : "chrisUser2",
				"password": "password",
				"email" : "chrisuser2@simplon.co",
				"authorities": [{
					"authority": "ROLE_USER"
				}]
				}
				""";

		this.mvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(user))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		var userCreated = """
				{
				"password" : "password",
				"email" : "chrisuser2@simplon.co"
				}
				""";

		this.mvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userCreated))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
