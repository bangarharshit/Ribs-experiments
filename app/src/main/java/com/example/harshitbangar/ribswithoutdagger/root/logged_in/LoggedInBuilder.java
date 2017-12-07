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

package com.example.harshitbangar.ribswithoutdagger.root.logged_in;

import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.lazy.SingleCheck;
import com.example.harshitbangar.ribswithoutdagger.root.RootView;
import com.example.harshitbangar.ribswithoutdagger.root.UserName;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.off_game.OffGameBuilder;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.off_game.OffGameInteractor;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.random_winner.RandomWinnerBuilder;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.random_winner.RandomWinnerInteractor;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.tic_tac_toe.TicTacToeBuilder;
import com.example.harshitbangar.ribswithoutdagger.root.logged_in.tic_tac_toe.TicTacToeInteractor;
import com.google.common.collect.Lists;
import com.uber.rib.core.Builder;
import com.uber.rib.core.EmptyPresenter;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewRouter;
import java.util.List;
import javax.inject.Provider;

public class LoggedInBuilder extends Builder<LoggedInRouter, LoggedInBuilder.ParentComponent> {

  public LoggedInBuilder(ParentComponent dependency) {
    super(dependency);
  }

  /**
   * Builds a new {@link LoggedInRouter}.
   *
   * @return a new {@link LoggedInRouter}.
   */
  public LoggedInRouter build(UserName playerOne, UserName playerTwo) {
    LoggedInInteractor interactor = new LoggedInInteractor();
    Component component = new Component(getDependency(), playerOne, playerTwo, interactor);
    return component.loggedinRouter();
  }


  public interface ParentComponent {

    RootView rootView();
  }

  public class Component
      implements InteractorBaseComponent<LoggedInInteractor>,
      OffGameBuilder.ParentComponent,
      TicTacToeBuilder.ParentComponent,
      RandomWinnerBuilder.ParentComponent {

    private final UserName player1;
    private final UserName player2;
    private final LoggedInInteractor interactor;
    private final RootView rootView;
    private final Provider<MutableScoreStream> mutableScoreStream;
    private final Provider<OffGameInteractor.Listener> offGameListenerProvider;
    private final Provider<LoggedInInteractor.GameListener> gameListenerProvider;
    private List<GameProvider> gameProviders;
    private final EmptyPresenter emptyPresenter;

    public Component(
        ParentComponent parentComponent,
        final UserName player1, final UserName player2,
        LoggedInInteractor loggedInInteractor) {
      this.player1 = player1;
      this.player2 = player2;
      this.interactor = loggedInInteractor;
      this.rootView = parentComponent.rootView();
      mutableScoreStream = new SingleCheck<MutableScoreStream>() {
        @Override public MutableScoreStream create() {
          return new MutableScoreStream(player1, player2);
        }
      };
      offGameListenerProvider = new SingleCheck<OffGameInteractor.Listener>() {
        @Override public LoggedInInteractor.OffGameListener create() {
          return interactor.new OffGameListener();
        }
      };
      gameListenerProvider = new SingleCheck<LoggedInInteractor.GameListener>() {
        @Override public LoggedInInteractor.GameListener create() {
          return interactor.new GameListener();
        }
      };
      emptyPresenter = new EmptyPresenter();
    }

    @Override public RandomWinnerInteractor.Listener randomWinnerListener() {
      return gameListenerProvider.get();
    }

    @Override public TicTacToeInteractor.Listener ticTacToeListener() {
      return gameListenerProvider.get();
    }

    @Override public UserName playerOne() {
      return player1;
    }

    @Override public UserName playerTwo() {
      return player2;
    }

    @Override public OffGameInteractor.Listener listener() {
      return offGameListenerProvider.get();
    }

    @Override public ScoreStream scoreStream() {
      return mutableScoreStream.get();
    }

    @Override public List<? extends GameKey> gameKeys() {
      return gameProviders;
    }

    @Override public void inject(LoggedInInteractor interactor) {
      interactor.gameProviders = gameProviders;
      interactor.scoreStream = mutableScoreStream.get();
      interactor.setPresenter(emptyPresenter);
    }

    List<GameProvider> gameProviders(final Component component) {
      // Decorate the game builders with a "name" key so we can treat them generically elsewhere.
      GameProvider ticTacToeGame = new GameProvider() {
        @Override
        public String gameName() {
          return "TicTacToe";
        }

        @Override
        public ViewRouter viewRouter(ViewGroup viewGroup) {
          return new TicTacToeBuilder(component).build(viewGroup);
        }
      };
      GameProvider randomWinnerGame = new GameProvider() {
        @Override
        public String gameName() {
          return "RandomWinner";
        }

        @Override
        public ViewRouter viewRouter(ViewGroup viewGroup) {
          return new RandomWinnerBuilder(component).build(viewGroup);
        }
      };
      return Lists.newArrayList(ticTacToeGame, randomWinnerGame);
    }

    public LoggedInRouter loggedinRouter() {
      this.gameProviders = gameProviders(this);
      return new LoggedInRouter(
          interactor,
          this,
          rootView,
          new OffGameBuilder(this),
          new TicTacToeBuilder(this));
    }
  }
}
