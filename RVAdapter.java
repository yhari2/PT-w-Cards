package org.ubicomplab.cardviewtest;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by YasasviHari on 6/21/17.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder>{

    public List myDataSet = new List();

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        // this entire class serves as a holder for the desired view to reduce the calls
        // and thus the expense to the findViewById method
        CardView cv;
        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
        }
    }

    ////////////////////////// RV ADAPTER FROM HERE DOWN///////////////////////////////////////////

    RVAdapter(List mds){
        this.myDataSet=mds;
    } // constructor

    @Override // first of 3 abstract methods to be overwriten
    public int getItemCount() {
        return myDataSet.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int row) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        return new CardViewHolder(v);
        // this is designed to reduce the expense of searching the View

    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        // need to fill this with the relevant card properties
        // basically allows us to alter the attributes of the cards themselves in the holder
        // (to my understanding)
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // why override this at all?
    }

}