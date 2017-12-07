package com.example.harshitbangar.ribswithoutdagger.root.logged_in.random_winner;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Top level view for {@link RandomWinnerBuilder.RandomWinnerScope}.
 */
class RandomWinnerView extends FrameLayout implements RandomWinnerInteractor.RandomWinnerPresenter {

    public RandomWinnerView(Context context) {
        this(context, null);
    }

    public RandomWinnerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomWinnerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
