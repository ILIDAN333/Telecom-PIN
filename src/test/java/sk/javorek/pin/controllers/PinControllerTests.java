package sk.javorek.pin.controllers;

import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import sk.javorek.pin.controllers.dto.PinValidationDTO;

@WebMvcTest(PinController.class)
public class PinControllerTests {

    private static final String PATH_PIN_VALIDATOR = "/pinValidator";
    private static final String MESSAGE_VALID = "PIN je validní";
    private static final String MESSAGE_INVALID = "";

    @Autowired
    private MockMvc mockmvc;

    private static Stream<Arguments> pinProvider() {
        return Stream.of(
                Arguments.of(new PinValidationDTO("0000"), MockMvcResultMatchers.status().isOk(),
                        PinControllerTests.MESSAGE_VALID),
                Arguments.of(new PinValidationDTO("1111"), MockMvcResultMatchers.status().isOk(),
                        PinControllerTests.MESSAGE_VALID),
                Arguments.of(new PinValidationDTO("1234"), MockMvcResultMatchers.status().isOk(),
                        PinControllerTests.MESSAGE_VALID),
                Arguments.of(new PinValidationDTO("5678"), MockMvcResultMatchers.status().isOk(),
                        PinControllerTests.MESSAGE_VALID),
                Arguments.of(new PinValidationDTO("9012"), MockMvcResultMatchers.status().isOk(),
                        PinControllerTests.MESSAGE_VALID),
                Arguments.of(new PinValidationDTO("1"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("12"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("123"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO(" 1234"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("1234 "), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("1 234"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("12 34"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("123 4"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("12345"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("/*-+"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("abcd"), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO(""), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO(" "), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID),
                Arguments.of(new PinValidationDTO("    "), MockMvcResultMatchers.status().isForbidden(),
                        PinControllerTests.MESSAGE_INVALID));
    }

    @DisplayName("PIN validation")
    @ParameterizedTest(name = "{index} => PIN={0}, Status={1}, Message={2}")
    @MethodSource("pinProvider")
    public void pinValidator(PinValidationDTO pinValidationDTO, ResultMatcher status, String message) throws Exception {
        String json = new ObjectMapper().writeValueAsString(pinValidationDTO);

        this.mockmvc
                .perform(
                        MockMvcRequestBuilders.post(PinControllerTests.PATH_PIN_VALIDATOR).contentType(MediaType.APPLICATION_JSON)
                                .content(json).accept(MediaType.TEXT_PLAIN))
                .andExpect(status)
                .andExpect(MockMvcResultMatchers.content().string(Matchers.equalTo(message)));
    }

}
