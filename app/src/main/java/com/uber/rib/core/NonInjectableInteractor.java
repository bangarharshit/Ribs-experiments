package com.uber.rib.core;

public abstract class NonInjectableInteractor<P, R extends Router> extends Interactor<P, R> {

  // Hack we need it for injecting presenter since without dagger it is getting called manually.
  public void setPresenter(P p) {
    presenter = p;
  }
}
