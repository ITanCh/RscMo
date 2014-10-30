package com.tc.rscmo.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.view.View;

/**
 * 
 * 折线图基类
 * 
 * */
public abstract class XYChartBuilder {
	// data set
	protected XYMultipleSeriesDataset mDataset;
	// render
	protected XYMultipleSeriesRenderer mRenderer;
	// chart view
	private GraphicalView mChartView;
	
	private Context context;
	
	// constructor
	public XYChartBuilder(Context context) {
		this.context = context;
		mDataset = new XYMultipleSeriesDataset();
		mRenderer = new XYMultipleSeriesRenderer();
		
		//initData();
		//initRender();
	}
	
	// protected method
	abstract protected void initData();
	abstract protected void initRender();
	
	// public method
	public XYMultipleSeriesDataset getDataSetInstance() {
		return this.mDataset;
	}
	
	public void setData(XYMultipleSeriesDataset data) {
		this.mDataset = data;
	}
	
	public XYMultipleSeriesRenderer getRenderInstance() {
		return this.mRenderer;
	}
	
	public void setRender(XYMultipleSeriesRenderer render) {
		this.mRenderer = render;
	}
	
	public View getView() {
		mChartView = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return mChartView;
	}
	
	public void repaint() {
		if(mChartView != null) {
			mChartView.repaint();
		}
	}
}