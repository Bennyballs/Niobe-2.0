package org.niobe.net.packet;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Manages reading packet information from the netty's channel.
 * 
 * @author relex lawl
 */
public final class GamePacket {
	
	/**
	 * The Packet constructor.
	 * @param opcode	The packet id.
	 * @param packetType		The packetType of packet being read.
	 * @param buffer	The buffer used to receive information from the netty's channel.
	 */
	public GamePacket(int opcode, PacketType packetType, ChannelBuffer buffer) {
		this.opcode = opcode;
		this.packetType = packetType;
		this.buffer = buffer;
	}
	
	/**
	 * The packet id being received.
	 */
	private final int opcode;
	
	/**
	 * Gets the packet id.
	 * @return	The packet id being sent.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * The packetType of packet being read.
	 */
	private PacketType packetType;
	
	/**
	 * Gets the packet packetType.
	 * @return	The packetType of packet being read.
	 */
	public PacketType getType() {
		return packetType;
	}
	
	/**
	 * The buffer being used to read the packet information.
	 */
	private ChannelBuffer buffer;
	
	/**
	 * Gets the buffer used to receive the packet information.
	 * @return	The ChannelBuffer instance.
	 */
	public ChannelBuffer getBuffer() {
		return buffer;
	}
	
	/**
	 * Gets the size of the packet being read.
	 * @return	The size of the packet.
	 */
	public int getSize() {
		return buffer.readableBytes();
	}
	
	/**
	 * Reads an unsigned byte from the packet.
	 * @return	The unsigned byte.
	 */
	public byte readByte() {
		return buffer.readByte();
	}
	
	/**
	 * Reads a packetType-A byte from the packet.
	 * @return The unsigned byte - 128.
	 */
	public byte readByteA() {
		return (byte) (readByte() - 128);
	}
	
	/**
	 * Reads an inverse (negative) unsigned byte from the packet.
	 * @return readByte()
	 */
	public byte readByteC() {
		return (byte) (-readByte());
	}
	
	/**
	 * Reads a packetType-S byte from the packet.
	 * @return	128 - the unsigned byte value. 
	 */
	public byte readByteS() {
		return (byte) (128 - readByte());
	}
	
	/**
	 * Reads an unsigned packetType-S byte from the packet.
	 * @return	The unsigned readByteS value.
	 */
	public int readUnsignedByteS() {
		return readByteS() & 0xff;
	}
	
	/**
	 * Reads a byte array from the packet
	 */
	public GamePacket readBytes(byte[] bytes) {
		buffer.readBytes(bytes);
		return this;
	}
	
	/**
	 * Reads said amount of bytes from the packet.	
	 * @param amount	The amount of bytes to read.
	 * @return			The bytes array values.
	 */
	public byte[] readBytes(int amount) {
		byte[] bytes = new byte[amount];
		for (int i = 0; i < amount; i++) {
			bytes[i] = readByte();
		}
		return bytes;
	}
	
	/**
	 * Reads the amount of bytes packetType-A.
	 * @param amount	The amount of bytes packetType-A to read.
	 * @return 			The bytes array values.
	 */
	public byte[] readBytesA(int amount) {
		if (amount < 0)
			throw new NegativeArraySizeException("The byte array amount cannot have a negative value!");
		byte[] bytes = new byte[amount];
		for (int i = 0; i < amount; i++) {
			bytes[i] = (byte) (readByte() + 128);
		}
		return bytes;
	}

	/**
	 * Reads said amount of reversed-bytes from the packet.	
	 * @param amount	The amount of bytes to read.
	 * @return			The bytes array values.
	 */
	public byte[] readReversedBytesA(int amount) {
		byte[] bytes = new byte[amount];
		int position = amount - 1;
		for (; position >= 0; position--) {
			bytes[position] = (byte) (readByte() + 128);
		}
		return bytes;
	}
	
	/**
	 * Reads an unsigned byte.
	 * @return	The unsigned byte value read from the packet.
	 */
	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	/**
	 * Reads a short value.
	 * @return	The short value read from the packet.
	 */
	public short readShort() {
		return buffer.readShort();
	}
	
