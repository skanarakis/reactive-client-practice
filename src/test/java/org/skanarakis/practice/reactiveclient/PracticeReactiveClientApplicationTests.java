package org.skanarakis.practice.reactiveclient;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skanarakis.practice.reactiveclient.model.ImmutableCar;
import org.skanarakis.practice.reactiveclient.model.ImmutableUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(SpringExtension.class)
public class PracticeReactiveClientApplicationTests {

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
    public void sendingReactiveRequestForCar_Succeeds() {

        String url = "http://127.0.0.1:8089/car";
        ImmutableCar car = ImmutableCar.builder().model("Honda").year(2010).build();
        String carJson = gson.toJson(car);

        givenThat(get(urlEqualTo("/car")).withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json").withBody(carJson)));

        StepVerifier.create(carClient.sendRequestTo(url))
                .expectSubscription()
                .expectNext(car)
                .verifyComplete();

        verify(getRequestedFor(urlEqualTo("/car")));
    }

    @Test
    public void sendingReactiveRequestForUser_Succeeds() {

        String url = "http://127.0.0.1:8089/user";
        ImmutableUser user = ImmutableUser.builder().id(111).name("user").build();
        String userJson = gson.toJson(user);

        givenThat(get(urlEqualTo("/user")).withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json").withBody(userJson)));

        StepVerifier.create(userClient.sendRequestTo(url))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();

        verify(getRequestedFor(urlEqualTo("/user")));
    }
}
