package com.example.harshitbangar.ribswithoutdagger.transition;

import android.view.View;
import com.example.harshitbangar.ribswithoutdagger.ScreenStackImpl;

public interface Transition {

  void animate(View from, View to, ScreenStackImpl.NavigationType navType, ScreenStackImpl.Direction direction, Callback callback);

  interface Callback {
    void onAnimationEnd();
  }

}
