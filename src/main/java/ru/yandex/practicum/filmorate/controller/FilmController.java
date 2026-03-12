package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
	private final Map<Integer, Film> films = new HashMap<>();
	private final FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}







	@GetMapping
	public Collection<Film> findAll() {
		return films.values();
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		if (film.getName() == null || film.getName().isBlank()) {
			log.error("Название фильма не может быть пустым!");
			throw new ValidationException("Название фильма не может быть пустым!");
		}
		if (film.getDescription().length() > 200) {
			log.error("Описание фильма не должно превышать 200 символов");
			throw new ValidationException("Описание фильма не должно превышать 200 символов");
		}
		if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
			log.error("Дата релиза фильма не может быть раньше 28.12.1895");
			throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
		}
		if (film.getDuration() <= 0) {
			log.warn("Продолжительность фильма должна быть положительным числом");
			throw new ValidationException("Продолжительность фильма должна быть положительным числом");
		}
		film.setId(getNextId());
		films.put(film.getId(), film);
		log.info("Фильм успешно добавлен с ID: " + film.getId());
		return film;
	}

	private int getNextId() {
		int currentMaxId = films.keySet()
				.stream()
				.mapToInt(id -> id)
				.max()
				.orElse(0);
		return ++currentMaxId;
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		Film currentFilm = films.get(newFilm.getId());
		if (currentFilm == null) {
			log.error("Фильм с ID " + newFilm.getId() + " не найден");
			throw new ValidationException("Фильм с указанным id не найден");
		}
		if (newFilm.getName() == null || newFilm.getName().isBlank()) {
			log.error("Название фильма не может быть пустым");
			throw new ValidationException("Название фильма не может быть пустым");
		}
		currentFilm.setName(newFilm.getName());
		String description = newFilm.getDescription();
		if (description == null || description.length() > 200) {
			log.error("Длина описания фильма не должна превышать 200 символов");
			throw new ValidationException("Длина описания фильма не должна превышать 200 символов");
		}
		currentFilm.setDescription(description);
		LocalDate releaseDate = newFilm.getReleaseDate();
		if (releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
			log.error("Дата релиза не может быть раньше 28 декабря 1895г.");
			throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895г.");
		}
		currentFilm.setReleaseDate(releaseDate);
		if (newFilm.getDuration() <= 0) {
			log.error("Продолжительность фильма должна быть положительным числом");
			throw new ValidationException("Продолжительность фильма должна быть положительным числом");
		}
		currentFilm.setDuration(newFilm.getDuration());
		log.info("Фильм с ID " + newFilm.getId() + " успешно обновлен");
		return currentFilm;
	}
}
