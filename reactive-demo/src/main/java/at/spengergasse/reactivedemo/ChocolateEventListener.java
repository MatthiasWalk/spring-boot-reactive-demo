package at.spengergasse.reactivedemo;

import at.spengergasse.reactivedemo.models.Chocolate;

public interface ChocolateEventListener {
    void onData(Chocolate event);
    void processComplete();
}
