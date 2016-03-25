package org.niobe.net.security;

/**
 * This object holds two {@link org.niobe.net.security.IsaacRandom}
 * instances.
 *
 * @author relex lawl
 */
public final class IsaacRandomPair {

	/**
	 * The IsaacRandomPair constructor.
	 * @param encode	The random seed used for encoding.
	 * @param decode	The random seed used for decoding.
	 */
	public IsaacRandomPair(IsaacRandom encode, IsaacRandom decode) {
		this.encode = encode;
		this.decode = decode;
	}
	
	/**
	 * The random seed generated for encoding.
	 */
	private final IsaacRandom encode;
	
	/**
	 * The random seed generated for decoding.
	 */
	private final IsaacRandom decode;
	
	/**
	 * Gets the encoding random generator.
	 * @return 	The random number generator for encoding.
	 */
	public IsaacRandom getEncode() {
		return encode;
	}
	
	/**
	 * Gets the decoding random generator.
	 * @return 	The random number generator for decoding.
	 */
	public IsaacRandom getDecode() {
		return decode;
	}
}
