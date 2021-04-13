# VeryBasicBrowser

A minimalistic web browser written in Java. The browser has only
three components:  the url field, the HTML text pane, and a status
text field.

To run the browser, you can create a project in an IDE or run
the browser from the command line.

In an IDE, you can create a project, add the Java file, and then add
a dependency on the jsoup jar file. After that you can compile and
run the browser.

For command-line use, you have to put VeryBasicBrowser.java and the 
jsoup jar file in one directory. Then compile and run the browser in 
that directory.

The instructions for operating systems other than Windows are the following:
- /path/to/javac -cp jsoup-1.13.1.jar VeryBasicBrowser.java
- /path/to/java -cp .:jsoup-1.13.1.jar VeryBasicBrowser

The instructions for Windows are the following:
- C:\path\to\javac -cp jsoup-1.13.1.jar VeryBasicBrowser.java
- C:\path\to\java -cp .;jsoup-1.13.1.jar VeryBasicBrowser

For more details, please read the pdf.
