package com.example.youneverknow.finalproject.AutoDetect.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


import com.example.youneverknow.finalproject.DataClass.dataFor10days;
import com.example.youneverknow.finalproject.DataClass.dataFor5days;
import com.example.youneverknow.finalproject.R;

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

            tvAutoDetectTodayCityName.setText(dataFor10days.cityName);
            tvAutoDetectTodayTemperature.setText(round2decimal(String.valueOf(dataFor10days.data[0].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetectTodayDescription.setText(formalString(dataFor10days.data[0].description));
            tvAutoDetectTodayPressure.setText("Pressure: " + dataFor10days.data[0].pressure + " hPa");
            tvAutoDetectTodayHumidity.setText("Humidity: " + dataFor10days.data[0].humidity + "%");
            tvAutoDetectTodayWind.setText("Wind: " + dataFor10days.data[0].wind + " m/s");
            tvAutoDetectTodaySunrise.setText(dataFor10days.sunRise);
            tvAutoDetectTodaySunset.setText(dataFor10days.sunSet);
            ivAutoDetectTodayWeatherIcon.setImageResource(R.drawable.cloudytest);

        }
        else if (position == 1){
            rootView = inflater.inflate(R.layout.fragment_card_5days,container,false);

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
            rootView = inflater.inflate(R.layout.fragment_card_10days,container,false);

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
            tvAutoDetect10daysPressure.setText("You can tap a column");
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

    public String[] _5days = new String[]{"Today", "+1", "+2", "+3", "+4"};
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
            tvAutoDetect5daysDescription.setText(formalString(dataFor10days.data[columnIndex].description));
            tvAutoDetect5daysPressure.setText("Pressure: " + dataFor10days.data[columnIndex].pressure + " hPa");
            tvAutoDetect5daysHumidity.setText("Humidity: " + dataFor10days.data[columnIndex].humidity + "%");
            tvAutoDetect5daysWind.setText("Wind: " + dataFor10days.data[columnIndex].wind + " m/s");
            ivAutoDetect5daysWeatherIcon.setImageResource(R.drawable.cloudytest);
            if(columnIndex == 0)
                tvAutoDetect5daysTime.setText("Today");
            else tvAutoDetect5daysTime.setText("Day " + columnIndex);

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
            tvAutoDetect5daysDescription.setText(formalString(dataFor5days.data[columnSelectedIndex].time[i1].description));
            tvAutoDetect5daysPressure.setText("Pressure: " + dataFor5days.data[columnSelectedIndex].time[i1].pressure + " hPa");
            tvAutoDetect5daysHumidity.setText("Humidity: " + dataFor5days.data[columnSelectedIndex].time[i1].humidity + "%");
            tvAutoDetect5daysWind.setText("Wind: " + dataFor5days.data[columnSelectedIndex].time[i1].wind + " m/s");
            ivAutoDetect5daysWeatherIcon.setImageResource(R.drawable.cloudytest);
            if(columnSelectedIndex == 0)
                tvAutoDetect5daysTime.setText("Today: " + tempTime);
            else tvAutoDetect5daysTime.setText("Day " + columnSelectedIndex + ": " + tempTime);
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
            tvAutoDetect10daysDescription.setText(formalString(dataFor10days.data[columnIndex].description));
            tvAutoDetect10daysPressure.setText("Pressure: " + dataFor10days.data[columnIndex].pressure + " hPa");
            tvAutoDetect10daysHumidity.setText("Humidity: " + dataFor10days.data[columnIndex].humidity + "%");
            tvAutoDetect10daysWind.setText("Wind: " + dataFor10days.data[columnIndex].wind + " m/s");
            ivAutoDetect10daysWeatherIcon.setImageResource(R.drawable.cloudytest);
            if(columnIndex == 0)
                tvAutoDetect10daysTime.setText("Today");
            else tvAutoDetect10daysTime.setText("Day " + columnIndex);
        }

        @Override
        public void onValueDeselected() {}
    }

    private class ValueTouchListener_line_16 implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int i, int i1, PointValue pointValue) {
            tvAutoDetect10daysTemperature.setText(round2decimal(String.valueOf(dataFor10days.data[i1].temperature - 273)) + (char) 0x00B0 + "C");
            tvAutoDetect10daysDescription.setText(formalString(dataFor10days.data[i1].description));
            tvAutoDetect10daysPressure.setText("Pressure: " + dataFor10days.data[i1].pressure + " hPa");
            tvAutoDetect10daysHumidity.setText("Humidity: " + dataFor10days.data[i1].humidity + "%");
            tvAutoDetect10daysWind.setText("Wind: " + dataFor10days.data[i1].wind + " m/s");
            ivAutoDetect10daysWeatherIcon.setImageResource(R.drawable.cloudytest);
            if(i1 == 0)
                tvAutoDetect10daysTime.setText("Today");
            else tvAutoDetect10daysTime.setText("Day " + i1);
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

    String formalString(String str){
        if(str == "")
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((char)(str.charAt(0) - 32));
        for (int i = 1; i < str.length(); i++){
            if(str.charAt(i) == '-'){
                stringBuilder.append(" ");
                continue;
            }
            stringBuilder.append(str.charAt(i));
        }
        return stringBuilder.toString();
    }
}
