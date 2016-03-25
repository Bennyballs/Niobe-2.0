package org.niobe.net;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.Timer;
import org.niobe.net.handshake.HandshakeDecoder;

/**
 * An implementation of netty's {@link ChannelPipelineFactory}, 
 * which adds determined objects into the pipeline.
 *
 * @author relex lawl
 */
public final class PipelineFactory implements ChannelPipelineFactory {
	
	/**
	 * The PipelineFactory constructor.
	 * @param channelHandler	The network's channel handler.
	 * @param timer				The hashed wheel timer (in this case).
	 */
	public PipelineFactory(NetworkChannelHandler channelHandler, Timer timer) {
		this.channelHandler = channelHandler;
		this.timer = timer;
	}
	
	/**
	 * The network's channel handler.
	 */
	private final NetworkChannelHandler channelHandler;
	
	/**
	 * The hashed wheel timer.
	 */
	private final Timer timer;

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("handshakeDecoder", new HandshakeDecoder());
		pipeline.addLast("timeout", new ReadTimeoutHandler(timer, NetworkConstants.IDLE_TIME, TimeUnit.MINUTES));
		pipeline.addLast("handler", channelHandler);
		return pipeline;
	}

}
