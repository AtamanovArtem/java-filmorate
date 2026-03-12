package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Arrays;
import java.util.Collection;

public interface FilmStorage {
	void addFilm(Film film);
	void removeFilm(Integer filmId);
	void updateFilm(Film film);
	Collection<Film> getAllFilms();
}
