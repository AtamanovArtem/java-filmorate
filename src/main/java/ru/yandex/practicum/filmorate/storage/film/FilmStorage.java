package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
	void addFilm(Film film);
	void removeFilm(Integer filmId);
	void updateFilm(Film film);
	Collection<Film> getAllFilms();
	boolean contains(long filmId);
	Film getFilmById(long id);
}
