package it.biffi.jirc.bot;

public class FileTransferConfig implements BotConfig {

	private String sourceChannel;

	private String sourcePeer;

	private String fileName;

	private String inputCommand;

	private String outputPath;

	public String getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getInputCommand() {
		return inputCommand;
	}

	public void setInputCommand(String inputCommand) {
		this.inputCommand = inputCommand;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String getSourcePeer() {
		return sourcePeer;
	}

	public void setSourcePeer(String sourcePeer) {
		this.sourcePeer = sourcePeer;
	}

}
