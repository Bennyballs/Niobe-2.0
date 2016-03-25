package org.niobe.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.niobe.net.handshake.HandshakeMessage;
import org.niobe.net.session.Session;
import org.niobe.net.session.impl.LoginSession;

/**
 * An implementation of netty's {@link SimpleChannelUpstreamHandler} to handle
 * all of netty's incoming events.
 *
 * @author relex lawl
 */
public final class NetworkChannelHandler extends IdleStateAwareChannelUpstreamHandler {
	
	/**
	 * The {@link NetworkChannelHandler} logger to print information and warnings.
	 */
	private static final Logger logger = Logger.getLogger(NetworkChannelHandler.class.getName());
	
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e) throws Exception {
		e.getChannel().close();
		logger.info("Channel idle; closing...");
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		logger.info("Channel connected: " + channel);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		Object attachment = ctx.getAttachment();
		if (attachment != null) {
			((Session) attachment).finalize();
		}
		logger.info("Channel disconnected: " + channel);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (ctx.getAttachment() == null) {
			Object message = e.getMessage();
			HandshakeMessage handshakeMessage = (HandshakeMessage) message;
			switch (handshakeMessage.getId()) {
			case NetworkConstants.LOGIN_REQUEST:
				ctx.setAttachment(new LoginSession(ctx.getChannel(), ctx));
				break;
			case NetworkConstants.UPDATE_REQUEST:
				//TODO a {@link org.niobe.net.session.Session} implementation for update requests.
				logger.warning("Update requests unhandled!");
				break;
			default:
				throw new Exception("Unhandled handshake message id: " + handshakeMessage.getId());
			}
		} else {
			((Session) ctx.getAttachment()).receiveMessage(e.getMessage());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) throws Exception {
		context.getChannel().close();
		logger.log(Level.WARNING, "Exception caught in channel: " + event.getChannel() + "; cause: ", event.getCause());
	}
}
