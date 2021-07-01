package com.esign.service.dfplatform.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.springframework.util.CollectionUtils;
import com.esign.service.dfplatform.exception.BaseCheckException;

/**
 * @author houlandong
 * @Description 公用的工具类
 * @Date: 2020/6/2 18:11
 */
public class DfplaformUtil {

    /**
     * 字符串不能为空，包括空字符串，否则抛出message异常
     *
     * @param param
     * @param message
     */
    public static void isNotEmpty(String param, String message) {

        if (StringUtils.isBlank(param)) {
            throw new BaseCheckException(message);
        }
    }

    /**
     * 对象不能为空，否则抛出message异常
     *
     * @param object
     * @param message
     */
    public static void isNotNull(Object object, String message) {

        if (object == null) {
            throw new BaseCheckException(message);
        }
    }

    /**
     * 对象为空，否则抛出message异常
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {

        if (object != null) {
            throw new BaseCheckException(message);
        }
    }

    /**
     * 容器不能为空，否则抛出message异常
     *
     * @param collection
     * @param message
     */
    public static void isNotBlank(Collection collection, String message) {

        if (CollectionUtils.isEmpty(collection)) {
            throw new BaseCheckException(message);
        }
    }

    /**
     * 表示成功，否则抛出message异常
     *
     * @param expression
     * @param message
     */
    public static void state(boolean expression, String message) {

        if (!expression) {
            throw new BaseCheckException(message);
        }
    }

    /**
     * 获取唯一的UUID
     *
     * @return
     */
    public static String getUUID() {

        UUIDGenerator generator = UUIDGenerator.getInstance();
        UUID uuid = generator.generateTimeBasedUUID();
        return uuid.toString();
    }

    /**
     * 获取异常详细信息
     *
     * @param ex
     * @return
     */
    public static String getExceptionDetail(Exception ex) {

        String ret = null;
        ByteArrayOutputStream out = null;
        PrintStream pout = null;
        try {

            out = new ByteArrayOutputStream();
            pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            pout.close();
            out.close();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            pout.close();
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public static String getInet4Address() {

        Enumeration<NetworkInterface> nis;
        String ip = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            for (; nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                for (; ias.hasMoreElements(); ) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet4Address && !ia.getHostAddress().equals("127.0.0.1")) {
                        ip = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return "http://" + ip + ":";
    }
}
