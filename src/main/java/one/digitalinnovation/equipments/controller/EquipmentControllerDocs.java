package one.digitalinnovation.equipments.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.exception.EquipmentAlreadyRegisteredException;
import one.digitalinnovation.equipments.exception.EquipmentCodeException;
import one.digitalinnovation.equipments.exception.EquipmentNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages beer stock")
public interface EquipmentControllerDocs {

    @ApiOperation(value = "Equipment creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success beer creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) throws EquipmentAlreadyRegisteredException, EquipmentCodeException;

    @ApiOperation(value = "Returns Equipment found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success Equipment found in the system"),
            @ApiResponse(code = 404, message = "Equipment with given name not found.")
    })
    EquipmentDTO findByNp(@PathVariable String np) throws EquipmentNotFoundException;

    @ApiOperation(value = "Returns a list of all Equipments registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all Equipments registered in the system"),
    })
    List<EquipmentDTO> listEquipments();

    @ApiOperation(value = "Delete a Equipment found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success Equipment deleted in the system"),
            @ApiResponse(code = 404, message = "Equipment with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws EquipmentNotFoundException;
}
