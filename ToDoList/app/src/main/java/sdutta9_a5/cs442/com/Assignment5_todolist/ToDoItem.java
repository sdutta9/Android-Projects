package sdutta9_a5.cs442.com.Assignment5_todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.text.format.Time;


public class ToDoItem {

  String task;
  Date created;
  int counter = 0;
  Time today;
  public int getNum() {return counter;}
  public void setNum(int c){counter =c;}

  public String getTask() {
    return task;
  }

  public Date getCreated() {
    return created;
  }

  public ToDoItem(String _task) {
    this(_task, new Date(java.lang.System.currentTimeMillis()));
  }

  public ToDoItem(String _task, Date _created) {
    task = _task;
    created = _created;

    today = new Time(Time.getCurrentTimezone());
    today.setToNow();
  }
  public String storeItem(){
    String datestr = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(created);
    String numstr = Integer.toString(counter);
    String result_itm =numstr+"\t"+task+"\t"+datestr;
    return result_itm;
  }

  @Override
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    String dateString = sdf.format(created);
      String timeString = today.format("%k:%M:%S");
    return "(" + timeString +") " + task;
  }
}