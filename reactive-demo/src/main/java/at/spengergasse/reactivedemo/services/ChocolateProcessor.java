package at.spengergasse.reactivedemo.services;

import at.spengergasse.reactivedemo.ChocolateEventListener;
import at.spengergasse.reactivedemo.models.Chocolate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ChocolateProcessor {
    private ChocolateEventListener listener;

    public void register(ChocolateEventListener listener) {
        this.listener = listener;
    }

    public void onEvent(Chocolate event) {
        if (listener != null) {
            listener.onData(event);
        }
    }

    public void onComplete() {
        if (listener != null) {
            listener.processComplete();
        }
    }

    @KafkaListener(topics = "chocolate", groupId = "group_id")
    public void consume(Chocolate message) {
        System.out.println(String.format("#### -> Consumed message -> %s", message));
        onEvent(message);
    }
}
