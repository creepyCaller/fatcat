package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.io.io.StandardReader;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum Cache {
    INSTANCE;

    private final Map<String, Cache.Entry> cache = new HashMap<>();

    public void put(String direction, File file) throws IOException {
        if (!cache.containsKey(direction)) {
            if (!file.exists() || !file.isFile()) {
                // 不能请求文件夹和不存在的文件
                throw new FileNotFoundException(); // FileNotFoundException
            }
            synchronized (Cache.INSTANCE.cache) {
                if (!cache.containsKey(direction)) {
                    cache.put(direction, Cache.Entry.newEntry(file));
                }
            }
        }
    }

    public Cache.Entry get(String direction) {
        return cache.get(direction);
    }

    @Builder
    public static class Entry implements AutoCloseable {
        public static final int capacity = 1024 * 1024;
        private File file;
        private FileInputStream fIStr;
        private FileChannel fileChannel;
        private byte[] context;

        static Cache.Entry newEntry(File file) throws IOException {
            FileInputStream fIStr = new FileInputStream(file);
            FileChannel fileChannel = fIStr.getChannel();
            return builder()
                    .file(file)
                    .fIStr(fIStr)
                    .fileChannel(fileChannel)
                    .build();
        }

        public long getSize() throws IOException {
            return fileChannel.size();
        }

        public FileChannel getChannel() throws IOException {
            return fileChannel;
        }

        @Override
        public void close() throws Exception {
            fIStr.close();
            fileChannel.close();
        }

        public byte[] getContext() throws IOException {
            if (context == null) {
                synchronized (this) {
                    if (context == null) {
                        context = StandardReader.getReader(fIStr).readBinStr();
                    }
                }
            }
            return context;
        }
    }
}
