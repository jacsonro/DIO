package one.digitalinnovation.equipments.builder;

import lombok.Builder;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.enums.EquipmentType;

@Builder
public class EquipmentDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String np = "140152830";

    @Builder.Default
    private String brand = "FLYGT";

    @Builder.Default
    private String model = "NP 3206";

    @Builder.Default
    private EquipmentType type = EquipmentType.MOTORPUMP;

    public EquipmentDTO toEquipmentDTO() {
        return new EquipmentDTO(id,
                np,
                brand,
                model,
                type);
    }
}
