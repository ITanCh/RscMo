package com.tc.rscmo.chart;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;

import com.tc.rscmo.MainActivity;
import com.tc.rscmo.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

public class CpuChart extends XYChartBuilder {

	private static final int POINT_COUNT=20;
	private int bgColor;
	private XYSeries series;
	private double[] data;
	private Handler mHandler;
	public CpuChart(Context context,Handler h) {
		super(context);
		mHandler=h;
		data=new double[POINT_COUNT];
		for(int i=0;i<POINT_COUNT;i++)
			data[i]=0;
		
		bgColor = context.getResources().getColor(R.color.text_orange);
		initData();
		initRender();
	}

	@Override
	protected void initData() {
		String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
		series = new XYSeries(seriesTitle);
		updateSeries();
		mDataset.addSeries(series);
	}

	private void updateSeries(){
		series.clear();
		for(int i=0;i<POINT_COUNT;i++)
			series.add(i, data[i]);
	}
	
	@Override
	protected void initRender() {
		mRenderer.setApplyBackgroundColor(true);
		// 折线图背景
		mRenderer.setBackgroundColor(Color.TRANSPARENT);
		// 外围背景。必须使用 Color.argb 方式来设置，否则无效
		mRenderer.setMarginsColor(Color.argb(00, 11, 11, 11));

		// X轴颜色
		mRenderer.setAxesColor(bgColor);
		// 曲线图标题
		mRenderer.setChartTitle("CPU Load");
		mRenderer.setChartTitleTextSize(25);
		// 在scrollview中可以滑动
		//mRenderer.setInScroll(true);
		// 坐标颜色，文字大小
		mRenderer.setLabelsColor(bgColor);
		mRenderer.setLabelsTextSize(20);
		
		// 图例字号
		//mRenderer.setLegendTextSize(40);
		// 不显示图例
		mRenderer.setShowLegend(false);
		
		// 设置外边框（上下左右）
		mRenderer.setMargins(new int[] { 0, 30, 0, 15 });
		// 设置是否允许拖动（貌似无效，应该是必须有scrollview才行）
		mRenderer.setPanEnabled(true);
		// 设置是否允许放大和缩小，必须通过缩放按钮才能生效
		//mRenderer.setZoomButtonsVisible(true);
		//mRenderer.setZoomEnabled(true);
		//mRenderer.setZoomButtonsVisible(false);
		// mRenderer.setAxisTitleTextSize(25);
		// 曲线图中“点”的大小
		mRenderer.setPointSize(4);
		
		mRenderer.setYTitle("rate(%)");
		
		mRenderer.setYAxisMax(100);
		mRenderer.setYAxisMin(0);
		// mRenderer.setGridColor(Color.TRANSPARENT);
		mRenderer.setYLabelsPadding(15);
		// mRenderer.setXLabelsPadding(20);
		// X轴、Y轴的文字颜色
		mRenderer.setYLabelsColor(0, bgColor);
		mRenderer.setXLabelsColor(bgColor);

		// create a new renderer for the new series
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		// set some renderer properties
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(10);
		renderer.setChartValuesTextSize(17);
		
		// enable the chart click events
		mRenderer.setClickEnabled(true);
		mRenderer.setSelectableBuffer(10);
	}
	
	//add new data
	public void addData(double newdata){
		for(int i=POINT_COUNT-1;i>0;i--){
			data[i]=data[i-1];
		}
		data[0]=newdata;
		updateSeries();
		//model update view
		mHandler.obtainMessage(MainActivity.UPDATE_CPU).sendToTarget();
	}

}
