package edu.usc.csci310.project;

import edu.usc.csci310.project.controller.ParkSearchController;
import edu.usc.csci310.project.service.Search;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ParkSearchControllerTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private Search searchService;

    private ParkSearchController controller;

    private final String parkName = "Yellowstone";
    private final String stateCode = "WY";
    private final String[] amenities = new String[]{"lodging", "camping"};
    private final String[] activities = new String[]{"hiking", "fishing"};

    @BeforeEach
    void setUp() {
        controller = new ParkSearchController(httpClient);
        try {
            Field searchServiceField = ParkSearchController.class.getDeclaredField("searchService");
            searchServiceField.setAccessible(true);
            searchServiceField.set(controller, searchService);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void constructor_InitializesSearchService() throws Exception {
        Field searchServiceField = ParkSearchController.class.getDeclaredField("searchService");
        searchServiceField.setAccessible(true);
        Search searchService = (Search) searchServiceField.get(controller);
        assertNotNull(searchService);
    }

    @Test
    void searchParks_CallsSearchServiceWithCorrectParameters() throws UnsupportedEncodingException {
        controller.searchParks(parkName, stateCode, amenities, activities);
        verify(searchService).getParks(eq(parkName), eq(stateCode), eq(amenities), eq(activities));
    }

    @Test
    void searchParks_ReturnsEmptyArrayWhenServiceReturnsNull() throws UnsupportedEncodingException {
        when(searchService.getParks(eq(parkName), eq(stateCode), eq(amenities), eq(activities))).thenReturn(null);
        String result = controller.searchParks(parkName, stateCode, amenities, activities);
        assertEquals("[]", result);
    }

    @Test
    void searchParks_ReturnsJSONArrayToString() throws UnsupportedEncodingException {
        JSONArray mockArray = new JSONArray();
        mockArray.put("park1");
        mockArray.put("park2");
        when(searchService.getParks(eq(parkName), eq(stateCode), eq(amenities), eq(activities))).thenReturn(mockArray);
        String result = controller.searchParks(parkName, stateCode, amenities, activities);
        assertEquals(mockArray.toString(), result);
    }
}
