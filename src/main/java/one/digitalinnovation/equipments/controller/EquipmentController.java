package one.digitalinnovation.equipments.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.dto.QuantityDTO;
import one.digitalinnovation.equipments.exception.EquipmentAlreadyRegisteredException;
import one.digitalinnovation.equipments.exception.EquipmentNotFoundException;
import one.digitalinnovation.equipments.exception.EquipmentsExceededException;
import one.digitalinnovation.equipments.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EquipmentController implements EquipmentControllerDocs {

    private final EquipmentService equipmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentDTO createBeer(@RequestBody @Valid EquipmentDTO equipmentDTO) throws EquipmentAlreadyRegisteredException {
        return equipmentService.createBeer(equipmentDTO);
    }

    @GetMapping("/{name}")
    public EquipmentDTO findByName(@PathVariable String name) throws EquipmentNotFoundException {
        return equipmentService.findByName(name);
    }

    @GetMapping
    public List<EquipmentDTO> listBeers() {
        return equipmentService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws EquipmentNotFoundException {
        equipmentService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public EquipmentDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws EquipmentNotFoundException, EquipmentsExceededException {
        return equipmentService.increment(id, quantityDTO.getQuantity());
    }
}
