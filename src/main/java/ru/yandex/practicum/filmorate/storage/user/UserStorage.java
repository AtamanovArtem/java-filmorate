package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
	void addUser(User user);
	void removeUser(Integer userId);
	void updateUser(User user);
	Collection<User> getAllUsers();
}
