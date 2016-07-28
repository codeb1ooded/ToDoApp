package com.example.megha.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<ToDoListContents> listToDos;
    ToDoListAdapter adapter;
    final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listToDos = getToDos();
        adapter = new ToDoListAdapter(this, listToDos);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, SeperateToDoActivity.class);
                ToDoListContents toDo = listToDos.get(position);
                i.putExtra(Constants.MainActivityToDo, toDo);
                i.putExtra(Constants.MainActivityPosition, position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                final int position = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you really want to delete all items");
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_confirm_all_deletion, null);
                builder.setView(v);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDoListContents toDo = listToDos.get(position);
                        listToDos.remove(position);
                        adapter.notifyDataSetChanged();
                        SQLHelper sqlHelper = new SQLHelper(MainActivity.this, 1);
                        SQLiteDatabase db = sqlHelper.getWritableDatabase();
                        db.delete(SQLHelper.TABLE_NAME, SQLHelper._ID + "=" + toDo.getID(), null);
                        Toast.makeText(MainActivity.this, toDo.title + " ToDo deleted", Toast.LENGTH_SHORT);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.create().show();
                return true;
            }
        });
    }

    private ArrayList<ToDoListContents> getToDos() {
        ArrayList<ToDoListContents> list_to_dos = new ArrayList<>();
        SQLHelper sqlHelper = new SQLHelper(this, 1);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        String columns[] = {SQLHelper.TITLE, SQLHelper.DATE, SQLHelper.CONTENT, SQLHelper._ID};
        Cursor c = db.query(false, SQLHelper.TABLE_NAME, columns, null, null, null, null, SQLHelper.DATE + " DESC", null);
        while (c.moveToNext()) {
            String title = c.getString(c.getColumnIndex(SQLHelper.TITLE));
            String date = c.getString(c.getColumnIndex(SQLHelper.DATE));
            String content = c.getString(c.getColumnIndex(SQLHelper.CONTENT));
            ToDoListContents todo = new ToDoListContents(title, date, content);
            list_to_dos.add(todo);
        }
        return list_to_dos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addItem) {
            Intent i = new Intent();
            i.setClass(MainActivity.this, SeperateToDoActivity.class);
            i.putExtra(Constants.MainActivityToDo, new ToDoListContents("", "", ""));
            startActivityForResult(i,REQUEST_CODE);
        }
        else if(item.getItemId() == R.id.deleteAllItem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm");
            builder.setMessage("Do you really want to delete all items");
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.dialog_confirm_all_deletion, null);
            builder.setView(v);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int size = listToDos.size();
                    for(int i =0; i<size; i++)
                        listToDos.remove(0);
                    adapter.notifyDataSetChanged();
                    SQLHelper sqlHelper = new SQLHelper(MainActivity.this, 1);
                    SQLiteDatabase db = sqlHelper.getWritableDatabase();
                    db.delete(SQLHelper.TABLE_NAME, null, null);
                    Toast.makeText(MainActivity.this, "All ToDos deleted", Toast.LENGTH_SHORT);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            builder.create().show();
        }
       /* }
        else if(item.getItemId() == R.id.searchItem){
            //To Do later on
        }*/
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra(Constants.SepereateToDoActivityPosition, -1);
            SQLHelper sqlHelper = new SQLHelper(this, 1);
            SQLiteDatabase db = sqlHelper.getWritableDatabase();
            ToDoListContents toDo = (ToDoListContents) data.getSerializableExtra(Constants.SepereateToDoActivityToDo);
            if (position == -1) {
                listToDos.add(toDo);
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper._ID, toDo.getID());
                cv.put(SQLHelper.CONTENT, toDo.content);
                cv.put(SQLHelper.DATE, toDo.date);
                cv.put(SQLHelper.TITLE, toDo.title);
                db.insert(SQLHelper.TABLE_NAME, null, cv);
                adapter.notifyDataSetChanged();
            } else {
                listToDos.set(position, toDo);
                String query = "UPDATE " + SQLHelper.TABLE_NAME + " SET " + SQLHelper.DATE + "= \"" + toDo.date +
                        "\" , " + SQLHelper.TITLE + "= \"" + toDo.title + "\" , " + SQLHelper.CONTENT + "= \"" + toDo.content +
                        "\"  WHERE " + SQLHelper._ID + " = " + toDo.getID() + ";";
                db.execSQL(query);
                adapter.notifyDataSetChanged();
            }
        }
    }
}