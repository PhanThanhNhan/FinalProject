package com.example.youneverknow.finalproject.AutoDetect.Adapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


import com.example.youneverknow.finalproject.AutoDetect.AutoDetectActivity;
import com.example.youneverknow.finalproject.AutoDetect.ContactActivity;
import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor5days;
import com.example.youneverknow.finalproject.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class SuperAwesomeCardFragment extends Fragment{

    private static final String ARG_POSITION = "position";

    private TextView tvAutoDetectTodayTemperature, tvAutoDetectTodayDescription, tvAutoDetectTodayPressure,
            tvAutoDetectTodayHumidity, tvAutoDetectTodayWind, tvAutoDetectTodayCityName, tvAutoDetectTodaySunrise, tvAutoDetectTodaySunset;
    private ImageView ivAutoDetectTodayWeatherIcon;
    private ImageButton ibtnAutoDetectSMS;

    private int position;

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        if(position == 0){
            rootView = inflater.inflate(R.layout.fragment_card_today,container,false);

            tvAutoDetectTodayCityName = (TextView) rootView.findViewById(R.id.tvAutoDetectTodayCityname);
            tvAutoDetectTodayTemperature = (TextView) rootView.findViewById(R.id.tvAutoDetectTodayTemperature);
            tvAutoDetectTodayDescription = (TextView) rootView.findViewById(R.id.tvAutoDetectTodayWeatherDescription);
            tvAutoDetectTodayPressure = (TextView) rootView.findViewById(R.id.tvAutoDetectTodayPressure);
            tvAutoDetectTodayHumidity = (TextView) rootView.findViewById(R.id.tvAutoDetectTodayHumidity);
            tvAutoDetectTodayWind = (TextView) rootView.findViewById(R.id.tvAutoDetectTodayWind);
            tvAutoDetectTodaySunrise = (TextView) rootView.findViewById(R.id.tvAutoDetectTodaySunrise);
            tvAutoDetectTodaySunset = (TextView) rootView.findViewById(R.id.tvAutoDetectTodaySunset);
            ivAutoDetectTodayWeatherIcon = (ImageView) rootView.findViewById(R.id.ivAutoDetectTodayWeatherIcon);

            ibtnAutoDetectSMS = (ImageButton) rootView.findViewById(R.id.ibtnAutoDetectShareSMS);
            ibtnAutoDetectSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), ContactActivity.class);
                    startActivity(i);
                }
            });

            tvAutoDetectTodayCityName.setText(dataFor10days.cityName);
            tvAutoDetectTodayTemperature.setText(round2decimal(String.valueOf(dataFor10days.data[0].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetectTodayDescription.setText(dataFor10days.data[0].description);
            tvAutoDetectTodayPressure.setText(getString(R.string.pressure) + dataFor10days.data[0].pressure + " hPa");
            tvAutoDetectTodayHumidity.setText(getString(R.string.humidity) + dataFor10days.data[0].humidity + "%");
            tvAutoDetectTodayWind.setText(getString(R.string.wind) + dataFor10days.data[0].wind + " m/s");
            tvAutoDetectTodaySunrise.setText(getString(R.string.sunRise) + dataFor10days.sunRise);
            tvAutoDetectTodaySunset.setText(getString(R.string.sunSet) + dataFor10days.sunSet);
            ivAutoDetectTodayWeatherIcon.setImageResource(getIcon(dataFor10days.data[0].description));

        }
        else if (position == 1){
            rootView = inflater.inflate(R.layout.fragment_card_5days, container, false);
            _5days = new String[]{getString(R.string.today), "+1", "+2", "+3", "+4"};

            _5days_chartTop = (LineChartView) rootView.findViewById(R.id._5days_chart_top);
            // Generate and set data for line chart
            generateInitialLineData();
            // *** BOTTOM COLUMN CHART ***
            _5days_chartBottom = (ColumnChartView) rootView.findViewById(R.id._5days_chart_bottom);
            generateColumnData();

            tvAutoDetect5daysTemperature = (TextView) rootView.findViewById(R.id.tvAutoDetect5daysTemperature);
            tvAutoDetect5daysDescription = (TextView) rootView.findViewById(R.id.tvAutoDetect5daysDescription);
            tvAutoDetect5daysPressure = (TextView) rootView.findViewById(R.id.tvAutoDetect5daysPressure);
            tvAutoDetect5daysHumidity = (TextView) rootView.findViewById(R.id.tvAutoDetect5daysHumidity);
            tvAutoDetect5daysWind = (TextView) rootView.findViewById(R.id.tvAutoDetect5daysWind);
            tvAutoDetect5daysTime = (TextView) rootView.findViewById(R.id.tvAutoDetect5daysTime);
            ivAutoDetect5daysWeatherIcon = (ImageView) rootView.findViewById(R.id.ivAutoDetect5daysWeatherIcon);
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_card_10days, container, false);

            _16days_chartTop = (LineChartView) rootView.findViewById(R.id._16days_chart_top);
            _16days_chartBottom = (ColumnChartView) rootView.findViewById(R.id._16days_chart_bottom);
            generateInitialLineData_16();
            generateLineData_16(ChartUtils.COLOR_RED, 100);
            generateColumnData_16();

            tvAutoDetect10daysTemperature = (TextView) rootView.findViewById(R.id.tvAutoDetect16daysTemperature);
            tvAutoDetect10daysDescription = (TextView) rootView.findViewById(R.id.tvAutoDetect16daysDescription);
            tvAutoDetect10daysPressure = (TextView) rootView.findViewById(R.id.tvAutoDetect16daysPressure);
            tvAutoDetect10daysHumidity = (TextView) rootView.findViewById(R.id.tvAutoDetect16daysHumidity);
            tvAutoDetect10daysWind = (TextView) rootView.findViewById(R.id.tvAutoDetect16daysWind);
            tvAutoDetect10daysTime = (TextView) rootView.findViewById(R.id.tvAutoDetect16daysTime);
            ivAutoDetect10daysWeatherIcon = (ImageView) rootView.findViewById(R.id.ivAutoDetect16daysWeatherIcon);

            tvAutoDetect10daysTemperature.setText("");
            tvAutoDetect10daysDescription.setText("");
            tvAutoDetect10daysPressure.setText(getString(R.string.tapColumnToView));
            tvAutoDetect10daysHumidity.setText("");
            tvAutoDetect10daysWind.setText("");
            tvAutoDetect10daysTime.setText("");

        }
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    //                                Data For 5 days forecast                           //
    ///////////////////////////////////////////////////////////////////////////////////////

    public String[] _5days;
    public String[] _5times = new String[]{"0h", "3h", "6h", "9h", "12h", "15h", "18h", "21h"};

    private LineChartView _5days_chartTop;
    private ColumnChartView _5days_chartBottom;

    private LineChartData _5days_lineData;
    private ColumnChartData _5days_columnData;

    private TextView tvAutoDetect5daysTemperature, tvAutoDetect5daysDescription, tvAutoDetect5daysPressure,
            tvAutoDetect5daysHumidity, tvAutoDetect5daysWind, tvAutoDetect5daysTime;
    private ImageView ivAutoDetect5daysWeatherIcon;

    private void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = _5days.length;

        /* Find average value of rains */
        int aveRain = 0;
        int sumRain = 0;
        int numRainDiff0 = 0;
        for (int i = 0; i < numColumns; i++){
            if(dataFor10days.data[i].rain == 0){
                ++numRainDiff0;
                continue;
            }
            sumRain += dataFor10days.data[i].rain;
        }
        if(numRainDiff0 < numColumns){
            aveRain = sumRain/(numColumns - numRainDiff0);
            if(aveRain < 1)
                aveRain = 1;
        } else aveRain = 5;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                //values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.COLOR_BLUE));
                if(dataFor10days.data[i].rain == 0)
                    values.add(new SubcolumnValue((float) aveRain, Color.GRAY));
                else values.add(new SubcolumnValue((float) dataFor10days.data[i].rain, ChartUtils.COLOR_BLUE));
            }

            axisValues.add(new AxisValue(i).setLabel(_5days[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        /* Setup chart */
        _5days_columnData = new ColumnChartData(columns);
        _5days_columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        _5days_columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        _5days_chartBottom.setColumnChartData(_5days_columnData);
        // Set value touch listener that will trigger changes for chartTop.
        _5days_chartBottom.setOnValueTouchListener(new ValueTouchListener());
        // Set selection mode to keep selected month column highlighted.
        _5days_chartBottom.setValueSelectionEnabled(true);
        _5days_chartBottom.setZoomType(ZoomType.HORIZONTAL);
        _5days_chartBottom.setZoomEnabled(false);


    }

    /**
     * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
     * will select value on column chart.
     */
    private void generateInitialLineData() {
        int numValues = 8;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(_5times[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_RED).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        _5days_lineData = new LineChartData(lines);
        _5days_lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        _5days_lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        _5days_chartTop.setLineChartData(_5days_lineData);

        // For build-up animation you have to disable viewport recalculation.
        _5days_chartTop.setViewportCalculationEnabled(true);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 7, 0);
        _5days_chartTop.setMaximumViewport(v);
        _5days_chartTop.setCurrentViewport(v);

        _5days_chartTop.setZoomType(ZoomType.HORIZONTAL);
        _5days_chartTop.setZoomEnabled(false);
        _5days_chartTop.setOnValueTouchListener(new ValueTouchListener_line());
    }

    private void generateLineData(int columnIndex, int color, float range) {
        // Cancel last animation if not finished.
        _5days_chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = _5days_lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        int iPos = 0;
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) dataFor5days.data[columnIndex].time[iPos].temperature - 273);
            ++iPos;
        }

        // Start new data animation with 300ms duration;
        _5days_chartTop.startDataAnimation(300);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            //generateLineData(value.getColor(), 100);
            generateLineData(columnIndex, ChartUtils.COLOR_RED, 100);
            tvAutoDetect5daysTemperature.setText(round2decimal(String.valueOf(dataFor10days.data[columnIndex].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetect5daysDescription.setText(dataFor10days.data[columnIndex].description);
            tvAutoDetect5daysPressure.setText(getString(R.string.pressure) + dataFor10days.data[columnIndex].pressure + " hPa");
            tvAutoDetect5daysHumidity.setText(getString(R.string.humidity) + dataFor10days.data[columnIndex].humidity + "%");
            tvAutoDetect5daysWind.setText(getString(R.string.wind) + dataFor10days.data[columnIndex].wind + " m/s");
            ivAutoDetect5daysWeatherIcon.setImageResource(getIcon(dataFor10days.data[columnIndex].description));
            if(columnIndex == 0)
                tvAutoDetect5daysTime.setText(getString(R.string.today));
            else tvAutoDetect5daysTime.setText(getString(R.string.day) + columnIndex);

            isColumnSelected = true;
            columnSelectedIndex = columnIndex;
        }

        @Override
        public void onValueDeselected() {

        }
    }
    private boolean isColumnSelected = false;
    int columnSelectedIndex = -1;

    private class ValueTouchListener_line implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int i, int i1, PointValue pointValue) {
            if(!isColumnSelected)
                return;
            String tempTime = "";
            switch (i1){
                case 0:
                    tempTime = "0h";
                    break;
                case 1:
                    tempTime = "3h";
                    break;
                case 2:
                    tempTime = "6h";
                    break;
                case 3:
                    tempTime = "9h";
                    break;
                case 4:
                    tempTime = "12h";
                    break;
                case 5:
                    tempTime = "15h";
                    break;
                case 6:
                    tempTime = "18h";
                    break;
                case 7:
                    tempTime = "21h";
                    break;
            }
            tvAutoDetect5daysTemperature.setText(round2decimal(String.valueOf(dataFor5days.data[columnSelectedIndex].time[i1].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetect5daysDescription.setText(dataFor5days.data[columnSelectedIndex].time[i1].description);
            tvAutoDetect5daysPressure.setText(getString(R.string.pressure) + dataFor5days.data[columnSelectedIndex].time[i1].pressure + " hPa");
            tvAutoDetect5daysHumidity.setText(getString(R.string.humidity) + dataFor5days.data[columnSelectedIndex].time[i1].humidity + "%");
            tvAutoDetect5daysWind.setText(getString(R.string.wind) + dataFor5days.data[columnSelectedIndex].time[i1].wind + " m/s");
            ivAutoDetect5daysWeatherIcon.setImageResource(getIcon(dataFor5days.data[columnSelectedIndex].time[i1].description));
            if(columnSelectedIndex == 0)
                tvAutoDetect5daysTime.setText(getString(R.string.today) + ": " + tempTime);
            else tvAutoDetect5daysTime.setText(getString(R.string.day) + columnSelectedIndex + ": " + tempTime);
        }

        @Override
        public void onValueDeselected() {

        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    //                                Data For 10 days forecast                           //
    ///////////////////////////////////////////////////////////////////////////////////////

    public String[] _16days_rain = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public String[] _16days_temperature = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private LineChartView _16days_chartTop;
    private ColumnChartView _16days_chartBottom;

    private LineChartData _16days_lineData;
    private ColumnChartData _16days_columnData;

    private TextView tvAutoDetect10daysTemperature, tvAutoDetect10daysDescription, tvAutoDetect10daysPressure,
            tvAutoDetect10daysHumidity, tvAutoDetect10daysWind, tvAutoDetect10daysTime;
    private ImageView ivAutoDetect10daysWeatherIcon;

    private void generateColumnData_16() {

        int numSubcolumns = 1;
        int numColumns = _16days_rain.length;

        /* Find average value of rains */
        int aveRain = 0;
        int sumRain = 0;
        int numRainDiff0 = 0;
        for (int i = 0; i < numColumns; i++){
            if(dataFor10days.data[i].rain == 0){
                ++numRainDiff0;
                continue;
            }
            sumRain += dataFor10days.data[i].rain;
        }
        if(numRainDiff0 < numColumns){
            aveRain = sumRain/(numColumns - numRainDiff0);
            if(aveRain < 1)
                aveRain = 1;
        } else aveRain = 5;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                //values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.COLOR_BLUE));
                if (dataFor10days.data[i].rain == 0)
                    values.add(new SubcolumnValue((float) aveRain, Color.GRAY));
                else
                    values.add(new SubcolumnValue((float) dataFor10days.data[i].rain, ChartUtils.COLOR_BLUE));
            }

            axisValues.add(new AxisValue(i).setLabel(_16days_rain[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }

        /* Setup chart */
        _16days_columnData = new ColumnChartData(columns);
        _16days_columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        _16days_columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));
        _16days_chartBottom.setColumnChartData(_16days_columnData);
        // Set value touch listener that will trigger changes for chartTop.
        _16days_chartBottom.setOnValueTouchListener(new ValueTouchListener_16());
        // Set selection mode to keep selected month column highlighted.
        _16days_chartBottom.setValueSelectionEnabled(true);
        _16days_chartBottom.setZoomType(ZoomType.HORIZONTAL);
        _16days_chartBottom.setZoomEnabled(false);

    }

    /**
     * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
     * will select value on column chart.
     */
    private void generateInitialLineData_16() {
        int numValues = 10;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(_16days_temperature[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_RED).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);


        _16days_lineData = new LineChartData(lines);
        _16days_lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        _16days_lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        _16days_chartTop.setLineChartData(_16days_lineData);

        // For build-up animation you have to disable viewport recalculation.
        _16days_chartTop.setViewportCalculationEnabled(true);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 9, 0);
        _16days_chartTop.setMaximumViewport(v);
        _16days_chartTop.setCurrentViewport(v);

        _16days_chartTop.setZoomType(ZoomType.HORIZONTAL);
        _16days_chartTop.setZoomEnabled(false);
        _16days_chartTop.setOnValueTouchListener(new ValueTouchListener_line_16());
    }

    private void generateLineData_16(int color, float range) {
        // Cancel last animation if not finished.
        _16days_chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = _16days_lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        int iPos = 0;
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            //value.setTarget(value.getX(), (float) Math.random() * range);
            value.setTarget(value.getX(), (float) dataFor10days.data[iPos].temperature - 273);
            ++iPos;
        }

        // Start new data animation with 300ms duration;
        _16days_chartTop.startDataAnimation(300);
    }

    private class ValueTouchListener_16 implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {

            tvAutoDetect10daysTemperature.setText(round2decimal(String.valueOf(dataFor10days.data[columnIndex].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetect10daysDescription.setText(dataFor10days.data[columnIndex].description);
            tvAutoDetect10daysPressure.setText(getString(R.string.pressure) + dataFor10days.data[columnIndex].pressure + " hPa");
            tvAutoDetect10daysHumidity.setText(getString(R.string.humidity) + dataFor10days.data[columnIndex].humidity + "%");
            tvAutoDetect10daysWind.setText(getString(R.string.wind) + dataFor10days.data[columnIndex].wind + " m/s");
            ivAutoDetect10daysWeatherIcon.setImageResource(getIcon(dataFor10days.data[columnIndex].description));
            if(columnIndex == 0)
                tvAutoDetect10daysTime.setText(getString(R.string.today));
            else tvAutoDetect10daysTime.setText(getString(R.string.day) + columnIndex);
        }

        @Override
        public void onValueDeselected() {
        }
    }

    private class ValueTouchListener_line_16 implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int i, int i1, PointValue pointValue) {
            tvAutoDetect10daysTemperature.setText(round2decimal(String.valueOf(dataFor10days.data[i1].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetect10daysDescription.setText(dataFor10days.data[i1].description);
            tvAutoDetect10daysPressure.setText(getString(R.string.pressure) + dataFor10days.data[i1].pressure + " hPa");
            tvAutoDetect10daysHumidity.setText(getString(R.string.humidity) + dataFor10days.data[i1].humidity + "%");
            tvAutoDetect10daysWind.setText(getString(R.string.wind) + dataFor10days.data[i1].wind + " m/s");
            ivAutoDetect10daysWeatherIcon.setImageResource(getIcon(dataFor10days.data[i1].description));
            if(i1 == 0)
                tvAutoDetect10daysTime.setText(getString(R.string.today));
            else tvAutoDetect10daysTime.setText(getString(R.string.day) + i1);
        }

        @Override
        public void onValueDeselected() {

        }
    }


    String round2decimal(String number){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number.length(); i++){
            if(number.charAt(i) == '.'){
                stringBuilder.append(number.charAt(i));
                stringBuilder.append(number.charAt(i + 1));
                if(i + 2 >= number.length())
                    stringBuilder.append("0");
                else
                    stringBuilder.append(number.charAt(i + 2));

                break;
            }
            stringBuilder.append(number.charAt(i));
        }
        return stringBuilder.toString();
    }

    int getIcon(String description){
        if(description.equals(getString(R.string.thunderStormWithLightRain)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithRain)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithHeavyRain)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.lightThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.heavyThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.raggedThunderStorm)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithLightDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithHeavyDrizzle)))
            return R.drawable.tstorm3;
        else if(description.equals(getString(R.string.thunderStormWithRain)))
            return R.drawable.tstorm3;

        else if(description.equals(getString(R.string.lightIntensityDrizzle)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.drizzle)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.heavyIntensityDrizzle)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.lightIntensityDrizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.drizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.heavyIntensityDrizzleRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.showerRainAndDrizzle)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.heavyShowerRainAndDrizzle)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.showerDrizzle)))
            return R.drawable.shower3;

        else if(description.equals(getString(R.string.lightRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.moderateRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.heavyIntensityRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.veryHeavyRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.extremeRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.freezingRain)))
            return R.drawable.hail;
        else if(description.equals(getString(R.string.lightIntensityShowerRain)))
            return R.drawable.light_rain;
        else if(description.equals(getString(R.string.showerRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.heavyIntensityShowerRain)))
            return R.drawable.shower3;
        else if(description.equals(getString(R.string.raggedShowerRain)))
            return R.drawable.shower3;

        else if(description.equals(getString(R.string.lightSnow)))
            return R.drawable.snow4;
        else if(description.equals(getString(R.string.snow)))
            return R.drawable.snow4;
        else if(description.equals(getString(R.string.heavySnow)))
            return R.drawable.snow5;
        else if(description.equals(getString(R.string.sleet)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.showerSleet)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.lightRainAndSnow)))
            return R.drawable.snow4;
        else if(description.equals(getString(R.string.rainAndSnow)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.lightShowerSnow)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.showerSnow)))
            return R.drawable.sleet;
        else if(description.equals(getString(R.string.heavyShowerSnow)))
            return R.drawable.sleet;

        else if(description.equals(getString(R.string.mist)))
            return R.drawable.mist;
        else if(description.equals(getString(R.string.smoke)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.haze)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.sandDustWhirls)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.fog)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.sand)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.dust)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.volcanicAsh)))
            return R.drawable.fog;
        else if(description.equals(getString(R.string.squalls)))
            return R.drawable.squalls;
        else if(description.equals(getString(R.string.tornado)))
            return R.drawable.tornado;

        else if(description.equals(getString(R.string.clearSky)))
            return R.drawable.sunny;
        else if(description.equals(getString(R.string.fewClouds)))
            return R.drawable.cloudy1;
        else if(description.equals(getString(R.string.scatteredClouds)))
            return R.drawable.cloudy2;
        else if(description.equals(getString(R.string.brokenClouds)))
            return R.drawable.cloudy5;
        else if(description.equals(getString(R.string.overcastClouds)))
            return R.drawable.overcast;
        else if(description.equals(getString(R.string.skyIsClear)))
            return R.drawable.sunny;
        else
            return R.drawable.dunno;
    }
}