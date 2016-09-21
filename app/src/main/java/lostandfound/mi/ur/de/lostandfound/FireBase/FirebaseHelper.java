package lostandfound.mi.ur.de.lostandfound.FireBase;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import lostandfound.mi.ur.de.lostandfound.LostItem;

/**
 * Created by Konstantin on 21.09.2016.
 */
public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved = null;
    ArrayList<String> LostItem = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //SAVE
    public boolean save(lostandfound.mi.ur.de.lostandfound.LostItem lostItem) {
        if (lostItem == null) {
            saved = false;
        } else {
            try {
                db.child("Lostitem").push().setValue(lostItem);
                saved = true;
            } catch (DatabaseException e) {
                saved = false;
            }
        }
        return saved;
    }

    //READ
    public ArrayList<String> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return LostItem;

    }

    private void fetchData(DataSnapshot dataSnapshot){
        LostItem.clear();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name=ds.getValue(LostItem.class).getName();
            LostItem.add(name);
        }
    }



}

