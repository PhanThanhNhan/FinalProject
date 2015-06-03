package com.example.youneverknow.finalproject.AutoDetect.Adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


import com.example.youneverknow.finalproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YouNeverKnow on 6/1/2015.
 */
public class SuperAwesomeCardFragment extends Fragment{

    private static final String ARG_POSITION = "position";

    //@InjectView(R.id.textView)
    //TextView textView;
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

        }
        else if (position == 1){
            rootView = inflater.inflate(R.layout.fragment_card_5days,container,false);
            _5days_chartTop = (LineChartView) rootView.findViewById(R.id._5days_chart_top);
            // Generate and set data for line chart
            generateInitialLineData();
            // *** BOTTOM COLUMN CHART ***
            _5days_chartBottom = (ColumnChartView) rootView.findViewById(R.id._5days_chart_bottom);
            generateColumnData();
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_card_16days,container,false);

            _16days_chartTop = (LineChartView) rootView.findViewById(R.id._16days_chart_top);
            // Generate and set data for line chart

            // *** BOTTOM COLUMN CHART ***
            _16days_chartBottom = (ColumnChartView) rootView.findViewById(R.id._16days_chart_bottom);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    generateInitialLineData_16();
                    generateLineData_16(ChartUtils.COLOR_RED, 100);
                    generateColumnData_16();
                }
            }, 200);

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

    private void generateColumnData() {

        int numSubcolumns = 1;
        int numColumns = _5days.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
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
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        _5days_lineData = new LineChartData(lines);
        _5days_lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        _5days_lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        _5days_chartTop.setLineChartData(_5days_lineData);

        // For build-up animation you have to disable viewport recalculation.
        _5days_chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 7, 0);
        _5days_chartTop.setMaximumViewport(v);
        _5days_chartTop.setCurrentViewport(v);

        _5days_chartTop.setZoomType(ZoomType.HORIZONTAL);
        _5days_chartTop.setZoomEnabled(false);
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        _5days_chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = _5days_lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }

        // Start new data animation with 300ms duration;
        _5days_chartTop.startDataAnimation(300);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            generateLineData(value.getColor(), 100);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    //                                Data For 16 days forecast                           //
    ///////////////////////////////////////////////////////////////////////////////////////

    public String[] _16days_rain = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    public String[] _16days_temperature = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};

    private LineChartView _16days_chartTop;
    private ColumnChartView _16days_chartBottom;

    private LineChartData _16days_lineData;
    private ColumnChartData _16days_columnData;

    private void generateColumnData_16() {

        int numSubcolumns = 1;
        int numColumns = _16days_rain.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
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
        int numValues = 16;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(_16days_temperature[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        _16days_lineData = new LineChartData(lines);
        _16days_lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        _16days_lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        _16days_chartTop.setLineChartData(_16days_lineData);

        // For build-up animation you have to disable viewport recalculation.
        _16days_chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 110, 15, 0);
        _16days_chartTop.setMaximumViewport(v);
        _16days_chartTop.setCurrentViewport(v);

        _16days_chartTop.setZoomType(ZoomType.HORIZONTAL);
        _16days_chartTop.setZoomEnabled(false);
    }

    private void generateLineData_16(int color, float range) {
        // Cancel last animation if not finished.
        _16days_chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = _16days_lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }

        // Start new data animation with 300ms duration;
        _16days_chartTop.startDataAnimation(300);
    }

    private class ValueTouchListener_16 implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {}

        @Override
        public void onValueDeselected() {}
    }
}
