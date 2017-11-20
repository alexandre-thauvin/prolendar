package com.prolendar.thauvi_a.prolendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by thauvi_a on 11/13/17.
 *
 * ******************************
 * * BOOM BITCH GET OUT THE WAY *
 * ******************************
 *
 */

public class FormActivity extends Activity implements
        View.OnClickListener {
        private String job;
        private String formule;
        private enum    Step {
            JOB,
            FORMULE
        }
        private Step step;
        private FirebaseDatabase database = FirebaseDatabase.getInstance();
        private final DatabaseReference myRef = database.getReference("job");
        private final List<String> exp = new ArrayList<>();
        private Spinner spinner;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            step = step.JOB;
            setContentView(R.layout.form);
            findViewById(R.id.done).setOnClickListener(this);
            spinner = findViewById(R.id.spinner);
            final ArrayAdapter adapter = new ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    exp
            );
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Please wait...");
            new Thread() {
                public void run() {
                    try{
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                    String name = messageSnapshot.child("").getKey();
                                    exp.add(name);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });
                    }
                    catch (Exception e) {
                        Log.e("FormActivity", e.getMessage());
                    }
                    progressDialog.dismiss();
                }
            }.start();
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

        }

        @Override
        public void onClick(View v)
        {
            if (this.step == step.JOB) {
                job = spinner.getSelectedItem().toString();
            }
            else
                formule = spinner.getSelectedItem().toString();
            updateUI();
        }

        public void updateUI()
        {
            if (this.step == step.JOB) {
                final ArrayAdapter adapter = new ArrayAdapter(
                        this,
                        R.layout.multiline_spinner_dropdown_item,
                        exp
                );
                exp.clear();
                myRef.child(job).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue().toString();
                            value = value.replaceAll("\\{", "").replaceAll("\\}","");
                            String tab[] = value.split(", ");
                            for (String stl : tab) {
                                exp.add(stl);
                            }
                        adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        step = step.FORMULE;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });
                TextView tv = findViewById(R.id.select);
                tv.setText(getString(R.string.formule));
            }
            else
            {
                //todo: new activity/layout
            }
        }
}