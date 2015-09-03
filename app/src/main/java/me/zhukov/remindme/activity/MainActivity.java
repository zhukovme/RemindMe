package me.zhukov.remindme.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.zhukov.remindme.R;
import me.zhukov.remindme.adapter.ReminderListAdapter;
import me.zhukov.remindme.model.Reminder;

public class MainActivity extends AppCompatActivity {

    private ReminderListAdapter mAdapter;
    private List<Reminder> mReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fabAddReminder = (FloatingActionButton) findViewById(R.id.fab_add_reminder);
        RecyclerView mRvReminderList = (RecyclerView) findViewById(R.id.rv_reminder_list);

        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mReminders = new ArrayList<>();

        //test
        Reminder reminder = new Reminder("First", "3/9/2015", "20:22", true, "5", "Minute", true);
        Reminder reminder2 = new Reminder("Second", "3/9/2015", "20:22", false, "5", "Minute", false);
        mReminders.add(reminder);
        mReminders.add(reminder2);
        //test

        mAdapter = new ReminderListAdapter(MainActivity.this, mReminders);
        mRvReminderList.setAdapter(mAdapter);
        mRvReminderList.setLayoutManager(getLayoutManager());
        registerForContextMenu(mRvReminderList);

        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context_main, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
