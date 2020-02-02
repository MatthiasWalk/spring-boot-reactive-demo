package at.spengergasse.reactivedemo;


import at.spengergasse.reactivedemo.models.Chocolate;
import net.jodah.concurrentunit.Waiter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeoutException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReactiveEndpointTests {

    private final WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();

    @Test
    public void testFindPersonsStreamBackPressure() throws TimeoutException, InterruptedException {
        final Waiter waiter = new Waiter();
        Flux<Chocolate> chocolateSource = client.get().uri("/api/chocolate").retrieve().bodyToFlux(Chocolate.class);
        chocolateSource.map(this::doSomeSlowWork).subscribe(chocolate -> {
            waiter.assertNotNull(chocolate);
            System.out.println(LocalTime.now() + ": Subscribing to Chocolate: " + chocolate);
            waiter.resume();
        });
        waiter.await(3000, 9);
    }

    private Chocolate doSomeSlowWork(Chocolate chocolate) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {
        }
        return chocolate;
    }
}