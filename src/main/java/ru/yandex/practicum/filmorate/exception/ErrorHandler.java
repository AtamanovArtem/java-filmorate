package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleValidation(final ValidationException e) {
		return new ErrorResponse(
				e.getMessage(),
				"Произошла ошибка валидации"
		);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(final NotFoundException e) {
		return new ErrorResponse(
				e.getMessage(),
				"Искомый объект не найден"
		);
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(final UnknownException e) {
		return new ErrorResponse(
				e.getMessage(),
				"Возникло исключение"
		);
	}
}
