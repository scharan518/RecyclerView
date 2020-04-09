package com.charan.recyclerviewtutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends  AppCompatActivity implements RecyclerInterface {

    List<String> movielist;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movielist=new ArrayList<String>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewAdapter=new RecyclerviewAdapter(movielist,this);
        recyclerView.setAdapter(recyclerviewAdapter);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        movielist.add("m1");
        movielist.add("m2");
        movielist.add("m3");
        movielist.add("m4");
        movielist.add("m5");
        movielist.add("m6");
        movielist.add("m7");
        movielist.add("m8");
        movielist.add("m9");
        movielist.add("m10");

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    //Adding search icon


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //newText is the text entered to search
                recyclerviewAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this,movielist.get(position),Toast.LENGTH_SHORT).show();
    }

    String deletedMovie=null;
    List<String> archivedMovies=new ArrayList<>();
    //Swipe from l-r and r-l
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN
            |ItemTouchHelper.START|ItemTouchHelper.END,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        //when we want to rearrange rows...
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();
            Collections.swap(movielist,fromPosition,toPosition);
            recyclerviewAdapter.notifyItemMoved(fromPosition,toPosition);
            return false;
        }

        //for swipe features...
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position=viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedMovie=movielist.get(position);
                    movielist.remove(position);
                    recyclerviewAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView,deletedMovie, BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    movielist.add(position,deletedMovie);
                                    recyclerviewAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    final String movieName=movielist.get(position);
                    archivedMovies.add(movieName);
                    //delete row
                    movielist.remove(position);
                    recyclerviewAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView,movieName+", archived", BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    archivedMovies.remove(archivedMovies.lastIndexOf(movieName));
                                    movielist.add(position,movieName);
                                    recyclerviewAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }

        //github dependency...for swipe decoration
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary))
                    .addSwipeRightActionIcon(R.drawable.ic_archive_black_24dp)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}
