package ru.kdv.study.Exception;

public class BadRequestException extends RuntimeException{

    public static BadRequestException create(String message) {
        return new BadRequestException(message);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
