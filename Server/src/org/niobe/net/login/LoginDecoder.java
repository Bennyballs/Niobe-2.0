package org.niobe.net.login;

import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.niobe.net.StatefulFrameDecoder;
import org.niobe.net.security.IsaacRandom;
import org.niobe.net.security.IsaacRandomPair;
import org.niobe.net.security.credential.LoginCredential;
import org.niobe.net.security.credential.PlayerCredential;
import org.niobe.util.FileUtil;

/**
 * A {@link org.niobe.net.StatefulFrameDecoder} which decodes
 * the login requests.
 *
 * @author relex lawl
 */
public final class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {

	/**
	 * The secure random generator.
	 */
	private static final SecureRandom random = new SecureRandom();
	
	/**
	 * The LoginDecoder constructor which sets
	 * the default state.
	 */
	public LoginDecoder() {
		super(LoginDecoderState.LOGIN_HANDSHAKE, true);
	}
	
	/**
	 * The user name string as a hash.
	 */
	private int usernameHash;
	
	/**
	 * The session key for the server.
	 */
	private long seed;
	
	/**
	 * This flag checks if the incoming connection
	 * is a reconnecting one.
	 */
	private boolean reconnecting;
	
	/**
	 * The amount of login packets.
	 */
	private int packetLength;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer, LoginDecoderState state) throws Exception {
		switch (state) {
		case LOGIN_HANDSHAKE:
			if (buffer.readable()) {
				usernameHash = buffer.readUnsignedByte();
				seed = random.nextLong();
				ChannelBuffer buff = ChannelBuffers.buffer(17);
				buff.writeByte(LoginConstants.LOGIN_EXCHANGE_DATA);
				buff.writeLong(0);
				buff.writeLong(seed);
				channel.write(buff);
				setState(LoginDecoderState.LOGIN_HEADER);
			}
			break;
		case LOGIN_HEADER:
			if (buffer.readableBytes() >= 2) {
				int loginType = buffer.readUnsignedByte();
				if (loginType != LoginConstants.NEW_CONNECTION_LOGIN_REQUEST && loginType != LoginConstants.RECONNECTING_LOGIN_REQUEST) {
					throw new Exception("Invalid login type: " + loginType);
				}
				reconnecting = loginType == LoginConstants.RECONNECTING_LOGIN_REQUEST;
				packetLength = buffer.readUnsignedByte();
				setState(LoginDecoderState.LOGIN_PAYLOAD);
			}
			break;
		case LOGIN_PAYLOAD:
			if (buffer.readableBytes() >= packetLength) {
				ChannelBuffer payload = buffer.readBytes(packetLength);
				if (payload.readUnsignedByte() != 0xFF) {
					throw new Exception("Invalid magic id");
				}
				int revision = payload.readUnsignedShort();
				int memory = payload.readUnsignedByte();
				if (memory != 0 && memory != 1) {
					throw new Exception("Unhandled memory byte value");
				}
				boolean lowMemory = memory == 1;
				int[] archiveCrcs = new int[9];
				for (int i = 0; i < 9; i++) {
					archiveCrcs[i] = payload.readInt();
				}
				int length = payload.readUnsignedByte();
				if (length != (packetLength - 41)) {
					throw new Exception("Packet length mismatch");
				}
				ChannelBuffer channelBuffer = payload.readBytes(length);
				int securityId = channelBuffer.readByte();
				if (securityId != 10) {
					throw new Exception("Unhandled securityId, expected: 10 - received: " + securityId);
				}
				long clientSeed = channelBuffer.readLong();
				long seedReceived = channelBuffer.readLong();
				if (seedReceived != seed) {
					throw new Exception("Unhandled seed read: [seed, seedReceived] : [" + seed + ", " + seedReceived + "]");
				}
				int[] seed = new int[4];
				seed[0] = (int) (clientSeed >> 32);
				seed[1] = (int) clientSeed;
				seed[2] = (int) (this.seed >> 32);
				seed[3] = (int) this.seed;
				IsaacRandom decodingRandom = new IsaacRandom(seed);
				for (int i = 0; i < seed.length; i++) {
					seed[i] += 50;
				}
				int uid = channelBuffer.readInt();
				String username = FileUtil.readString(channelBuffer);
				String password = FileUtil.readString(channelBuffer);
				if (username.length() > 12 || password.length() > 20) {
					throw new Exception("Username or password length too long");
				}
				IsaacRandom encodingRandom = new IsaacRandom(seed);
				PlayerCredential credentials = new PlayerCredential(username, password, usernameHash, uid);
				IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);
				LoginCredential request = new LoginCredential(credentials, randomPair, reconnecting, lowMemory, revision, archiveCrcs);
				if (buffer.readable()) {
					return new Object[] { request, buffer.readBytes(buffer.readableBytes()) };
				} else {
					return request;
				}
			}
			break;
		}
		return null;
	}
}
