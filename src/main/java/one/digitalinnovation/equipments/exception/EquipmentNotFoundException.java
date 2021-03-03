package one.digitalinnovation.equipments.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EquipmentNotFoundException extends Exception {

    public EquipmentNotFoundException(String np) {
        super(String.format("Equipment with name %s not found in the system.", np));
    }

    public EquipmentNotFoundException(Long id) {
        super(String.format("Equipment with id %s not found in the system.", id));
    }
}
