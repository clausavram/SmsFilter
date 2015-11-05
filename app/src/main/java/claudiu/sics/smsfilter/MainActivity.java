package claudiu.sics.smsfilter;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import sisc.claudiu.smsfilter.R;

public class MainActivity extends AppCompatActivity {

    private MessagesAdapter messagesAdapter;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;


    private final BroadcastReceiver smsSavedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (messagesAdapter != null) {
                messagesAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Updated message view: " + messagesAdapter.getCount(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SendMessageActivity.class));
            }
        });

        messagesAdapter = new MessagesAdapter();
        messagesAdapter.setManager(((Application) getApplication()).getManager());
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(messagesAdapter);
        registerForContextMenu(listView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "SMS_SEND permission granted!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS_SEND permission NOT granted!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(smsSavedReceiver, new IntentFilter(Resources.ACTION_SMS_CHANGED));
        Log.d(getClass().getSimpleName(), "registered local broadcast receiver");

        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsSavedReceiver);
        Log.i(getClass().getSimpleName(), "unregistered local broadcast receiver");
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filters) {
            startActivity(new Intent(this, FiltersActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            getMenuInflater().inflate(R.menu.menu_messages, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (R.id.action_respond == item.getItemId()) {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final Message message = (Message) messagesAdapter.getItem(info.position);
            if (message != null) {
                final String phone = message.getPhone();
                final Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
                intent.putExtra(Resources.PHONE_NUMBER, phone);
                startActivity(intent);
                FilterService.startActionHandleSMSHandled(getApplicationContext(), message);
            }
            return true;
        } else if (R.id.action_delete == item.getItemId()) {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final Message message = (Message) messagesAdapter.getItem(info.position);
            if (message != null) {
                FilterService.startActionHandleSMSDelete(getApplicationContext(), message);
            }
            return true;
        } else if (R.id.action_mark_handled == item.getItemId()) {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final Message message = (Message) messagesAdapter.getItem(info.position);
            if (message != null) {
                FilterService.startActionHandleSMSHandled(getApplicationContext(), message);
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
