package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
	private Map<Integer, Film> films = new HashMap<>();

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
}
