package com.zlk.bigdemo.app.notebook;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.main.MainActivity;
import com.zlk.bigdemo.app.main.fragment.SecondFragment;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by zale on 2015/11/2.
 */
public class NoteProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        if (action.equals(SecondFragment.DESKTOP_WIDGET_BROADCAST_ACTION)){
            Bundle bundle = intent.getExtras();
            String newNoteText = (String) bundle.getCharSequence(SecondFragment.DESKTOP_WIDGET_NEW_NOTE,"");
            if (newNoteText!=null&&!newNoteText.equals("")){
                updateAllWidgets(context, AppWidgetManager.getInstance(context), newNoteText);
            }
        }
    }

    /**
     * 更新所有的控件
     * @param context
     * @param instance
     */
    private void updateAllWidgets(Context context, AppWidgetManager instance,String newNoteText) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_note_desktop);
        remoteViews.setTextViewText(R.id.note_text,newNoteText);
        ComponentName provider = new ComponentName(context, NoteProvider.class);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.note_text,pendingIntent);
        instance.updateAppWidget(provider,remoteViews);
    }

    /**
     * 更新时执行
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * 删除时触发
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 第一个被添加
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 最后一个被删除
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

//
//    /**
//     * 显示所有的id
//     */
//    private void showAppWidgetId(){
//        Iterator<Integer> iterator = widgetIds.iterator();
//        while (iterator.hasNext()){
//            Log.i("widgetIds",iterator.next().toString());
//        }
//    }

}
