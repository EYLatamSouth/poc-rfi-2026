package br.com.poccore.service.impl;

import br.com.poccore.annotation.FeatureFlagRestriction;
import br.com.poccore.dao.FeatureFlagDao;
import br.com.poccore.exceptions.FeatureFlagRestrictionException;
import br.com.poccore.model.FeatureFlagModel;
import br.com.poccore.service.FeatureFlagService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class DefaultFeatureFlagService implements FeatureFlagService {

    private FeatureFlagDao featureFlagDao;

    @Override
    public void validateFeatureFlagIsEnable(ProceedingJoinPoint proceedingJoinPoint) throws FeatureFlagRestrictionException {
        String name = getMethodName(getMethod(proceedingJoinPoint));

        List<FeatureFlagModel> result = getFeatureFlagDao().findFeatureFlagByKey(name);

        if (CollectionUtils.isEmpty(result)) {
            throw new FeatureFlagRestrictionException("Feature Flag " + name + " does not Existing");

        } else {
            FeatureFlagModel featureFlagModel = result.get(0);
            if (featureFlagModel != null && !featureFlagModel.isStatus())
                throw new FeatureFlagRestrictionException("Feature Flag " + name + " disable");
        }
    }

    private Method getMethod(final ProceedingJoinPoint proceedingJoinPoint) {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        return signature.getMethod();
    }

    private String getMethodName(final Method method) {
        final FeatureFlagRestriction featureFlag = AnnotationUtils.findAnnotation(method, FeatureFlagRestriction.class);
        if (Objects.nonNull(featureFlag)) return featureFlag.name();
        return "";
    }

    public FeatureFlagDao getFeatureFlagDao() {
        return featureFlagDao;
    }

    public void setFeatureFlagDao(FeatureFlagDao featureFlagDao) {
        this.featureFlagDao = featureFlagDao;
    }
}
