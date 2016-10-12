package com.app.oscar.avisos;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AvisosActivity extends AppCompatActivity {
    private ListView mListView;
    private AvisosDBAdapter mDBAdapter;
    private AvisosSimpleCursorAdapter mCursorAdapter;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);

        mListView = (ListView) findViewById(R.id.avisos_list_view);
        mListView.setDivider(null);

        mDBAdapter = new AvisosDBAdapter(this);
        mDBAdapter.open();

        if(savedInstanceState == null){
            //limpiar todos los datos
            mDBAdapter.deleteAllReminders();

            //Add algunos datos
            mDBAdapter.createReminder("Visitar médico",true);
            mDBAdapter.createReminder("Enviar regalos",false);
            mDBAdapter.createReminder("Hacer compra semanal",false);

        }


        Cursor cursor = mDBAdapter.fetchAllReminders();

        //desde las columnas definidas en la base de datos
        String[] from = new String[]{
                AvisosDBAdapter.COL_CONTENT
        };

        //a la id de views en le layout
        int[] to = new int[]{
                R.id.row_text
        };

        mCursorAdapter = new AvisosSimpleCursorAdapter(
          //context
          AvisosActivity.this,
          //el layout de la fila
          R.layout.avisos_row,
          //cursor
          cursor,
          //desde columnas definidas en la base de datos
          from,
          //a las ids de views en el layout
          to,
          //flag - no usado
          0  );


        //el cursorAdapter (controller) está ahora actualizado la listView (view)
        //con datos desde la base de datos (modelo)
        mListView.setAdapter(mCursorAdapter);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu,menu);

                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_aviso:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--){
                                if(mListView.isItemChecked(nC)){
                                    mDBAdapter.deleteReminderById(getIdFromPosition(nC));
                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDBAdapter.fetchAllReminders());
                            return true;
                    }


                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {  }



                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) { }

            });

        }


        //cuando pulsamos en un item individual en la listview
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int masterListPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AvisosActivity.this);

                ListView modelListView = new ListView(AvisosActivity.this);
                String[] modes = new String[]{"Editar Aviso","Borrar Aviso"};

                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(AvisosActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);

                modelListView.setAdapter(modeAdapter);
                builder.setView(modelListView);

                final Dialog dialog = builder.create();
                dialog.show();

                modelListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //editar aviso
                        if(position == 0){
                            int nId = getIdFromPosition(masterListPosition);
                            Aviso aviso = mDBAdapter.fetchReminderById(nId);
                            fireCustomDialog(aviso);

                          /*  Toast.makeText(AvisosActivity.this, "editar " + position,
                                    Toast.LENGTH_SHORT).show();*/
                        }else{

                            mDBAdapter.deleteReminderById(getIdFromPosition(masterListPosition));
                            mCursorAdapter.changeCursor(mDBAdapter.fetchAllReminders());
                           /* Toast.makeText(AvisosActivity.this, "borrar " + position,
                                    Toast.LENGTH_SHORT).show();*/
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }


    private int getIdFromPosition(int nC){
        Log.d(getLocalClassName(),"position " + nC);
        return (int) mCursorAdapter.getItemId(nC);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avisos, menu);
        return true;
    }

    private void fireCustomDialog(final Aviso aviso){
        //custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);

        TextView titleTextView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) dialog.findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (aviso != null);

        if(isEditOperation){
            titleTextView.setText("Editar Aviso");
            checkBox.setChecked(aviso.getImportant() == 1);
            editCustom.setText(aviso.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.amarillo_apagado));
        }

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if(isEditOperation){
                    Aviso reminderEdited = new Aviso(aviso.getId(),reminderText,checkBox.isChecked() ? 1 : 0);
                    mDBAdapter.updateReminder(reminderEdited);
                }else{
                    mDBAdapter.createReminder(reminderText,checkBox.isChecked());
                }

                mCursorAdapter.changeCursor(mDBAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });

        Button buttonCancel = (Button) dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch (item.getItemId()){

           case R.id.action_nuevo:
               //crear nuevo aviso
              // Log.d(getLocalClassName(),"crear nuevo aviso");
               fireCustomDialog(null);
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
