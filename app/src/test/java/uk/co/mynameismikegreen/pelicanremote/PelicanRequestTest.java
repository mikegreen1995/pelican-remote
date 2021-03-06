package uk.co.mynameismikegreen.pelicanremote;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class PelicanRequestTest {


    private final PelicanRequest pelicanRequest = new PelicanRequest();

    public static final WireMockServer mockServer = new WireMockServer(wireMockConfig());

    @BeforeClass
    public static void setUpClass(){
        mockServer.start();
    }

    @After
    public void setUpTest(){
        mockServer.resetAll();
    }

    @AfterClass
    public static void cleanUpMockPelicanServer() {
        mockServer.stop();
    }

    @Test
    public void returnsResponseBodyAsString() {
        // Given: a server returning fixed data
        String serverResponse = "My server response";
        String endpoint = "/status";
        mockServer.stubFor(get(urlPathEqualTo(endpoint))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(serverResponse)
                        .withStatus(200)));

        // When: the class performs its background task
        String result = pelicanRequest.doInBackground("http://localhost:" + mockServer.port() + endpoint);

        // Then: the server response is returned
        Assert.assertEquals(serverResponse, result);
    }

}