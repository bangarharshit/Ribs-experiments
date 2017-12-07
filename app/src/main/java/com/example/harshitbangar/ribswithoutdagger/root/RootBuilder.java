/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.harshitbangar.ribswithoutdagger.root;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.R;
import com.example.harshitbangar.ribswithoutdagger.lazy.SingleCheck;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.LoggedInBuilder;
import com.example.harshitbangar.ribswithoutdagger.root.logged_out.LoggedOutBuilder;
import com.example.harshitbangar.ribswithoutdagger.root.logged_out.LoggedOutInteractor;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;

/**
 * Builder.
 */
public class RootBuilder extends ViewBuilder<RootView, RootRouter, RootBuilder.ParentComponent> {

  public RootBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link RootRouter}.
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new {@link RootRouter}.
   */
  public RootRouter build(ViewGroup parentViewGroup) {
    RootView view = createView(parentViewGroup);
    RootInteractor interactor = new RootInteractor();
    Component component = new Component(interactor, view);
    return component.rootRouter;
  }

  @Override
  protected RootView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
    return (RootView) inflater.inflate(R.layout.root_rib, parentViewGroup, false);
  }

  public interface ParentComponent {
    // Define dependencies required from your parent interactor here.
  }

  static class Component implements
      InteractorBaseComponent<RootInteractor>,
      LoggedOutBuilder.ParentComponent,
      LoggedInBuilder.ParentComponent {

    private final RootView rootView;
    private final SingleCheck<LoggedOutInteractor.Listener> loggedOutListener;
    private final RootRouter rootRouter;

    public Component(final RootInteractor rootInteractor, final RootView rootView) {
      this.rootView = rootView;
      loggedOutListener = new SingleCheck<LoggedOutInteractor.Listener>() {
        @Override public LoggedOutInteractor.Listener create() {
          return rootInteractor.new LoggedOutListener();
        }
      };
      // Note we are escaping this before object construction. This is safe here but an anti-pattern.
      rootRouter = new RootRouter(
          rootView,
          rootInteractor,
          this,
          new LoggedOutBuilder(this),
          new LoggedInBuilder(this));
    }

    @Override public RootView rootView() {
      return rootView;
    }

    @Override public LoggedOutInteractor.Listener listener() {
      return loggedOutListener.get();
    }

    @Override public void inject(RootInteractor interactor) {
      interactor.presenter = rootView;
      interactor.setPresenter(rootView);
    }
  }
}
