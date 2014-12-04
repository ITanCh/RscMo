package com.tc.rscmo.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.util.Log;

import com.tc.rscmo.RscActivity;
import com.tc.rscmo.chart.CpuChart;

import com.tc.rscmo.chart.MemChart;

/*
 * get cpu usage rate and reprint chart
 * */
public class UpdateThread implements Runnable {

	private double rate = 0;
	private CpuChart cpuChart;
	private MemChart memChart;
	private RscActivity mActivity;

	public UpdateThread(RscActivity activity) {
		// set model
		mActivity = activity;
		cpuChart = mActivity.getCpuChart();
		memChart = mActivity.getMemChart();
	}

	@Override
	public void run() {
		// get total mem
		ActivityManager am = (ActivityManager) mActivity
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		long totalMem = mi.totalMem / (1024 * 1024); // M
		// System.out.println("total mem:"+totalMem);
		
		while (true) {
			// update cpu model
			getCpuInfo();

			// update mem model
			am.getMemoryInfo(mi);
			long availMem = (mi.availMem) / (1024 * 1024);
			// System.out.println("avail mem:"+mi.availMem);
			memChart.updateSeries((int) availMem, (int) (totalMem - availMem));

		}
	}

	// get cpu information from the proc/stat
	private void getCpuInfo() {
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
			String load = reader.readLine();

			String[] toks = load.split(" ");

			long idle1 = Long.parseLong(toks[5]);
			long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
					+ Long.parseLong(toks[4]) + Long.parseLong(toks[6])
					+ Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
			//get process name list in this time
			getTopCpu();

			reader.seek(0);
			load = reader.readLine();
			reader.close();

			toks = load.split(" ");

			// find right data is the key
			// System.out.println(toks[0]+" 1 "+toks[1]+" 2 "+toks[2]+" "+toks[3]+" "+toks[4]+" "+toks[5]+" "+toks[6]+" "+toks[7]+" 8 "+toks[8]);
			long idle2 = Long.parseLong(toks[5]);
			long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3])
					+ Long.parseLong(toks[4]) + Long.parseLong(toks[6])
					+ Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			// System.out.println(idle2+" "+cpu2);
			rate = (float) (cpu2 - cpu1)
					/ (float) ((cpu2 + idle2) - (cpu1 + idle1));
			// System.out.println(rate);
			int temprate = (int) ((rate + 0.05) * 100);
			if (temprate < 0 || temprate > 100) {
				// System.out.println(toks[0]+" 1 "+toks[1]+" 2 "+toks[2]+" "+toks[3]+" "+toks[4]+" "+toks[5]+" "+toks[6]+" "+toks[7]+" 8 "+toks[8]);
				return;
			}
			cpuChart.addData(temprate);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void getTopCpu() {
		cpuChart.clearName();
		String[] items;
		try {
			// -m 10, how many entries you want, -d 1, delay by how much, -n
			// 1,
			// number of iterations
			Process p = Runtime.getRuntime().exec("top -m 5 -d 1 -n 1");

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = reader.readLine();
			int i = 0;
			while (line != null) {
				//Log.e("top" + i, line);
				if(i>=7){
					items=line.trim().split("\\s+");
//					int j=0;
//					for(String item:items){
//						Log.e("item" + j, item);
//						j++;
//					}
					try{
					String rate=items[2];
					String name=items[items.length-1];
					cpuChart.addName(name, rate);
					}catch(ArrayIndexOutOfBoundsException e){
						Log.e("item","out of bounds");
					}
				}
				
				i++;
				line = reader.readLine();
			}
			// p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("shell top", "error !");
		}
	}
	// get total mem size from the proc/meminfo
	// private long getTotalMem() {
	// String str1 = "/proc/meminfo";// 系统内存信息文件
	// String str2;
	//
	// String[] strArr;
	// long allMem = 0;
	//
	// try {
	// FileReader fileReader = new FileReader(str1);
	// BufferedReader buffReader = new BufferedReader(fileReader, 8192);
	// str2 = buffReader.readLine(); // 读取meminfo第一行，系统总内存大小
	//
	// strArr = str2.split(" +");
	// // for (String num : strArr) {
	// // System.out.println(num);
	// // }
	// // System.out.println(strArr.length);
	// if (strArr.length >= 2) {
	// allMem = (Long.parseLong(strArr[1]));
	// buffReader.close();
	// System.out.println(allMem);
	// }
	//
	// buffReader.close();
	// fileReader.close();
	// } catch (IOException e) {
	// System.out.println("error :get mem");
	// }
	// return allMem;
	// }
	//
	// //get available mem
	// private void getAvailMemory() {// 获取android当前可用内存大小
	// ActivityManager am =
	// (ActivityManager)mActivity.getSystemService(Context.ACTIVITY_SERVICE);
	// MemoryInfo mi = new MemoryInfo();
	// am.getMemoryInfo(mi);
	// //mi.availMem; 当前系统的可用内存
	// System.out.println("2 total: "+mi.totalMem);
	// System.out.println("2 avail: "+mi.availMem);
	// //return Formatter.formatFileSize(getBaseContext(), mi.availMem);//
	// 将获取的内存大小规格化
	// }

}
