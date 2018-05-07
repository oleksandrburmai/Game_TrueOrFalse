package com.alex.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView factV;
    private DatabaseReference mDatabase;
    private ArrayList<Fact> factsL = new ArrayList<>();
    private int result = 0;
    private int idFact = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        put();


        factV = findViewById(R.id.factView);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Fact>> t = new GenericTypeIndicator<>();
                factsL = dataSnapshot.child("Facts").getValue(t);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        findViewById(R.id.btn_true).setOnClickListener(this);
        findViewById(R.id.btn_false).setOnClickListener(this);
        factV.setText(factsL.get(0).getFact());

    }


    private void factReview(boolean isTrue, int factId) {
        if (factsL.get(factId).getIsTrue() == isTrue) {
            result++;
        }
        if (factId <= factsL.size()) {
            factV.setText(factsL.get(factId + 1).getFact());
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_true) {
            factReview(true, idFact);
            if (idFact < factsL.size() - 1) {
                idFact++;
            }
        } else if (v.getId() == R.id.btn_false) {
            factReview(false, idFact);
            if (idFact < factsL.size() - 1) {
                idFact++;
            }
        }
    }

    private void put() {
        Fact fact1 = new Fact("Людина має шітсть відчутів", false);
        Fact fact2 = new Fact("Кінь має чотири ноги", true);
        Fact fact3 = new Fact("Людина може прожити без їжі до 45 днів", true);
        Fact fact4 = new Fact("Людина може прожити без води до 10 днів", true);
        Fact fact5 = new Fact("Велику китайську стіну видно з космосу", false);
        factsL.add(fact1);
        factsL.add(fact2);
        factsL.add(fact3);
        factsL.add(fact4);
        factsL.add(fact5);

    }
}
