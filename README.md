# Ribs-without-dagger
Ribs without dagger


```
The changes:
1. All the values injected in the same rib are eagerily constructed.
2. All the values injected in the child rib are lazily constructed.
3. SingleCheckProvider (and not double check locking) is used since injection in interactor is thread safe.
4. All the modules are removed and instantiation logic moved to Component itself.
```
