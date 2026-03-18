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
	private final FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping
	public Collection<Film> findAll() {
		return filmService.findAll();
	}

	@GetMapping("/popular")
	public Collection<Film> getTopPopularFilms(@RequestParam(defaultValue = "10") int count) {
		return filmService.getTopPopularFilms(count);
	}

	@PostMapping
	public Film create(@RequestBody Film film) {
		return filmService.create(film);
	}

	@PutMapping
	public Film update(@RequestBody Film newFilm) {
		return filmService.update(newFilm);
	}

	@PutMapping("/{filmId}/like/{userId}")
	public void addLike(@PathVariable long filmId,
						@PathVariable long userId) {
		filmService.addLike(filmId, userId);
	}

	@DeleteMapping("/{filmId}/like/{userId}")
	public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
		filmService.removeLike(filmId, userId);
	}

}
