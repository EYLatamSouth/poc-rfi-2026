package br.com.poccore;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class PocUtil {

    /**
     * Validates whether the given parameter is instance of targeted Class.
     *
     * @param parameter     The Object that will be verified.
     * @param clazz         The target Class.
     */
    public static void validateParameterType(final Object parameter, final Class clazz) {
        Preconditions.checkArgument(parameter != null, "Parameter cannot be null.");
        Preconditions.checkArgument(clazz != null, "Clazz cannot be null.");
        if (!clazz.isInstance(parameter)) {
            String message = String.format("Given parameter is from type '%s', is not instance of '%s'", parameter.getClass(), clazz);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Validates conditions for a given string.
     *
     * @param parameter     The string.
     * @param attribute     Attribute name for error message.
     * @param regex         Regex to match given parameter.
     */
    public static void validateStringValues(final String parameter, final String attribute, String regex) {
        Preconditions.checkArgument(parameter != null, String.format("%s cannot be null.", attribute));
        Preconditions.checkArgument(StringUtils.isNotEmpty(parameter), String.format("%s cannot be an empty String.", attribute));
        if (regex != null) {
            Preconditions.checkArgument(parameter.matches(regex), String.format("%s cannot be an empty String.", attribute));
        }
    }
}
