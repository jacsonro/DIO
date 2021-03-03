package one.digitalinnovation.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.digitalinnovation.equipments.enums.EquipmentType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {

    private Long id;

    @NotNull
    @Size(min = 9, max = 9)
    private String np;

    @NotNull
    @Size(min = 3, max = 200)
    private String brand;

    @NotNull
    @Size(min = 3, max = 200)
    private String model;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EquipmentType type;
}
