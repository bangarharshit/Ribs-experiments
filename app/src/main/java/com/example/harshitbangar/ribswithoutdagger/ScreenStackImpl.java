package com.example.harshitbangar.ribswithoutdagger;

import android.view.View;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.transition.CircularRevealTransition;
import com.example.harshitbangar.ribswithoutdagger.transition.CrossfadeTransition;
import com.example.harshitbangar.ribswithoutdagger.transition.Transition;
import com.uber.rib.core.screenstack.ScreenStackBase;
import com.uber.rib.core.screenstack.ViewProvider;
import java.util.ArrayDeque;
import java.util.Deque;

import static com.example.harshitbangar.ribswithoutdagger.ScreenStackImpl.Direction.FORWARD;
import static com.example.harshitbangar.ribswithoutdagger.Views.whenMeasured;

public class ScreenStackImpl implements ScreenStackBase {

  private final Deque<ViewProvider> viewProviders = new ArrayDeque<>();
  private final ViewGroup parentViewGroup;
  private View ghostView; // keep track of the disappearing view we are animating
  private Transition overridingTransition = new CrossfadeTransition();



  public ScreenStackImpl(ViewGroup parentViewGroup) {
    this.parentViewGroup = parentViewGroup;
  }

  public void overrideTransition(Transition overridingTransition) {
    this.overridingTransition = overridingTransition;
  }

  @Override public void pushScreen(final ViewProvider viewProvider) {
    navigate(new Runnable() {
      @Override public void run() {
        viewProviders.push(viewProvider);
      }
    }, Direction.FORWARD);
  }

  @Override public void pushScreen(ViewProvider viewProvider, boolean shouldAnimate) {

  }

  @Override public void popScreen() {
    navigate(new Runnable() {
      @Override public void run() {
        viewProviders.remove();
      }
    }, Direction.BACKWARD);
  }

  @Override public void popScreen(boolean shouldAnimate) {

  }

  @Override public void popBackTo(int index, boolean shouldAnimate) {

  }

  @Override public boolean handleBackPress() {
    return false;
  }

  @Override public boolean handleBackPress(boolean shouldAnimate) {
    return false;
  }

  @Override public int size() {
    return viewProviders.size();
  }

  private void navigate(final Runnable backStackOperation, final Direction direction) {
    View from = hideCurrentScreen();
    backStackOperation.run();
    View to = showCurrentScreen(direction);
    animateAndRemove(from, to, NavigationType.GO, direction);
  }

  private View hideCurrentScreen() {
    if (isAnimating()) {
      parentViewGroup.removeView(ghostView);
      ghostView = null;
    }
    View view = parentViewGroup.getChildAt(0); // will be removed at the end of the animation
    return view;
  }



  private boolean isAnimating() {
    return ghostView != null;
  }

  private View showCurrentScreen(final Direction direction) {
    if (size() == 0) {
      return null;
    }
    View currentView = viewProviders.peek().buildView(parentViewGroup);
    parentViewGroup.addView(currentView, direction == FORWARD ? parentViewGroup.getChildCount() : 0);
    return currentView;
  }

  private void animateAndRemove(
      final View from, final View to, final NavigationType navType, final Direction direction) {
    if (from == null) {
      return;
    }
    if (to == null) {
      parentViewGroup.removeView(from);
      return;
    }
    ghostView = from;
    final Transition transitionToUse = overridingTransition;
    //overridingTransition = null;
    whenMeasured(to, new Views.OnMeasured() {
      @Override
      public void onMeasured() {
        transitionToUse.animate(from, to, navType, direction, new Transition.Callback() {
          @Override
          public void onAnimationEnd() {
            if (parentViewGroup != null) {
              parentViewGroup.removeView(from);
              if (from == ghostView) {
                // Only clear the ghost if it's the same as the view we just removed
                ghostView = null;
              }
            }
          }
        });
      }
    });
  }

  public enum Direction {

    FORWARD(1),
    BACKWARD(-1);

    private final int sign;

    Direction(int sign) {
      this.sign = sign;
    }

    public int sign() {
      return sign;
    }
  }

  public enum NavigationType {
    SHOW,
    GO,
    NO_ANIM
  }
}
