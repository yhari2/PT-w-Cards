package org.ubicomplab.cardviewtest;



import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true); // scrollbar that displays the relevant cards to at that position

        LinearLayoutManager llm = new LinearLayoutManager(this);
        // why do we use this as the parameter?
        rv.setLayoutManager(llm); // this can manage the arrangements of the cards on the screen
        // it also handles all of the aesthetic stuff (to my understanding)
        // it makes the RecyclerView look like a ListView

        ArrayList cards = new ArrayList(CardView);

        RVAdapter adapter = new RVAdapter(cards);
        rv.setAdapter(adapter); //This is the adapter that communicates with the dataset
        // see the RVAdapter.java
    }
}
