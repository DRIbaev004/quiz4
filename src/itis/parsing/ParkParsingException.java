package itis.parsing;

import java.util.List;

//Эта ошибка бросается при неудачном парсинге файла
//Для каждого поля, которое не прошло проверку, формируется свой обьект внутреннего класса "ParkValidationError".
public class ParkParsingException extends RuntimeException {

    private List<ParkValidationError> validationErrors;

    public ParkParsingException(String message, List<ParkValidationError> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public List<ParkValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ParkValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public static class ParkValidationError{

        //имя поля, которое не прошло проверку
        public String fieldName;

        //Сообщение об ошибке
        public String validationError;

        public ParkValidationError(String fieldName, String validationError) {
            this.fieldName = fieldName;
            this.validationError = validationError;
        }
    }
}


