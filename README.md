# Ribs-experiments

## Ribs without dagger


```
The changes:
1. All the values injected in the same rib are eagerily constructed.
2. All the values injected in the child rib are lazily constructed.
3. SingleCheckProvider (and not double check locking) is used since injection in interactor is thread safe.
4. All the modules are removed and instantiation logic moved to Component itself.
```

## Async injection using ribs compiler and rx-java
Update ribs compiler for [async injection](http://frogermcs.github.io/async-injection-in-dagger-2-with-rxjava/).

## Update ribs compiler/dagger compiler to find unused dependencies.
