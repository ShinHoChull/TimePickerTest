package com.bk.timepickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    TimePicker time_picker;
    Resources system;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.time_picker = findViewById(R.id.timepicker);
        this.time_picker.setIs24HourView(false);

        this.time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(getApplicationContext() , "hour = "+hourOfDay + " / min = "+minute,Toast.LENGTH_SHORT).show();
            }
        });


        //this.set_timepicker_text_colour();

    }


    private void set_timepicker_text_colour(){
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) time_picker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) time_picker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) time_picker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        set_numberpicker_text_colour(ampm_numberpicker);
        //하단 컬러 변ㄱㅇ...
        try
        {
            Class<?> clsParent = Class.forName( "com.android.internal.R$id" );
            NumberPicker clsAmPm = (NumberPicker)findViewById( clsParent.getField( "amPm" ).getInt( null ) );
            NumberPicker clsHour = (NumberPicker)findViewById( clsParent.getField( "hour" ).getInt( null ) );
            NumberPicker clsMin = (NumberPicker)findViewById( clsParent.getField( "minute" ).getInt( null ) );
            Class<?> clsNumberPicker = Class.forName( "android.widget.NumberPicker" );
            Field clsSelectionDivider = clsNumberPicker.getDeclaredField( "mSelectionDivider" );

            clsSelectionDivider.setAccessible( true );
            ColorDrawable clsDrawable = new ColorDrawable( 0xFFFFFFFF );


            // 오전/오후, 시, 분 구분 라인의 색상을 변경한다.
            clsSelectionDivider.set( clsAmPm, clsDrawable );
            clsSelectionDivider.set( clsHour, clsDrawable );
            clsSelectionDivider.set( clsMin, clsDrawable );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }

    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color = getResources().getColor(R.color.text);

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
                //((Paint)wheelpaint_field.get(number_picker)).setTextSize(80);
                ((EditText)child).setTextColor(color);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTextCol", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTextColo", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTextColo", e);
            }
        }
    }
}
