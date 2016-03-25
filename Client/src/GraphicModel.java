package src;

final class GraphicModel extends Animable {
	
	public GraphicModel(int i, int j, int l, int i1, int j1, int k1, int l1)
	{
		aBoolean1567 = false;
		aSpotAnim_1568 = StillGraphics.get(i1);
		anInt1560 = i;
		anInt1561 = l1;
		anInt1562 = k1;
		anInt1563 = j1;
		anInt1564 = j + l;
		aBoolean1567 = false;
	}

	public Model getRotatedModel()
	{
		Model model = aSpotAnim_1568.getModel();
		if (model == null)
			return null;
		Animation animDef = aSpotAnim_1568.animDef;
		int j = animDef != null ? animDef.getFrame(anInt1569):-1;//animDef != null && animDef.anIntArray353 != null ? animDef.anIntArray353[anInt1569]:-1;
		//if (aSpotAnim_1568.animDef != null && Skeleton.get(aSpotAnim_1568.animDef.anIntArray353[0]) == null)
		//	return null;

		boolean rotate = aSpotAnim_1568.anInt410 != 128 || aSpotAnim_1568.anInt411 != 128;
		int flags = 0;
		if (rotate)
			flags |= 0x4;

		if (!aBoolean1567 && j != -1)
			flags |= Model.getFlags(j, animDef);

		if (aSpotAnim_1568.anInt412 != 0)
		{
			if (aSpotAnim_1568.anInt412 == 90)
				flags |= 0x4;

			if (aSpotAnim_1568.anInt412 == 180)
				flags |= 0x4;

			if (aSpotAnim_1568.anInt412 == 270)
				flags |= 0x4;

		}
		Model model_1 = new Model(flags, model);//true, Skeleton.method532(j), false
		if (!aBoolean1567 && j != -1)
		{
			model_1.createBones();
			model_1.applyTransformation(j, animDef);
			model_1.triangleSkin = null;
			model_1.vectorSkin = null;
		}
		if (rotate)
			model_1.scale(aSpotAnim_1568.anInt410, aSpotAnim_1568.anInt410, aSpotAnim_1568.anInt411);

		if (aSpotAnim_1568.anInt412 != 0)
		{
			if (aSpotAnim_1568.anInt412 == 90)
				model_1.rotate90Degrees();

			if (aSpotAnim_1568.anInt412 == 180)
			{
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
			if (aSpotAnim_1568.anInt412 == 270)
			{
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
		}
		model_1.setLighting(64 + aSpotAnim_1568.anInt413, 850 + aSpotAnim_1568.anInt414, -30, -50, -30, true, false);
		return model_1;
	}

	public void method454(int i)
	{
		for (anInt1570 += i; anInt1570 > aSpotAnim_1568.animDef.method258(anInt1569);)
		{
			anInt1570 -= aSpotAnim_1568.animDef.method258(anInt1569) + 1;
			anInt1569++;
			if (anInt1569 >= aSpotAnim_1568.animDef.anInt352 && (anInt1569 < 0 || anInt1569 >= aSpotAnim_1568.animDef.anInt352))
			{
				anInt1569 = 0;
				aBoolean1567 = true;
			}
		}
	}

	public final int anInt1560;
	public final int anInt1561;
	public final int anInt1562;
	public final int anInt1563;
	public final int anInt1564;
	public boolean aBoolean1567;
	private final StillGraphics aSpotAnim_1568;
	private int anInt1569;
	private int anInt1570;
}
