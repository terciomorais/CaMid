package br.ufpe.cin.in1118.management.monitoring;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataPoint implements Serializable{
	private static final long serialVersionUID = -7071323912334870781L;
	
	private double	average				= 0;
	private double	lowerValue			= 0;
	private double	higherValue			= 0;
	private double	standardDeviation	= 0;
	private long	beginTimeStamp		= 0;
	private long	endTimeStamp		= 0;
	private int		count				= 0;
	private int 	failCount			= 0;
	private double	sum					= 0;
	private	double	squareSum			= 0;
	
	private	List<Double> metrics		= null;
	
	public DataPoint(){
		this.metrics = new ArrayList<Double>();
	}
	
	public DataPoint(List<Double> metrics){
		this.metrics = metrics;
	}
	
	public List<Double> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Double> metrics) {
		this.metrics = metrics;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public void setLowerValue(double lowerValue) {
		this.lowerValue = lowerValue;
	}

	public void setHighValue(double highValue) {
		this.higherValue = highValue;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public void setBeginTimeStamp(long beginTimeStamp) {
		this.beginTimeStamp = beginTimeStamp;
	}

	public void setEndTimeStamp(long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getSuccessCount(){
		return this.count - this.failCount;
	}
	
	public double getAverage() {
		return this.average;
	}
	public double getLowerValue() {
		return lowerValue;
	}
	public double getHighValue() {
		return higherValue;
	}
	public double getStandardDeviation() {
		return standardDeviation;
	}
	public long getBeginTimeStamp() {
		return beginTimeStamp;
	}
	public long getEndTimeStamp() {
		return endTimeStamp;
	}
	public int getCount() {
		return count;
	}
	public int getFailCount() {
		return failCount;
	}

	public double getSum() {
		return sum;
	}

	public double getThroughput(){
		if((this.getEndTimeStamp() - this.getBeginTimeStamp())*0.000000000001 != 0)
			return this.getSuccessCount()/(this.getEndTimeStamp() - this.getBeginTimeStamp())*0.000000000001;
		else
			return -1;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getSquareSum() {
		return this.squareSum;
	}

	public void setSquareSum(double squareSum) {
		this.squareSum = squareSum;
	}
	
	public void setData(){
		if(this.metrics != null){
			this.setLowerValue(this.metrics.get(0));
			this.setHighValue(this.metrics.get(0));
			this.setCount(this.metrics.size());

			for (int idx = 0; idx < this.metrics.size(); idx++){
				this.sum += this.metrics.get(idx);
				this.squareSum += this.metrics.get(idx)*this.metrics.get(idx);
			}
		}
	}
	
	public void setStatistics(){
		if(this.metrics != null){
			int sucessCount = (this.count - this.failCount);
			this.average = (Double.isNaN(this.sum/sucessCount)||Double.isInfinite(this.sum/sucessCount)) ? 0.0 : (this.sum/sucessCount);
			this.standardDeviation = Math.sqrt((this.squareSum - (this.sum * this.sum/sucessCount))/(sucessCount - 1));
			this.standardDeviation = (Double.isNaN(this.standardDeviation) || Double.isInfinite(this.standardDeviation)) ? 0.0 : this.standardDeviation;
		}
	}
}
