package one.digitalinnovation.equipments.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.entity.Equipment;
import one.digitalinnovation.equipments.enums.EquipmentType;
import one.digitalinnovation.equipments.exception.EquipmentAlreadyRegisteredException;
import one.digitalinnovation.equipments.exception.EquipmentCodeException;
import one.digitalinnovation.equipments.exception.EquipmentNotFoundException;
import one.digitalinnovation.equipments.mapper.EquipmentMapper;
import one.digitalinnovation.equipments.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper = EquipmentMapper.INSTANCE;

    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) throws EquipmentAlreadyRegisteredException, EquipmentCodeException {
        verifyNpCode(equipmentDTO);
        verifyIfIsAlreadyRegistered(equipmentDTO.getNp());
        Equipment equipment = equipmentMapper.toModel(equipmentDTO);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toDTO(savedEquipment);
    }

    public EquipmentDTO findByNp(String np) throws EquipmentNotFoundException {
        Equipment foundEquipment = equipmentRepository.findByNp(np)
                .orElseThrow(() -> new EquipmentNotFoundException(np));
        return equipmentMapper.toDTO(foundEquipment);
    }

    public List<EquipmentDTO> listAll() {
        return equipmentRepository.findAll()
                .stream()
                .map(equipmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws EquipmentNotFoundException {
        verifyIfExists(id);
        equipmentRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String np) throws EquipmentAlreadyRegisteredException {
        Optional<Equipment> optSavedBeer = equipmentRepository.findByNp(np);
        if (optSavedBeer.isPresent()) {
            throw new EquipmentAlreadyRegisteredException(np);
        }
    }

    private Equipment verifyIfExists(Long id) throws EquipmentNotFoundException {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
    }

    //Jacson Ramos
    protected void verifyNpCode(EquipmentDTO equipmentDTO) throws EquipmentCodeException {
        EquipmentType type = equipmentDTO.getType();

        if (!equipmentDTO.getNp().startsWith(type.getInitialCode())){
            throw new EquipmentCodeException(equipmentDTO.getNp());
        }

    }
/*
    public EquipmentDTO increment(Long id, int quantityToIncrement) throws EquipmentNotFoundException, EquipmentsExceededException {
        Equipment equipmentToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + equipmentToIncrementStock.getQuantity();
        if (quantityAfterIncrement <= equipmentToIncrementStock.getMax()) {
            equipmentToIncrementStock.setQuantity(equipmentToIncrementStock.getQuantity() + quantityToIncrement);
            Equipment incrementedEquipmentStock = equipmentRepository.save(equipmentToIncrementStock);
            return equipmentMapper.toDTO(incrementedEquipmentStock);
        }
        throw new EquipmentsExceededException(id, quantityToIncrement);
    }
    */

}
