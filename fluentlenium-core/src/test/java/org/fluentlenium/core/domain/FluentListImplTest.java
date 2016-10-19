package org.fluentlenium.core.domain;

import org.assertj.core.api.ThrowableAssert;
import org.fluentlenium.adapter.FluentAdapter;
import org.fluentlenium.core.FluentControl;
import org.fluentlenium.core.components.ComponentInstantiator;
import org.fluentlenium.core.conditions.WebElementConditions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FluentListImplTest {
    @Mock
    private FluentWebElement fluentWebElement1;

    @Mock
    private FluentWebElement fluentWebElement2;

    @Mock
    private FluentWebElement fluentWebElement3;

    @Mock
    private WebDriver driver;

    private FluentList<FluentWebElement> list;
    private FluentList<FluentWebElement> emptyList;

    private FluentAdapter fluentAdapter;

    @Before
    public void before() {
        fluentAdapter = new FluentAdapter();
        fluentAdapter.initFluent(driver);

        emptyList = fluentAdapter.newFluentList();

        when(fluentWebElement1.conditions()).thenReturn(new WebElementConditions(fluentWebElement1));
        when(fluentWebElement2.conditions()).thenReturn(new WebElementConditions(fluentWebElement2));
        when(fluentWebElement3.conditions()).thenReturn(new WebElementConditions(fluentWebElement3));

        list = spy(fluentAdapter.newFluentList(fluentWebElement1, fluentWebElement2, fluentWebElement3));
    }

    @After
    public void after() {
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);
    }

    @Test
    public void testFirst() {
        assertThat(list.first()).isSameAs(fluentWebElement1);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.first();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testLast() {
        assertThat(list.last()).isSameAs(fluentWebElement3);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.last();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testEach() {
        when(fluentWebElement1.enabled()).thenReturn(true);
        when(fluentWebElement2.enabled()).thenReturn(true);
        when(fluentWebElement3.enabled()).thenReturn(true);

        assertThat(list.each().enabled()).isTrue();

        verify(fluentWebElement1).enabled();
        verify(fluentWebElement2).enabled();
        verify(fluentWebElement3).enabled();
    }

    @Test
    public void testOne() {
        when(fluentWebElement2.enabled()).thenReturn(true);
        when(fluentWebElement3.enabled()).thenReturn(true);

        assertThat(list.one().enabled()).isTrue();

        verify(fluentWebElement1).enabled();
        verify(fluentWebElement2).enabled();
        verify(fluentWebElement3, never()).enabled();
    }

    @Test
    public void testClick() {
        when(fluentWebElement2.conditions().clickable()).thenReturn(true);
        when(fluentWebElement3.conditions().clickable()).thenReturn(true);

        list.click();

        verify(fluentWebElement1, never()).click();
        verify(fluentWebElement2).click();
        verify(fluentWebElement3).click();

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.click();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        when(fluentWebElement2.conditions().clickable()).thenReturn(false);
        when(fluentWebElement3.conditions().clickable()).thenReturn(false);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.contextClick();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("has no element clickable");
    }

    @Test
    public void testDoubleClick() {
        when(fluentWebElement2.conditions().clickable()).thenReturn(true);
        when(fluentWebElement3.conditions().clickable()).thenReturn(true);

        list.doubleClick();

        verify(fluentWebElement1, never()).doubleClick();
        verify(fluentWebElement2).doubleClick();
        verify(fluentWebElement3).doubleClick();

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.doubleClick();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        when(fluentWebElement2.conditions().clickable()).thenReturn(false);
        when(fluentWebElement3.conditions().clickable()).thenReturn(false);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.contextClick();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("has no element clickable");
    }

    @Test
    public void testContextClick() {
        when(fluentWebElement2.conditions().clickable()).thenReturn(true);
        when(fluentWebElement3.conditions().clickable()).thenReturn(true);

        list.contextClick();

        verify(fluentWebElement1, never()).contextClick();
        verify(fluentWebElement2).contextClick();
        verify(fluentWebElement3).contextClick();

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.contextClick();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        when(fluentWebElement2.conditions().clickable()).thenReturn(false);
        when(fluentWebElement3.conditions().clickable()).thenReturn(false);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.contextClick();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("has no element clickable");
    }

    @Test
    public void testText() {
        when(fluentWebElement2.displayed()).thenReturn(true);
        when(fluentWebElement3.displayed()).thenReturn(true);
        when(fluentWebElement2.enabled()).thenReturn(true);
        when(fluentWebElement3.enabled()).thenReturn(true);

        list.write("abc");

        verify(fluentWebElement1, never()).write("abc");
        verify(fluentWebElement2).write("abc");
        verify(fluentWebElement3).write("abc");

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.write("abc");
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.write("abc");
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void testSubmit() {
        when(fluentWebElement2.enabled()).thenReturn(true);
        when(fluentWebElement3.enabled()).thenReturn(true);

        list.submit();

        verify(fluentWebElement1, never()).submit();
        verify(fluentWebElement2).submit();
        verify(fluentWebElement3).submit();

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.submit();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        when(fluentWebElement2.enabled()).thenReturn(false);
        when(fluentWebElement3.enabled()).thenReturn(false);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.submit();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("has no element enabled");

    }

    @Test
    public void testClearAll() {
        when(fluentWebElement2.enabled()).thenReturn(true);
        when(fluentWebElement3.enabled()).thenReturn(true);

        list.clearAll();

        verify(fluentWebElement1, never()).clear();
        verify(fluentWebElement2).clear();
        verify(fluentWebElement3).clear();

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                emptyList.clearAll();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        when(fluentWebElement2.enabled()).thenReturn(false);
        when(fluentWebElement3.enabled()).thenReturn(false);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.submit();
            }
        }).isExactlyInstanceOf(NoSuchElementException.class).hasMessageContaining("has no element enabled");
    }

    @Test
    public void testProperties() {
        when(fluentWebElement1.value()).thenReturn("1");
        when(fluentWebElement2.value()).thenReturn("2");
        when(fluentWebElement3.value()).thenReturn("3");

        assertThat(list.values()).containsExactly("1", "2", "3");
        assertThat(list.value()).isEqualTo("1");
        assertThat(emptyList.values()).isEmpty();
        assertThat(emptyList.value()).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.id()).thenReturn("1");
        when(fluentWebElement2.id()).thenReturn("2");
        when(fluentWebElement3.id()).thenReturn("3");

        assertThat(list.ids()).containsExactly("1", "2", "3");
        assertThat(list.id()).isEqualTo("1");
        assertThat(emptyList.ids()).isEmpty();
        assertThat(emptyList.id()).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.attribute("attr")).thenReturn("1");
        when(fluentWebElement2.attribute("attr")).thenReturn("2");
        when(fluentWebElement3.attribute("attr")).thenReturn("3");

        assertThat(list.attributes("attr")).containsExactly("1", "2", "3");
        assertThat(list.attribute("attr")).isEqualTo("1");
        assertThat(emptyList.attributes("attr")).isEmpty();
        assertThat(emptyList.attribute("attr")).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.name()).thenReturn("1");
        when(fluentWebElement2.name()).thenReturn("2");
        when(fluentWebElement3.name()).thenReturn("3");

        assertThat(list.names()).containsExactly("1", "2", "3");
        assertThat(list.name()).isEqualTo("1");
        assertThat(emptyList.names()).isEmpty();
        assertThat(emptyList.name()).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.tagName()).thenReturn("1");
        when(fluentWebElement2.tagName()).thenReturn("2");
        when(fluentWebElement3.tagName()).thenReturn("3");

        assertThat(list.tagNames()).containsExactly("1", "2", "3");
        assertThat(list.tagName()).isEqualTo("1");
        assertThat(emptyList.tagNames()).isEmpty();
        assertThat(emptyList.tagName()).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.text()).thenReturn("1");
        when(fluentWebElement2.text()).thenReturn("2");
        when(fluentWebElement3.text()).thenReturn("3");

        assertThat(list.texts()).containsExactly("1", "2", "3");
        assertThat(list.text()).isEqualTo("1");
        assertThat(emptyList.texts()).isEmpty();
        assertThat(emptyList.text()).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.textContent()).thenReturn("1");
        when(fluentWebElement2.textContent()).thenReturn("2");
        when(fluentWebElement3.textContent()).thenReturn("3");

        assertThat(list.textContents()).containsExactly("1", "2", "3");
        assertThat(list.textContent()).isEqualTo("1");
        assertThat(emptyList.textContents()).isEmpty();
        assertThat(emptyList.textContent()).isNull();
        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.value()).thenReturn("1");
        when(fluentWebElement2.value()).thenReturn("2");
        when(fluentWebElement3.value()).thenReturn("3");

        assertThat(list.values()).containsExactly("1", "2", "3");
        assertThat(list.value()).isEqualTo("1");
        assertThat(emptyList.values()).isEmpty();
        assertThat(emptyList.value()).isNull();

        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);
    }

    @Test
    public void testFind() {
        final FluentWebElement ret1 = mock(FluentWebElement.class);
        final FluentWebElement ret2 = mock(FluentWebElement.class);
        final FluentWebElement ret3 = mock(FluentWebElement.class);

        when(fluentWebElement1.find()).thenReturn(fluentAdapter.newFluentList(ret1));
        when(fluentWebElement2.find()).thenReturn(fluentAdapter.newFluentList(ret2));
        when(fluentWebElement3.find()).thenReturn(fluentAdapter.newFluentList(ret3));

        assertThat(list.el().now()).isSameAs(ret1);
        assertThat(list.find()).containsExactly(ret1, ret2, ret3);
        assertThat(list.find().index(1)).isSameAs(ret2);
        assertThat(list.$()).containsExactly(ret1, ret2, ret3);
        assertThat(list.$().index(1)).isSameAs(ret2);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.find().index(3);
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.find(".test")).thenReturn(fluentAdapter.newFluentList(ret1));
        when(fluentWebElement2.find(".test")).thenReturn(fluentAdapter.newFluentList(ret2));
        when(fluentWebElement3.find(".test")).thenReturn(fluentAdapter.newFluentList(ret3));

        assertThat(list.el(".test")).isSameAs(ret1);
        assertThat(list.find(".test")).containsExactly(ret1, ret2, ret3);
        assertThat(list.find(".test").index(1)).isSameAs(ret2);
        assertThat(list.$(".test")).containsExactly(ret1, ret2, ret3);
        assertThat(list.$(".test").index(1)).isSameAs(ret2);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.find(".test").index(3);
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);

        when(fluentWebElement1.find(By.cssSelector(".test"))).thenReturn(fluentAdapter.newFluentList(ret1));
        when(fluentWebElement2.find(By.cssSelector(".test"))).thenReturn(fluentAdapter.newFluentList(ret2));
        when(fluentWebElement3.find(By.cssSelector(".test"))).thenReturn(fluentAdapter.newFluentList(ret3));

        assertThat(list.el(By.cssSelector(".test"))).isSameAs(ret1);
        assertThat(list.find(By.cssSelector(".test"))).containsExactly(ret1, ret2, ret3);
        assertThat(list.find(By.cssSelector(".test")).index(1)).isSameAs(ret2);
        assertThat(list.$(By.cssSelector(".test"))).containsExactly(ret1, ret2, ret3);
        assertThat(list.$(By.cssSelector(".test")).index(1)).isSameAs(ret2);

        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                list.find(By.cssSelector(".test")).index(3);
            }
        }).isExactlyInstanceOf(NoSuchElementException.class);

        reset(fluentWebElement1, fluentWebElement2, fluentWebElement3);
    }

    @Test
    public void testNowTrue() {
        list.now(true);
        verify(list).reset();
        verify(list).now();
    }

    @Test
    public void testNowFalse() {
        list.now(false);
        verify(list, never()).reset();
        verify(list).now();
    }

    @Test
    public void testAs() {
        final FluentList<Component> as = list.as(Component.class);
        assertThat(as).hasSameSizeAs(list);
    }

    private static class Component extends FluentWebElement {
        Component(final WebElement webElement, final FluentControl fluentControl, final ComponentInstantiator instantiator) {
            super(webElement, fluentControl, instantiator);
        }
    }

}
