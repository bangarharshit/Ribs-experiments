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

package com.example.harshitbangar.ribswithoutdagger.root.logged_in.tic_tac_toe;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.R;
import com.example.harshitbangar.ribswithoutdagger.root.UserName;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;

/**
 * Builder for the {@link TicTacToeScope}.
 */
public class TicTacToeBuilder
    extends ViewBuilder<TicTacToeView, TicTacToeRouter, TicTacToeBuilder.ParentComponent> {

  public TicTacToeBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link TicTacToeRouter}.
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new {@link TicTacToeRouter}.
   */
  public TicTacToeRouter build(ViewGroup parentViewGroup) {
    TicTacToeView view = createView(parentViewGroup);
    TicTacToeInteractor interactor = new TicTacToeInteractor();
    Component component = new Component(getDependency(), interactor, view);
    return component.tictactoeRouter();
  }

  @Override
  protected TicTacToeView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
    return (TicTacToeView) inflater.inflate(R.layout.tic_tac_toe_rib, parentViewGroup, false);
  }

  public interface ParentComponent {
    TicTacToeInteractor.Listener ticTacToeListener();
    UserName playerOne();
    UserName playerTwo();
  }

  static class Component implements InteractorBaseComponent<TicTacToeInteractor> {

    private final ParentComponent parentComponent;
    private final TicTacToeInteractor interactor;
    private final TicTacToeView view;

    public Component(
        ParentComponent parentComponent,
        TicTacToeInteractor ticTacToeInteractor,
        TicTacToeView ticTacToeView) {
      this.parentComponent = parentComponent;
      this.interactor = ticTacToeInteractor;
      this.view = ticTacToeView;
    }

    public TicTacToeRouter tictactoeRouter() {
      return new TicTacToeRouter(view, interactor, this);
    }

    @Override public void inject(TicTacToeInteractor interactor) {
      interactor.presenter = view;
      interactor.setPresenter(view);

      interactor.listener = parentComponent.ticTacToeListener();
      interactor.playerOne = parentComponent.playerOne();
      interactor.playerTwo = parentComponent.playerTwo();
      interactor.board = new Board();
    }
  }
}
