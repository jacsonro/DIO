package one.digitalinnovation.equipments.service;

import one.digitalinnovation.equipments.builder.EquipmentDTOBuilder;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.entity.Equipment;
import one.digitalinnovation.equipments.exception.EquipmentAlreadyRegisteredException;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private EquipmentRepository equipmentRepository;

    private EquipmentMapper equipmentMapper = EquipmentMapper.INSTANCE;

    @InjectMocks
    private EquipmentService equipmentService;

    @Test
    void whenBeerInformedThenItShouldBeCreated() throws EquipmentAlreadyRegisteredException {
        // given
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedSavedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        // when
        when(equipmentRepository.findByName(expectedEquipmentDTO.getName())).thenReturn(Optional.empty());
        when(equipmentRepository.save(expectedSavedEquipment)).thenReturn(expectedSavedEquipment);

        //then
        EquipmentDTO createdEquipmentDTO = equipmentService.createBeer(expectedEquipmentDTO);

        assertThat(createdEquipmentDTO.getId(), is(equalTo(expectedEquipmentDTO.getId())));
        assertThat(createdEquipmentDTO.getName(), is(equalTo(expectedEquipmentDTO.getName())));
        assertThat(createdEquipmentDTO.getQuantity(), is(equalTo(expectedEquipmentDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        // given
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment duplicatedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        // when
        when(equipmentRepository.findByName(expectedEquipmentDTO.getName())).thenReturn(Optional.of(duplicatedEquipment));

        // then
        assertThrows(EquipmentAlreadyRegisteredException.class, () -> equipmentService.createBeer(expectedEquipmentDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws EquipmentNotFoundException {
        // given
        EquipmentDTO expectedFoundEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedFoundEquipment = equipmentMapper.toModel(expectedFoundEquipmentDTO);

        // when
        when(equipmentRepository.findByName(expectedFoundEquipment.getName())).thenReturn(Optional.of(expectedFoundEquipment));

        // then
        EquipmentDTO foundEquipmentDTO = equipmentService.findByName(expectedFoundEquipmentDTO.getName());

        assertThat(foundEquipmentDTO, is(equalTo(expectedFoundEquipmentDTO)));
    }

    @Test
    void whenNotRegisteredBeerNameIsGivenThenThrowAnException() {
        // given
        EquipmentDTO expectedFoundEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();

        // when
        when(equipmentRepository.findByName(expectedFoundEquipmentDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.findByName(expectedFoundEquipmentDTO.getName()));
    }

    @Test
    void whenListBeerIsCalledThenReturnAListOfBeers() {
        // given
        EquipmentDTO expectedFoundEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedFoundEquipment = equipmentMapper.toModel(expectedFoundEquipmentDTO);

        //when
        when(equipmentRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundEquipment));

        //then
        List<EquipmentDTO> foundListBeersDTO = equipmentService.listAll();

        assertThat(foundListBeersDTO, is(not(empty())));
        assertThat(foundListBeersDTO.get(0), is(equalTo(expectedFoundEquipmentDTO)));
    }

    @Test
    void whenListBeerIsCalledThenReturnAnEmptyListOfBeers() {
        //when
        when(equipmentRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<EquipmentDTO> foundListBeersDTO = equipmentService.listAll();

        assertThat(foundListBeersDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABeerShouldBeDeleted() throws EquipmentNotFoundException {
        // given
        EquipmentDTO expectedDeletedEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedDeletedEquipment = equipmentMapper.toModel(expectedDeletedEquipmentDTO);

        // when
        when(equipmentRepository.findById(expectedDeletedEquipmentDTO.getId())).thenReturn(Optional.of(expectedDeletedEquipment));
        doNothing().when(equipmentRepository).deleteById(expectedDeletedEquipmentDTO.getId());

        // then
        equipmentService.deleteById(expectedDeletedEquipmentDTO.getId());

        verify(equipmentRepository, times(1)).findById(expectedDeletedEquipmentDTO.getId());
        verify(equipmentRepository, times(1)).deleteById(expectedDeletedEquipmentDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementBeerStock() throws EquipmentNotFoundException, EquipmentsExceededException {
        //given
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        //when
        when(equipmentRepository.findById(expectedEquipmentDTO.getId())).thenReturn(Optional.of(expectedEquipment));
        when(equipmentRepository.save(expectedEquipment)).thenReturn(expectedEquipment);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedEquipmentDTO.getQuantity() + quantityToIncrement;

        // then
        EquipmentDTO incrementedEquipmentDTO = equipmentService.increment(expectedEquipmentDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedEquipmentDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedEquipmentDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        when(equipmentRepository.findById(expectedEquipmentDTO.getId())).thenReturn(Optional.of(expectedEquipment));

        int quantityToIncrement = 80;
        assertThrows(EquipmentsExceededException.class, () -> equipmentService.increment(expectedEquipmentDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        EquipmentDTO expectedEquipmentDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
        Equipment expectedEquipment = equipmentMapper.toModel(expectedEquipmentDTO);

        when(equipmentRepository.findById(expectedEquipmentDTO.getId())).thenReturn(Optional.of(expectedEquipment));

        int quantityToIncrement = 45;
        assertThrows(EquipmentsExceededException.class, () -> equipmentService.increment(expectedEquipmentDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(equipmentRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.increment(INVALID_BEER_ID, quantityToIncrement));
    }
//
//    @Test
//    void whenDecrementIsCalledThenDecrementBeerStock() throws EquipmentNotFoundException, EquipmentsExceededException {
//        EquipmentDTO expectedBeerDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
//        Equipment expectedBeer = equipmentMapper.toModel(expectedBeerDTO);
//
//        when(equipmentRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(equipmentRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 5;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        EquipmentDTO incrementedBeerDTO = equipmentService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//        assertThat(expectedQuantityAfterDecrement, greaterThan(0));
//    }
//
//    @Test
//    void whenDecrementIsCalledToEmptyStockThenEmptyBeerStock() throws EquipmentNotFoundException, EquipmentsExceededException {
//        EquipmentDTO expectedBeerDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
//        Equipment expectedBeer = equipmentMapper.toModel(expectedBeerDTO);
//
//        when(equipmentRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//        when(equipmentRepository.save(expectedBeer)).thenReturn(expectedBeer);
//
//        int quantityToDecrement = 10;
//        int expectedQuantityAfterDecrement = expectedBeerDTO.getQuantity() - quantityToDecrement;
//        EquipmentDTO incrementedBeerDTO = equipmentService.decrement(expectedBeerDTO.getId(), quantityToDecrement);
//
//        assertThat(expectedQuantityAfterDecrement, equalTo(0));
//        assertThat(expectedQuantityAfterDecrement, equalTo(incrementedBeerDTO.getQuantity()));
//    }
//
//    @Test
//    void whenDecrementIsLowerThanZeroThenThrowException() {
//        EquipmentDTO expectedBeerDTO = EquipmentDTOBuilder.builder().build().toBeerDTO();
//        Equipment expectedBeer = equipmentMapper.toModel(expectedBeerDTO);
//
//        when(equipmentRepository.findById(expectedBeerDTO.getId())).thenReturn(Optional.of(expectedBeer));
//
//        int quantityToDecrement = 80;
//        assertThrows(EquipmentsExceededException.class, () -> equipmentService.decrement(expectedBeerDTO.getId(), quantityToDecrement));
//    }
//
//    @Test
//    void whenDecrementIsCalledWithInvalidIdThenThrowException() {
//        int quantityToDecrement = 10;
//
//        when(equipmentRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());
//
//        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.decrement(INVALID_BEER_ID, quantityToDecrement));
//    }
}
