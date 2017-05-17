## Getting Started
When running this multithreaded program, we open a port on our local machines which handles outbound HTTP requests.

When an HTTP request is made, this program will parse the request and make a socket connection with the correct web server to retrieve the web objects that are necessary to return to the initial requester.

### Prerequisites
This forward proxy functions purely on the Java Virtual Machine. Therefore, you will need the **Java Virtual Machine** and **Java Development Kit** to run this program.

We will also be configuring network settings through Google Chrome.

## Utilization
After cloning this repository to your local machine, navigate to the directory which you cloned it to.

To begin running the program,
```
$ javac Retrieve.java
$ Java Retrieve
```

After running this, you should see a statement similar to this be printed on your terminal
```
172.21.108.63 at port number: 3000
```

### Browser Configuration
Now we need to configure the network settings on our machines.
1. Open Google Chrome
2. Open Google Chrome's Settings
3. Click "Show Advanced Settings"
4. Network -> "Change Proxy Settings"
5. Configure the "Web Proxy" option with the IP and Port Number that was printed out to your terminal


## Example
```
$ Java Retrieve
172.21.108.63 at port number: 3000
```
Type "www.cse.scu.edu/~sfigueira/index.html" in your Google Chrome. When you do that, the beginning of your response should look like this:
```
Command: GET http://www.cse.scu.edu/~sfigueira/index.html HTTP/1.1
SOCKET PORT: 57318
GET http://www.cse.scu.edu/~sfigueira/index.html HTTP/1.1
host: www.cse.scu.edu
path: /~sfigueira/index.html

GET /~sfigueira/index.html HTTP/1.1
Host: www.cse.scu.edu
Connection: close
coen168: 1234


HTTP/1.1 200 OK
Date: Wed, 17 May 2017 15:24:12 GMT
Server: Apache/2.2
Last-Modified: Mon, 10 Apr 2017 01:28:16 GMT
ETag: "2e6e32c-17ed-54cc5e3b34c00"
Accept-Ranges: bytes
Content-Length: 6125
Connection: close
Content-Type: text/html


<HTML>
<HEAD>

<TITLE>
Silvia Figueira
</TITLE>
...
```
