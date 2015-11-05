package claudiu.sics.smsfilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import sisc.claudiu.smsfilter.R;

public class SMSBuddyFiltersActivity extends AppCompatActivity {

    private SMSBuddyFiltersAdapter filtersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filtersAdapter = new SMSBuddyFiltersAdapter();
        filtersAdapter.setManager(((SMSBuddyApp) getApplication()).getManager());
        ListView listView = (ListView) findViewById(R.id.filterListView);
        listView.setAdapter(filtersAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_date_filter) {
            startActivityForResult(new Intent(this, SMSBuddyFilterDateActivity.class), SMSBuddyResources.RESULT_NEW_DATE_FILTER);
            return true;
        } else if (id == R.id.action_time_filter) {
            startActivityForResult(new Intent(this, SMSBuddyFilterTimeActivity.class), SMSBuddyResources.RESULT_NEW_TIME_FILTER);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.filterListView) {
            getMenuInflater().inflate(R.menu.menu_list_filters, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (R.id.filter_edit == item.getItemId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            SMSBuddyFilter filter = (SMSBuddyFilter) filtersAdapter.getItem(info.position);
            if (filter != null) {
                if (filter.isDateFilter()) {
                    final Intent intent = new Intent(this, SMSBuddyFilterDateActivity.class);
                    intent.putExtra(SMSBuddyResources.EDIT_FILTER, filter);
                    startActivityForResult(intent, SMSBuddyResources.RESULT_EDIT_DATE_FILTER);
                } else {
                    final Intent intent = new Intent(this, SMSBuddyFilterTimeActivity.class);
                    intent.putExtra(SMSBuddyResources.EDIT_FILTER, filter);
                    startActivityForResult(intent, SMSBuddyResources.RESULT_EDIT_TIME_FILTER);
                }
            }
            return true;
        } else if (R.id.filter_delete == item.getItemId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            SMSBuddyFilter filter = (SMSBuddyFilter) filtersAdapter.getItem(info.position);
            if (filter != null) {
                ((SMSBuddyApp) getApplication()).getManager().deleteFilter(filter);
                filtersAdapter.notifyDataSetChanged();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((SMSBuddyResources.RESULT_NEW_DATE_FILTER == requestCode || SMSBuddyResources.RESULT_NEW_TIME_FILTER == requestCode ||
                SMSBuddyResources.RESULT_EDIT_DATE_FILTER == requestCode || SMSBuddyResources.RESULT_EDIT_TIME_FILTER == requestCode) &&
                Activity.RESULT_OK == resultCode) {
            SMSBuddyFilter filter = (SMSBuddyFilter) data.getSerializableExtra(SMSBuddyResources.EDIT_FILTER);
            Log.d(getClass().getSimpleName(), "Editing finished => filter " + filter);
            if (filter != null) {
                ((SMSBuddyApp) getApplication()).getManager().saveFilter(filter);
                Log.d(getClass().getSimpleName(), "Repo now contains: " + ((SMSBuddyApp) getApplication()).getManager().getFilters());
                filtersAdapter.notifyDataSetChanged();
            }
        }
    }
}
