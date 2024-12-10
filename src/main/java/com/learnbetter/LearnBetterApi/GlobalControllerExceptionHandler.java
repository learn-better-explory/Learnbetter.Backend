package com.learnbetter.LearnBetterApi;

import com.learnbetter.LearnBetterApi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(WrongRegisterDataException.class)
    public ResponseEntity<?> handleIncorrectUserData(){
        return new ResponseEntity<>("The user provided has either incorrect username or email", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordDoesntMatchException.class)
    public ResponseEntity<?> handleIncorrectPassword(){
        return new ResponseEntity<>("The password is incorrect!", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleNotFoundUser(){
        return new ResponseEntity<>("The user doesn't exist!", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(){
        return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DoesntHavePermission.class)
    public ResponseEntity<?> handleNoPermission(){
        return new ResponseEntity<>("The user doesn't have permission for this table", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DefinitionsTableException.class)
    public ResponseEntity<?> handleNoTable(){
        return new ResponseEntity<>("This table doesn't exist", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TitleTooLongException.class)
    public ResponseEntity<?> handleTitleTooLong(TitleTooLongException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
