package sdutta9_a5.cs442.com.Assignment5_todolist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import sdutta9_a5.cs442.com.Assignment5_todolist.R;

public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener {

    private ArrayList<ToDoItem> todoItems;
    private ToDoItemAdapter aa;
    private mySharedPreference sharedPreference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate your view
        setContentView(R.layout.main);

        // Get references to the Fragments
        FragmentManager fm = getFragmentManager();
        ToDoListFragment todoListFragment =
                (ToDoListFragment)fm.findFragmentById(R.id.TodoListFragment);

        // Create the array list of to do items
        todoItems = new ArrayList<ToDoItem>();

        // Create the array adapter to bind the array to the ListView
        int resID = R.layout.todolist_item;
        aa = new ToDoItemAdapter(this, resID, todoItems);

        // Bind the array adapter to the ListView.
        todoListFragment.setListAdapter(aa);
    }

    public void onNewItemAdded(String newItem) {
        ToDoItem newTodoItem = new ToDoItem(newItem);
        if (todoItems.size()==0)
            newTodoItem.setNum(1);
        else
            newTodoItem.setNum(todoItems.get(0).getNum()+1);
        todoItems.add(0, newTodoItem);
        aa.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreference = new mySharedPreference();
        Iterator<ToDoItem> i = todoItems.iterator();
        String result="";
        while(i.hasNext()){
            ToDoItem temp = i.next();
            result+=temp.storeItem()+"#";
        }
        sharedPreference.save(this, result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreference = new mySharedPreference();
        String itemlst = sharedPreference.getValue(this);
        if(itemlst !=null) {
            StringTokenizer tkn = new StringTokenizer(itemlst, "#");
            todoItems.clear();
            while (tkn.hasMoreTokens()) {
                StringTokenizer temp = new StringTokenizer(tkn.nextToken(), "\t");
                int counter = Integer.parseInt(temp.nextToken());
                String task = temp.nextToken();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date created;
                try {
                    created = sdf.parse(temp.nextToken());
                } catch (ParseException e) {
                    created = new Date(java.lang.System.currentTimeMillis());
                }
                //ToDoItem newTodoItem = new ToDoItem(task,created);
                ToDoItem newTodoItem = new ToDoItem(task, created);
                newTodoItem.setNum(counter);
                todoItems.add(todoItems.size(), newTodoItem);
            }
            aa.notifyDataSetChanged();
        }
    }

    public void ResetBtnClicked(View view){
        todoItems.clear();
        aa.notifyDataSetChanged();
    }

    public void ExportBtnClicked(View view) {
        if (!isExternalStorageWritable())
            Toast.makeText(this,"SD Card is unmounted.",Toast.LENGTH_LONG).show();
        else{
            //Toast.makeText(this,"SD Card is mounted.",Toast.LENGTH_LONG).show();
            File file = new File(this.getExternalFilesDir(null),"A5_ToDoList.txt");

            //Toast.makeText(this,"Path: "+this.getExternalFilesDir(null).getPath(),Toast.LENGTH_LONG).show();
            try {
                Iterator<ToDoItem> i = todoItems.iterator();
                String result="";
                while(i.hasNext()){
                    ToDoItem temp = i.next();
                    result+=temp.storeItem()+"\n";
                }
                FileWriter fw =new FileWriter(file);
                fw.write(result);
                fw.close();
                Toast.makeText(this,"ToDoList exported.",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.w("ExternalStorage", "Error writing " + file, e);
            }
        }
    }
    public void ImportBtnClicked(View view) {
        if (!isExternalStorageWritable())
            Toast.makeText(this,"SD Card is unmounted.",Toast.LENGTH_LONG).show();
        else{
            //Toast.makeText(this,"SD Card is mounted.",Toast.LENGTH_LONG).show();
            File file = new File(this.getExternalFilesDir(null),"A5_ToDoList.txt");
            try {
                todoItems.clear();
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while((line = br.readLine())!=null) {
                    StringTokenizer temp = new StringTokenizer(line, "\t");
                    int counter = Integer.parseInt(temp.nextToken());
                    String task = temp.nextToken();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    Date created;
                    try {
                        created = sdf.parse(temp.nextToken());
                    } catch (ParseException e) {
                        created = new Date(java.lang.System.currentTimeMillis());
                    }
                    ToDoItem newTodoItem = new ToDoItem(task, created);
                    newTodoItem.setNum(counter);
                    todoItems.add(todoItems.size(), newTodoItem);
                }
                aa.notifyDataSetChanged();
                fr.close();
            } catch (IOException e) {
                Log.w("ExternalStorage", "Error reading " + file, e);
            }
        }
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        /*String state = Environment.getExternalStorageState();
        Toast.makeText(this,state, Toast.LENGTH_LONG).show();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;*/
        Boolean state = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return state;
    }
}