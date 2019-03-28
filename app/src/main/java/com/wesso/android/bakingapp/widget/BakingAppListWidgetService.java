package com.wesso.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.provider.IngredientContract;

import java.util.ArrayList;
import java.util.List;

public class BakingAppListWidgetService extends RemoteViewsService {

    public final static String EXTRA_RECIPE = "com.wesso.android.bakingapp.recipe";
    public final static String TAG = "BakingAppListWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private Cursor mCursor;
        private static final String TAG = "ListRemoteViewsFacory";

        public ListRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            if(mCursor != null) {
                mCursor.close();
            }

            final long identityToken = Binder.clearCallingIdentity();
            Uri uri = IngredientContract.IngredientEntry.CONTENT_URI;
            mCursor = mContext.getContentResolver().query(uri,
                    null,
                    null,
                    null,
                    IngredientContract.IngredientEntry._ID + " ASC");

            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if(mCursor != null) {
                mCursor.close();
            }
        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
                return null;
            }

            RemoteViews view = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);

            view.setTextViewText(android.R.id.text1,mCursor.getString(3));
            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
