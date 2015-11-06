package sdutta9_a6.cs442.com.foodItem;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * An activity representing a single MenuItem detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link FoodItemListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link FoodItemDetailFragment}.
 */
public class FoodItemDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fooditem_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("item", getIntent().getParcelableExtra("item"));
            FoodItemDetailFragment fragment = new FoodItemDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.FoodItem_detail_container, fragment, "detailfrag")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainlst, menu);
        menu.removeItem(R.id.action_Add);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, FoodItemListActivity.class));
            return true;
        }
        sdutta9_a6.cs442.com.foodItem.MenuItem m = getIntent().getParcelableExtra("item");
        String menuid = Integer.toString(m.id);

        if(id == R.id.action_Delete){
            final Uri uri = Uri.parse(ExampleProvider.CONTENT_URI + "/" + menuid);
            new AlertDialog.Builder(this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            getContentResolver().delete(uri, null, null);
                            navigateUpTo(new Intent(getApplicationContext(),FoodItemListActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        if(id== R.id.action_Update){
            Intent foodItemEdit = new Intent(getBaseContext(), FoodItemEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("mode", "update");
            bundle.putString("rowId", menuid);
            foodItemEdit.putExtras(bundle);
            startActivity(foodItemEdit);
        }

        return super.onOptionsItemSelected(item);
    }
}
