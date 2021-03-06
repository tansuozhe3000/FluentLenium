package org.fluentlenium.core.css;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.fluentlenium.core.script.JavascriptControl;
import org.fluentlenium.core.wait.AwaitControl;
import org.openqa.selenium.WebDriverException;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Features related to CSS loaded in the active page.
 */
public class CssSupportImpl implements CssSupport {

    private final JavascriptControl javascriptControl;
    private final AwaitControl awaitControl;

    /**
     * Creates a new implementation of css support
     *
     * @param javascriptControl javascript control
     * @param awaitControl      await control
     */
    public CssSupportImpl(final JavascriptControl javascriptControl, final AwaitControl awaitControl) {
        this.javascriptControl = javascriptControl;
        this.awaitControl = awaitControl;
    }

    @Override
    public void inject(String cssText) {
        final InputStream injectorScript = this.getClass().getResourceAsStream("/org/fluentlenium/core/css/injector.js");
        String injectorJs;
        try {
            injectorJs = IOUtils.toString(injectorScript, Charset.forName("UTF-8"));
        } catch (final IOException e) {
            throw new IOError(e);
        } finally {
            IOUtils.closeQuietly(injectorScript);
        }
        cssText = cssText.replace("\r\n", "").replace("\n", "");
        cssText = StringEscapeUtils.escapeEcmaScript(cssText);
        this.executeScriptRetry("cssText = \"" + cssText + "\"" + ";\n" + injectorJs);
    }

    @Override
    public void injectResource(final String cssResourceName) {
        final InputStream cssStream = this.getClass().getResourceAsStream(cssResourceName);
        String cssText;
        try {
            cssText = IOUtils.toString(cssStream, Charset.forName("UTF-8"));
        } catch (final IOException e) {
            throw new IOError(e);
        } finally {
            IOUtils.closeQuietly(cssStream);
        }
        this.inject(cssText);
    }

    private void executeScriptRetry(final String script) {
        int retries = 0;
        while (true) {
            try {
                javascriptControl.executeScript(script);
                break;
            } catch (final WebDriverException e) {
                retries += 1;
                if (retries >= 10) {
                    throw e;
                }
                awaitControl.await().explicitlyFor(250L);
            }
        }
    }
}
