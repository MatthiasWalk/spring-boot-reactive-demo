package at.spengergasse.reactivedemo.controllers;

import at.spengergasse.reactivedemo.ChocolateEventListener;
import at.spengergasse.reactivedemo.models.Chocolate;
import at.spengergasse.reactivedemo.services.ChocolateProcessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class HotChocolateController {

    private final ChocolateProcessor processor;

    private Flux<Chocolate> chocolateFlux;

    public HotChocolateController(ChocolateProcessor processor){
        this.processor = processor;
        this.chocolateFlux = createStream().publish().autoConnect().cache(10).log();
    }


    @GetMapping(value = "/stream/chocolate", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Chocolate> priceStream() {
       return chocolateFlux;
    }

    private Flux<Chocolate> createStream() {
        return Flux.create(sink -> { // (2)
            processor.register(new ChocolateEventListener() {

                @Override
                public void processComplete() {
                    sink.complete();
                }

                @Override
                public void onData(Chocolate data) {
                    sink.next(data);
                }
            });
        });
    }
}
