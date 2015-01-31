package it.luigibifulco.xdcc4j.common.model;

import java.io.Serializable;

public class XdccRequest implements Serializable {

	protected String channel;

	protected String peer;

	protected String resource;

	protected String host;

	protected String destination;

	public XdccRequest() {

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
		return "XdccRequest [channel=" + channel + ", peer=" + peer
				+ ", resource=" + resource + ", host=" + host
				+ ", destination=" + destination + "]";
	}

}
