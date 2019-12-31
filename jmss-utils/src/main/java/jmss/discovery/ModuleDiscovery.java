package jmss.discovery;

import jmss.annotations.Module;
import jmss.specificationCore.Solver;
import jmss.specificationCore.SolverListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("unchecked") // if something crashes, then at the beginning when doing dependency injection
public class ModuleDiscovery {
    private Map<String, Map<String, Class>> moduleBuckets = getAllModules();

    public static <A> A getModule(String module, final String mname, Solver solver, String[] args, Object... opts) {
        //noinspection unchecked
        String name = getModuleName(module, mname);
        Class<A> moduleClass = (Class<A>) getModuleClass(module, name);

        Constructor<A> matchingConstructor = null;
        Map<Integer, Object> argsMap = new HashMap<>();


        if (moduleClass.getDeclaredConstructors().length == 0)
            throw new RuntimeException("No constructors for " + moduleClass + " found");

        for (Constructor<?> declaredConstructor : moduleClass.getDeclaredConstructors()) {
            if (declaredConstructor.getGenericParameterTypes().length > 3) continue;
            argsMap = new HashMap<>();
            int i = 0;
            for (Class parameterType : declaredConstructor.getParameterTypes()) {
                if (parameterType.equals(Solver.class)) argsMap.put(++i, solver);
                if (parameterType.equals(String[].class)) argsMap.put(++i, args);
                if (parameterType.equals(Object[].class)) argsMap.put(++i, opts);
            }

            if (argsMap.size() != declaredConstructor.getParameterTypes().length) continue;

            matchingConstructor = (Constructor<A>) declaredConstructor;
            break;
        }


        try {
            solver.getObs().forEach(o -> o.onModuleSelect(module, name));

            if (argsMap.size() == 1)
                return matchingConstructor.newInstance(argsMap.get(1));
            if (argsMap.size() == 2)
                return matchingConstructor.newInstance(argsMap.get(1), argsMap.get(2));
            if (argsMap.size() == 3)
                return matchingConstructor.newInstance(argsMap.get(1), argsMap.get(2), argsMap.get(3));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("No constructors found");
    }

    public static String getModuleName(final String module, final String name) {
        if(name == null) {
            // set default
            // assumption: only one default per module set
            Map<String, Map<String, Class>> allModules = instance.getAllModules();
            for (String modKey : allModules.keySet()) {
                if(!modKey.equalsIgnoreCase(module)) continue;
                Map<String, Class> allImpls = allModules.getOrDefault(modKey, new HashMap<>());
                for (String nameKey : allImpls.keySet()) {
                    if (((Module) allImpls.get(nameKey).getAnnotation(Module.class)).defaultModule()) {
                        return nameKey;
                    }
                }
            }
        } else {
            return name;
        }

        throw new RuntimeException("No default found for module " + module);
    }

    private static Object getModuleClass(String module, String name)  {

        Class<?> formulaClass = instance.getModule(module, name);
        if(formulaClass == null)
            throw new IllegalArgumentException("Cannot find module " + module + ":" + name);
        return formulaClass;
    }

    public static ModuleDiscovery instance = new ModuleDiscovery();

    static void printHelp() {
        System.out.println("=== Modules");
        System.out.println("-m, --m module-type module-name");
        instance.getAllModules().forEach((module, nameimplpair) -> {
            System.out.println(module);
            nameimplpair.forEach((name, implclass) ->
                    System.out.println("    " + name +
                            (((Module) implclass.getAnnotation(Module.class)).defaultModule() ? " [default]" : "")
                    )
            );
        });
    }


    private Class getModule(String formula, String name) {
        return moduleBuckets.getOrDefault(formula, new HashMap<>()).get(name);
    }

    public Map<String, Map<String, Class>> getAllModules() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources("jmss");

            List<URL> paths = new LinkedList<>();
            while (resources.hasMoreElements()) {
                URL element = resources.nextElement();

                if(isDirectory(element)) {
                    paths.add(element);
                }
            }

            Map<String, Map<String, Class>> modules = new HashMap<>();
            for (URL p : paths) {
                merge(modules, findModules(p));
            }

            return modules;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could process jmss initialization");
        }
    }

    private Map<String, Map<String, Class>> findModules(URL directory) throws IOException {
        if(directory.getProtocol().equalsIgnoreCase("file")) {
            return findModulesFile(new File(directory.getFile()), "");
        } else if(directory.getProtocol().equalsIgnoreCase("jar")) {
            return findModulesJar(directory);
        }
        throw new IllegalArgumentException("Protocol not supported: " + directory.getProtocol() + " at " + directory);
    }

    private Map<String, Map<String, Class>> findModulesJar(URL directory) throws IOException {
        Map<String, Map<String, Class>> modules = new HashMap<>();

        String file = directory.getFile();
        int bangIndex = file.indexOf('!');
        file = new URL(file.substring(0, bangIndex)).getFile();
        ZipFile zip = new ZipFile(file);
        zip.stream().forEach(e -> {
            if(e.getName().endsWith(".class")) {
                try {
                    String className = e.getName().substring(0, e.getName().length() - 6);
                    Class<?> moduleClass = Class.forName(className.replace("/","."));
                    addIfModule(moduleClass, modules);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    throw new IllegalArgumentException(ex);
                }
            }
        });

        return modules;
    }

    private Map<String, Map<String, Class>> findModulesFile(File directory, String parent) {
        Map<String, Map<String, Class>> modules = new HashMap<>();

        if (!directory.exists()) {
            return modules;
        }

        File[] files = directory.listFiles();
        assert files != null;

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                merge(modules, findModulesFile(file, parent +"."+ file.getName()));
            } else if (file.getName().endsWith(".class")) {
                handleClass(modules, file, parent);
            }
        }

        return modules;
    }

    private void handleClass(Map<String, Map<String, Class>> modules, File file, String parent) {
        if(parent.isEmpty()) return;

        String parentPackages = parent.substring(1);
        String className = file.getName().substring(0, file.getName().length() - 6);

        try {
            Class<?> foundClass = Class.forName("jmss."+parentPackages + "." + className);
            addIfModule(foundClass, modules);
        } catch (Exception e) {
            System.err.println("Could not discover class " + className);
            e.printStackTrace();
        }
    }

    private void addIfModule(Class<?> foundClass, Map<String, Map<String, Class>> modules) {
        if(isModule(foundClass)) {
            Module moduleAnnotation = foundClass.getAnnotation(Module.class);
            Map<String, Class> impls = modules.getOrDefault(moduleAnnotation.module(), new HashMap<>());
            impls.put(moduleAnnotation.name(), foundClass);
            modules.put(moduleAnnotation.module(), impls);
        }
    }

    private boolean isModule(Class a) {
        Annotation annotation = a.getAnnotation(Module.class);
        return annotation != null;
    }

    private <A,B,C> void merge(Map<A, Map<B, C>> mergeInto, Map<A, Map<B, C>> other) {
        other.forEach((mod, impls) -> {
            Map<B,C> map = mergeInto.getOrDefault(mod, new HashMap<>());
            map.putAll(impls);
            mergeInto.put(mod, map);
        });
    }

    private static boolean isDirectory(URL url) throws IOException {
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            return new File(url.getFile()).isDirectory();
        }
        if (protocol.equals("jar")) {
            String file = url.getFile();
            int bangIndex = file.indexOf('!');
            String jarPath = file.substring(bangIndex + 2);
            file = new URL(file.substring(0, bangIndex)).getFile();
            ZipFile zip = new ZipFile(file);
            ZipEntry entry = zip.getEntry(jarPath);
            boolean isDirectory = entry.isDirectory();
            if (!isDirectory) {
                InputStream input = zip.getInputStream(entry);
                isDirectory = input == null;
                if (input != null) input.close();
            }
            return isDirectory;
        }
        throw new RuntimeException("Invalid protocol: " + protocol);
    }
}
