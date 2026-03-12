package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
	@Autowired
	private final UserStorage userStorage;
	private Map<Long, Set<Long>> userFriends = new HashMap<>(); // Ключ — userId, значение — набор userId, которые являются друзьями

	@Autowired
	public UserService(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public void addFriend(long userId, long friendId) {
		if (!userFriends.containsKey(userId)) {
			userFriends.put(userId, new HashSet<>());
		}
		userFriends.get(userId).add(friendId);

		if (!userFriends.containsKey(friendId)) {
			userFriends.put(friendId, new HashSet<>());
		}
		userFriends.get(friendId).add(userId);
	}

	public void removeFriend(long userId, long friendId) {
		userFriends.getOrDefault(userId, Set.of()).remove(friendId);
		userFriends.getOrDefault(friendId, Set.of()).remove(userId);
	}

	public Set<Long> getCommonFriends(long userId1, long userId2) {
		Set<Long> commonFriends = new HashSet<>(userFriends.getOrDefault(userId1, Set.of()));
		commonFriends.retainAll(userFriends.getOrDefault(userId2, Set.of()));
		return commonFriends;
	}
}
