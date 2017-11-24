package com.prolendar.thauvi_a.prolendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private String artisan;
    private Context context = this;
    private static final Utils utils = new Utils();
    public String getJob() {
        return job;
    }

    public String getFormule() {
        return formule;
    }

    public String getArtisan() {
        return artisan;
    }

    public Context getContext() {
        return context;
    }

    private enum Step {
        JOB,
        FORMULE,
        ARTISAN
    }

    private Step step;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("job");
    private final List<String> exp = new ArrayList<>();
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        step = Step.JOB;
        setContentView(R.layout.form);
        findViewById(R.id.finish).setVisibility(View.GONE);
        findViewById(R.id.done).setOnClickListener(this);
        spinner = findViewById(R.id.spinner);

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Please wait...");
        new Thread() {
            public void run() {
                try {
                    setJob();
                } catch (Exception e) {
                    Log.e("FormActivity", e.getMessage());
                }
                progressDialog.dismiss();
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        if (this.step == Step.JOB) {
            job = spinner.getSelectedItem().toString();
            setFormule();
        } else if (this.step == Step.FORMULE) {
            formule = spinner.getSelectedItem().toString();
            setArtisan();
        }
    }

    public void setJob() {
        final ArrayAdapter adapter = new ArrayAdapter(
                this,
                R.layout.multiline_spinner_dropdown_item,
                exp
        );
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
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
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setFormule() {
        this.step = Step.FORMULE;
        final ArrayAdapter adapter = new ArrayAdapter(
                this,
                R.layout.multiline_spinner_dropdown_item,
                exp
        );
        exp.clear();
        myRef.child(job).child("formule").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                value = value.replaceAll("\\{", "").replaceAll("\\}", "");
                String tab[] = value.split(", ");
                for (String stl : tab) {
                    exp.add(stl);
                }
                adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        TextView tv = findViewById(R.id.select);
        tv.setText(getString(R.string.formule));
    }

    public void setArtisan() {
        this.step = Step.ARTISAN;
        final ArrayAdapter adapter = new ArrayAdapter(
                this,
                R.layout.multiline_spinner_dropdown_item,
                exp
        );
        exp.clear();
        myRef.child(job).child("artisan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String name = messageSnapshot.child("").child("name").getValue().toString();
                    exp.add(name);
                }
                adapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                step = Step.FORMULE;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
        TextView tv = findViewById(R.id.select);
        tv.setText(getString(R.string.artisan));
        findViewById(R.id.finish).setVisibility(View.VISIBLE);
        findViewById(R.id.done).setVisibility(View.GONE);
    }

    public void toAppointment(View view)
    {
        artisan = spinner.getSelectedItem().toString();
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Selection")
                .setMessage(getString(R.string.resum_selection, this.job, this.formule, this.artisan))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(FormActivity.this, AppointmentActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dial;
        dial = builder.create();
        dial.show();
    }
}