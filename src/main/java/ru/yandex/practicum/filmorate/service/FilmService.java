package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {
	@Autowired
	private final FilmStorage filmStorage;
	private final UserStorage userStorage;
	private final Map<Long, Set<Long>> filmLikes = new HashMap<>();
	private final Integer maxDescription = 200;
	private final LocalDate dateOfBeginning = LocalDate.of(1895, 12, 28);

	public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
	}

	public Collection<Film> findAll() {
		return filmStorage.getAllFilms();
	}

	public void addLike(long filmId, long userId) {
		if (!filmStorage.contains(filmId)) {
			log.error("Фильм не найден");
			throw new NotFoundException("Фильм с id=" + filmId + " не найден");
		}
		if (!userStorage.contains(userId)) {
			log.error("Пользователь не найден");
			throw new NotFoundException("Пользователь с id=" + userId + " не найден");
		}
		if (!filmLikes.containsKey(filmId)) {
			filmLikes.put(filmId, new HashSet<>());
		}
		filmLikes.get(filmId).add(userId);
		log.info("Лайк добавлен: фильм {}, пользователь {}", filmId, userId);
	}

	public void removeLike(long filmId, long userId) {
		if (!filmStorage.contains(filmId)) {
			log.error("Фильм не найден");
			throw new NotFoundException("Фильм с id=" + filmId + " не найден");
		}
		if (!userStorage.contains(userId)) {
			log.error("Пользователь не найден");
			throw new NotFoundException("Пользователь с id=" + userId + " не найден");
		}
		if (filmLikes.containsKey(filmId)) {
			filmLikes.get(filmId).remove(userId);
		}
		log.info("Лайк удалён: фильм {}, пользователь {}", filmId, userId);
	}

	public Collection<Film> getTopPopularFilms(int count) {
		return filmStorage.getAllFilms().stream()
				.sorted((f1, f2) -> {
					int likes1 = filmLikes.getOrDefault(f1.getId(), new HashSet<>()).size();
					int likes2 = filmLikes.getOrDefault(f2.getId(), new HashSet<>()).size();
					return Integer.compare(likes2, likes1); // По убыванию
				})
				.limit(count)
				.toList();
	}

	public Film create(Film film) {
		validateFilm(film);
		film.setId(getNextId());
		filmStorage.addFilm(film);
		log.info("Фильм создан с ID: {}", film.getId());
		return film;
	}

	private long getNextId() {
		return filmStorage.getAllFilms().stream()
				.mapToLong(Film::getId)
				.max()
				.orElse(0L) + 1;
	}

	public void validateFilm(Film film) {
		if (film.getName() == null || film.getName().isBlank()) {
			log.error("Название фильма не может быть пустым!");
			throw new ValidationException("Название фильма не может быть пустым!");
		}
		if (film.getDescription() != null && film.getDescription().length() > maxDescription) {
			log.error("Описание фильма не должно превышать 200 символов и не может быть пустым!");
			throw new ValidationException("Описание фильма не должно превышать 200 символов");
		}
		if (film.getReleaseDate().isBefore(dateOfBeginning)) {
			log.error("Дата релиза фильма не может быть раньше 28.12.1895");
			throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
		}
		if (film.getDuration() <= 0) {
			log.warn("Продолжительность фильма должна быть положительным числом");
			throw new ValidationException("Продолжительность фильма должна быть положительным числом");
		}
	}

	public Film update(Film newFilm) {
		if (!filmStorage.contains(newFilm.getId())) {
			throw new NotFoundException("Фильм с id=" + newFilm.getId() + " не найден");
		}
		validateFilm(newFilm);
		filmStorage.updateFilm(newFilm);
		log.info("Фильм с ID {} обновлен", newFilm.getId());
		return newFilm;
	}
}
