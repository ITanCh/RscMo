package com.tc.rscmo;

import java.util.ArrayList;
import java.util.List;

import com.tc.rscmo.chart.CpuChart;
import com.tc.rscmo.chart.MemChart;
import com.tc.rscmo.thread.ProcThread;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	public static final int UPDATE_CPU=1;
	public static final int UPDATE_MEM=2;

	private Context context;
	private ViewPager viewPager;
	private List<View> viewList; // cpu mem temp view list

	private TextView cpuText, memText, tempText;
	private View cpuPager, memPager, tempPager;

	private CpuChart cpuChart;			
	private MemChart memChart;

	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
    private ImageView cursor;// 动画图片
    
    private ProcThread crthread;
    
    private Handler mHandler;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;
		
		//init handler 
		mHandler=new Handler(){
			public void handleMessage (Message msg){
				switch(msg.what){
				case UPDATE_CPU:
					cpuChart.repaint();
					break;
				case UPDATE_MEM:
					memChart.repaint();
					break;
				}
			}
		};
		
		//init view
		initImageView();
		initViewPager();
		initHeaderView();
	
		//start thread
		crthread=new ProcThread(this);
		Thread t1=new Thread(crthread);
		t1.start();
	}

	public CpuChart getCpuChart() {
		return cpuChart;
	}
	
	public MemChart getMemChart() {
		return memChart;
	}
	// init three page view
	private void initViewPager() {

		// the space
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();

		cpuPager = inflater.inflate(R.layout.cpu_pager, null);
		memPager = inflater.inflate(R.layout.mem_pager, null);
		tempPager = inflater.inflate(R.layout.temp_pager, null);

		LinearLayout layout1 = (LinearLayout) cpuPager
				.findViewById(R.id.chart1);
		LinearLayout layout2 = (LinearLayout) memPager
				.findViewById(R.id.chart2);
		LinearLayout layout3 = (LinearLayout) tempPager
				.findViewById(R.id.chart3);

		cpuChart = new CpuChart(context,mHandler);
		View ccView = cpuChart.getView();
		if (ccView != null) {
			layout1.addView(ccView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			cpuChart.repaint();
		}

		memChart =new MemChart(context,mHandler);
		View mcView=memChart.getView();
		if(mcView!=null){
			layout2.addView(mcView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			memChart.repaint();
		}
		// BabyWeight weight = new BabyWeight(context);
		// View chartWeight = weight.getView();
		// if(chartWeight != null) {
		// layout2.addView(chartWeight, new
		// LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		//
		// weight.repaint();
		// }
		//
		// BabyMood mood = new BabyMood(context);
		// View chartMood = mood.getView();
		// if(chartMood != null) {
		// layout3.addView(chartMood, new
		// LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		//
		// mood.repaint();
		// }

		viewList.add(cpuPager);
		viewList.add(memPager);
		viewList.add(tempPager);

		viewPager.setAdapter(new MyPagerAdapter(viewList));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new onPageChangeListener());
	}

	// 监听view变化
	public class onPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(200);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 初始化动画
	 * */
	private void initImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	// init header tap
	private void initHeaderView() {
		cpuText = (TextView) findViewById(R.id.cpu_text);
		memText = (TextView) findViewById(R.id.mem_text);
		tempText = (TextView) findViewById(R.id.temp_text);

		cpuText.setOnClickListener(new headerViewOnClickListener(0));
		memText.setOnClickListener(new headerViewOnClickListener(1));
		tempText.setOnClickListener(new headerViewOnClickListener(2));
	}

	// 处理tap点击事件
	public class headerViewOnClickListener implements View.OnClickListener {
		private int index = 0;

		public headerViewOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
