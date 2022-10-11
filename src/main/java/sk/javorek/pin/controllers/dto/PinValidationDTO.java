package sk.javorek.pin.controllers.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PinValidationDTO {

    @NotBlank(message = "The PIN code cannot be empty.")
    @Pattern(regexp = "^\\d{4}$", message = "The PIN has an invalid range or syntax.")
    private String pin;

    public PinValidationDTO() {
        super();
        this.pin = null;
    }

    public PinValidationDTO(String pin) {
        super();
        this.pin = pin;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        PinValidationDTO other = (PinValidationDTO) obj;
        return Objects.equals(this.pin, other.pin);
    }

    @Override
    public String toString() {
        return "PinDTO [pin=" + this.pin + "]";
    }

}
