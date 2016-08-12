package com.jimmywilcox.knittingcounter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CounterCursorAdapter extends CursorAdapter {

    public CounterCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.counter_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String counterName = cursor.getString(
                cursor.getColumnIndex(DBOpenHelper.COUNTER_NAME));

        int pos = counterName.indexOf(10);
        if (pos != -1) {
            counterName = counterName.substring(0, pos) + " ...";
        }

        TextView tv = (TextView) view.findViewById(R.id.tvCounter);
        tv.setText(counterName);
    }
}
