package it.luigibifulco.xdcc4j.common.model;

import java.io.Serializable;

public class DownloadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1969559672287760222L;

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

	@Override
	public String toString() {
		return "DownloadBean [id=" + id + ", desc=" + desc + ", perc=" + perc
				+ ", rate=" + rate + ", state=" + state + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadBean other = (DownloadBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
