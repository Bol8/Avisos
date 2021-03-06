package com.app.oscar.avisos;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Oscar on 10/10/2016.
 */

public class AvisosSimpleCursorAdapter extends SimpleCursorAdapter {


    public AvisosSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);


        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder == null){
            holder = new ViewHolder();
            holder.colImp = cursor.getColumnIndexOrThrow(AvisosDBAdapter.COL_IMPORTANT);
            holder.listTab = view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }

        if(cursor.getInt(holder.colImp) > 0){
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.naranja));
        }else{
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.rosa));
        }

    }


    static class ViewHolder{
        //almacena el indx de la columna
        int colImp;

        //store the view
        View listTab;
    }


}
