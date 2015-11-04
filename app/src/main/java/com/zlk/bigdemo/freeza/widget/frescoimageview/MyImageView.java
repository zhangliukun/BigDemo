//package com.zlk.bigdemo.freeza.widget.frescoimageview;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.drawable.ProgressBarDrawable;
//import com.facebook.drawee.drawable.ScalingUtils;
//import com.facebook.drawee.generic.GenericDraweeHierarchy;
//import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
//import com.facebook.drawee.generic.RoundingParams;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.zlk.bigdemo.android.volley.toolbox.ImageRequest;
//
///**
// * Created by ShineMo-177 on 2015/10/8.
// */
//public class MyImageView extends SimpleDraweeView{
//    public MyImageView(Context context) {
//        super(context);
//        init();
//    }
//
//    public MyImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init();
//    }
//
//    public void init(){
//
//    }
//
//
//    public GenericDraweeHierarchy getHierarchy(){
//        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
//        GenericDraweeHierarchy hierarchy = builder
//                .setFadeDuration(300)
//                .setProgressBarImage(new ProgressBarDrawable())
//                .setRoundingParams(roundingParams)
//                .build();
//    }
//
//
//    private GenericDraweeHierarchy getGenericDraweeHierarchy(Context context, SimpleDraweeView draweeView) {
//        return new GenericDraweeHierarchyBuilder(context.getResources())
//                .setFadeDuration(draweeView.getFadeDuration())
//                .setOverlay(draweeView.getmOverlay())
//                .setActualImageScaleType(draweeView.getDraweeViewScaleType())
//                .setProgressBarImage(draweeView.getProgressBar(), ScalingUtils.ScaleType.CENTER_INSIDE)
//                .setPlaceholderImage(draweeView.getPlaceholderDrawable(), ScalingUtils.ScaleType.CENTER_CROP)
//                .build();
//    }
//
////    private GenericDraweeHierarchy getRoundGenericDraweeHierarchy(Context context, MySimpleDraweeView draweeView) {
////        return new GenericDraweeHierarchyBuilder(context.getResources())
////                .setPlaceholderImage(draweeView.getPlaceholderDrawable())
////                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
//////                .setProgressBarImage(draweeView.getProgressBar(), ScalingUtils.ScaleType.CENTER_INSIDE)
////                .setRoundingParams(RoundingParams.asCircle())
////                .build();
////    }
//
//
//    private DraweeController getDraweeController(ImageRequest imageRequest, MySimpleDraweeView view) {
//        return Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(view.getAutoPlayAnimations())//自动播放图片动画
//                .setControllerListener(view.getControllerListener())
//                .setImageRequest(imageRequest)
//                .setOldController(view.getController())
//                .build();
//    }
//
//
//}
