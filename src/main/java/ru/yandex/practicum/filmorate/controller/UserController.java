package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
	private final Map<Integer, User> users = new HashMap<>();
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}






	@GetMapping
	public Collection<User> findAll() {
		return users.values();
	}

	@PostMapping
	public User create(@RequestBody User user) {
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			log.error("Email должен быть указан");
			throw new ValidationException("Email должен быть указан");
		}
		if (!user.getEmail().contains("@")) {
			log.error("Электронная почта должна содержать символ @");
			throw new ValidationException("Электронная почта должна содержать символ @");
		}
		if (user.getLogin() == null || user.getLogin().contains(" ")) {
			log.error("Логин не может быть пустым и содержать пробелы!");
			throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
		}
		if (user.getName() == null) {
			log.warn("Имя для отображения не может быть пустым!Будет использован login");
			user.setName(user.getLogin());
		}
		if (user.getBirthday().isAfter(ChronoLocalDate.from(LocalDateTime.now()))) {
			log.error("День рождения не может быть в будущем!");
			throw new ValidationException("День рождения не может быть в будущем!");
		}

		user.setId(getNextId());
		users.put(user.getId(), user);
		log.info("Пользователь успешно добавлен с ID: " + user.getId());
		return user;
	}

	private int getNextId() {
		int currentMaxId = users.keySet()
				.stream()
				.mapToInt(id -> id)
				.max()
				.orElse(0);
		return ++currentMaxId;
	}

	@PutMapping
	public User update(@RequestBody User newUser) {
		User currentUser = users.get(newUser.getId());
		if (currentUser == null) {
			log.warn("Пользователь с указанным id не найден");
			throw new ValidationException("Пользователь с указанным id не найден");
		}

		if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
			log.error("Email должен содержать @ и не должен быть пустым");
			throw new ValidationException("Email должен содержать @ и не должен быть пустым");
		}
		currentUser.setEmail(newUser.getEmail());

		if (newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
			log.error("Логин не может быть пустым и содержать пробелы");
			throw new ValidationException("Логин не может быть пустым и содержать пробелы");
		}
		currentUser.setLogin(newUser.getLogin());

		if (newUser.getName() == null || newUser.getName().isBlank()) {
			currentUser.setName(newUser.getLogin());
		} else {
			currentUser.setName(newUser.getName());
		}

		if (newUser.getBirthday().isAfter(LocalDate.now())) {
			log.error("Дата рождения не может быть в будущем");
			throw new ValidationException("Дата рождения не может быть в будущем");
		}
		currentUser.setBirthday(newUser.getBirthday());

		log.info("Пользователь с ID {} успешно обновлен", newUser.getId());
		return currentUser;
	}
}
