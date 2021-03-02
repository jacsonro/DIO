package one.digitalinnovation.equipments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EquipmentAlreadyRegisteredException extends Exception{

    public EquipmentAlreadyRegisteredException(String beerName) {
        super(String.format("Equipment with name %s already registered in the system.", beerName));
    }
}
