package com.tc.rscmo.chart;

import org.achartengine.renderer.SimpleSeriesRenderer;

import com.tc.rscmo.MainActivity;
import com.tc.rscmo.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

public class MemChart extends PieChartBuilder {

	private static int[] COLORS = new int[] { Color.GREEN, Color.GRAY,
			Color.GREEN, Color.CYAN };

	private int dividerColor;
	private Handler mHandler;

	public MemChart(Context context,Handler h) {
		super(context);
		dividerColor = context.getResources().getColor(R.color.text_orange);
		COLORS[0] = dividerColor;
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
		mRenderer.setLabelsColor(Color.BLUE);
		mRenderer.setLabelsTextSize(18);
		mRenderer.setChartTitle("Mem Load (M)");
		mRenderer.setChartTitleTextSize(25);


		mSeries.add("Free ", 1);
		SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer();
		renderer1
				.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
		mRenderer.addSeriesRenderer(renderer1);

		mSeries.add("Used ", 1);
		SimpleSeriesRenderer renderer2 = new SimpleSeriesRenderer();
		renderer2
				.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
		mRenderer.addSeriesRenderer(renderer2);

//		mSeries.add("平静 ", 29);
//		SimpleSeriesRenderer renderer3 = new SimpleSeriesRenderer();
//		renderer3
//				.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
//		mRenderer.addSeriesRenderer(renderer3);

		mRenderer.setClickEnabled(true);
	}
	
	public void updateSeries(int f,int u){
		mSeries.set(0, "Free", f);
		mSeries.set(1, "Used", u);
		mHandler.obtainMessage(MainActivity.UPDATE_MEM).sendToTarget();
	}
}
