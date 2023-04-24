package ru.practicum.shareit.constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.user.UserDLAStorage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Component
public class AllForEmailValidator implements ConstraintValidator<AllForEmail, String> {
    UserDLAStorage userStorage;

    @Autowired
    public AllForEmailValidator(UserDLAStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public boolean isValid(final String valueToValidate, final ConstraintValidatorContext context) {

        if (valueToValidate == null || valueToValidate.isEmpty())
            return false;

//        String regexPattern = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
//                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
//        if (!patternMatches(valueToValidate, regexPattern))
//            return false;

        if (userStorage.getALL().stream().anyMatch(e -> valueToValidate.equals(e.getEmail()))) {
            throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));
        }

        return true;
    }

//    public static boolean patternMatches(String emailAddress, String regexPattern) {
//        return Pattern.compile(regexPattern)
//                .matcher(emailAddress)
//                .matches();
//    }
}
