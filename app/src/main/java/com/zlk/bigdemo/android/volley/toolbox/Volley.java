/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zlk.bigdemo.android.volley.toolbox;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.zlk.bigdemo.android.volley.Network;
import com.zlk.bigdemo.android.volley.RequestQueue;
import com.zlk.bigdemo.freeza.Freeza;
import com.zlk.bigdemo.freeza.util.FileUtils;


import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Volley {

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "shinemo";

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack   An {@link HttpStack} to use for the network, or null for
     *                default.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }}, new SecureRandom());

        } catch (Exception e) {
        }
        if(stack == null){
            if(sc != null){
                stack = new HurlStack(null, sc.getSocketFactory());
            }else{
                stack = new HurlStack();
            }
        }

        File cacheDir = new File(FileUtils.getCachePath(context),
                DEFAULT_CACHE_DIR);
        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir),
                network);
        queue.start();
        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newImageRequestQueue(Context context) {

        String userAgent = "userAgent";
        String packageName = "packageName";
        try {
            packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (Exception e) {
        } catch (Throwable t) {

        }
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }}, new SecureRandom());

        } catch (Exception e) {
        }
        HttpStack stack = null;
        if(sc != null){
            stack = new HurlStack(null, sc.getSocketFactory());
        }else{
            stack = new HurlStack();
        }
        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(Freeza.getInstance()
                .getImageDiskCache(), network);
        queue.start();
        return queue;
    }

    /**
     * Creates a default instance of the worker pool and calls
     * {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }
}
