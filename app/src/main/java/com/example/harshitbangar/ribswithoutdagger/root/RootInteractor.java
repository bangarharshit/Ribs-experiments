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

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.harshitbangar.ribswithoutdagger.R;
import com.example.harshitbangar.ribswithoutdagger.root.logged_out.LoggedOutInteractor;
import com.uber.rib.core.Bundle;
import com.uber.rib.core.Interactor;
import com.uber.rib.core.RibInteractor;
import com.uber.rib.core.screenstack.ScreenStackBase;
import com.uber.rib.core.screenstack.ViewProvider;
import java.util.Random;
import javax.inject.Inject;

/**
 * Coordinates Business Logic for {@link RootBuilder.RootScope}.
 */
@RibInteractor
public class RootInteractor
    extends Interactor<RootInteractor.RootPresenter, RootRouter>
    implements RootActionableItem {

  @Inject RootPresenter presenter;
  @Inject ScreenStackBase screenStackBase;
  @Inject Context context;

  @Override
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {
    super.didBecomeActive(savedInstanceState);
    //getRouter().attachLoggedOut();
    pushScreen();
  }

  private void pushScreen() {
    screenStackBase.pushScreen(new ViewProvider() {
      @Override public View buildView(ViewGroup parentView) {
        View root = LayoutInflater.from(context).inflate(R.layout.view_test, parentView, false);
        TextView arbit = root.findViewById(R.id.arbit);
        arbit.setText(String.valueOf(new Random().nextInt(100)));
        root.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            pushScreen();
          }
        });
        return root;
      }
    });
  }

  @Override public boolean handleBackPress() {
    if (screenStackBase.size() > 0) {
      screenStackBase.popScreen();
      return true;
    }
    return super.handleBackPress();
  }

  class LoggedOutListener implements LoggedOutInteractor.Listener {

    @Override
    public void requestLogin(UserName playerOne, UserName playerTwo) {
      // Switch to logged in. Let’s just ignore userName for now.
      getRouter().detachLoggedOut();
      getRouter().attachLoggedIn(playerOne, playerTwo);
    }
  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface RootPresenter { }
}
