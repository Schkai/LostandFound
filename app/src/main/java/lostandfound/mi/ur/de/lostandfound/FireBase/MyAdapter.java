package lostandfound.mi.ur.de.lostandfound.FireBase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lostandfound.mi.ur.de.lostandfound.R;

/**
 * Created by Konstantin on 21.09.2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context c;
    ArrayList<String> LostItem;

    public MyAdapter(Context c, ArrayList<String> lostItem) {
        this.c = c;
        LostItem = lostItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.model, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTxt.setText(LostItem.get(position));
        holder.contentTxt.setText(LostItem.get(position));
        holder.descTxt.setText(LostItem.get(position));
    }

    @Override
    public int getItemCount() {
        return LostItem.size();
    }
}
