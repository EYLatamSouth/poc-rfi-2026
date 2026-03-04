package br.com.poccore.annotation;

import br.com.poccore.service.FeatureFlagService;
import org.aspectj.lang.ProceedingJoinPoint;

public class FeatureFlagRestrictionAspect {

    private FeatureFlagService featureFlagService;

    public Object isActive(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        getFeatureFlagService().validateFeatureFlagIsEnable(proceedingJoinPoint);
        return proceedingJoinPoint.proceed();
    }

    public FeatureFlagService getFeatureFlagService() {
        return featureFlagService;
    }

    public void setFeatureFlagService(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }
}
