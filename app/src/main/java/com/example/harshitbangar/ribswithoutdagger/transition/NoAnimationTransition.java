package com.example.harshitbangar.ribswithoutdagger.transition;

import android.view.View;
import com.example.harshitbangar.ribswithoutdagger.ScreenStackImpl;

public class NoAnimationTransition implements Transition {

  @Override
  public void animate(
      View from, View to, ScreenStackImpl.Direction direction, Callback callback) {
    callback.onAnimationEnd();
  }

}