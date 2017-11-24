package com.prolendar.thauvi_a.prolendar;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by thauvi_a on 11/23/17.
 * <p>
 * ******************************
 * * BOOM BITCH GET OUT THE WAY *
 * ******************************
 */

public class AppointmentActivity extends Activity {

    private String beginTime;
    private String endTime;

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment);
    }

}
