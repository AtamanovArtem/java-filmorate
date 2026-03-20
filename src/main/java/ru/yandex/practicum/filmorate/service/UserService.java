package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
	@Autowired
	private final UserStorage userStorage;
	private final Map<Long, Set<Long>> userFriends = new HashMap<>();

	public UserService(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public Collection<User> findAll() {
		return userStorage.getAllUsers();
	}

	public User create(User user) {
		validateUser(user);
		user.setId(getNextId());
		userStorage.addUser(user);
		log.info("Пользователь создан с ID: {}", user.getId());
		return user;
	}

	private long getNextId() {
		return userStorage.getAllUsers().stream()
				.mapToLong(User::getId)  // ← mapToLong, а не mapToInt!
				.max()
				.orElse(0L) + 1;  // ← 0L — long literal
	}


	public User update(User newUser) {
		if (!userStorage.contains(newUser.getId())) {
			log.error("Пользователь не найден");
			throw new NotFoundException("Пользователь с id=" + newUser.getId() + " не найден");
		}
		validateUser(newUser);
		userStorage.updateUser(newUser);
		log.info("Пользователь с ID {} обновлен", newUser.getId());
		return newUser;
	}


	public void addFriend(long userId, long friendId) {

		if (userId <= 0 || friendId <= 0) {
			log.error("ID пользователя и друга должны быть больше нуля");
			throw new ValidationException("ID пользователя и друга должны быть больше нуля.");
		}
		if (userId == friendId) {
			log.error("Пользователь не может быть добавлен к себе в друзья.");
			throw new ValidationException("Пользователь не может быть добавлен к себе в друзья.");
		}
		if (!userStorage.contains(userId)) {
			log.error("NotFoundException: пользователь {} не найден", userId);
			throw new NotFoundException("Пользователь с id=" + userId + " не найден");
		}
		if (!userStorage.contains(friendId)) {
			log.error("NotFoundException: друг {} не найден", friendId);
			throw new NotFoundException("Друг с id=" + friendId + " не найден");
		}
		if (!userFriends.containsKey(userId)) {
			userFriends.put(userId, new HashSet<>());
		}
		if (!userFriends.containsKey(friendId)) {
			userFriends.put(friendId, new HashSet<>());
		}
		if (userFriends.get(userId).contains(friendId)) {
			log.warn("Пользователь уже в списке друзей.");
			throw new ValidationException("Пользователь уже в списке друзей.");
		}
		userFriends.get(userId).add(friendId);
		userFriends.get(friendId).add(userId);
	}

	public void removeFriend(long userId, long friendId) {

		if (userId <= 0 || friendId <= 0) {
			log.error("ID пользователя и друга должны быть больше нуля.");
			throw new ValidationException("ID пользователя и друга должны быть больше нуля.");
		}
		if (userId == friendId) {
			log.error("Пользователь не может быть удалён из своих собственных друзей.");
			throw new ValidationException("Пользователь не может быть удалён из своих собственных друзей.");
		}
		if (!userStorage.contains(userId) || !userStorage.contains(friendId)) {
			log.error("Пользователь не найден");
			throw new NotFoundException("Пользователь не найден");
		}
		if (!userFriends.containsKey(userId) || !userFriends.get(userId).contains(friendId)) {
			return;
		}
		userFriends.get(userId).remove(friendId);
		userFriends.get(friendId).remove(userId);
	}

	public Collection<User> getCommonFriends(long userId1, long userId2) {

		if (userId1 <= 0 || userId2 <= 0) {
			log.error("ID пользователя должен быть больше нуля");
			throw new ValidationException("ID пользователя должен быть больше нуля");
		}
		if (!userStorage.contains(userId1) || !userStorage.contains(userId2)) {
			log.error("Пользователь не найден");
			throw new NotFoundException("Пользователь не найден");
		}
		Set<Long> friends1 = userFriends.getOrDefault(userId1, Set.of());
		Set<Long> friends2 = userFriends.getOrDefault(userId2, Set.of());
		Set<Long> commonIds = new HashSet<>(friends1);
		commonIds.retainAll(friends2);

		return commonIds.stream()
				.map(userStorage::getUserById)
				.toList();
	}

	public Collection<User> getFriends(long id) {
		if (id <= 0) {
			log.error("ID пользователя должен быть больше нуля");
			throw new ValidationException("ID пользователя должен быть больше нуля");
		}
		if (!userStorage.contains(id)) {
			log.error("Пользователь не найден");
			throw new NotFoundException("Пользователь с id=" + id + " не найден");
		}
		Set<Long> friendIds = userFriends.getOrDefault(id, Set.of());
		return friendIds.stream()
				.map(userStorage::getUserById)
				.toList();
	}

	private void validateUser(User user) {
		if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
			log.error("Email должен быть указан и содержать @");
			throw new ValidationException("Email должен быть указан и содержать @");
		}
		if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
			log.error("Логин не может быть пустым и содержать пробелы");
			throw new ValidationException("Логин не может быть пустым и содержать пробелы");
		}
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
			log.error("День рождения не может быть в будущем");
			throw new ValidationException("День рождения не может быть в будущем");
		}
	}
}

