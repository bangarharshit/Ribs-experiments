/*
 * Copyright (C) 2016 The Dagger Authors.
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
 * A {@link Lazy} and {@link Provider} implementation that memoizes the value returned from a
 * delegate using the double-check idiom described in Item 71 of <i>Effective Java 2</i>.
 */
public abstract class DoubleCheck<T> implements Provider<T> {
  private volatile T instance = null;

  @Override
  public T get() {
    if (instance == null) {
      synchronized (this) {
        if (instance == null) {
          instance = create();
        }
      }
    }
    return instance;
  }

  public abstract T create();
}
