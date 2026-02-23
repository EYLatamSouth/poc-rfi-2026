package com.br.pococc.occ.validators;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PocProductQuestionValidator implements Validator {

    private static final String FIELD_REQUIRED = "field.required";

    @Override
    public boolean supports(Class<?> clazz) {
        return PocProductQuestionWsDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if(target instanceof PocProductQuestionWsDTO questionWsDTO) {
            if(StringUtils.isBlank(questionWsDTO.getUserId())) {
                errors.rejectValue("userId", FIELD_REQUIRED);
            }

            if(StringUtils.isBlank(questionWsDTO.getQuestion())) {
                errors.rejectValue("question", FIELD_REQUIRED);
            }
        } else {
            errors.reject("Invalid object type. Expected PocProductQuestionWsDTO.");
        }
    }
}
