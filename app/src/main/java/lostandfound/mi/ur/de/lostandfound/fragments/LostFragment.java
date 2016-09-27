package lostandfound.mi.ur.de.lostandfound.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lostandfound.mi.ur.de.lostandfound.R;

/**
 * Created by Konstantin on 27.09.2016.
 */

public class LostFragment extends Fragment {

    public LostFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false);
    }

}
