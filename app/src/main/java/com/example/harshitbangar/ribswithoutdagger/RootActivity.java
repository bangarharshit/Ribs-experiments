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

package com.example.harshitbangar.ribswithoutdagger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import com.example.harshitbangar.ribswithoutdagger.root.RootBuilder;
import com.example.harshitbangar.ribswithoutdagger.root.RootInteractor;
import com.example.harshitbangar.ribswithoutdagger.root.RootRouter;
import com.example.harshitbangar.ribswithoutdagger.root.RootWorkflow;
import com.example.harshitbangar.ribswithoutdagger.root.WorkflowFactory;
import com.uber.autodispose.SingleScoper;
import com.uber.rib.core.Optional;
import com.uber.rib.core.RibActivity;
import com.uber.rib.core.ViewRouter;
import io.reactivex.functions.Consumer;

/** The sample app's single activity. */
public class RootActivity extends RibActivity {

  private RootInteractor rootInteractor;

  @SuppressWarnings("unchecked")
  @Override
  protected ViewRouter<?, ?, ?> createRouter(ViewGroup parentViewGroup) {
    RootBuilder rootBuilder = new RootBuilder(new RootBuilder.ParentComponent() {});
    RootRouter router = rootBuilder.build(parentViewGroup);
    rootInteractor = router.getInteractor();
    return router;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getIntent() != null) {
      handleDeepLink(getIntent());
    }
  }

  private void handleDeepLink(Intent intent) {
    RootWorkflow<?, ?> rootWorkflow = new WorkflowFactory().getWorkflow(intent);
    if (rootWorkflow != null) {
      rootWorkflow
          .createSingle(rootInteractor)
          .to(new SingleScoper<Optional<?>>(this))
          .subscribe(
              new Consumer<Optional<?>>() {
                @Override
                public void accept(Optional<?> optional) throws Exception {}
              });
    }
  }
}
