package org.niobe.net.session;

import org.jboss.netty.channel.Channel;

/**
 * An abstract class that is used to attach to netty's 
 * channel handler's context.
 *
 * @author relex lawl
 */
public abstract class Session {
	
	/**
	 * The Session constructor.
	 * @param channel	The netty channel used for this session.
	 */
	public Session(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * The channel used for the session.
	 */
	private final Channel channel;
	
	/**
	 * Gets the channel used for the session.
	 * @return	The network channel.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Called when the session has received a message.
	 * @param message		The message being received.
	 * @throws Exception
	 */
	public abstract void receiveMessage(Object message) throws Exception;
	
	/**
	 * Called when the session has been destructed or finalized.
	 * @throws Exception
	 */
	@Override
	public abstract void finalize() throws Exception;
}
