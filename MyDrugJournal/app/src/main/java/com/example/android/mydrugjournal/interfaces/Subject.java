package com.example.android.mydrugjournal.interfaces;

public interface Subject {
    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObservers();
}
