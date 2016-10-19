package org.fluentlenium.integration;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.integration.localtest.IntegrationFluentTest;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;

public class ProxyLazynessTest extends IntegrationFluentTest {

    @Test
    public void testMissingElementList() {
        final FluentList<FluentWebElement> fluentWebElements = $("#missing");

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                fluentWebElements.now();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("#missing");;
    }

    @Test
    public void testMissingElement() {
        final FluentWebElement fluentWebElement = el("#missing");

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                fluentWebElement.now();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("#missing");
    }

    @Test
    public void testMissingElementChainList() {
        final FluentList fluentWebElements = $("#missing1").$("#missing2").reset();

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                fluentWebElements.now();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("#missing1");
    }

    @Test
    public void testMissingElementChain() {
        final FluentWebElement fluentWebElement = el("#missing1").el("#missing2");

        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                fluentWebElement.now();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("#missing2");
    }
}
