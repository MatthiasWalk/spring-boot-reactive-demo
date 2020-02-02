package at.spengergasse.reactivedemo.controllers;

import at.spengergasse.reactivedemo.models.Chocolate;
import at.spengergasse.reactivedemo.services.ChocolateService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ChocolateController {

    private final ChocolateService chocolateService;

    ChocolateController(ChocolateService chocolateService) {
        this.chocolateService = chocolateService;
    }

    @PatchMapping("/api/chocolate")
    public Mono<Boolean> patch(@RequestBody Chocolate chocolate){
        return chocolateService.addChocolate(chocolate);
    }


    @GetMapping("/api/chocolate/{id}")
    public Mono<Chocolate> getById(@PathVariable String id) {
        return chocolateService.getChocolateById(id);
    }


    @GetMapping(value = "/api/chocolate",  produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Chocolate> getAll() {
        return chocolateService.getAllChocolate();
    }

    @DeleteMapping("/api/chocolate/{id}")
    public Mono<Boolean> delete(@PathVariable String id){
        return chocolateService.deleteChocolateById(id);
    }
}
