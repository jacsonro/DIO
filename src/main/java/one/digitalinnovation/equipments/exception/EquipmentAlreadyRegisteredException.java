package one.digitalinnovation.equipments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EquipmentAlreadyRegisteredException extends Exception{

    public EquipmentAlreadyRegisteredException(String np) {
        super(String.format("Equipment with NP %s already registered in the system.", np));
    }
}
