package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.exceptions;

public class PlayerAlreadyExistsException extends RuntimeException{
    public PlayerAlreadyExistsException(String message){
        super(message);
    }
}
