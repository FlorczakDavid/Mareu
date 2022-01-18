
package com.florczakdavid.maru.ui;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.florczakdavid.maru.DI.DI;
        import com.florczakdavid.maru.DI.MeetingApiService;
        import com.florczakdavid.maru.R;
        import com.florczakdavid.maru.model.Meeting;
        import org.greenrobot.eventbus.EventBus;

        import com.florczakdavid.maru.DI.DeleteMeetingEvent;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;

public class MeetingListRecycleViewAdapter extends RecyclerView.Adapter<MeetingListRecycleViewAdapter.ViewHolder> {

    private List<Meeting> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private MeetingApiService mApiService;

    // data is passed into the constructor
    MeetingListRecycleViewAdapter(Context context, List<Meeting> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mApiService = DI.getMeetingApiService();
        View view = mInflater.inflate(R.layout.meeting_list_cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meeting meeting = mData.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
        SimpleDateFormat endFormat = new SimpleDateFormat("HH:mm");
        long endDate = meeting.getTime().getTime();
        endDate += 45 * 60 * 1000;
        Date end = new Date(endDate);
        String displayedDate = simpleDateFormat.format(meeting.getTime().getTime()) + " - " + endFormat.format(end);

        holder.titleTextView.setText(meeting.getLocation()+" "+displayedDate+" "+meeting.getTopic());
        String emails = "";
        for (int i=0; i<meeting.getParticipants().length; i++) {
            emails += meeting.getParticipants()[i].getMail() + ", ";
        }
        holder.subtitleTextView.setText(emails);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView myTextView;
        TextView titleTextView;
        TextView subtitleTextView;
        ImageButton deleteButton;


        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.meetingListRecycleViewCellTittleTextView);
            subtitleTextView = itemView.findViewById(R.id.meetingListRecycleViewCellSubtittleTextView);
            deleteButton = itemView.findViewById(R.id.meetingListRecycleViewCellDeleteImageButton);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Meeting getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
