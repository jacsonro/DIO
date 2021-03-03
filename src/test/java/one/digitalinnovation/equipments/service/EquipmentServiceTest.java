package one.digitalinnovation.equipments.service;

import one.digitalinnovation.equipments.builder.EquipmentDTOBuilder;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.entity.Equipment;
import one.digitalinnovation.equipments.exception.EquipmentAlreadyRegisteredException;
import one.digitalinnovation.equipments.exception.EquipmentCodeException;
import one.digitalinnovation.equipments.exception.EquipmentNotFoundException;
import one.digitalinnovation.equipments.exception.EquipmentsExceededException;
import one.digitalinnovation.equipments.mapper.EquipmentMapper;
import one.digitalinnovation.equipments.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {

    private static final long INVALID_EQUIPMENT_ID = 1L;

    @Mock
    private EquipmentRepository equipmentRepository;

    private final EquipmentMapper equipmentMapper = EquipmentMapper.INSTANCE;

    @InjectMocks
    private EquipmentService equipmentService;

    @Test
    void WhenEquipmentCodeThrowsAnExceptionShouldBeThrown(){
        // given
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        expectedEquipmentDTO.setNp("140160001");

        //then
        assertThrows(EquipmentCodeException.class, () -> equipmentService.verifyNpCode(expectedEquipmentDTO));

    }


    @Test
    void whenEquipmentInformedThenItShouldBeCreated() throws EquipmentAlreadyRegisteredException, EquipmentCodeException {
        // given
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        Equipment expectedSavedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        // when
        when(equipmentRepository.findByNp(expectedEquipmentDTO.getNp())).thenReturn(Optional.empty());
        when(equipmentRepository.save(expectedSavedEquipment)).thenReturn(expectedSavedEquipment);

        //then
        EquipmentDTO createdEquipmentDTO = equipmentService.createEquipment(expectedEquipmentDTO);

        assertThat(createdEquipmentDTO.getId(), is(equalTo(expectedEquipmentDTO.getId())));
        assertThat(createdEquipmentDTO.getNp(), is(equalTo(expectedEquipmentDTO.getNp())));
    }

    @Test
    void whenAlreadyRegisteredEquipmentInformedThenAnExceptionShouldBeThrown() {
        // given
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        Equipment duplicatedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        // when
        when(equipmentRepository.findByNp(expectedEquipmentDTO.getNp())).thenReturn(Optional.of(duplicatedEquipment));

        // then
        assertThrows(EquipmentAlreadyRegisteredException.class, () -> equipmentService.createEquipment(expectedEquipmentDTO));
    }

    @Test
    void whenValidEquipmentNameIsGivenThenReturnAEquipment() throws EquipmentNotFoundException {
        // given
        EquipmentDTO expectedFoundEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        Equipment expectedFoundEquipment = equipmentMapper.toModel(expectedFoundEquipmentDTO);

        // when
        when(equipmentRepository.findByNp(expectedFoundEquipment.getNp())).thenReturn(Optional.of(expectedFoundEquipment));

        // then
        EquipmentDTO foundEquipmentDTO = equipmentService.findByNp(expectedFoundEquipmentDTO.getNp());

        assertThat(foundEquipmentDTO, is(equalTo(expectedFoundEquipmentDTO)));
    }

    @Test
    void whenNotRegisteredEquipmentNameIsGivenThenThrowAnException() {
        // given
        EquipmentDTO expectedFoundEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        // when
        when(equipmentRepository.findByNp(expectedFoundEquipmentDTO.getNp())).thenReturn(Optional.empty());

        // then
        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.findByNp(expectedFoundEquipmentDTO.getNp()));
    }

    @Test
    void whenListEquipmentIsCalledThenReturnAListOfEquipments() {
        // given
        EquipmentDTO expectedFoundEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        Equipment expectedFoundEquipment = equipmentMapper.toModel(expectedFoundEquipmentDTO);

        //when
        when(equipmentRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundEquipment));

        //then
        List<EquipmentDTO> foundListEquipmentsDTO = equipmentService.listAll();

        assertThat(foundListEquipmentsDTO, is(not(empty())));
        assertThat(foundListEquipmentsDTO.get(0), is(equalTo(expectedFoundEquipmentDTO)));
    }

    @Test
    void whenListEquipmentIsCalledThenReturnAnEmptyListOfEquipments() {
        //when
        when(equipmentRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<EquipmentDTO> foundListEquipmentsDTO = equipmentService.listAll();

        assertThat(foundListEquipmentsDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAEquipmentShouldBeDeleted() throws EquipmentNotFoundException {
        // given
        EquipmentDTO expectedDeletedEquipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        Equipment expectedDeletedEquipment = equipmentMapper.toModel(expectedDeletedEquipmentDTO);

        // when
        when(equipmentRepository.findById(expectedDeletedEquipmentDTO.getId())).thenReturn(Optional.of(expectedDeletedEquipment));
        doNothing().when(equipmentRepository).deleteById(expectedDeletedEquipmentDTO.getId());

        // then
        equipmentService.deleteById(expectedDeletedEquipmentDTO.getId());

        verify(equipmentRepository, times(1)).findById(expectedDeletedEquipmentDTO.getId());
        verify(equipmentRepository, times(1)).deleteById(expectedDeletedEquipmentDTO.getId());
    }
}
