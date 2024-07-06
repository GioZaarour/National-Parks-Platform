package edu.usc.csci310.project;


import edu.usc.csci310.project.service.Search;
import org.json.JSONException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SearchTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    @Mock
    private HttpResponse<String> mockResponse;

    @InjectMocks
    private Search searchService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private Search search;



    @BeforeEach
    void setUp() {
        searchService = new Search(mockHttpClient);
    }

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    @Test
    void getParks_ReturnsParksOnSuccess() throws Exception {
        String jsonResponse = "{\"data\":[{\"name\":\"Yellowstone\",\"state\":\"WY\"}]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks("Yellowstone", "WY", null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        assertNotNull(result, "The result should not be null.");
        assertEquals(1, result.length(), "There should be one park in the result.");
        assertEquals("Yellowstone", result.getJSONObject(0).getString("name"), "The park name should be Yellowstone.");
        assertEquals("WY", result.getJSONObject(0).getString("state"), "The park state should be WY.");
    }

    @Test
    void getParks_ReturnsEmptyOnNoParksFound() throws Exception {
        String jsonResponse = "{\"data\":[]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks("NoSuchPark", "ZZ", null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()));
        assertNotNull(result, "The result should not be null.");
        assertEquals(0, result.length(), "The result array should be empty.");
    }

    @Test
    void searchAndPrintParks_ProcessesMultipleParks() throws Exception {
        String jsonResponse = "{\"data\":[" + "{\"name\":\"Yellowstone\",\"state\":\"WY\"}," + "{\"name\":\"Zion\",\"state\":\"UT\"}" + "]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
                .thenReturn(mockHttpResponse);
        final StringBuilder printedContent = new StringBuilder();
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {
                printedContent.append((char) b);
            }
        }));

        JSONArray parks = searchService.getParks("testPark", "testState", null, null);
        searchService.searchAndPrintParks(parks);
        System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(java.io.FileDescriptor.out)));
        String capturedOutput = printedContent.toString();
        assertTrue(capturedOutput.contains("Yellowstone"), "The output should contain 'Yellowstone'");
        assertTrue(capturedOutput.contains("Zion"), "The output should contain 'Zion'");
    }

    @Test
    void getParks_WithOnlyParkName() throws Exception {
        String jsonResponse = "{\"data\":[{\"name\":\"Yosemite\",\"state\":\"CA\"}]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks("Yosemite", "", null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        assertNotNull(result);
        assertEquals(1, result.length());
        assertEquals("Yosemite", result.getJSONObject(0).getString("name"));
    }

    @Test
    void getParks_WithOnlyStateCode() throws Exception {
        String jsonResponse = "{\"data\":[{\"name\":\"Yosemite\",\"state\":\"CA\"}]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks("", "CA", null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        assertNotNull(result);
        assertEquals(1, result.length());
        assertEquals("CA", result.getJSONObject(0).getString("state"));
    }

    @Test
    void getParks_WithNeitherParkNameNorStateCode() throws Exception {
        String jsonResponse = "{\"data\":[]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks("", "", null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        assertNotNull(result);
        assertEquals(0, result.length(), "The result array should be empty when no parameters are provided.");
    }

    @Test
    void searchAndPrintParks_PrintsParkInformation() throws Exception {
        JSONArray mockParks = new JSONArray();
        mockParks.put(new JSONObject().put("name", "Yosemite").put("state", "CA"));
        String jsonResponse = "{\"data\":[" + "{\"name\":\"Yosemite\",\"state\":\"CA\"}" + "]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenReturn(mockHttpResponse);
        JSONArray parks = searchService.getParks("NoSuchPark", "ZZ", null, null);
        searchService.searchAndPrintParks(parks);
        String printedOutput = outContent.toString();
        assertTrue(printedOutput.contains("Yosemite"), "Printed output should contain the park name.");
        assertTrue(printedOutput.contains("CA"), "Printed output should contain the state code.");
    }

    @Test
    void searchAndPrintParks_PrintsErrorMessageOnNull() throws Exception {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass()))).thenThrow(IOException.class);
        JSONArray parks = searchService.getParks("NoSuchPark", "ZZ", null, null);
        searchService.searchAndPrintParks(parks);
        String printedOutput = outContent.toString();
        assertTrue(printedOutput.contains("No parks found or there was an error."),
                "Expected error message was not printed.");
    }

    @Test
    public void testSearchAndPrintParks_HandlesJSONException() throws JSONException {
        // Create mock JSONArray that throws JSONException when accessed
        JSONArray mockParks = mock(JSONArray.class);
        when(mockParks.length()).thenReturn(1);
        when(mockParks.getJSONObject(anyInt())).thenThrow(new JSONException("Mock JSON Exception"));

        // Expect a RuntimeException to be thrown due to the JSONException
        Exception exception = assertThrows(RuntimeException.class, () -> {
            searchService.searchAndPrintParks(mockParks);
        });

        // Verify the exception message
        String expectedMessage = "Mock JSON Exception";
        String actualMessage = exception.getCause().getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        // Verify that the getParks method was called with the correct arguments
//        verify(searchService).getParks("Yosemite", "CA", null, null);
    }

    @Test
    void getParks_WithNullParkNameAndValidStateCode() throws Exception {
        String jsonResponse = "{\"data\":[]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks(null, "CA", null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        assertNotNull(result);
        // Additional assertions as needed
    }

    @Test
    void getParks_WithValidParkNameAndNullStateCode() throws Exception {
        String jsonResponse = "{\"data\":[]}";
        when(mockHttpResponse.body()).thenReturn(jsonResponse);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockHttpResponse);
        JSONArray result = searchService.getParks("Yosemite", null, null, null);
        verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        assertNotNull(result);
        // Additional assertions as needed
    }

}
