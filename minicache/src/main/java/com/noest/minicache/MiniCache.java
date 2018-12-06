package com.noest.minicache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * you can cache byte[] here, with high io speed,
 * data is safe, it will not lost when crash
 */
public class MiniCache {
    /**
     * cache folder, cache file in {@code sCacheFolder}/minicache/*
     */
    private static String sCacheFolder;

    /**
     * same process share one minicach instance
     */
    private static Map<String, MiniCache> cacheMap = new HashMap<>();

    private MiniCache() {
    }

    /**
     * init cache folder, if not exist create it
     *
     * @param cacheFolder
     * @return
     */
    public static String init(String cacheFolder) {
        File file = new File(cacheFolder);
        if (file.exists() && file.isFile()) {
            throw new IllegalArgumentException(cacheFolder + " is not folder");
        }
        File folder = new File(cacheFolder, "minicach");
        folder.mkdirs();
        sCacheFolder = folder.getAbsolutePath();
        return sCacheFolder;
    }

    public static MiniCache getDefaultCache() {
        return getCache("default");
    }

    private MmapOperator mOperator;

    public static MiniCache getCache(String id) {
        if (sCacheFolder == null) {
            throw new IllegalStateException("need call MiniCache.init() first");
        }
        if (cacheMap.containsKey(id)) {
            return cacheMap.get(id);
        }
        File cacheFile = new File(sCacheFolder, id);

        if (!cacheFile.exists()) {
            boolean success = false;
            try {
                success = cacheFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (!success) {
                    throw new IllegalStateException("create cache file failed , check permission or storage");
                }
            }
        }

        MiniCache miniCache = new MiniCache();
        miniCache.mOperator = new MmapOperator(cacheFile.getAbsolutePath());
        return miniCache;
    }


    public void put(String key, byte[] data) {
        mOperator.put(key, data);
    }

    public byte[] get(String key) {
        return mOperator.get(key);
    }

    public Map<String, byte[]> getAll() {
        return mOperator.getAll();
    }

    public void reload() {
        mOperator.loadData();
    }
}
