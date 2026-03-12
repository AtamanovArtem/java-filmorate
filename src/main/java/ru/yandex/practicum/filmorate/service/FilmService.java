package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
	@Autowired
	private final FilmStorage filmStorage;
	private Map<Long, Integer> likesCount = new HashMap<>(); // Карта для хранения количества лайков для каждого фильма
	private Map<Long, Set<Long>> userLikes = new HashMap<>(); // Ключ — userId, значение — набор filmId, которым пользователь поставил лайк

	@Autowired
	public FilmService(FilmStorage filmStorage) {
		this.filmStorage = filmStorage;
	}

	public void addLike(long userId, long filmId) {
		if (!userLikes.containsKey(userId)) {
			userLikes.put(userId, new HashSet<>());
		}
		Set<Long> likedFilms = userLikes.get(userId);
		if (!likedFilms.contains(filmId)) {
			likedFilms.add(filmId);
			likesCount.merge(filmId, 1, Integer::sum);
		}
	}

	public void removeLike(long userId, long filmId) {
		if (userLikes.containsKey(userId)) {
			Set<Long> likedFilms = userLikes.get(userId);
			if (likedFilms.contains(filmId)) {
				likedFilms.remove(filmId);
				if (likesCount.get(filmId) > 0) {
					likesCount.put(filmId, likesCount.get(filmId) - 1);
				}
			}
		}
	}

	public List<Film> getTopPopularFilms() {
		return filmStorage.getAllFilms().stream()
				.sorted((film1, film2) -> Integer.compare(likesCount.getOrDefault(film2.getId(),
						0), likesCount.getOrDefault(film1.getId(), 0)))
				.limit(10)
				.collect(Collectors.toList());
	}

}
