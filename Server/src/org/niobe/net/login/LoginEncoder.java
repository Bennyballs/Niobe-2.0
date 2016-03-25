package org.niobe.net.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link org.jboss.netty.handler.codec.oneone.OneToOneEncoder} which encodes
 * the values received from the login protocol.
 *
 * @author relex lawl
 */
public final class LoginEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext context, Channel channel,
			Object message) throws Exception {
		if (!(message instanceof LoginResponse)) {
			return message;
		}
		LoginResponse response = (LoginResponse) message;
		ChannelBuffer buffer = ChannelBuffers.buffer(3);
		buffer.writeByte(response.getResponse());
		if (response.getResponse() == LoginConstants.LOGIN_SUCCESSFUL) {
			buffer.writeByte(response.getRights());
			buffer.writeByte(response.getFlagStatus());
		}
		return buffer;
	}

}
