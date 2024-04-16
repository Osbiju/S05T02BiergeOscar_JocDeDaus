package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.exceptions;

public class InvalidUsernameException extends RuntimeException{
    public InvalidUsernameException(){}

    public InvalidUsernameException(String message){
        super(message);
    }
}
