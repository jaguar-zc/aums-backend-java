package cn.stackflow.aums.common.cache;

import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 10:43
 */
public abstract class AbstractCache<T, E> extends CacheLoader<T, E> implements LoadingCache<T, E>, RemovalListener<T, E> {

    private LoadingCache<T, E> loadingCache;

    //设置写缓存后8秒钟过期
    public int getExpireAfterWrite() {
        return 86400;
    }
    //设置写缓存后5秒钟刷新
    public int getRefreshAfterWrite() {
        return 5;
    }

    protected void newBuilder() {
        this.loadingCache = CacheBuilder.newBuilder()
                //设置并发级别为10，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(10)
                //设置写缓存后8秒钟过期
                .expireAfterWrite(getExpireAfterWrite(), TimeUnit.SECONDS)
                //设置写缓存后1秒钟刷新
                .refreshAfterWrite(getRefreshAfterWrite(), TimeUnit.SECONDS)
                //设置缓存容器的初始容量为5
                .initialCapacity(5)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知
                .removalListener(this)
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(this);
    }

    public AbstractCache() {
        newBuilder();
    }

    @Override
    public E get(T t) {
        try {
            return loadingCache.get(t);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public E getUnchecked(T t) {
        return loadingCache.getUnchecked(t);
    }

    @Override
    public ImmutableMap<T, E> getAll(Iterable<? extends T> iterable) throws ExecutionException {
        return loadingCache.getAll(iterable);
    }

    /**
     * @param t
     * @deprecated
     */
    @Override
    public E apply(T t) {
        return loadingCache.apply(t);
    }

    @Override
    public void refresh(T t) {
        loadingCache.refresh(t);
    }

    @Override
    public ConcurrentMap<T, E> asMap() {
        return loadingCache.asMap();
    }


    @Nullable
    @Override
    public E getIfPresent(Object o) {
        return loadingCache.getIfPresent(o);
    }

    @Override
    public E get(T t, Callable<? extends E> callable) throws ExecutionException {
        return loadingCache.get(t, callable);
    }

    @Override
    public ImmutableMap<T, E> getAllPresent(Iterable<?> iterable) {
        return loadingCache.getAllPresent(iterable);
    }

    @Override
    public void put(T t, E e) {
        loadingCache.put(t, e);
    }

    @Override
    public void putAll(Map<? extends T, ? extends E> map) {
        loadingCache.putAll(map);
    }

    @Override
    public void invalidate(Object o) {
        loadingCache.invalidate(o);
    }

    @Override
    public void invalidateAll(Iterable<?> iterable) {
        loadingCache.invalidateAll(iterable);
    }

    @Override
    public void invalidateAll() {
        loadingCache.invalidateAll();
    }

    @Override
    public long size() {
        return loadingCache.size();
    }

    @Override
    public CacheStats stats() {
        return loadingCache.stats();
    }

    @Override
    public void cleanUp() {
        loadingCache.cleanUp();
    }

}
