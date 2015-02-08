package it.luigibifulco.xdcc4j.common.model;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;

public class XdccRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -802658214843268582L;

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
		String s = new String(channel + peer + resource);
		return DigestUtils.md5Hex(s);
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

}
