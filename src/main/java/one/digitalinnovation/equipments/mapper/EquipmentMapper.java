package one.digitalinnovation.equipments.mapper;

import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.entity.Equipment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentMapper {

    EquipmentMapper INSTANCE = Mappers.getMapper(EquipmentMapper.class);

    Equipment toModel(EquipmentDTO equipmentDTO);

    EquipmentDTO toDTO(Equipment equipment);
}
