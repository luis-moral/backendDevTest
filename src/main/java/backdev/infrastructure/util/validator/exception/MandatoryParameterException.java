package backdev.infrastructure.util.validator.exception;

public class MandatoryParameterException extends ValidationException {

    public MandatoryParameterException(String parameter) {
        super("Parameter [" + parameter + "] is mandatory");
    }
}
