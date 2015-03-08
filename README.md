# xdcc4j
Project implementing the XDCC protocol for searching, querying and run file transfer on IRC trough Java

I have created this project to simplify IRC download process from a Bot.

This is a modular project :

 - xdcc-common contains all common model and utils class.
 - xdcc-search offer the basic API for searching on most suitable irc database bot on various channels.
 - xdcc-ft (ft stay for File Transfer) offer basic API to start, cancel, pause resume and remove your file transfers in really simple way.
 - xdcc-downloader: it can use xdcc-search and xdcc-ft for starting more downloads in parallel mode. It expose an awesome high level REST API for managing your downloader. 
 - xdcc-ui contains a web User Interface widget providing basic user experience. Trough the web ui experience, users can control searches and downloads of wanted files. 
 
Distributions:
Xdcc Downloader 1.0-pre-alpha was released. There are some known issues. It will be solved in the next beta release. However your downloads and your search operation will work nicely. If you find some bugs or mistakes you can contact me, and feel free to open issues on github.
The pre-alpha release provide a nice "Web Distribution" with a stand-alone server working on Windows Platform.
You can download binaries of pre-alpha release frome here: http://www.filedropper.com/xdcc-downloader-10-pre-alpha

Once started the tray icon is showed on your system. Double click on and the browser will start with the UI for searching and downloading. Follow the istruction messages and good luck with your downloads.

  
  
 
