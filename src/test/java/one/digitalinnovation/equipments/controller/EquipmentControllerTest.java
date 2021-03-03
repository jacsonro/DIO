package one.digitalinnovation.equipments.controller;

import one.digitalinnovation.equipments.builder.EquipmentDTOBuilder;
import one.digitalinnovation.equipments.dto.EquipmentDTO;
import one.digitalinnovation.equipments.dto.QuantityDTO;
import one.digitalinnovation.equipments.exception.EquipmentNotFoundException;
import one.digitalinnovation.equipments.service.EquipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.equipments.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EquipmentControllerTest {

    private static final String EQUIPMENT_API_URL_PATH = "/api/v1/equipments";
    private static final long VALID_EQUIPMENT_ID = 1L;
    private static final long INVALID_EQUIPMENT_ID = 2l;
    private static final String EQUIPMENT_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String EQUIPMENT_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAEquipmentIsCreated() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        // when
        when(equipmentService.createEquipment(equipmentDTO)).thenReturn(equipmentDTO);

        // then
        mockMvc.perform(post(EQUIPMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(equipmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.np", is(equipmentDTO.getNp())))
                .andExpect(jsonPath("$.brand", is(equipmentDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(equipmentDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();
        equipmentDTO.setBrand(null);

        // then
        mockMvc.perform(post(EQUIPMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(equipmentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        //when
        when(equipmentService.findByNp(equipmentDTO.getNp())).thenReturn(equipmentDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(EQUIPMENT_API_URL_PATH + "/" + equipmentDTO.getNp())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(equipmentDTO.getNp())))
                .andExpect(jsonPath("$.brand", is(equipmentDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(equipmentDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        //when
        when(equipmentService.findByNp(equipmentDTO.getNp())).thenThrow(EquipmentNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(EQUIPMENT_API_URL_PATH + "/" + equipmentDTO.getNp())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithEquipmentsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        //when
        when(equipmentService.listAll()).thenReturn(Collections.singletonList(equipmentDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(EQUIPMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(equipmentDTO.getNp())))
                .andExpect(jsonPath("$[0].brand", is(equipmentDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(equipmentDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutEquipmentsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        //when
        when(equipmentService.listAll()).thenReturn(Collections.singletonList(equipmentDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(EQUIPMENT_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        EquipmentDTO equipmentDTO = EquipmentDTOBuilder.builder().build().toEquipmentDTO();

        //when
        doNothing().when(equipmentService).deleteById(equipmentDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(EQUIPMENT_API_URL_PATH + "/" + equipmentDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(EquipmentNotFoundException.class).when(equipmentService).deleteById(INVALID_EQUIPMENT_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(EQUIPMENT_API_URL_PATH + "/" + INVALID_EQUIPMENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
