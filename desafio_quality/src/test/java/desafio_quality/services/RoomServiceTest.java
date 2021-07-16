package desafio_quality.services;

import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Should delete a room given a valid id.")
    void testDeleteRoomWithAValidId(){
        Long roomId = 1L;

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(new Room()));
        doNothing().when(roomRepository).deleteById(any(Long.class));

        assertDoesNotThrow(() -> {
            roomService.deleteRoom(roomId);
        });
    }

    @Test
    @DisplayName("Should throw exception when deleting a room given an invalid id.")
    void testDeleteRoomWithAnInvalidId(){
        Long roomId = 11L;

        when(roomRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);
        doNothing().when(roomRepository).deleteById(any(Long.class));

        assertThrows(ResourceNotFoundException.class, () -> {
            roomService.deleteRoom(roomId);
        });
    }

    @Test
    @DisplayName("Should return a room when finding with a valid id.")
    void testFindRoomByIdWithAValidId(){
        Long roomId = 1L;

        District mockDistrict = new District("Costa e Silva", new BigDecimal("3000"));
        Property mockProperty = new Property("Casinha", mockDistrict);
        Room mockRoom = new Room("Quarto", 1.0, 1.0, mockProperty);
        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(mockRoom));

        Room room = roomService.findById(roomId);

        District expectedDistrict = new District(mockDistrict.getName(), mockDistrict.getSquareMeterValue());
        Property expectedProperty = new Property(mockProperty.getName(), expectedDistrict);
        Room expectedRoom = new Room(mockRoom.getName(), mockRoom.getWidth(), mockRoom.getLength(), expectedProperty);
        assertThat(expectedRoom).usingRecursiveComparison().isEqualTo(room);
    }

    @Test
    @DisplayName("Should throw exception when finding a room with invalid id.")
    void testFindDistrictyByIdWithAnInvalidId(){
        Long roomId = 1L;

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            roomService.findById(roomId);
        });

        assertEquals("Room " + roomId + " does not exist.", ex.getMessage());
    }
}