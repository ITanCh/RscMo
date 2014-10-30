package com.tc.rscmo.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.view.View;

/**
 * 
 * 饼状图基类
 * 
 * */
abstract public class PieChartBuilder {
	// data
	protected CategorySeries mSeries;
	// render
	protected DefaultRenderer mRenderer;
	
	private GraphicalView mChartView;
	
	private Context context;
	
	// constructor
	public PieChartBuilder(Context context) {
		this.context = context;
		mSeries = new CategorySeries("");
		mRenderer = new DefaultRenderer();
	}
	
	// protected method
	abstract protected void initData();
	abstract protected void initRender();
	
	// public method
	public CategorySeries getDataSetInstance() {
		return this.mSeries;
	}
	
	public void setData(CategorySeries data) {
		this.mSeries = data;
	}
	
	public DefaultRenderer getRenderInstance() {
		return this.mRenderer;
	}
	
	public void setRender(DefaultRenderer render) {
		this.mRenderer = render;
	}
	
	public View getView() {
		mChartView = ChartFactory.getPieChartView(context, mSeries, mRenderer);
		return mChartView;
	}
	
	public void repaint() {
		if(mChartView != null) {
			mChartView.repaint();
		}
	}
}