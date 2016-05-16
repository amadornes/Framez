package com.amadornes.framez.compat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;

public class ModularRegistry<T> implements Iterable<T> {

    private Map<String, Triple<Dependency, Class<? extends T>, Class<? extends T>>> uncompiled = new HashMap<String, Triple<Dependency, Class<? extends T>, Class<? extends T>>>();
    private Map<String, T> compiled = new HashMap<String, T>();

    @SuppressWarnings("unchecked")
    public <J extends T> J registerNow(String identifier, ModularRegistry.Dependency dep, Class<J> object) {

        return (J) registerNow(identifier, dep, object, null);
    }

    public T registerNow(String identifier, ModularRegistry.Dependency dep, Class<? extends T> object, Class<? extends T> alt) {

        return compileDependency(identifier, dep, object, alt);
    }

    public void register(String identifier, ModularRegistry.Dependency dep, Class<? extends T> object) {

        register(identifier, dep, object, null);
    }

    public void register(String identifier, ModularRegistry.Dependency dep, Class<? extends T> object, Class<? extends T> alt) {

        uncompiled.put(identifier,
                new ImmutableTriple<ModularRegistry.Dependency, Class<? extends T>, Class<? extends T>>(dep, object, alt));
    }

    public void compileRegistry() {

        for (Entry<String, Triple<Dependency, Class<? extends T>, Class<? extends T>>> e : uncompiled.entrySet())
            compileDependency(e.getKey(), e.getValue().getLeft(), e.getValue().getMiddle(), e.getValue().getRight());
    }

    private T compileDependency(String identifier, ModularRegistry.Dependency dep, Class<? extends T> object, Class<? extends T> alt) {

        if (!dep.isAvailable(this)) return null;;

        try {
            T obj = null;
            if (object != null) obj = object.newInstance();
            else if (alt != null) obj = alt.newInstance();
            if (obj != null) compiled.put(identifier, obj);
            return obj;
        } catch (Exception ex) {
            throw new RuntimeException("An exception occured while instatiating module \"" + identifier + "\"!", ex);
        }
    }

    public Collection<T> getRegisteredObjects() {

        return Collections.unmodifiableCollection(compiled.values());
    }

    @Override
    public Iterator<T> iterator() {

        return getRegisteredObjects().iterator();
    }

    public boolean isModulePresent(String module) {

        return compiled.containsKey(module);
    }

    public boolean isModuleOffered(String module) {

        return uncompiled.containsKey(module);
    }

    public IRegistryAccess<T> getImmutableAccess() {

        return new IRegistryAccess<T>() {

            @Override
            public Collection<T> getRegisteredObjects() {

                return ModularRegistry.this.getRegisteredObjects();
            }

            @Override
            public Iterator<T> iterator() {

                return ModularRegistry.this.iterator();
            }

            @Override
            public boolean isModulePresent(String module) {

                return ModularRegistry.this.isModulePresent(module);
            }

            @Override
            public boolean isModuleOffered(String module) {

                return ModularRegistry.this.isModuleOffered(module);
            }
        };
    }

    public static abstract class PrecompiledDependency<O> {

        private PrecompiledDependency() {

        }

        public abstract Dependency on(O dep);
    }

    public static abstract class PrecompiledExternalDependency<O> {

        private PrecompiledExternalDependency() {

        }

        public abstract <T> Dependency on(O dep, IRegistryAccess<T> registry);
    }

    public static abstract class Dependency {

        public static final PrecompiledDependency<String> MOD = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable(ModularRegistry<?> registry) {

                        return Loader.isModLoaded(dep);
                    }
                };
            }
        };
        public static final PrecompiledDependency<String> API = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable(ModularRegistry<?> registry) {

                        return ModAPIManager.INSTANCE.hasAPI(dep);
                    }
                };
            }
        };
        public static final PrecompiledDependency<String> CLASS = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable(ModularRegistry<?> registry) {

                        try {
                            Class.forName(dep);
                            return true;
                        } catch (Exception ex) {
                        }
                        return false;
                    }
                };
            }
        };
        public static final PrecompiledDependency<String> MODULE = new PrecompiledDependency<String>() {

            @Override
            public ModularRegistry.Dependency on(final String dep) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable(ModularRegistry<?> registry) {

                        return registry.isModuleOffered(dep);
                    }
                };
            }
        };
        public static final PrecompiledExternalDependency<String> EXTERNAL_MODULE = new PrecompiledExternalDependency<String>() {

            @Override
            public <T> ModularRegistry.Dependency on(final String dep, final IRegistryAccess<T> registry_) {

                return new ModularRegistry.Dependency() {

                    @Override
                    public boolean isAvailable(ModularRegistry<?> registry) {

                        return registry_.isModuleOffered(dep);
                    }
                };
            }
        };
        public static final Dependency NONE = new Dependency() {

            @Override
            public boolean isAvailable(ModularRegistry<?> registry) {

                return true;
            }

        };

        public abstract boolean isAvailable(ModularRegistry<?> registry);

        public Dependency or(Dependency dependency) {

            if (this instanceof AnyConditionDependency) {
                ((AnyConditionDependency) this).add(dependency);
                return this;
            } else {
                return new AnyConditionDependency(this, dependency);
            }
        }

        public Dependency and(Dependency dependency) {

            if (this instanceof MultiConditionDependency) {
                ((MultiConditionDependency) this).add(dependency);
                return this;
            } else {
                return new MultiConditionDependency(this, dependency);
            }
        }

        public Dependency negate() {

            return new InvertedDependency(this);
        }
    }

    public static class MultiConditionDependency extends Dependency {

        private final Set<Dependency> deps = new HashSet<Dependency>();

        public MultiConditionDependency(Dependency... deps) {

            this.deps.addAll(Arrays.asList(deps));
        }

        public MultiConditionDependency add(Dependency dep) {

            deps.add(dep);
            return this;
        }

        @Override
        public boolean isAvailable(ModularRegistry<?> registry) {

            if (deps.size() == 0) return false;

            for (Dependency dep : deps)
                if (!dep.isAvailable(registry)) return false;

            return true;
        }

    }

    public static class AnyConditionDependency extends Dependency {

        private final Set<Dependency> deps = new HashSet<Dependency>();

        public AnyConditionDependency(Dependency... deps) {

            this.deps.addAll(Arrays.asList(deps));
        }

        public AnyConditionDependency add(Dependency dep) {

            deps.add(dep);
            return this;
        }

        @Override
        public boolean isAvailable(ModularRegistry<?> registry) {

            for (Dependency dep : deps)
                if (dep.isAvailable(registry)) return true;

            return false;
        }

    }

    public static class InvertedDependency extends Dependency {

        private final Dependency dependency;

        public InvertedDependency(Dependency dependency) {

            this.dependency = dependency;
        }

        @Override
        public boolean isAvailable(ModularRegistry<?> registry) {

            return !dependency.isAvailable(registry);
        }

    }

}
