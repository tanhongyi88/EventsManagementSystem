package com.fit2081.assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.assignment1.provider.Event;

import java.util.ArrayList;

public class RecyclerAdapterEvent extends RecyclerView.Adapter<RecyclerAdapterEvent.EventItemViewHolder> {

    ArrayList<Event> eventList = new ArrayList<>();

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event_item, parent, false); //CardView inflated as RecyclerView list item
        EventItemViewHolder viewHolder = new EventItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemViewHolder holder, int position) {
        holder.idView.setText(eventList.get(position).getEventId());
        holder.nameView.setText(eventList.get(position).getName());
        holder.categoryIdView.setText(eventList.get(position).getCategoryId());
        holder.ticketsView.setText(String.valueOf(eventList.get(position).getTicketsAvailable()));
        holder.isActive.setText(eventList.get(position).isActive() ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventItemViewHolder extends RecyclerView.ViewHolder {
        public TextView idView;
        public TextView nameView;
        public TextView categoryIdView;
        public TextView ticketsView;
        public TextView isActive;

        public EventItemViewHolder(@NonNull View itemView) {
            super(itemView);
            idView = itemView.findViewById(R.id.event_item_id_view);
            nameView = itemView.findViewById(R.id.event_item_name_view);
            categoryIdView = itemView.findViewById(R.id.event_item_category_id_view);
            ticketsView = itemView.findViewById(R.id.event_item_tickets_view);
            isActive = itemView.findViewById(R.id.event_item_is_active_view);
        }
    }
}
