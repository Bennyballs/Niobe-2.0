package org.niobe.net.handshake;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.niobe.net.NetworkConstants;
import org.niobe.net.login.LoginDecoder;
import org.niobe.net.login.LoginEncoder;

/**
 * A {@link org.jboss.netty.handler.codec.frame.FrameDecoder} used to reads the handshake
 * values sent from client and modifies the pipeline.
 *
 * @author relex lawl
 */
public final class HandshakeDecoder extends FrameDecoder {
	
	/**
	 * Initializes the HandshakeDecoder.
	 */
	public HandshakeDecoder() {
		super(true);
	}

	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel, 
			ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			int requestId = buffer.readUnsignedByte();
			switch (requestId) {
			case NetworkConstants.LOGIN_REQUEST:
				context.getPipeline().addFirst("loginEncoder", new LoginEncoder());
				context.getPipeline().addBefore("handler", "loginDecoder", new LoginDecoder());
				break;
			case NetworkConstants.UPDATE_REQUEST:
				//TODO update server
				System.out.println("====Update server here====");
				break;
			default:
				throw new Exception("Invalid handshake request id");	
			}
			context.getPipeline().remove(this);
			HandshakeMessage message = new HandshakeMessage(requestId);
			return buffer.readable() ? new Object[] {message, buffer.readBytes(buffer.readableBytes())} : message;
		}
		return null;
	}

}
