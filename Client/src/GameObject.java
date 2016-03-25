package src;

final class GameObject extends Animable
{
	public Model getRotatedModel()
	{
		int j = -1;
		if (animDef != null)
		{
			int k = Client.loopCycle - anInt1608;
			if (k > 100 && animDef.anInt356 > 0)
				k = 100;
			while (k > animDef.method258(anInt1599))
			{
				k -= animDef.method258(anInt1599);
				anInt1599++;
				if (anInt1599 < animDef.anInt352)
					continue;
				anInt1599 -= animDef.anInt356;
				if (anInt1599 >= 0 && anInt1599 < animDef.anInt352)
					continue;
				animDef = null;
				break;
			}
			anInt1608 = Client.loopCycle - k;
			if (animDef != null)
				j = animDef != null ? animDef.getFrame(anInt1599):-1;//animDef.anIntArray353[anInt1599];
		}
		ObjectDefinition class46;
		if (anIntArray1600 != null)
			class46 = method457();
		else
			class46 = ObjectDefinition.forId(anInt1610);
		if (class46 == null)
		{
			return null;
		}
		else
		{
			return class46.getModelFromPosition(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, j, animDef);
		}
	}

	private ObjectDefinition method457()
	{
		try
		{
			int i = -1;
			if (anInt1601 != -1)
			{
				VarBit variableBit = VarBit.cache[anInt1601];
				int k = variableBit.anInt648;
				int l = variableBit.anInt649;
				int i1 = variableBit.anInt650;
				int j1 = Client.anIntArray1232[i1 - l];
				i = clientInstance.variousSettings[k] >> l & j1;
			}
			else if (anInt1602 != -1)
				i = clientInstance.variousSettings[anInt1602];
			if (i < 0 || i >= anIntArray1600.length || anIntArray1600[i] == -1)
				return null;
			else
				return ObjectDefinition.forId(anIntArray1600[i]);
		}
		catch (Exception e)
		{
		}
		return null;
	}

	public GameObject(int i, int j, int k, int l, int i1, int j1, int k1, int l1, boolean flag)
	{
		anInt1610 = i;
		anInt1611 = k;
		anInt1612 = j;
		anInt1603 = j1;
		anInt1604 = l;
		anInt1605 = i1;
		anInt1606 = k1;
		if (l1 != -1 && l1 < Animation.anims.length)
		{
			animDef = Animation.anims[l1];
			anInt1599 = 0;
			anInt1608 = Client.loopCycle;
			if (flag && animDef.anInt356 != -1)
			{
				anInt1599 = (int) (Math.random() * (double) animDef.anInt352);
				anInt1608 -= (int) (Math.random() * (double) animDef.method258(anInt1599));
			}
		}
		ObjectDefinition class46 = ObjectDefinition.forId(anInt1610);
		anInt1601 = class46.anInt774;
		anInt1602 = class46.anInt749;
		anIntArray1600 = class46.childrenIDs;
	}

	private int anInt1599;
	private final int[] anIntArray1600;
	private final int anInt1601;
	private final int anInt1602;
	private final int anInt1603;
	private final int anInt1604;
	private final int anInt1605;
	private final int anInt1606;
	private Animation animDef;
	private int anInt1608;
	public static Client clientInstance;
	private final int anInt1610;
	private final int anInt1611;
	private final int anInt1612;
}
