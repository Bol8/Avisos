package com.app.oscar.avisos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AvisosActivity extends AppCompatActivity {
    private ListView mListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);

        mListView = (ListView) findViewById(R.id.avisos_list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, //context
                R.layout.avisos_row, //layout (view)
                R.id.row_text, //row (view)
                new String[]{"first record,second record,third record,fourth record,five record"}  );

        mListView.setAdapter(arrayAdapter);
    }

   /*  @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_avisos);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                         .setAction("Action", null).show();
             }
         });
     }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avisos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){

           case R.id.action_nuevo:
               //crear nuevo aviso
               Log.d(getLocalClassName(),"crear nuevo aviso");
               return true;

           case R.id.action_salir:
               finish();
               return true;

           default:
               return false;
       }
    }



    /* @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();

         //noinspection SimplifiableIfStatement
         if (id == R.id.action_settings) {
             return true;
         }

         return super.onOptionsItemSelected(item);
     }*/
}
