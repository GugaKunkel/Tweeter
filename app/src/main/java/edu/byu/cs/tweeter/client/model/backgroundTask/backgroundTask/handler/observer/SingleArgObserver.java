package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer;

public interface SingleArgObserver<T> extends ServiceObserver {
    void handleSuccess(T value);
}
