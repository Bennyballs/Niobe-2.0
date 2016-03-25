package org.niobe.model.container.impl.currency;

import org.niobe.model.Item;

/**
 * An implementation of {@link ItemCurrency} to load
 * through the shops.xml file.
 *
 * @author relex lawl
 */
public final class CoinCurrency extends ItemCurrency {

	/**
	 * The CoinCurrency constructor.
	 */
	public CoinCurrency() {
		super(new Item(995));
	}
}
