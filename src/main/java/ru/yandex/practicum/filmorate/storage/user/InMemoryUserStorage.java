package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
	private Map<Integer, User> users = new HashMap<>();

	@Override
	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	@Override
	public void removeUser(Integer userId) {
		users.remove(userId);
	}

	@Override
	public void updateUser(User user) {
		if (users.containsKey(user.getId())) {
			users.put(user.getId(), user);
		}
	}

	@Override
	public Collection<User> getAllUsers() {
		return users.values();
	}
}
