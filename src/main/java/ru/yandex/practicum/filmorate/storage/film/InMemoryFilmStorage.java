package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
	private final Map<Long, Film> films = new HashMap<>();

	@Override
	public void addFilm(Film film) {
		films.put(film.getId(), film);
	}

	@Override
	public void removeFilm(Integer filmId) {
		films.remove(filmId);
	}

	@Override
	public void updateFilm(Film film) {
		if (films.containsKey(film.getId())) {
			films.put(film.getId(), film);
		}
	}

	@Override
	public Collection<Film> getAllFilms() {
		return films.values();
	}

	@Override
	public boolean contains(long id) {
		return films.containsKey(id);
	}

	@Override
	public Film getFilmById(long id) {
		if (!films.containsKey(id)) {
			throw new NotFoundException("Фильм с id=" + id + " не найден");
		}
		return films.get(id);
	}
}
