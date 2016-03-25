package src;


final class Item extends Animable {

	public final Model getRotatedModel() {
		ItemDef itemDef = ItemDef.forId(id);
		return itemDef.getModelForAmount(amount);
	}

	public Item() {
	}

	public int id;
	public int x;
	public int y;
	public int amount;
}
