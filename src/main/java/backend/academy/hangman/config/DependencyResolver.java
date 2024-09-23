package backend.academy.hangman.config;

import backend.academy.hangman.exception.ServiceNotRegisteredException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DependencyResolver {
    private final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();
    private final Map<Class<?>, Object> instances = new HashMap<>();

    private DependencyResolver() {
    }

    public <T> void register(Class<T> serviceClass, Supplier<T> supplier) {
        suppliers.put(serviceClass, supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> serviceClass) {
        Object instance = instances.get(serviceClass);
        if (instance != null) {
            return (T) instance;
        }

        var supplier = suppliers.get(serviceClass);
        if (supplier == null) {
            throw new ServiceNotRegisteredException("No implementation registered for " + serviceClass.getName());
        }

        T newInstance = (T) supplier.get();
        instances.put(serviceClass, newInstance);
        return newInstance;
    }

    public void clear() {
        suppliers.clear();
        instances.clear();
    }

    public static DependencyResolver getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DependencyResolver INSTANCE = new DependencyResolver();
    }
}
