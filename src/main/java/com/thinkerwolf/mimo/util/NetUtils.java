package com.thinkerwolf.mimo.util;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络攻击
 * 
 * @author wukai
 *
 */
public class NetUtils {

	private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);
	private static InetAddress localAddress;
	private static final Object lock = new Object();

	private static boolean ipv4Perfer = true;

	public static InetAddress getLocalhostAddress() {
		if (localAddress != null) {
			return localAddress;
		}

		synchronized (lock) {
			try {
				InetAddress candidateAddress = null;
				// 遍历所有的网络接口
				for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
						.hasMoreElements();) {
					NetworkInterface iface = ifaces.nextElement();
					// 在所有的接口下再遍历IP
					for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
						InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
						if (ipv4Perfer) {
							if (!(inetAddr instanceof Inet4Address)) {
								continue;
							}
						} else {
							if (!(inetAddr instanceof Inet6Address)) {
								continue;
							}
						}

						if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
							if (inetAddr.isSiteLocalAddress()) {
								localAddress = inetAddr;
								break;
							} else if (candidateAddress == null) {
								// site-local类型的地址未被发现，先记录候选地址
								candidateAddress = inetAddr;
							}
						}
					}
				}
				if (candidateAddress != null && localAddress == null) {
					localAddress = candidateAddress;
				}

				if (localAddress == null) {
					localAddress = InetAddress.getLocalHost();
				}
			} catch (Exception e) {
				logger.debug("obtain local address error!", e);
			}
		}
		return localAddress;
	}

	public static SocketAddress getLocalSocketAddress(int port) {
		return new InetSocketAddress(getLocalhostAddress(), port);
	}

	public static SocketAddress getRemoteSocketAddress(String hostname, int port) {
		return new InetSocketAddress(hostname, port);
	}

	public static boolean connect(final SocketChannel socketChannel, final SocketAddress remote) throws IOException {
		// AccessController的作用
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {

				@Override
				public Boolean run() throws Exception {
					return socketChannel.connect(remote);
				}
			});

		} catch (PrivilegedActionException e) {
			throw (IOException) e.getCause();
		}
	}

	public static void bind(final ServerSocketChannel socketChannel,
			final SocketAddress socketAddress) throws IOException {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
				@Override
				public Void run() throws Exception {
					socketChannel.bind(socketAddress);
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			throw (IOException) e.getCause();
		}

	}


	/**
	 *
	 * @param serverSocketChannel
	 * @return
	 * @throws IOException
	 */
	public static SocketChannel accept(final ServerSocketChannel serverSocketChannel) throws IOException {
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<SocketChannel>() {

				@Override
				public SocketChannel run() throws Exception {
					return serverSocketChannel.accept();
				}
			});
		} catch (PrivilegedActionException e) {
			throw (IOException) e.getCause();
		}
	}

	public static void main(String[] args) {

		InetAddress address = getLocalhostAddress();
		System.out.println(address.getHostAddress());
		System.out.println(address.getHostName());

	}

}
