package one.digitalinnovation.equipments.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EquipmentType {

    PUMP("Pump", "14047"),
    MOTORPUMP("Motor Pump", "14015"),
    MOTOR("Motor", "14001"),
    AERATOR("Aerator", "14022"),
    GENERATOR("Generator", "14012"),
    REDUCER("Reducer", "14043"),
    TRANSFORMER("Transformer", "14006");

    private final String description;
    private final String initialCode;
}
