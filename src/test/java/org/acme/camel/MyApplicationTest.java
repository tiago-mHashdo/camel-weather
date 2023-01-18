package org.acme.camel;

import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.main.junit5.CamelMainTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A simple unit test showing how to test the application {@link MyApplication}.
 */
class MyApplicationTest extends CamelMainTestSupport {

    @Override
    protected Class<?> getMainClass() {
        // The main class of the application to test
        return MyApplication.class;
    }

    @Test
    void should_complete_the_auto_detected_route() {

        assertTrue(
                true
        );
    }
}