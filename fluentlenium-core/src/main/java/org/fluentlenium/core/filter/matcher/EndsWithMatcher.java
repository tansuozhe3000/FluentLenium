package org.fluentlenium.core.filter.matcher;

import java.util.regex.Pattern;

/**
 * Matcher checking that actual ends with input value.
 */
public class EndsWithMatcher extends AbstractMatcher {

    /**
     * Creates a ends with matcher.
     *
     * @param value input value
     */
    public EndsWithMatcher(final String value) {
        super(value);
    }

    /**
     * Creates a ends with matcher.
     *
     * @param value input value
     */
    public EndsWithMatcher(final Pattern value) {
        super(value);
    }

    @Override
    public MatcherType getMatcherType() {
        return MatcherType.ENDS_WITH;
    }

    @Override
    public boolean isSatisfiedBy(final String obj) {
        return CalculateService.endsWith(getPattern(), getValue(), obj);
    }

}
