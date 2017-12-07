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

package com.example.harshitbangar.ribswithoutdagger.root.logged_out;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.R;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;

/**
 * Builder for the {@link LoggedOutScope}.
 */
public class LoggedOutBuilder
    extends ViewBuilder<LoggedOutView, LoggedOutRouter, LoggedOutBuilder.ParentComponent> {

  public LoggedOutBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link LoggedOutRouter}.
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new {@link LoggedOutRouter}.
   */
  public LoggedOutRouter build(ViewGroup parentViewGroup) {
    LoggedOutView view = createView(parentViewGroup);
    LoggedOutInteractor interactor = new LoggedOutInteractor();
    Component component = new Component(getDependency(), view, interactor);
    return component.createLoggedOutRouter();
  }

  @Override
  protected LoggedOutView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
    return (LoggedOutView) inflater.inflate(R.layout.logged_out_rib, parentViewGroup, false);
  }

  public interface ParentComponent {

    LoggedOutInteractor.Listener listener();
  }

  class Component implements InteractorBaseComponent<LoggedOutInteractor> {
    private final ParentComponent parentComponent;
    private final LoggedOutInteractor loggedOutInteractor;
    private final LoggedOutView loggedOutView;

    public Component(
        ParentComponent parentComponent,
        LoggedOutView view,
        LoggedOutInteractor interactor) {
      this.loggedOutInteractor = interactor;
      this.loggedOutView = view;
      this.parentComponent = parentComponent;
    }

    public LoggedOutRouter createLoggedOutRouter() {
      return new LoggedOutRouter(loggedOutView, loggedOutInteractor, this);
    }

    @Override public void inject(LoggedOutInteractor interactor) {
      interactor.setPresenter(loggedOutView);
      interactor.presenter = loggedOutView;
      interactor.listener = parentComponent.listener();
    }
  }
}
