package ru.kdv.study.Exception;

public class NoDataFoundException extends RuntimeException {

    public static NoDataFoundException create(String message) {
        return new NoDataFoundException(message);
    }

    public NoDataFoundException(String message) {
        super(message);
    }
}
