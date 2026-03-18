package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
		return ResponseEntity
				.badRequest()  // 400
				.body(new ErrorResponse(e.getMessage(), "Произошла ошибка валидации"));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)  // 404
				.body(new ErrorResponse(e.getMessage(), "Искомый объект не найден"));
	}

	@ExceptionHandler(UnknownException.class)
	public ResponseEntity<ErrorResponse> handleException(UnknownException e) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
				.body(new ErrorResponse(e.getMessage(), "Возникло исключение"));
	}
}
