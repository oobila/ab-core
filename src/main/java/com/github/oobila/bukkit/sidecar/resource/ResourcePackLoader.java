package com.github.oobila.bukkit.sidecar.resource;

import com.github.oobila.bukkit.sidecar.SidecarConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourcePackLoader {

    private static final List<Class<? extends Resource>> RESOURCE_TYPES = new ArrayList<>();

    public static <R extends Resource> void register(Class<R> resourceClass) {
        RESOURCE_TYPES.add(resourceClass);
    }

    public static <P extends ResourcePack> P loadResourcePackMetaData(File file, Class<P> type) throws Exception {
        if (!FilenameUtils.getExtension(file.getName()).equals("zip")) {
            throw new Exception(file.getName() + " is not a resource pack");
        }
        P resourcePack = createResourcePack(file.getName(), file, type);
        try (ZipFile zip = new ZipFile(file)) {
            for (Enumeration<? extends ZipEntry> zipEntries = zip.entries(); zipEntries.hasMoreElements(); ) {
                ZipEntry entry = zipEntries.nextElement();
                try {
                    if (!entry.isDirectory()) {
                        //found file inside zip
                        InputStream inputStream = zip.getInputStream(entry);
                        Pair<String, String> zipNameDetails = getResourceDetails(entry);

                        //getting the type of resource for this file
                        Class<? extends Resource> resourceType = getResourceType(zipNameDetails);
                        Resource resource = createResource(zipNameDetails.getKey(), zipNameDetails.getValue(), resourceType, inputStream);
                        resource.loadMetaData(inputStream);
                        resourcePack.resourceMap.computeIfAbsent(zipNameDetails.getKey(), strings -> new HashMap<>());
                        resourcePack.resourceMap.get(zipNameDetails.getKey()).put(zipNameDetails.getValue(), resource);
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to load resource: " + entry.getName());
                    e.printStackTrace();
                }
            }
            return resourcePack;
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "Failed loading resource pack: " + file.getName());
        }
        return null;
    }

    static <P extends ResourcePack> void loadResourcePack(P resourcePack) {
        try (ZipFile zip = new ZipFile(resourcePack.file)) {
            for (Enumeration<? extends ZipEntry> zipEntries = zip.entries(); zipEntries.hasMoreElements(); ) {
                ZipEntry entry = zipEntries.nextElement();
                try {
                    if (!entry.isDirectory()) {
                        //found file inside zip
                        InputStream inputStream = zip.getInputStream(entry);
                        Pair<String, String> zipNameDetails = getResourceDetails(entry);

                        //match the resource
                        Resource resource = resourcePack.getResourceMap().get(zipNameDetails.getKey()).get(zipNameDetails.getValue());
                        if (resource == null) {
                            Bukkit.getLogger().log(Level.SEVERE, "Failed loading resource: " + resource.getName());
                            break;
                        }
                        resource.loadData(inputStream);
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to load resource: " + entry.getName());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "Failed loading resource pack: " + resourcePack.getName());
        }
    }

    private static Class<? extends Resource> getResourceType(Pair<String, String> zipNameDetails) throws NoSuchMethodException {
        for (Class<? extends Resource> resourceClass : RESOURCE_TYPES) {
            try {
                Method inspectResourceMethod = resourceClass.getMethod(
                        "inspectResource",
                        String.class,
                        String.class
                );
                boolean isResourceType = (boolean) inspectResourceMethod.invoke(null, zipNameDetails.getKey(), zipNameDetails.getValue());
                if (isResourceType) {
                    return resourceClass;
                }
            } catch (NoSuchMethodException e) {
                throw new NoSuchMethodException("Resource class " + resourceClass.getName() +
                        " requires the method 'inspectResource'");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Pair<String, String> getResourceDetails(ZipEntry entry) {
        String[] nameSplit = entry.getName().split("/");
        String[] folder = new String[0];
        if (nameSplit.length > 1) {
            folder = Arrays.copyOfRange(nameSplit, 0, nameSplit.length - 1);
        }
        String entryName = nameSplit[nameSplit.length - 1];
        return Pair.of(Arrays.stream(folder).collect(Collectors.joining("/")), entryName);
    }

    private static <T extends ResourcePack> T createResourcePack(String fileName, File file, Class<T> type) {
        try {
            T resourcePack = type.getConstructor().newInstance();
            resourcePack.name = FilenameUtils.getBaseName(fileName).toLowerCase();
            resourcePack.file = file;
            return resourcePack;
        } catch (InstantiationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed loading resource pack: " + fileName +
                    ". No default constructor for " + type.getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Resource createResource(String folder, String name, Class<? extends Resource> type, InputStream inputStream) {
        try {
            Resource<?> resource;
            if (YAMLResource.class.isAssignableFrom(type)) {
                Class<? extends YAMLResource> yamlType = (Class<? extends YAMLResource>) type;
                resource = YAMLResource.construct(inputStream, yamlType);
            } else {
                resource = type.getConstructor().newInstance();
            }
            resource.name = FilenameUtils.getBaseName(name).toLowerCase();
            resource.fileExtension = FilenameUtils.getExtension(name).toLowerCase();
            resource.folder = folder;
            resource.type = type;
            return resource;
        } catch (InstantiationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed loading resource: " + name +
                    ". No default constructor for " + type.getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getResourceNames(Plugin plugin, String path) {
        File file = new File(plugin.getDataFolder(), path);
        if (file.exists() && file.isDirectory()) {
            return Arrays.stream(file.listFiles(f -> FilenameUtils.getExtension(f.getName()).equals("zip"))).map(File::getName).toList();
        } else {
            return null;
        }
    }

}
