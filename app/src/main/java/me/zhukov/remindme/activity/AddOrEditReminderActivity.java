package me.zhukov.remindme.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import me.zhukov.remindme.DateAndTimeManager;
import me.zhukov.remindme.R;
import me.zhukov.remindme.model.Reminder;

public class AddOrEditReminderActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Reminder mReminder;

    private Toolbar mToolbar;
    private EditText mEtReminderTitle;
    private FloatingActionButton mFabReminderSilent;
    private TextView mTvReminderDate;
    private TextView mTvReminderTime;
    private TextView mTvReminderRepeat;
    private Switch mSwitchReminderRepeat;
    private TextView mTvReminderRepeatNumber;
    private TextView mTvReminderRepeatType;

    private DateAndTimeManager mDtManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_reminder);

        //test
        mReminder = Reminder.getDefaultReminder();
        //test

        mDtManager = new DateAndTimeManager();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mEtReminderTitle = (EditText) findViewById(R.id.et_reminder_title);
        mFabReminderSilent = (FloatingActionButton) findViewById(R.id.fab_reminder_silent);
        mTvReminderDate = (TextView) findViewById(R.id.tv_reminder_date);
        mTvReminderTime = (TextView) findViewById(R.id.tv_reminder_time);
        mTvReminderRepeat = (TextView) findViewById(R.id.tv_reminder_repeat);
        mSwitchReminderRepeat = (Switch) findViewById(R.id.switch_reminder_repeat);
        mTvReminderRepeatNumber = (TextView) findViewById(R.id.tv_reminder_repeat_number);
        mTvReminderRepeatType = (TextView) findViewById(R.id.tv_reminder_repeat_type);

        setupToolbar();

        setViewValues();

        mEtReminderTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mReminder.setTitle(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mFabReminderSilent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminder.setSilent(!mReminder.getSilent());
                changeFabIcon();
            }
        });

        mSwitchReminderRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReminder.setRepeat(((Switch) v).isChecked());
                mTvReminderRepeat.setText(mReminder.repeatToString());
            }
        });
    }

    private void setupToolbar() {
        mToolbar.setTitle(R.string.add_or_edit_reminder_toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setViewValues() {
        mTvReminderDate.setText(mReminder.getDate());
        mTvReminderTime.setText(mReminder.getTime());
        mTvReminderRepeat.setText(mReminder.repeatToString());
        mSwitchReminderRepeat.setChecked(mReminder.getRepeat());
        mTvReminderRepeatNumber.setText(mReminder.getRepeatNumber());
        mTvReminderRepeatType.setText(mReminder.getRepeatType());
        changeFabIcon();
    }

    private void changeFabIcon() {
        if (mReminder.getSilent()) {
            mFabReminderSilent.setImageResource(R.drawable.ic_bell_off_white);
        } else {
            mFabReminderSilent.setImageResource(R.drawable.ic_bell_ring_white);
        }
    }

    private void saveReminder() {

    }

    public void onClickSetDate(View view) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                mDtManager.getCurrentYear(),
                mDtManager.getCurrentMonth(),
                mDtManager.getCurrentDay()
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mReminder.setDate(mDtManager.convertDate(day, month + 1, year));
        mTvReminderDate.setText(mReminder.getDate());
    }

    public void onClickSetTime(View view) {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                mDtManager.getCurrentDay(),
                mDtManager.getCurrentMinute(),
                true
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        mReminder.setTime(mDtManager.convertTime(hour, minute));
        mTvReminderTime.setText(mReminder.getTime());
    }

    public void onClickSetRepeatNumber(View view) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.enter_number));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton(
                getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String number = input.getText().toString().trim();
                        if (number.isEmpty() || Integer.valueOf(number) == 0) {
                            dialog.dismiss();
                        } else {
                            mReminder.setRepeatNumber(Integer.valueOf(number).toString());
                            mTvReminderRepeatNumber.setText(mReminder.getRepeatNumber());
                            mTvReminderRepeat.setText(mReminder.repeatToString());
                        }
                    }
                }
        );
        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    public void onClickSetRepeatType(View view) {
        final String[] items = getResources().getStringArray(R.array.repeat_type);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.select_type));
        builder.setItems(
                items,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        mReminder.setRepeatType(items[item]);
                        mTvReminderRepeatType.setText(mReminder.getRepeatType());
                        mTvReminderRepeat.setText(mReminder.repeatToString());
                    }
                }
        );
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_or_edit_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_discard_reminder:
                onBackPressed();
                return true;
            case R.id.action_save_reminder:
                if (mEtReminderTitle.getText().toString().isEmpty()) {
                    mEtReminderTitle.setError(getResources().getString(R.string.blank_title_error));
                } else {
                    Toast.makeText(
                            this,
                            getResources().getString(R.string.saved),
                            Toast.LENGTH_SHORT
                    ).show();
                    saveReminder();
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
