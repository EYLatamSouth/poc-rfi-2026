package br.com.poccore.service.impl;

import br.com.poccore.annotation.FeatureFlagRestriction;
import br.com.poccore.dao.FeatureFlagDao;
import br.com.poccore.exceptions.FeatureFlagRestrictionException;
import br.com.poccore.model.FeatureFlagModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultFeatureFlagServiceTest {

    private DefaultFeatureFlagService service;
    private FeatureFlagDao featureFlagDao;
    private ProceedingJoinPoint proceedingJoinPoint;
    private MethodSignature methodSignature;

    @Before
    public void setUp() {
        service = new DefaultFeatureFlagService();
        featureFlagDao = mock(FeatureFlagDao.class);
        proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        methodSignature = mock(MethodSignature.class);

        // Inject mock DAO into service
        service.setFeatureFlagDao(featureFlagDao);
    }

    // Helper method to create a method with FeatureFlagRestriction annotation dynamically
    private Method getAnnotatedMethod() throws NoSuchMethodException {
        class TestClass {
            @FeatureFlagRestriction(name = "testFeature")
            public void annotatedMethod() {
            }
        }
        return TestClass.class.getMethod("annotatedMethod");
    }

    @Test
    public void testValidateFeatureFlagIsEnable_FeatureFlagNotFound_ThrowsException() throws Throwable {
        Method method = getAnnotatedMethod();

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(featureFlagDao.findFeatureFlagByKey("testFeature")).thenReturn(Collections.emptyList());

        FeatureFlagRestrictionException exception = assertThrows(FeatureFlagRestrictionException.class, () -> {
            service.validateFeatureFlagIsEnable(proceedingJoinPoint);
        });

        assertEquals("Feature Flag testFeature does not Existing", exception.getMessage());
    }

    @Test
    public void testValidateFeatureFlagIsEnable_FeatureFlagDisabled_ThrowsException() throws Throwable {
        Method method = getAnnotatedMethod();

        FeatureFlagModel disabledFlag = new FeatureFlagModel();
        disabledFlag.setStatus(false);

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(featureFlagDao.findFeatureFlagByKey("testFeature")).thenReturn(List.of(disabledFlag));

        FeatureFlagRestrictionException exception = assertThrows(FeatureFlagRestrictionException.class, () -> {
            service.validateFeatureFlagIsEnable(proceedingJoinPoint);
        });

        assertEquals("Feature Flag testFeature disable", exception.getMessage());
    }

    @Test
    public void testValidateFeatureFlagIsEnable_FeatureFlagEnabled_NoException() throws Throwable {
        Method method = getAnnotatedMethod();

        FeatureFlagModel enabledFlag = new FeatureFlagModel();
        enabledFlag.setStatus(true);

        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(method);
        when(featureFlagDao.findFeatureFlagByKey("testFeature")).thenReturn(List.of(enabledFlag));

        assertDoesNotThrow(() -> {
            service.validateFeatureFlagIsEnable(proceedingJoinPoint);
        });
    }
}