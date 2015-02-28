package it.luigibifulco.xdcc4j.common.model;

import java.io.Serializable;

public class XdccRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -802658214843268582L;

	protected String id;

	protected String channel;

	protected String peer;

	protected String resource;

	protected String host;

	protected String destination;

	protected String description;

	protected long ttl;

	public XdccRequest() {

	}

	public String getId() {
		// String s = new String(channel + peer + resource);
		// this.id = DigestUtils.md5Hex(s);
		return this.id;
	}

	public void setId(String id) {
		// String s = new String(channel + peer + resource);
		// this.id = DigestUtils.md5Hex(s);
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPeer() {
		return peer;
	}

	public void setPeer(String peer) {
		this.peer = peer;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		if (host == null) {
			host = "";
		}
		this.host = host;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return "XdccRequest [id=" + getId() + ", channel=" + channel
				+ ", peer=" + peer + ", resource=" + resource + ", host="
				+ host + ", destination=" + destination + ", description="
				+ description + ", ttl=" + ttl + "]";
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		XdccRequest other = (XdccRequest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
