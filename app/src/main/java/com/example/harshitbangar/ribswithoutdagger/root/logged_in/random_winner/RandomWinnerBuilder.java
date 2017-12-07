package com.example.harshitbangar.ribswithoutdagger.root.logged_in.random_winner;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.root.UserName;
import com.uber.rib.core.InteractorBaseComponent;
import com.uber.rib.core.ViewBuilder;

/**
 * Builder for the {@link RandomWinnerScope}. Not a real game. This just picks a random winner than exits.
 */
public class RandomWinnerBuilder
        extends
    ViewBuilder<RandomWinnerView, RandomWinnerRouter, RandomWinnerBuilder.ParentComponent> {

    public RandomWinnerBuilder(ParentComponent dependency) {
        super(dependency);
    }

    /**
     * Builds a new {@link RandomWinnerRouter}.
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new {@link RandomWinnerRouter}.
     */
    public RandomWinnerRouter build(ViewGroup parentViewGroup) {
        RandomWinnerView view = createView(parentViewGroup);
        RandomWinnerInteractor interactor = new RandomWinnerInteractor();
        Component component = new Component(view, interactor, getDependency());
        return component.randomwinnerRouter();
    }

    @Override
    protected RandomWinnerView inflateView(LayoutInflater inflater, ViewGroup parentViewGroup) {
        // Just inflate a silly useless view that does nothing.
        return new RandomWinnerView(parentViewGroup.getContext());
    }

    public interface ParentComponent {
        RandomWinnerInteractor.Listener randomWinnerListener();
        UserName playerOne();
        UserName playerTwo();
    }

    static class Component implements InteractorBaseComponent<RandomWinnerInteractor> {

        private final ParentComponent parentComponent;
        private final RandomWinnerView randomWinnerView;
        private final RandomWinnerInteractor randomWinnerInteractor;

        public Component(
            RandomWinnerView randomWinnerView,
            RandomWinnerInteractor randomWinnerInteractor,
            ParentComponent parentComponent) {
            this.randomWinnerView = randomWinnerView;
            this.randomWinnerInteractor = randomWinnerInteractor;
            this.parentComponent = parentComponent;
        }

        public RandomWinnerRouter randomwinnerRouter() {
            return new RandomWinnerRouter(randomWinnerView, randomWinnerInteractor, this);
        }

        @Override public void inject(RandomWinnerInteractor interactor) {
            randomWinnerInteractor.setPresenter(randomWinnerView);
            randomWinnerInteractor.listener = parentComponent.randomWinnerListener();
            randomWinnerInteractor.playerOne = parentComponent.playerOne();
            randomWinnerInteractor.playerTwo = parentComponent.playerTwo();
        }
    }
}
