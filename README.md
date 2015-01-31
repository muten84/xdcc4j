# xdcc4j
Project implementing the XDCC protocol for searching, querying and run file transfer on IRC trough Java

I have created this project to simplify IRC download process from a Bot.

This is a modular project :

 - xdcc-common contains all common model and utils class.
 - xdcc-search offer the basic API for searching on most suitable irc database bot on various channels.
 - xdcc-ft (ft stay for File Transfer) offer basdic API to start new file transfer in very simple way.
 - xdcc-downloader: it can use xdcc-search and xdcc-ft for starting more downloads in parallel mode.
 
 Actual State:
  - xdcc-search actually support only the xdcc.it site, i'm working for supporting more databases.
  - xdcc-ft can only start in-memory file transfer. It will be extended with a FileTransferService and with a persistent layer
  - xdcc-downloader actually it's a simple demo of how to use xdcc-search together with xdcc-ft.
  The SimpleXdccDownloader show how they can be used:
  
  Let's see simple demo code:
  
  XdccDownloader downloader = new SimpleXdccDownloader("xdcc.it");
  
  /*search what you want!!*/
  downloader.search("my favourite series");
  
  /*SimpleXdccDownloader is statefull so let's start the party!!
   *if you want to leave all the work to the downloader 
   *you can invoke the startAnyAvailableFromList method*/
   
  String id = downloader.startAnyAvailableFromList();
  
  // check the download status
  Download d = downloader.getDownload(id);
	TransferState state = d.getCurrentTransfer().getState();
	Assert.assertTrue(state == TransferState.WORKING);
  
  That's all!
  
  Check out test classes for more details and stay tuned!!
  
  
 
