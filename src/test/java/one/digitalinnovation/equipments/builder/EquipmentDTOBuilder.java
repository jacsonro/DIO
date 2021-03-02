package one.digitalinnovation.equipments.builder;

import lombok.Builder;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.enums.EquipmentType;

@Builder
public class EquipmentDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Brahma";

    @Builder.Default
    private String brand = "Ambev";

    @Builder.Default
    private int max = 50;

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private EquipmentType type = EquipmentType.LAGER;

    public EquipmentDTO toBeerDTO() {
        return new EquipmentDTO(id,
                name,
                brand,
                max,
                quantity,
                type);
    }
}
