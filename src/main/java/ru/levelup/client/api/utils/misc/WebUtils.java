package ru.levelup.client.api.utils.misc;

import java.net.*;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class WebUtils
{
    public static String agent1;
    public static String agent2;

    public static String visitSite(final String s) {
        StringBuilder responseBuilder = new StringBuilder();

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(s).openConnection();
            httpURLConnection.addRequestProperty(WebUtils.agent1, WebUtils.agent2);

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(); // Обработка ошибок, например, вывод стека вызовов
        }

        return responseBuilder.toString();
    }
    public static String getFormattedHWID() {
        StringBuilder hwidBuilder = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    StringBuilder macStringBuilder = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        macStringBuilder.append(String.format("02Xs", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    hwidBuilder.append(macStringBuilder.toString());
                }
            }
            InetAddress[] inetAddresses = InetAddress.getLocalHost().getAllByName(InetAddress.getLocalHost().getHostName());
            for (InetAddress inetAddress : inetAddresses) {
                hwidBuilder.append(inetAddress.getHostAddress());
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hwid = hwidBuilder.toString().toUpperCase().replace(":", "").replace("-", "");
        return "c" + hwid + "Kasd" + hwid.length();
    }

    static {
        WebUtils.agent1 = "User-Agent";
        WebUtils.agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
    }

    private static final String SECRET_KEY = "YourSecretKey"; // Замените на свой секретный ключ
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) {
        System.out.println(getFormattedHWID());
    }
}