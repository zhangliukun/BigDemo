package com.zlk.bigdemo.app.main;


import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.zlk.bigdemo.freeza.BaseApplication;
import com.zlk.bigdemo.freeza.util.FileUtils;


import java.io.File;

public class MyApplication extends BaseApplication {


	@Override
	public void onCreate() {
		super.onCreate();
		DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder()
				.setBaseDirectoryName("fresco_img")
				.setBaseDirectoryPath(new File(FileUtils.getCachePath(this)))
				.build();
		ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
				.setMainDiskCacheConfig(diskCacheConfig)
				.build();

		Fresco.initialize(this, config);
	}
}
