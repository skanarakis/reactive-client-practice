package org.skanarakis.practice.reactiveclient;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skanarakis.practice.reactiveclient.model.ImmutableCar;
import org.skanarakis.practice.reactiveclient.model.ImmutableUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(SpringExtension.class)
public class PracticeReactiveClientApplicationTests {

    private static final int DELAY_FOR_WAITING_REACTIVE_CALL = 100;
    private Logger logger = LoggerFactory.getLogger(PracticeReactiveClientApplicationTests.class);

    private static Gson gson;
    private static HttpGetReactiveClient<ImmutableCar> carClient;
    private static HttpGetReactiveClient<ImmutableUser> userClient;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @BeforeClass
    public static void setup() {
        gson = new GsonBuilder().create();
        carClient = new HttpGetReactiveClient<>(ImmutableCar.class);
        userClient = new HttpGetReactiveClient<>(ImmutableUser.class);
    }

    @Test
    public void sendingReactiveRequestForCar_Succeeds() throws InterruptedException {

        String url = "http://127.0.0.1:8089/car";
        ImmutableCar car = ImmutableCar.builder().model("Honda").year(2010).build();
        String carJson = gson.toJson(car);

        givenThat(get(urlEqualTo("/car")).withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json").withBody(carJson)));

        carClient.sendRequestTo(url).subscribe(aCar -> {
            logger.info("Reactive Response: \n{}", aCar);
            Assertions.assertEquals(car, aCar);
        }, e -> logger.error("Error: {}", e), () -> logger.debug("Completion"));

        TimeUnit.MILLISECONDS.sleep(DELAY_FOR_WAITING_REACTIVE_CALL);
        verify(getRequestedFor(urlEqualTo("/car")));
    }

    @Test
    public void sendingReactiveRequestForUser_Succeeds() throws InterruptedException {

        String url = "http://127.0.0.1:8089/user";
        ImmutableUser user = ImmutableUser.builder().id(111).name("user").build();
        String userJson = gson.toJson(user);

        givenThat(get(urlEqualTo("/user")).withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json").withBody(userJson)));

        userClient.sendRequestTo(url).subscribe(aUser -> {
            logger.info("Reactive Response: \n{}", aUser);
            Assertions.assertEquals(user, aUser);
        }, e -> logger.error("Error: {}", e), () -> logger.debug("Completion"));

        TimeUnit.MILLISECONDS.sleep(DELAY_FOR_WAITING_REACTIVE_CALL);
        verify(getRequestedFor(urlEqualTo("/user")));
    }
}
