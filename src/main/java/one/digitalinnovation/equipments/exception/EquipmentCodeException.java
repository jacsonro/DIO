package one.digitalinnovation.equipments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EquipmentCodeException extends Exception{

    public EquipmentCodeException(String np) {
        super(String.format("The Equipment with NP %s has code is not persistent.", np));
    }
}
