package com.example.harshitbangar.ribswithoutdagger.root.logged_in.random_winner;

import android.support.annotation.Nullable;
import com.example.harshitbangar.ribswithoutdagger.root.UserName;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.NonInjectableInteractor;
import com.uber.rib.core.RibInteractor;
import java.util.Random;

@RibInteractor
public class RandomWinnerInteractor
        extends
    NonInjectableInteractor<RandomWinnerInteractor.RandomWinnerPresenter, RandomWinnerRouter> {

    Listener listener;
    UserName playerOne;
    UserName playerTwo;

    @Override
    protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
        super.didBecomeActive(savedInstanceState);
        if (new Random(System.currentTimeMillis()).nextBoolean()) {
            listener.gameWon(playerOne);
        } else {
            listener.gameWon(playerTwo);
        }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RandomWinnerPresenter { }

    public interface Listener {

        /**
         * Called when the game is over.
         *
         * @param winner player that won, or null if it's a tie.
         */
        void gameWon(UserName winner);
    }
}
