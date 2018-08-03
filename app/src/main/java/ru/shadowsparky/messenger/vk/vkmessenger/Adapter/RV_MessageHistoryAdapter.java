package ru.shadowsparky.messenger.vk.vkmessenger.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.JSONClasses.MessageHistory;
import ru.shadowsparky.messenger.vk.vkmessenger.R;

public class RV_MessageHistoryAdapter extends RecyclerView.Adapter<RV_MessageHistoryAdapter.MainViewHolder> {
    private LinkedList<MessageHistory> messageList;
    private PublishSubject<String> endScrollSubject;
    private Context context;
    private int UserID;

    public RV_MessageHistoryAdapter(LinkedList<MessageHistory> messageList, Context context, int UserID, PublishSubject<String> endScrollSubject) {
        this.messageList = messageList;
        this.context = context;
        this.UserID = UserID;
        this.endScrollSubject = endScrollSubject;
    }

    @NonNull
    @Override public RV_MessageHistoryAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_message_in_history, parent, false);
        RV_MessageHistoryAdapter.MainViewHolder mvh = new RV_MessageHistoryAdapter.MainViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (messageList.get(position).getFrom_id() != UserID) {
            holder.userMessageLayout.setVisibility(View.GONE);
            holder.meHistoryTextView.setText(messageList.get(position).getMessage_subject());
        } else {
            holder.meMessageLayout.setVisibility(View.GONE);
            holder.userHistoryTextView.setText(messageList.get(position).getMessage_subject());
        }
        if (position == 0) {
            endScrollSubject.onNext("START");
//            Log.println(Log.DEBUG, "MAIN_TAG", "scrolled on the start");
        }
    }

    @Override public int getItemCount() {
        return messageList.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout meMessageLayout;
        private ConstraintLayout userMessageLayout;
        private TextView meHistoryTextView;
        private TextView userHistoryTextView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            meMessageLayout = itemView.findViewById(R.id.MeHistoryLayout);
            meHistoryTextView = itemView.findViewById(R.id.MeHistoryTextView);
            userMessageLayout = itemView.findViewById(R.id.UserHistoryLayout);
            userHistoryTextView = itemView.findViewById(R.id.UserHistoryTextView);
        }
    }
}
