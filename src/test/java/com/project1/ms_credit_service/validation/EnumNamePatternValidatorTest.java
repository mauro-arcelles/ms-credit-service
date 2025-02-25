package com.project1.ms_credit_service.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EnumNamePatternValidatorTest {

    private EnumNamePatternValidator validator;

    @MockBean
    private ConstraintValidatorContext context;

    @MockBean
    private EnumNamePattern annotation;

    @BeforeEach
    void setUp() {
        validator = new EnumNamePatternValidator();
        when(annotation.regexp()).thenReturn("ACTIVE|PAID");
        validator.initialize(annotation);
    }

    @Test
    void whenNullValue_thenValid() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void whenValidPattern_thenValid() {
        assertTrue(validator.isValid("ACTIVE", context));
    }

    @Test
    void whenInvalidPattern_thenInvalid() {
        assertFalse(validator.isValid("payment", context));
    }
}
