package com.github.wakingrufus.elo.tech;


import lombok.extern.slf4j.Slf4j;

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
        return ip;
    }
}
