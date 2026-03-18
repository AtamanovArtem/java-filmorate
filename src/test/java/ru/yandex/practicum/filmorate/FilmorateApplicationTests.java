package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	public void testFindAllUsers() {/*
		UserController controller = new UserController();

		User user1 = User.builder()
				.email("email1@example.com")
				.login("login1")
				.name("name1")
				.birthday(LocalDate.of(1991, 4, 12))
				.build();
		User created1 = controller.create(user1);

		User user2 = User.builder()
				.email("email2@example.com")
				.login("login2")
				.name("name2")
				.birthday(LocalDate.of(1990, 4, 10))
				.build();
		User created2 = controller.create(user2);

		Collection<User> users = controller.findAll();
		assertEquals(2, users.size());

		assertTrue(users.stream().anyMatch(u ->
				u.getId() == (created1.getId()) &&
						u.getEmail().equals("email1@example.com") &&
						u.getLogin().equals("login1") &&
						u.getName().equals("name1") &&
						u.getBirthday().equals(LocalDate.of(1991, 4, 12))
		));

		assertTrue(users.stream().anyMatch(u ->
				u.getId() == (created2.getId()) &&
						u.getEmail().equals("email2@example.com") &&
						u.getLogin().equals("login2") &&
						u.getName().equals("name2") &&
						u.getBirthday().equals(LocalDate.of(1990, 4, 10))
		));
	}

	@Test
	public void testFindAllUsersWithoutUsers() {
		UserController userController = new UserController();
		Collection<User> users = userController.findAll();
		assertEquals(0, users.size());
		assertFalse(users.contains(new User(1, "email1@example.com", "login1", "name1",
				LocalDate.of(1991, 4, 12))));
		assertFalse(users.contains(new User(2, "email2@example.com", "login2", "name2",
				LocalDate.of(1990, 4, 10))));
	}

	@Test
	public void testFindAllFilms() {
		FilmController filmController = new FilmController();
		Film film1 = new Film(1, "name1", "description1",
				LocalDate.of(2020, 12, 2), 100);
		filmController.create(film1);
		Film film2 = new Film(2, "name2", "description2",
				LocalDate.of(2000, 12, 2), 200);
		filmController.create(film2);

		Collection<Film> films = filmController.findAll();
		assertEquals(2, films.size());
		assertTrue(films.contains(new Film(1, "name1", "description1",
				LocalDate.of(2020, 12, 2), 100)));
		assertTrue(films.contains(new Film(2, "name2", "description2",
				LocalDate.of(2000, 12, 2), 200)));
	}

	@Test
	public void testFindAllFilmsWithoutFilms() {
		FilmController filmController = new FilmController();
		Collection<Film> films = filmController.findAll();
		assertEquals(0, films.size());
		assertFalse(films.contains(new Film(1, "name1", "description1",
				LocalDate.of(2020, 12, 2), 100)));
		assertFalse(films.contains(new Film(2, "name2", "description2",
				LocalDate.of(2000, 12, 2), 200)));
	}

	@Test
	public void testCreateFilm() {
		FilmController filmController = new FilmController();
		Film film = Film.builder()
				.id(1)
				.name("name1")
				.description("description1")
				.releaseDate(LocalDate.of(1991, 12, 1))
				.duration(90)
				.build();
		Film createdFilm = filmController.create(film);
		Collection<Film> films = filmController.findAll();
		assertNotNull(createdFilm);
		assertEquals("name1", createdFilm.getName());
		assertEquals("description1", createdFilm.getDescription());
		assertEquals(LocalDate.of(1991, 12, 1), createdFilm.getReleaseDate());
		assertEquals(90, createdFilm.getDuration());
		assertTrue(films.contains(createdFilm));
	}

	@Test
	public void testUpdateFilm() {
		FilmController filmController = new FilmController();

		Film film1 = new Film(1, "name1", "description1",
				LocalDate.of(2020, 12, 2), 100);
		Film newFilm = new Film(1, "newName", "description2",
				LocalDate.of(2010, 12, 2), 85);
		filmController.create(film1);
		Film updatedFilm = filmController.update(newFilm);
		Collection<Film> films = filmController.findAll();
		assertEquals("newName", updatedFilm.getName());
		assertEquals("description2", updatedFilm.getDescription());
		assertEquals(85, updatedFilm.getDuration());
		assertEquals(LocalDate.of(2010, 12, 2), updatedFilm.getReleaseDate());
		assertTrue(films.contains(updatedFilm));
	}

	@Test
	public void testCreateFilmMinimumReleaseDate() {
		FilmController filmController = new FilmController();
		Film film = Film.builder()
				.id(1)
				.name("name1")
				.description("description1")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(90)
				.build();
		Film createdFilm = filmController.create(film);
		Collection<Film> films = filmController.findAll();
		assertNotNull(createdFilm);
		assertEquals("name1", createdFilm.getName());
		assertEquals("description1", createdFilm.getDescription());
		assertEquals(LocalDate.of(1895, 12, 28), createdFilm.getReleaseDate());
		assertEquals(90, createdFilm.getDuration());
		assertTrue(films.contains(createdFilm));
	}

	@Test
	public void testUpdateFilmWithEmptyName() {
		FilmController filmController = new FilmController();

		Film film1 = new Film(1, "name1", "description1",
				LocalDate.of(2020, 12, 2), 100);
		filmController.create(film1);

		Film newFilm = new Film(1, "", "description2",
				LocalDate.of(2010, 12, 2), 85);

		assertThrows(ValidationException.class, () -> {
			filmController.update(newFilm);
		});
	}

	@Test
	public void testCreateFilmWithEmptyName() {
		FilmController filmController = new FilmController();
		Film newFilm = new Film(1, null, "description2",
				LocalDate.of(2010, 12, 2), 85);

		assertThrows(ValidationException.class, () -> {
			filmController.create(newFilm);
		});
	}

	@Test
	public void testUpdateFilmWithExcessDescription() {
		FilmController filmController = new FilmController();
		Film film = new Film(1, "name1", "description1",
				LocalDate.of(2002, 12, 2), 90);
		filmController.create(film);

		Film newFilm = new Film(1, "newName", "description2333333333333333333333333333333333333333" +
				"33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333" +
				"33333333333333333333333333333333333333333333333333333333333333333",
				LocalDate.of(2010, 12, 2), 85);

		assertThrows(ValidationException.class, () -> {
			filmController.update(newFilm);
		});
	}

	@Test
	public void testUpdateFilmWithMaxDescription() {
		FilmController filmController = new FilmController();
		Film film = new Film(1, "name1", "description1",
				LocalDate.of(2002, 12, 2), 90);
		filmController.create(film);

		Film newFilm = new Film(1, "newName", "description22222222222222222222222222222222222222222" +
				"22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222" +
				"22222222222222222222222222222222222222222222222",
				LocalDate.of(2010, 12, 2), 85);
		Film updatedFilm = filmController.update(newFilm);
		Collection<Film> films = filmController.findAll();
		assertEquals("newName", updatedFilm.getName());
		assertEquals("description22222222222222222222222222222222222222222222222222222222222222222222222222222" +
				"222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222" +
				"2222222222", updatedFilm.getDescription());
		assertEquals(85, updatedFilm.getDuration());
		assertEquals(LocalDate.of(2010, 12, 2), updatedFilm.getReleaseDate());
		assertTrue(films.contains(updatedFilm));
	}

	@Test
	public void testUpdateFilmWithNegativeDuration() {
		FilmController filmController = new FilmController();
		Film film = new Film(1, "name1", "description1",
				LocalDate.of(2002, 12, 2), 90);
		filmController.create(film);

		Film newFilm = new Film(1, "newName", "description2",
				LocalDate.of(2010, 12, 2), -1);

		assertThrows(ValidationException.class, () -> {
			filmController.update(newFilm);
		});
	}

	@Test
	public void testUpdateFilmWithMinReleaseDate() {
		FilmController filmController = new FilmController();
		Film film = new Film(1, "name1", "description1",
				LocalDate.of(2002, 12, 2), 90);
		filmController.create(film);

		Film newFilm = new Film(1, "newName", "description2",
				LocalDate.of(1895, 12, 28), 100);
		Film updatedFilm = filmController.update(newFilm);
		Collection<Film> films = filmController.findAll();
		assertEquals("newName", updatedFilm.getName());
		assertEquals("description2", updatedFilm.getDescription());
		assertEquals(100, updatedFilm.getDuration());
		assertEquals(LocalDate.of(1895, 12, 28), updatedFilm.getReleaseDate());
		assertTrue(films.contains(updatedFilm));
	}

	@Test
	public void testCreateFilmWithExcessDescription() {
		FilmController filmController = new FilmController();
		Film newFilm = new Film(1, "name1", "description23333333333333333333333333333333333333333333" +
				"333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333" +
				"333333333333333333333333333333333333333333333333333333333333333",
				LocalDate.of(2010, 12, 2), 85);

		assertThrows(ValidationException.class, () -> {
			filmController.create(newFilm);
		});
	}

	@Test
	public void testCreateFilmWithNegativeDuration() {
		FilmController filmController = new FilmController();
		Film newFilm = new Film(1, "name1", "description",
				LocalDate.of(2010, 12, 2), 0);

		assertThrows(ValidationException.class, () -> {
			filmController.create(newFilm);
		});
	}





	@Test
	public void testCreateUser() {
		UserController userController = new UserController();
		User user = new User(1, "email1@example.com", "login1", "name1",
				LocalDate.of(1991, 4, 12));
		User createdUser = userController.create(user);
		assertNotNull(createdUser);
		assertEquals(1, createdUser.getId());
		assertEquals("name1", createdUser.getName());
		assertEquals("email1@example.com", createdUser.getEmail());
		assertEquals("login1", createdUser.getLogin());
		assertEquals(LocalDate.of(1991, 4, 12), createdUser.getBirthday());
		Collection<User> users = userController.findAll();
		assertEquals(1, users.size());
	}

	@Test
	public void testCreateUserWithoutName() {
		UserController userController = new UserController();
		User user = new User(1, "email1@example.com", "login1", null,
				LocalDate.of(1991, 4, 12));
		User createdUser = userController.create(user);
		assertNotNull(createdUser);
		assertEquals(1, createdUser.getId());
		assertEquals("login1", createdUser.getName());
		assertEquals("email1@example.com", createdUser.getEmail());
		assertEquals("login1", createdUser.getLogin());
		assertEquals(LocalDate.of(1991, 4, 12), createdUser.getBirthday());
		Collection<User> users = userController.findAll();
		assertEquals(1, users.size());
	}

	@Test
	public void testCreateUserWithoutEmail() {
		UserController userController = new UserController();
		User user = new User(1, null, "login1", "name",
				LocalDate.of(1991, 4, 12));
		assertThrows(ValidationException.class, () -> {
			userController.create(user);
		});
	}

	@Test
	public void testCreateUserWithNoCorrectEmail() {
		UserController userController = new UserController();
		User user = new User(1, "email", "login1", "name",
				LocalDate.of(1991, 4, 12));
		assertThrows(ValidationException.class, () -> {
			userController.create(user);
		});
	}

	@Test
	public void testCreateUserWithoutLogin() {
		UserController userController = new UserController();
		User user = new User(1, "updatedEmail@example.com", null, "name",
				LocalDate.of(1991, 4, 12));
		assertThrows(ValidationException.class, () -> {
			userController.create(user);
		});
	}

	@Test
	public void testCreateUserWithNoCorrectLogin() {
		UserController userController = new UserController();
		User user = new User(1, "updatedEmail@example.com", "login login", "name",
				LocalDate.of(1991, 4, 12));
		assertThrows(ValidationException.class, () -> {
			userController.create(user);
		});
	}

	@Test
	public void testCreateUserWithNoCorrectBirthday() {
		UserController userController = new UserController();
		User user = new User(1, "updatedEmail@example.com", "login", "name",
				LocalDate.of(2027, 4, 12));
		assertThrows(ValidationException.class, () -> {
			userController.create(user);
		});
	}

	@Test
	public void testUpdateUserEmailToNull() {
		UserController controller = new UserController();
		User user = User.builder()
				.email("user1@example.com")
				.login("login1")
				.name("Name1")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		User createdUser = controller.create(user);
		User updateData = User.builder()
				.id(createdUser.getId())
				.email("")
				.login("login2")
				.name("Name2")
				.birthday(LocalDate.of(1995, 1, 1))
				.build();
		ValidationException exception = assertThrows(ValidationException.class, () -> {
			controller.update(updateData);
		});
		assertEquals("Email должен содержать @ и не должен быть пустым", exception.getMessage());
	}

	@Test
	public void testUpdateUserBirthdayToNoCorrectDate() {
		UserController controller = new UserController();
		User user1 = User.builder()
				.email("user1@example.com")
				.login("login1")
				.name("Name1")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		User created1 = controller.create(user1);
		int id1 = created1.getId();
		User user2 = User.builder()
				.email("user2@example.com")
				.login("login2")
				.name("Name2")
				.birthday(LocalDate.of(1995, 2, 2))
				.build();
		User created2 = controller.create(user2);
		int id2 = created2.getId();
		User updateData = User.builder()
				.birthday(LocalDate.now().plusDays(1))
				.build();
		assertThrows(ValidationException.class, () -> {
			controller.update(updateData);
		});
	}

	@Test
	public void testUpdateUserNameToEmptyString() {
		UserController controller = new UserController();
		User user = User.builder()
				.id(1)
				.email("test@example.com")
				.login("login")
				.name("Original")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		int userId = controller.create(user).getId();
		User updateData = User.builder()
				.id(1)
				.email("test1@example.com")
				.login("newLogin")
				.name("")
				.birthday(LocalDate.of(1995, 12, 1))
				.build();
		User updated = controller.update(updateData);
		assertEquals("newLogin", updated.getName());
	}

	@Test
	public void testUpdateUserNameToOtherName() {
		UserController controller = new UserController();
		User user = User.builder()
				.id(1)
				.email("test@example.com")
				.login("login")
				.name("Original")
				.birthday(LocalDate.of(1990, 1, 1))
				.build();
		int userId = controller.create(user).getId();
		User updateData = User.builder()
				.id(1)
				.email("test1@example.com")
				.login("login1")
				.name("newName")
				.birthday(LocalDate.of(1995, 12, 10))
				.build();
		User updated = controller.update(updateData);
		assertEquals("newName", updated.getName());
	}
*/
	}
}
