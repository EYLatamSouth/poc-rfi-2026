package br.com.poccore.annotation;

import br.com.poccore.service.FeatureFlagService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FeatureFlagRestrictionAspectTest {

    @InjectMocks
    private FeatureFlagRestrictionAspect aspect;

    @Mock
    private FeatureFlagService featureFlagService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsActive_FeatureFlagEnabled_ShouldProceed() throws Throwable {
        // Arrange
        Object expectedResult = "result";
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);

        when(proceedingJoinPoint.proceed()).thenReturn(expectedResult);

        // Act
        Object result = aspect.isActive(proceedingJoinPoint);

        // Assert
        verify(featureFlagService, times(1)).validateFeatureFlagIsEnable(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsActive_FeatureFlagDisabled_ShouldThrowException() throws Throwable {
        // Arrange
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        doThrow(new RuntimeException("Feature flag disabled")).when(featureFlagService).validateFeatureFlagIsEnable(proceedingJoinPoint);

        // Act & Assert
        try {
            aspect.isActive(proceedingJoinPoint);
        } catch (RuntimeException e) {
            assertEquals("Feature flag disabled", e.getMessage());
        }

        verify(featureFlagService, times(1)).validateFeatureFlagIsEnable(proceedingJoinPoint);
        verify(proceedingJoinPoint, never()).proceed();
    }

}
