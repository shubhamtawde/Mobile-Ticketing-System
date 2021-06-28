package com.shubham.mts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {

    public List<Bookings> BookingsList;

    public BookingAdapter(List<Bookings> BookingsList) {
        this.BookingsList = BookingsList;
    }


    @NonNull
    @Override
    public BookingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new BookingHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHolder bookingsHolder, int i) {
        bookingsHolder.name.setText(BookingsList.get(i).getNum());
        bookingsHolder.email.setText(BookingsList.get(i).getFare());
        bookingsHolder.mobile.setText(BookingsList.get(i).getID());
        bookingsHolder.route.setText(BookingsList.get(i).getRoute());
    }

    @Override
    public int getItemCount() {
        return BookingsList.size();
    }

    public class BookingHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView mobile;
        TextView route;

        public BookingHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title_name);
            email = itemView.findViewById(R.id.title_email);
            mobile = itemView.findViewById(R.id.title_mobile);
            route = itemView.findViewById(R.id.route);
        }

    }

}
