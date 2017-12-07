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

package com.example.harshitbangar.ribswithoutdagger.root.logged_in.off_game;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.R;
import com.example.harshitbangar.ribswithoutdagger.root.UserName;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.GameKey;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.ScoreStream;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;
import java.util.List;

/**
 * Builder for the {@link OffGameScope}.
 */
public class OffGameBuilder
    extends ViewBuilder<OffGameView, OffGameRouter, OffGameBuilder.ParentComponent> {

  public OffGameBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link OffGameRouter}.
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new {@link OffGameRouter}.
   */
  public OffGameRouter build(ViewGroup parentViewGroup) {
    OffGameView view = createView(parentViewGroup);
    OffGameInteractor interactor = new OffGameInteractor();
    Component component = new Component(view, interactor, getDependency());
    return component.offgameRouter();
  }

  @Override
  protected OffGameView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
    return (OffGameView) inflater.inflate(R.layout.off_game_rib, parentViewGroup, false);
  }

  public interface ParentComponent {
    UserName playerOne();
    UserName playerTwo();
    OffGameInteractor.Listener listener();
    ScoreStream scoreStream();
    List<? extends GameKey> gameKeys();
  }

  static class Component implements InteractorBaseComponent<OffGameInteractor> {

    private final OffGameView view;
    private final OffGameInteractor interactor;
    private final ParentComponent parentComponent;

    public Component(
        OffGameView view,
        OffGameInteractor interactor,
        ParentComponent parentComponent) {
      this.view = view;
      this.interactor = interactor;
      this.parentComponent = parentComponent;
    }

    @Override public void inject(OffGameInteractor interactor) {
      interactor.gameNames = parentComponent.gameKeys();
      interactor.listener = parentComponent.listener();
      interactor.playerOne = parentComponent.playerOne();
      interactor.playerTwo = parentComponent.playerTwo();
      interactor.presenter = view;
      interactor.setPresenter(view);
      interactor.scoreStream = parentComponent.scoreStream();
    }

    OffGameRouter offgameRouter() {
      return new OffGameRouter(view, interactor, this);
    }

  }
}
