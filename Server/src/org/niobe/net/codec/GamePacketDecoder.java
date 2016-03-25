package org.niobe.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.niobe.net.NetworkConstants;
import org.niobe.net.packet.GamePacket;
import org.niobe.net.packet.GamePacket.PacketType;
import org.niobe.net.security.IsaacRandom;

/**
 * An implementation of netty's {@link FrameDecoder} to
 * decode incoming packets.
 *
 * @author relex lawl
 */
public final class GamePacketDecoder extends FrameDecoder {
	
	/**
	 * The GamePacketDecoder constructor.
	 * @param decoder	The random seed generator to decode packets.
	 */
	public GamePacketDecoder(IsaacRandom decoder) {
		this.decoder = decoder;
	}
	
	/**
	 * The random seed used to decode packets and send 
	 * to the player's client.
	 */
	private final IsaacRandom decoder;

	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel,
			ChannelBuffer buffer) throws Exception {
		int opcode = -1, size = -1;
		if (opcode == -1) {
			if (buffer.readableBytes() < 1) {
				return null;
			}
			opcode = buffer.readUnsignedByte();
			opcode = (opcode - decoder.nextInt()) & 0xFF;
			size = NetworkConstants.PACKET_SIZES[opcode];
		}
		if (size == -1) {
			if (buffer.readableBytes() < 1) {
				return null;
			}
			size = buffer.readUnsignedByte();
		}
		if (buffer.readableBytes() >= size) {
			final byte[] DATA = new byte[size];
			buffer.readBytes(DATA);
			final ChannelBuffer payload = ChannelBuffers.buffer(size);
			payload.writeBytes(DATA);
			GamePacket packet = new GamePacket(opcode, PacketType.FIXED, payload);
			if (opcode != 0) {
				return packet;
			}
		}
		return null;
	}

	
}
