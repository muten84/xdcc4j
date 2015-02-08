package it.luigibifulco.xdcc4j.downloader.service.model;

public class DownloadBean {

	private String id;

	private String desc;

	private double perc;

	private double rate;

	private String state;

	public DownloadBean() {

	}

	public DownloadBean(String id, String desc) {
		this.id = id;
		this.desc = desc;
		this.perc = 0;
		this.rate = 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPerc() {
		return perc;
	}

	public void setPerc(double perc) {
		this.perc = perc;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
