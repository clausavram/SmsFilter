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

public class FiltersActivity extends AppCompatActivity {

    private FiltersAdapter filtersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filtersAdapter = new FiltersAdapter();
        filtersAdapter.setManager(((Application) getApplication()).getManager());
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
            startActivityForResult(new Intent(this, FilterDateActivity.class), Resources.RESULT_NEW_DATE_FILTER);
            return true;
        } else if (id == R.id.action_time_filter) {
            startActivityForResult(new Intent(this, FilterTimeActivity.class), Resources.RESULT_NEW_TIME_FILTER);
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
            Filter filter = (Filter) filtersAdapter.getItem(info.position);
            if (filter != null) {
                if (filter.isDateFilter()) {
                    final Intent intent = new Intent(this, FilterDateActivity.class);
                    intent.putExtra(Resources.EDIT_FILTER, filter);
                    startActivityForResult(intent, Resources.RESULT_EDIT_DATE_FILTER);
                } else {
                    final Intent intent = new Intent(this, FilterTimeActivity.class);
                    intent.putExtra(Resources.EDIT_FILTER, filter);
                    startActivityForResult(intent, Resources.RESULT_EDIT_TIME_FILTER);
                }
            }
            return true;
        } else if (R.id.filter_delete == item.getItemId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Filter filter = (Filter) filtersAdapter.getItem(info.position);
            if (filter != null) {
                ((Application) getApplication()).getManager().deleteFilter(filter);
                filtersAdapter.notifyDataSetChanged();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((Resources.RESULT_NEW_DATE_FILTER == requestCode || Resources.RESULT_NEW_TIME_FILTER == requestCode ||
                Resources.RESULT_EDIT_DATE_FILTER == requestCode || Resources.RESULT_EDIT_TIME_FILTER == requestCode) &&
                Activity.RESULT_OK == resultCode) {
            Filter filter = (Filter) data.getSerializableExtra(Resources.EDIT_FILTER);
            Log.d(getClass().getSimpleName(), "Editing finished => filter " + filter);
            if (filter != null) {
                ((Application) getApplication()).getManager().saveFilter(filter);
                Log.d(getClass().getSimpleName(), "Repo now contains: " + ((Application) getApplication()).getManager().getFilters());
                filtersAdapter.notifyDataSetChanged();
            }
        }
    }
}
