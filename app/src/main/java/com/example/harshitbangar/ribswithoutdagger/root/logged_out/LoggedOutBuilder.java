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
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;
import com.example.harshitbangar.ribswithoutdagger.R;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Provides;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;
import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.CLASS;

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
    return component.loggedoutRouter();
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
    private final LoggedOutRouter loggedOutRouter;
    private final LoggedOutInteractor loggedOutInteractor;
    private final LoggedOutView loggedOutView;

    public Component(
        ParentComponent parentComponent,
        LoggedOutView view,
        LoggedOutInteractor interactor) {
      this.loggedOutInteractor = interactor;
      this.loggedOutView = view;
      this.parentComponent = parentComponent;
      this.loggedOutRouter = new LoggedOutRouter(view, interactor, this);
    }

    public LoggedOutRouter loggedoutRouter() {
      return loggedOutRouter;
    }

    @Override public void inject(LoggedOutInteractor interactor) {
      loggedOutInteractor.presenter = loggedOutView;
      loggedOutInteractor.setPresenter(loggedOutView);
      loggedOutInteractor.listener = parentComponent.listener();
    }
  }
}
