/*
 * Copyright (C) 2014 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.harshitbangar.ribswithoutdagger.lazy;

import javax.inject.Provider;

/**
 * A {@link Provider} implementation that memoizes the result of another {@link Provider} using
 * simple lazy initialization, not the double-checked lock pattern.
 */
public abstract class SingleCheck<T> implements Provider<T> {

  private volatile T instance = null;

  @SuppressWarnings("unchecked") // cast only happens when result comes from the delegate provider
  @Override
  public T get() {
    if (instance == null) {
      instance = create();
    }
    return instance;
  }

  public abstract T create();

}
