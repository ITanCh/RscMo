package com.tc.rscmo.chart;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;

import com.tc.rscmo.R;

import android.content.Context;
import android.graphics.Color;

public class CPUChart extends XYChartBuilder {

	private int bgColor;

	public CPUChart(Context context) {
		super(context);
		bgColor = context.getResources().getColor(R.color.text_orange);
		initData();
		initRender();
	}

	@Override
	protected void initData() {
		String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
		XYSeries series = new XYSeries(seriesTitle);
		series.add(1, 0.35);
		series.add(2, 0.62);
		series.add(3, 0.80);
		series.add(4, 0.91);
		series.add(5, 1.05);
		series.add(6, 1.12);
		mDataset.addSeries(series);
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
		mRenderer.setChartTitleTextSize(20);
		// 在scrollview中可以滑动
		mRenderer.setInScroll(true);
		// 坐标颜色，文字大小
		mRenderer.setLabelsColor(bgColor);
		mRenderer.setLabelsTextSize(18);
		// 图例字号
		mRenderer.setLegendTextSize(18);
		// 不显示图例
		mRenderer.setShowLegend(false);
		// 设置外边框（上下左右）
		mRenderer.setMargins(new int[] { 30, 30, 5, 25 });
		// 设置是否允许拖动（貌似无效，应该是必须有scrollview才行）
		mRenderer.setPanEnabled(true);
		// 设置是否允许放大和缩小，必须通过缩放按钮才能生效
		mRenderer.setZoomEnabled(true);
		mRenderer.setZoomButtonsVisible(false);
		// mRenderer.setAxisTitleTextSize(25);
		// 曲线图中“点”的大小
		mRenderer.setPointSize(5);
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

		// enable the chart click events
		mRenderer.setClickEnabled(true);
		mRenderer.setSelectableBuffer(10);
	}

}
