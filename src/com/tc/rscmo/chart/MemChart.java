package com.tc.rscmo.chart;

import org.achartengine.renderer.SimpleSeriesRenderer;

import com.tc.rscmo.RscActivity;
import android.content.Context;
import android.os.Handler;

public class MemChart extends PieChartBuilder {

	private Handler mHandler;

	public MemChart(Context context,Handler h) {
		super(context);
		mHandler=h;

		initRender();
		initData();
	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initRender() {
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setStartAngle(180);
		mRenderer.setDisplayValues(true);
		mRenderer.setLabelsColor(ChartColor.BLUE2);
		mRenderer.setLabelsTextSize(25);
		mRenderer.setChartTitle("Mem Load (M)");
		mRenderer.setChartTitleTextSize(30);


		mSeries.add("Free ", 1);
		SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer();
		renderer1
				.setColor(ChartColor.BLUE);		//green
		mRenderer.addSeriesRenderer(renderer1);

		mSeries.add("Used ", 1);
		SimpleSeriesRenderer renderer2 = new SimpleSeriesRenderer();
		renderer2
				.setColor(ChartColor.RED);		//red
		mRenderer.addSeriesRenderer(renderer2);

		mRenderer.setClickEnabled(true);
	}
	
	public void updateSeries(int f,int u){
		mSeries.set(0, "Free", f);
		mSeries.set(1, "Used", u);
		mHandler.obtainMessage(RscActivity.UPDATE_MEM).sendToTarget();
	}
}
