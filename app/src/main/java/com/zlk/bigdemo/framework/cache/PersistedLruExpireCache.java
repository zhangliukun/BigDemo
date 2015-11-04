package com.zlk.bigdemo.framework.cache;

import com.zlk.bigdemo.app.main.MyApplication;
import com.zlk.bigdemo.freeza.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 持久化LRU支持两种大类数据格式的缓存：1.序列化对象（支持过期）；2.文件，以流形式支持缓存
 * 注意要在适当的时候调用 flush() 方法
 *
 */
public class PersistedLruExpireCache<K extends String,V extends Serializable> extends Cache<K,V>{

    static final String sTAG = "PersistedLruExpireCache";

    private static final String CACHE_DIR_PREFIX="disc_lru_";

    private DiskLruCache mDiskLruCache = null;
    private long expireTime;

    public PersistedLruExpireCache(int cacheId, String accountId, long maxSize, int appVersion, long expireTime) throws IOException {
        super(accountId,10);// capacity 此处无意义

        this.expireTime = expireTime;
        File cacheDir = MyApplication.getInstance().getExternalCacheDir();
        if(cacheDir==null){
            //没有sd卡的情况
            cacheDir = MyApplication.getInstance().getFilesDir();
        }
        if(cacheDir == null){
            throw new IllegalStateException("can't get cache dir");
        }
        File dir = new File(cacheDir,CACHE_DIR_PREFIX+accountId+"_"+cacheId);
        if(!dir.exists()||!dir.isDirectory()){
            dir.mkdirs();
        }
        mDiskLruCache = DiskLruCache.open(dir,appVersion,1,maxSize);
    }

    /**
     *
     * @see #put(String, Serializable, long)
     * @see #put(String, Serializable)
     * 配合上面两个方法的读取缓存的方法
     * @param key
     * @return
     */
    @Override
    public Serializable get(String key) {
        InputStream is = null;
        boolean remove=false;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if(snapshot==null){
                return null;
            }
            is = snapshot.getInputStream(0);
            ObjectInputStream ois = new ObjectInputStream(is);
            ExpireCacheItem<Serializable> obj = (ExpireCacheItem<Serializable>)ois.readObject();
            Serializable value = obj.getValue(this.expireTime);
            if(value==null){
                remove=true;
            }
            return value;
        } catch (IOException e) {
            LogUtil.e(sTAG, e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LogUtil.e(sTAG, e.getMessage(), e);
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e(sTAG, e.getMessage(), e);
                }
            }
            if(remove){
                try {
                    mDiskLruCache.remove(key);
                } catch (IOException e) {
                    LogUtil.e(sTAG, e.getMessage(), e);
                }
            }
        }
        return null;
    }

    /**
     * @see #put(String, InputStream)
     * 配合上面的存储方法的读取缓存的方法
     * @param key
     * @return InputStream 使用完注意关闭
     */
    public InputStream getInputStream(String key){
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if(snapshot==null){
                return null;
            }
            InputStream is = snapshot.getInputStream(0);
            return is;
        } catch (IOException e) {
            LogUtil.e(sTAG, e.getMessage(), e);
        }
        return null;

    }


    @Override
    public void put(String k, Serializable v) {
        this.put(k,v,-1);

    }

    /**
     *
     * @param k
     * @param v
     * @param expireTime 过期时间，单位毫秒
     */
    public void put(String k, Serializable v,long expireTime) {
        OutputStream os = null;
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(k);
            ExpireCacheItem<Serializable> item = new ExpireCacheItem<Serializable>(v,expireTime);

            os = editor.newOutputStream(0);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(item);
            oos.flush();
            editor.commit();
        } catch (IOException e) {
            LogUtil.e(sTAG, e.getMessage(), e);
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.e(sTAG,e.getMessage(),e);
                }
            }
        }

    }

    /**
     *
     * @param k
     * @param inputStream 外部调用者自行关闭
     */
    public void put(String k,InputStream inputStream){
        DiskLruCache.Editor editor = null;
        OutputStream os = null;
        try {
            editor = mDiskLruCache.edit(k);
            os = editor.newOutputStream(0);

            byte[] buffer = new byte[32];
            int count = -1;
            while((count = inputStream.read(buffer)) != -1){
                os.write(buffer, 0, count);
            }
            os.flush();
            editor.commit();
        } catch (IOException e) {
            LogUtil.e(sTAG,e.getMessage(),e);
            if(editor!=null){
                try {
                    editor.abort();
                } catch (IOException e1) {
                    LogUtil.e(sTAG,e1.getMessage(),e1);
                }
            }
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    LogUtil.e(sTAG, e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void clear() {
        if(mDiskLruCache!=null){
            try {
                mDiskLruCache.delete();
            } catch (IOException e) {
                LogUtil.e(sTAG, e.getMessage(), e);
            }
        }
    }

    public void flush(){
        if(mDiskLruCache!=null){
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                LogUtil.e(sTAG, e.getMessage(), e);
            }
        }
    }
    public void close(){
        if(mDiskLruCache!=null){
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                LogUtil.e(sTAG, e.getMessage(), e);
            }
        }
    }
}
