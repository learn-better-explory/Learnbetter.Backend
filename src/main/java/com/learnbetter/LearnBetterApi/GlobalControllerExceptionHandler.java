package com.learnbetter.LearnBetterApi;

import com.learnbetter.LearnBetterApi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    public static class ErrorMessage{

        private String message;
        private HttpStatus httpStatus;

        private ErrorMessage(String message, HttpStatus httpStatus){
            this.message = message;
            this.httpStatus = httpStatus;
        }

        public ResponseEntity<?> wrap(){
            return new ResponseEntity<>(Map.of("message", message, "status", httpStatus.value()), httpStatus);
        }
    }

    @ExceptionHandler(WrongRegisterDataException.class)
    public ResponseEntity<?> handleIncorrectUserData(){
        return new ErrorMessage("The user provided has either incorrect username or email", HttpStatus.BAD_REQUEST).wrap();
    }

    @ExceptionHandler(PasswordDoesntMatchException.class)
    public ResponseEntity<?> handleIncorrectPassword(){
        return new ErrorMessage("The password is incorrect!", HttpStatus.FORBIDDEN).wrap();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleNotFoundUser(){
        return new ErrorMessage("The user doesn't exist!", HttpStatus.BAD_REQUEST).wrap();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(){
        return new ErrorMessage("User already exists", HttpStatus.BAD_REQUEST).wrap();
    }

    @ExceptionHandler(DoesntHavePermission.class)
    public ResponseEntity<?> handleNoPermission(){
        return new ErrorMessage("The user doesn't have permission for this table", HttpStatus.FORBIDDEN).wrap();
    }

    @ExceptionHandler(DefinitionsTableException.class)
    public ResponseEntity<?> handleNoTable(){
        return new ErrorMessage("This table doesn't exist", HttpStatus.BAD_REQUEST).wrap();
    }

    @ExceptionHandler(TitleTooLongException.class)
    public ResponseEntity<?> handleTitleTooLong(TitleTooLongException exception){
        return new ErrorMessage(exception.getMessage(), HttpStatus.BAD_REQUEST).wrap();
    }

    @ExceptionHandler(DefinitionWordNotExists.class)
    public ResponseEntity<?> handleWordNoExist(){
        return new ErrorMessage("This word is not present in the specified table!", HttpStatus.BAD_REQUEST).wrap();
    }
}