	/**
	 * Reads a short packetType-A from the packet.
	 * @return	The short packetType-A value.
	 */
	public short readShortA() {
		int value = ((readByte() & 0xFF) << 8) | (readByte() - 128 & 0xFF);
		return (short) (value > 32767 ? value - 0x10000 : value);
	}
	
	/**
	 * Reads a little-endian short from the packet.
	 * @return	The little-endian short value.
	 */
	public short readLEShort() {
		int value = (readByte() & 0xFF) | (readByte() & 0xFF) << 8;
		return (short) (value > 32767 ? value - 0x10000 : value);
	}
	
	/**
	 * Reads a little-endian packetType-A short from the packet.
	 * @return	The little-endian packetType-A short value.
	 */
	public short readLEShortA() {
		int value = (readByte() - 128 & 0xFF) | (readByte() & 0xFF) << 8;
		return (short) (value > 32767 ? value - 0x10000 : value);
	}
	
	/**
	 * Reads the unsigned short value from the packet.
	 * @return	The unsigned short value.
	 */
	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}
	
	/**
	 * Reads the unsigned short value packetType-A from the packet.
	 * @return	The unsigned short packetType-A value.
	 */
	public int readUnsignedShortA() {
		int value = 0;
		value |= readUnsignedByte() << 8;
		value |= (readByte() - 128) & 0xff;
		return value;
	}
	
	/**
	 * Reads an int value from the packet.
	 * @return	The int value.
	 */
	public int readInt() {
		return buffer.readInt();
	}
	
	/**
	 * Reads a single int value from the packet.
	 * @return	The single int value.
	 */
	public int readSingleInt() {
		byte firstByte = readByte(), secondByte = readByte(), thirdByte = readByte(), fourthByte = readByte();
		return ((thirdByte << 24) & 0xFF) | ((fourthByte << 16) & 0xFF) | ((firstByte << 8) & 0xFF) | (secondByte & 0xFF);
	}
	
	/**
	 * Reads a double int value from the packet.
	 * @return	The double int value.
	 */
	public int readDoubleInt() {
		int firstByte = readByte() & 0xFF, secondByte = readByte() & 0xFF, thirdByte = readByte() & 0xFF, fourthByte = readByte() & 0xFF;
		return ((secondByte << 24) & 0xFF) | ((firstByte << 16) & 0xFF) | ((fourthByte << 8) & 0xFF) | (thirdByte & 0xFF);
	}
	
	/**
	 * Reads a triple int value from the packet.
	 * @return	The triple int value.
	 */
	public int readTripleInt() {
		return ((readByte() << 16) & 0xFF) | ((readByte() << 8) & 0xFF) | (readByte() & 0xFF);
	}
	
	/**
	 * Reads the long value from the packet.
	 * @return	The long value.
	 */
	public long readLong() {
		return buffer.readLong();
	}
	
	/**
	 * Reads the string value from the packet.
	 * @return	The string value.
	 */
	public String readString() {
		StringBuilder builder = new StringBuilder();
		byte value;
		while (buffer.readable() && (value = buffer.readByte()) != 10) {
			builder.append((char) value);
		}
		return builder.toString();
	}
	
	/**
	 * Reads a smart value from the packet.
	 * @return	The smart value.
	 */
	public int readSmart() {
		return buffer.getByte(buffer.readerIndex()) < 128 ? readByte() & 0xFF : (readShort() & 0xFFFF) - 32768;
	}

	/**
	 * Reads a signed smart value from the packet.
	 * @return	The signed smart value.
	 */
	public int readSignedSmart() {
		return buffer.getByte(buffer.readerIndex()) < 128 ? (readByte() & 0xFF) - 64 : (readShort() & 0xFFFF) - 49152;
	}
	
	@Override
	public String toString() {
		return "Packet - [opcode, size] : [" + getOpcode() + ", " + getSize() + "]";
	}
	
	/**
	 * Represents a Packet type.
	 * 
	 * @author relex lawl
	 */
	public enum PacketType {
		FIXED,
		BYTE,
		SHORT;
	}
}