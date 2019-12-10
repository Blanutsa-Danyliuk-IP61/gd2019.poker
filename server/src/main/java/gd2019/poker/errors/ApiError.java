package gd2019.poker.errors;

public class ApiError extends RuntimeException {

    public ApiError(String message) {
        super(message);
    }
}
