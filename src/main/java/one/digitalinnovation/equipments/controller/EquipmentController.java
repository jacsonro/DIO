package one.digitalinnovation.equipments.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.dto.QuantityDTO;
import one.digitalinnovation.equipments.exception.EquipmentAlreadyRegisteredException;
import one.digitalinnovation.equipments.exception.EquipmentCodeException;
import one.digitalinnovation.equipments.exception.EquipmentNotFoundException;
import one.digitalinnovation.equipments.exception.EquipmentsExceededException;
import one.digitalinnovation.equipments.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/equipments")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EquipmentController implements EquipmentControllerDocs {

    private final EquipmentService equipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentDTO createEquipment(@RequestBody @Valid EquipmentDTO equipmentDTO) throws EquipmentAlreadyRegisteredException, EquipmentCodeException {
        return equipmentService.createEquipment(equipmentDTO);
    }

    @GetMapping("/{np}")
    public EquipmentDTO findByNp(@PathVariable String np) throws EquipmentNotFoundException {
        return equipmentService.findByNp(np);
    }

    @GetMapping
    public List<EquipmentDTO> listEquipments() {
        return equipmentService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws EquipmentNotFoundException {
        equipmentService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public EquipmentDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws EquipmentNotFoundException, EquipmentsExceededException {
        return null;//////////////////////equipmentService.increment(id, quantityDTO.getQuantity());
    }
}
