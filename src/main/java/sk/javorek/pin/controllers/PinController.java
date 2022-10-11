package sk.javorek.pin.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sk.javorek.pin.controllers.dto.PinValidationDTO;

@RestController
public class PinController {

    private static final Logger LOGGER = LogManager.getLogger(PinController.class);

    @PostMapping(path = "pinValidator", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> pinValidator(@Valid @RequestBody PinValidationDTO pinValidationDTO, BindingResult bindingResult) {
        PinController.LOGGER.info("PIN: '{}'", pinValidationDTO.getPin());

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .forEach(PinController.LOGGER::debug);

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok("PIN je validní");
    }

}
