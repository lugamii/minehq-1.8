package dev.lugami.uhub.util;


import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    private ClassUtil() {
        throw new RuntimeException("Cannot instantiate a utility class.");
    }

    /**
     * Gets all the classes in a the provided package.
     *
     * @param clazz       The class
     * @param packageName The package to scan classes in.
     *
     * @return The classes in the package packageName.
     */
    public static Collection<Class<?>> getClassesInPackage(Class clazz, String packageName) {
        Collection<Class<?>> classes = new ArrayList<>();

        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();
        String relPath = packageName.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jarFile;

        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw (new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e));
        }

        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;

            if (entryName.endsWith(".class") && entryName.startsWith(relPath) &&
                    entryName.length() > (relPath.length() + "/".length())) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }

            if (className != null) {
                Class<?> foundClazz = null;

                try {
                    foundClazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (foundClazz != null) {
                    classes.add(foundClazz);
                }
            }
        }

        try {
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (ImmutableSet.copyOf(classes));
    }

}