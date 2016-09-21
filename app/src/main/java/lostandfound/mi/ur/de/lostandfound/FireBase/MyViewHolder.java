package lostandfound.mi.ur.de.lostandfound.FireBase;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import lostandfound.mi.ur.de.lostandfound.R;

/**
 * Created by Konstantin on 21.09.2016.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView nameTxt, descTxt, contentTxt;

    public MyViewHolder(View itemView) {
        super(itemView);

        nameTxt=(TextView) itemView.findViewById(R.id.nameTxt);
        descTxt=(TextView) itemView.findViewById(R.id.descTxt);
        contentTxt=(TextView) itemView.findViewById(R.id.contentTxt);

    }

}
