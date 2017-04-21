package org.elasticsearch.plugin;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.test.ESTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.plugin.HttpRequestTypes.GET;
import static org.hamcrest.Matchers.hasEntry;

public abstract class BaseTest extends ESTestCase{
    protected static final Logger staticLogger = ESLoggerFactory.getLogger("test");
    protected final static int HTTP_TEST_PORT = 9400;
    protected static RestClient client;

    @BeforeClass
    public static void startRestClient() {
        client = RestClient.builder(new HttpHost("localhost", HTTP_TEST_PORT)).build();

        try {
            Response response = client.performRequest(GET, "/");
            Map<String, Object> responseMap = null; // entityAsMap;
            assertThat(responseMap, hasEntry("tagline", "You Know, for Search"));
            staticLogger.info("Tests is ready to start.. Cluster is running.");
        } catch (IOException ioException) {

        }
    }

    @AfterClass
    public static void stopRestClient() throws IOException {
        if (client != null) {
            client.close();
            client = null;
        }

        staticLogger.info("Stopping tests against an external cluster");
    }
}
