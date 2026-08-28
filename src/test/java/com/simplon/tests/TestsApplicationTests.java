package com.simplon.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
// import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// on vérifie u'un utilisateur avec le role USER peut récupérer les livres
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_USER" })
	public void shouldGetBooks() throws Exception {
		this.mvc.perform(get("/api/books")).andExpect(status().isOk());
	}

	// on vérifie qu'un utilisateur avec le role USER ne peut pas supprimer un livre
	@Test
	@WithMockUser(authorities = { "SCOPE_ROLE_USER" })
	public void shouldNotDeleteBooks() throws Exception {
		var mockId = "er45-e484f-ferf8r-rrefrt";
		StringBuilder mockUrl = new StringBuilder();
		mockUrl.append("/api/books/").append(mockId);
		this.mvc.perform(delete(mockUrl.toString())).andExpect(status().isForbidden());
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
				.andDo(print()).andExpect(status().isCreated());
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
				.andExpect(status().isCreated())
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
				.andExpect(status().isCreated())
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
		 * du coup on passe directement par le repository ou le service
		 */

		UserEntity savedUser = userRepository.findByEmail("chrisUser@simplon.co").orElseThrow();

		assertThat(savedUser.getPassword()).isNotEqualTo(password);
		assertThat(this.passwordEncoder.matches(password, savedUser.getPassword())).isTrue();
	}

	@Test
	public void shouldLogin() throws Exception {

		var email = "chrisUser2@simplon.co";
		var password = "password";

		var user = """
				{
				"name" : "chrisUser2",
				"password": "%s",
				"email" : "%s",
				"authorities": [{
					"authority": "ROLE_USER"
				}]
				}
				""".formatted(password, email);

		this.mvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(user))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		var userCreated = """
				{
				"password" : "%s",
				"email" : "%s"
				}
				""".formatted(password, email);

		this.mvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userCreated))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void shouldNotLogin() throws Exception {

		var email = "chrisUser3@simplon.co";
		var password = "password";

		var user = """
				{
				"name" : "chrisUser3",
				"password": "%s",
				"email" : "%s",
				"authorities": [{
					"authority": "ROLE_USER"
				}]
				}
				""".formatted(password, email);

		this.mvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(user))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		var badUser = """
				{
				"password" : "badpassword",
				"email" : "%s"
				}
				""".formatted(password, email);

		this.mvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(badUser))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}
}
