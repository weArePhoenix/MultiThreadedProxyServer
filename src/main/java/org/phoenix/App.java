package org.phoenix;

import org.phoenix.proxiserver.ProxyServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        // Start Proxy Server
        ProxyServer proxyServer = new ProxyServer();
        proxyServer.start(8888); // Start proxy server on port 8888

        // Simulate Proxy Client
//        ProxyClient proxyClient = new ProxyClient();
//        proxyClient.sendRequest("http://www.example.com/", "localhost", 8888);
    }
}
