package edu.usc.csci310.project;

import edu.usc.csci310.project.DTO.IdDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IdDTOTest {
    private IdDTO idDTO;

    @BeforeEach
    void setUp() {
        idDTO = new IdDTO();
    }

    @Test
    void testSetAndGetId() {
        int expectedId = 10;
        idDTO.setId(expectedId);
        assertEquals(expectedId, idDTO.getId(), "The id should match the set value.");
    }
}
