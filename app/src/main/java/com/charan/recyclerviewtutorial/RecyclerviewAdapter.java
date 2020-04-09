package com.charan.recyclerviewtutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> implements Filterable {
    @NonNull
    //contains filtered movie list
    List<String> movielist;
    //contains all the list of movies
    List<String> movielistall;
    private RecyclerInterface recyclerInterface;
    public RecyclerviewAdapter(@NonNull List<String> movielist,RecyclerInterface recyclerInterface) {
        this.movielist = movielist;
        movielistall=new ArrayList<>();
        movielistall.addAll(movielist);
        this.recyclerInterface=recyclerInterface;
    }

    //inflates the layout for recyclerview
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.items_layout,parent,false);
        return new ViewHolder(view);
    }

    //binds the views with data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(movielist.get(position));
        holder.textView2.setText(String.valueOf(position));
    }

    //returns position of the row item
    @Override
    public int getItemCount() {
        return movielist.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        //used to filter the list
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filterList=new ArrayList<>();
            if (constraint==null|| constraint.length()==0){
                filterList.addAll(movielistall);
            }else {
                for (String movie:movielistall){
                    if (movie.toLowerCase().toString().contains(constraint.toString().toLowerCase())){
                        filterList.add(movie);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movielist.clear();
            movielist.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.imageView);
            textView=itemView.findViewById(R.id.textView);
            textView2=itemView.findViewById(R.id.rowCountTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerInterface.onItemClick(getAdapterPosition());
                }
            });
        }

    }
}
