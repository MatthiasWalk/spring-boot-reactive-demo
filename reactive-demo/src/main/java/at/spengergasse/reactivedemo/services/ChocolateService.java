package at.spengergasse.reactivedemo.services;

import at.spengergasse.reactivedemo.models.Chocolate;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ChocolateService {
    private final ReactiveRedisOperations<String, Chocolate> chocolateOperations;

    private KafkaTemplate<String, Chocolate> kafkaTemplate;

    public ChocolateService(ReactiveRedisOperations<String, Chocolate> chocolateOperations, KafkaTemplate<String, Chocolate> kafkaTemplate){
        this.chocolateOperations=chocolateOperations;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Boolean> addChocolate(Chocolate chocolate){
        sendKafkaMessage("chocolate", chocolate);
        return chocolateOperations.opsForValue().set(chocolate.getId(), chocolate);
    }

    public Mono<Chocolate> getChocolateById(String id){
        return chocolateOperations.keys(id).flatMap(chocolateOperations.opsForValue()::get).next();
    }

    public Flux<Chocolate> getAllChocolate(){
        return chocolateOperations.keys("*")
                .flatMap(chocolateOperations.opsForValue()::get)
                .doOnNext(chocolate -> System.out.println(java.time.LocalTime.now() +": Producing: "+chocolate));
    }

    public Mono<Boolean> deleteChocolateById(String id){
        return chocolateOperations.opsForValue().delete(id);
    }

    private void sendKafkaMessage(String topic, Chocolate message) {
        kafkaTemplate.send(topic, message);
    }
}
