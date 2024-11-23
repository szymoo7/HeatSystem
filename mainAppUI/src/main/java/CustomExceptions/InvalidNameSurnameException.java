package CustomExceptions;

public class InvalidNameSurnameException extends Exception {

    public InvalidNameSurnameException(String message) {
        super(message);
    }

    public InvalidNameSurnameException(String message, Throwable cause) {
        super(message, cause);
    }
}
