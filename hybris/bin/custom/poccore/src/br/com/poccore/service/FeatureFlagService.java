package br.com.poccore.service;

import br.com.poccore.exceptions.FeatureFlagRestrictionException;
import org.aspectj.lang.ProceedingJoinPoint;

public interface FeatureFlagService {
    void validateFeatureFlagIsEnable(ProceedingJoinPoint proceedingJoinPoint) throws FeatureFlagRestrictionException;
}
