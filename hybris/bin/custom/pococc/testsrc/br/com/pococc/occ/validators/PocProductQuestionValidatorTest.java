package br.com.pococc.occ.validators;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Objects;

import static org.junit.Assert.*;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PocProductQuestionValidatorTest {

    @InjectMocks
    private PocProductQuestionValidator validator;

    @Before
    public void setUp() {}

    @Test
    public void testSupports() {
        assertTrue( validator.supports(PocProductQuestionWsDTO.class));
        assertFalse(validator.supports(String.class));
    }

     @Test
    public void testValidate() {
         PocProductQuestionWsDTO validDto = new PocProductQuestionWsDTO();
         Errors errors = new BeanPropertyBindingResult(validDto, "validDto");

         validator.validate(validDto, errors);
         assertTrue(errors.hasErrors());
         assertEquals("field.required", Objects.requireNonNull(errors.getFieldError("question")).getCode());

         validDto.setQuestion("DUMMY QUESTION");
         errors = new BeanPropertyBindingResult(validDto, "validDto");

         validator.validate(validDto, errors);
         assertFalse(errors.hasErrors());

         validDto = null;
         errors = new BeanPropertyBindingResult(validDto, "validDto");
         validator.validate(validDto, errors);
         assertTrue(errors.hasErrors());

     }

}
