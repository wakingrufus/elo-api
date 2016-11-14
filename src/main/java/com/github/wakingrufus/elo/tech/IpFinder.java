package com.github.wakingrufus.elo.tech;


import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Slf4j
public class IpFinder {

    public String findIp() {
        String ip = "localhost";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (!iface.isLoopback() && iface.isUp()) {
                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress inetAddress = addresses.nextElement();
                        String potentialIp = inetAddress.getHostAddress();
                        log.info(potentialIp);
                        if (!potentialIp.contains(":")) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException | NullPointerException e) {
            e.printStackTrace();
        }
        Client client = JerseyClientBuilder.createClient();

        WebTarget target = client.target("http://169.254.169.254/latest/meta-data/public-hostname");
        ClientResponse response = null;
        try {
            response = target.request(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        } catch (Exception e) {
            log.warn("error trying to get aws metadata");
        }

        if (response != null && response.getStatus() == 200) {
            String output = response.getEntity().toString();
            log.info("found AWS hostname: " + output);
            ip = output;
        }
        return ip;
    }
}
