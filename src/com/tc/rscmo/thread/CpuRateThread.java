package com.tc.rscmo.thread;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.tc.rscmo.chart.CpuChart;
/*
 * get cpu usage rate and reprint chart
 * */

public class CpuRateThread implements Runnable {

	private double rate=0;
	private CpuChart mChart;
	
	public CpuRateThread(CpuChart chart){
		//set model
		mChart=chart;
	}
	
	@Override
	public void run() {
		while(true)
		{
			try {
		        RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
		        String load = reader.readLine();

		        String[] toks = load.split(" ");

		        long idle1 = Long.parseLong(toks[5]);
		        long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
		              + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

		        try {
		            Thread.sleep(2000);
		        } catch (Exception e) {}

		        reader.seek(0);
		        load = reader.readLine();
		        reader.close();

		        toks = load.split(" ");
		        
		        //find right data is the key
		        //System.out.println(toks[0]+" 1 "+toks[1]+" 2 "+toks[2]+" "+toks[3]+" "+toks[4]+" "+toks[5]+" "+toks[6]+" "+toks[7]+" 8 "+toks[8]);
		        long idle2 = Long.parseLong(toks[5]);
		        long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
		            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
		        
		        //System.out.println(idle2+" "+cpu2);
		        rate=(float)(cpu2 - cpu1) /(float)((cpu2 + idle2) - (cpu1 + idle1));
		        //System.out.println(rate);
				int temprate = (int) ((rate + 0.05) * 100);
				if (temprate < 0 || temprate > 100) {
			        //System.out.println(toks[0]+" 1 "+toks[1]+" 2 "+toks[2]+" "+toks[3]+" "+toks[4]+" "+toks[5]+" "+toks[6]+" "+toks[7]+" 8 "+toks[8]);
					continue;
				}
		        mChart.addData(temprate);

		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		}
	}

}
