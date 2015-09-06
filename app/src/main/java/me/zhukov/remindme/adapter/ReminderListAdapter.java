package me.zhukov.remindme.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.List;

import me.zhukov.remindme.R;
import me.zhukov.remindme.activity.AddOrEditReminderActivity;
import me.zhukov.remindme.activity.MainActivity;
import me.zhukov.remindme.model.Reminder;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.VerticalItemHolder> {

    private MainActivity mMainActivity;
    private List<Reminder> mReminders;
    private MultiSelector mMultiSelector;
    private TextView mTvNoReminder;

    public ReminderListAdapter(AppCompatActivity activity, List<Reminder> reminders,
                               TextView tvNoReminder) {
        this.mMainActivity = (MainActivity) activity;
        this.mReminders = reminders;
        this.mMultiSelector = new MultiSelector();
        this.mTvNoReminder = tvNoReminder;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.reminder_item, parent, false);
        return new VerticalItemHolder(view, mMultiSelector);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder holder, int position) {
        Reminder reminder = mReminders.get(position);
        holder.setTitleAndThumbnail(reminder.getTitle());
        holder.setDateAndTime(reminder.getDate(), reminder.getTime());
        holder.setRepeatInfo(reminder.repeatToString());
        holder.setSilent(reminder.getSilent());
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    class VerticalItemHolder extends SwappingHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private ImageView mIvThumbnail;
        private ImageView mIvSilent;
        private TextView mTvTitle;
        private TextView mTvDataTime;
        private TextView mTvRepeatInfo;

        private ColorGenerator mColorGenerator;
        private TextDrawable mTextDrawable;

        private ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                super.onCreateActionMode(actionMode, menu);
                mMainActivity.getMenuInflater().inflate(R.menu.menu_context_main, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete_reminder:
                        mode.finish();
                        List<Reminder> deletedReminders = new ArrayList<>(getItemCount());
                        for (int position : mMultiSelector.getSelectedPositions()) {
                            deletedReminders.add(mReminders.get(position));
                        }
                        for (Reminder reminder : deletedReminders) {
                            mMainActivity.deleteItem(reminder);
                        }
                        if (mReminders.isEmpty()) {
                            mTvNoReminder.setVisibility(View.VISIBLE);
                        }
                        mMultiSelector.clearSelections();
                        return true;
                    case R.id.action_ok:
                        mode.finish();
                        mMultiSelector.clearSelections();
                        return true;
                }
                return false;
            }
        };

        public VerticalItemHolder(View itemView, MultiSelector multiSelector) {
            super(itemView, multiSelector);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);

            mIvThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            mIvSilent = (ImageView) itemView.findViewById(R.id.iv_silent);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvDataTime = (TextView) itemView.findViewById(R.id.tv_date_time);
            mTvRepeatInfo = (TextView) itemView.findViewById(R.id.tv_repeat_info);
            mColorGenerator = ColorGenerator.DEFAULT;
        }

        @Override
        public void onClick(View v) {
            if (!mMultiSelector.tapSelection(this)) {
                Reminder reminder = mReminders.get(getAdapterPosition());
                Intent intent = new Intent(
                        mMainActivity.getBaseContext(),
                        AddOrEditReminderActivity.class
                );
                intent.putExtra(MainActivity.UPDATE_REMINDER_INTENT, reminder);
                mMainActivity.setIntent(new Intent().putExtra(
                        MainActivity.UPDATE_REMINDER_ID_INTENT,
                        getAdapterPosition())
                );
                mMainActivity.startActivityForResult(intent, MainActivity.UPDATE_REMINDER_REQUEST);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mMainActivity.startSupportActionMode(mDeleteMode);
            mMultiSelector.setSelected(this, true);
            return true;
        }

        public void setTitleAndThumbnail(String title) {
            mTvTitle.setText(title);
            String letter = title.substring(0, 1);
            int color = mColorGenerator.getRandomColor();
            mTextDrawable = TextDrawable.builder().buildRound(letter, color);
            mIvThumbnail.setImageDrawable(mTextDrawable);
        }

        public void setDateAndTime(String date, String time) {
            mTvDataTime.setText(date + " " + time);
        }

        public void setRepeatInfo(String repeatInfo) {
            mTvRepeatInfo.setText(repeatInfo);
        }

        public void setSilent(boolean silent) {
            if (!silent) {
                mIvSilent.setImageResource(R.drawable.ic_bell_ring_grey);
            } else {
                mIvSilent.setImageResource(R.drawable.ic_bell_off_grey);
            }
        }
    }
}
