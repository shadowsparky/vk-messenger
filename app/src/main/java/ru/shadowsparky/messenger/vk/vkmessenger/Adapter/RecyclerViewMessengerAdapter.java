package ru.shadowsparky.messenger.vk.vkmessenger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.subjects.PublishSubject;
import ru.shadowsparky.messenger.vk.vkmessenger.R;
import ru.shadowsparky.messenger.vk.vkmessenger.Utils.MergedObject;

public class RecyclerViewMessengerAdapter extends RecyclerView.Adapter<RecyclerViewMessengerAdapter.MainViewHolder>{
    private MergedObject data;
    private Context context;
    private PublishSubject<Integer> subject;

    public RecyclerViewMessengerAdapter (MergedObject data, Context context, PublishSubject<Integer> subject) {
        this.data = data;
        this.context = context;
        this.subject = subject;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_layout, parent, false);
        MainViewHolder mvh = new MainViewHolder(v);
        return mvh;
    }

    @Override public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (data.getUser().get(position).getPhotoUrl() != null)
            Picasso.get().load(data.getUser().get(position).getPhotoUrl()).into(holder._image);
        holder._title.setText(data.getUser().get(position).getFirst_name() + " " + data.getUser().get(position).getLast_name());
        holder._url.setText(data.getMessages().get(position).getBody());
        holder._card.setOnClickListener((view)-> subject.onNext(position));
    }

    @Override public int getItemCount() {
        return data.getMessages().size();
    }
    public MergedObject getData(){
        return data;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private ImageView _image;
        private TextView _title;
        private TextView _url;
        private CardView _card;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            _image = itemView.findViewById(R.id.Image_IV);
            _title = itemView.findViewById(R.id.TitleTV);
            _url = itemView.findViewById(R.id.UrlTV);
            _card = itemView.findViewById(R.id.Card_CV);
//            ButterKnife.bind(this, itemView);
        }
    }
}