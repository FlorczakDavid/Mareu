package com.florczakdavid.maru.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.florczakdavid.maru.R;

import java.util.ArrayList;
import java.util.List;

public class FilterByLocationListRecycleViewAdapter extends RecyclerView.Adapter<FilterByLocationListRecycleViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> mSelectedData;
    private LayoutInflater mInflater;

    public FilterByLocationListRecycleViewAdapter(Context context) {
        this.mData = List.of(context.getResources().getStringArray(R.array.meetingRoomList));
        this.mSelectedData = new ArrayList<String>();
        this.mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_filter_meeting_by_location_cell, parent, false);
        return new FilterByLocationListRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mCheckBox.setText(mData.get(position));
    }

    public List<String> getSelectedRooms() {
        return  mSelectedData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.fragmentFilterMeetingByLocationCellCheckBox);
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        mSelectedData.add(mCheckBox.getText().toString());
                    } else {
                        mSelectedData.remove(mCheckBox.getText().toString());
                    }
                }
            });
        }
    }
}
