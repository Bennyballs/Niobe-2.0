package src;

public final class Player extends Entity {

	private int lastAnim1 = -1;
	private int lastAnim2 = -1;
	private int lastGFX = -1;

	public Model getRotatedModel()
	{
		if (!visible)
			return null;

		Model model = method452();
		if (model == null)
			return null;

		super.height = model.modelHeight;
		model.aBoolean1659 = true;
		if (aBoolean1699)
			return model;

		lastGFX = -1;
		if (super.graphicId >= 0 && super.graphicId < StillGraphics.cache.length && super.anInt1521 >= 0)
		{
			try
			{
				lastGFX = super.graphicId;
				StillGraphics graphicDef = StillGraphics.get(super.graphicId);
				if (graphicDef != null)
				{
					Model model_2 = graphicDef.getModel();
					if (model_2 != null)
					{
						Animation animDef = graphicDef.animDef;
						//if (animDef != null && Skeleton.get(animDef.anIntArray353[0]) == null)
						//{
							boolean rotate = graphicDef.anInt410 != 128 || graphicDef.anInt411 != 128;
							int frame = animDef != null ? animDef.getFrame(super.anInt1521):-1;//animDef != null && super.anInt1521 < animDef.anIntArray353.length ? animDef.anIntArray353[super.anInt1521]:-1;
							int flags = 0;
							if (rotate)
								flags |= 0x4;

							if (super.graphicHeight != 0)
								flags |= 0x10;

							if (frame != -1)
								flags |= Model.getFlags(frame, animDef);

							Model model_3 = new Model(flags, model_2);//true, Skeleton.method532(super.anInt1521), false
							if (super.graphicHeight != 0)
								model_3.translate(0, -super.graphicHeight, 0);

							if (frame != -1)
							{
								model_3.createBones();
								model_3.applyTransformation(frame, animDef);
								model_3.triangleSkin = null;
								model_3.vectorSkin = null;
							}
							if (rotate)
								model_3.scale(graphicDef.anInt410, graphicDef.anInt410, graphicDef.anInt411);

							model_3.setLighting(64 + graphicDef.anInt413, 850 + graphicDef.anInt414, -30, -50, -30, true, true);
							Model aclass30_sub2_sub4_sub6_1s[] = {
								model, model_3
							};
							model = new Model(aclass30_sub2_sub4_sub6_1s);
						//}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		if (objectTransformatiomModel != null)
		{
			if (Client.loopCycle >= primaryModel)
				objectTransformatiomModel = null;

			if (Client.loopCycle >= secondaryModel && Client.loopCycle < primaryModel)
			{
				Model model_1 = objectTransformatiomModel;
				model_1.translate(modelOffsetX - super.x, modelOffsetZ - anInt1709, modelOffsetY - super.y);
				if (super.turnDirection == 512)
				{
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				}
				else if (super.turnDirection == 1024)
				{
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				}
				else if (super.turnDirection == 1536)
					model_1.rotate90Degrees();
				Model aclass30_sub2_sub4_sub6s[] = {
					model, model_1
				};
				model = new Model(aclass30_sub2_sub4_sub6s);
				if (super.turnDirection == 512)
					model_1.rotate90Degrees();

				else if (super.turnDirection == 1024)
				{
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				}
				else if (super.turnDirection == 1536)
				{
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				}
				model_1.translate(super.x - modelOffsetX, anInt1709 - modelOffsetZ, super.y - modelOffsetY);
			}
		}
		model.aBoolean1659 = true;
		return model;
	}

	public void updatePlayer(JagexBuffer stream) {
		stream.currentOffset = 0;
		gender = stream.readByte();
		prayerId = stream.readByte();
		skullId = stream.readByte();
		desc = -1;
		team = 0;
		for (int j = 0; j < 12; j++) {
			int k = stream.readUnsignedByte();
			if (k == 0) {
				equipment[j] = 0;
				continue;
			}
			int i1 = stream.readUnsignedByte();
			equipment[j] = (k << 8) + i1;
			if (j == 0 && equipment[0] == 65535) {
				desc = stream.readUnsignedShort();
				break;
			}
			if (equipment[j] >= 512 && equipment[j] - 512 < ItemDef.totalItems) {
				int l1 = ItemDef.forId(equipment[j] - 512).team;
				if (l1 != 0)
					team = l1;
			}
		}
		for (int bodyPartId = 0; bodyPartId < 5; bodyPartId++) {
			int colour = stream.readUnsignedByte();
			if (colour < 0 || colour >= Client.VALID_CLOTHE_COLOUR[bodyPartId].length) {
				colour = 0;
			}
			clotheColour[bodyPartId] = colour;
		}
		super.standAnimation = stream.readUnsignedShort();
		if (super.standAnimation == 65535)
			super.standAnimation = -1;
		super.spinAnimation = stream.readUnsignedShort();
		if (super.spinAnimation == 65535)
			super.spinAnimation = -1;
		super.walkAnimation = stream.readUnsignedShort();
		if (super.walkAnimation == 65535)
			super.walkAnimation = -1;
		super.reverseAnimation = stream.readUnsignedShort();
		if (super.reverseAnimation == 65535)
			super.reverseAnimation = -1;
		super.turnLeftAnimation = stream.readUnsignedShort();
		if (super.turnLeftAnimation == 65535)
			super.turnLeftAnimation = -1;
		super.turnRightAnimation = stream.readUnsignedShort();
		if (super.turnRightAnimation == 65535)
			super.turnRightAnimation = -1;
		super.runAnimation = stream.readUnsignedShort();
		if (super.runAnimation == 65535)
			super.runAnimation = -1;
		
		// System.setProperty("sun.java2d.d3d", "true");
		// System.setProperty("sun.java2d.d3dtexbpp", "32");
		// System.setProperty("sun.java2d.ddforcevram", "true");
		//  System.setProperty("sun.java2d.translaccel", "true");
		//  System.setProperty("sun.java2d.pmoffscreen", "true");
		//  System.setProperty("sun.java2d.opengl", "true");

		name = TextClass.fixName(TextClass.nameForLong(stream.readLong()));
		combatLevel = stream.readUnsignedByte();
		//title = stream.readUByte();
		//rights = stream.readUByte();
		skill = stream.readUnsignedShort();
		visible = true;
		aLong1718 = 0L;
		for (int k1 = 0; k1 < 12; k1++) {
			aLong1718 <<= 4;
			if (equipment[k1] >= 256)
				aLong1718 += equipment[k1] - 256;
		}
		if (equipment[0] >= 256)
			aLong1718 += equipment[0] - 256 >> 4;
		if (equipment[1] >= 256)
			aLong1718 += equipment[1] - 256 >> 8;
		for (int i2 = 0; i2 < 5; i2++) {
			aLong1718 <<= 3;
			aLong1718 += clotheColour[i2];
		}
		aLong1718 <<= 1;
		aLong1718 += gender;
	}

	public Model method452()
	{
		lastAnim1 = -1;
		lastAnim2 = -1;
		NPCDefinition def = desc();
		if (def != null)
		{
			int j = -1;
			Animation animDef = null;
			if (super.animationId >= 0 && super.animationDelay == 0)
			{
				lastAnim1 = super.animationId;
				animDef = Animation.get(super.animationId);
				if (animDef != null)
					j = animDef.getFrame(super.anInt1527);

			}
			else if (super.anInt1517 >= 0)
			{
				lastAnim2 = super.anInt1517;
				animDef = Animation.get(super.anInt1517);
				if (animDef != null)
					j = animDef.getFrame(super.anInt1518);

			}
			Model model = def.method164(-1, j, animDef, false);
			return model;
		}
		long l = aLong1718;
		int k = -1;
		int i1 = -1;
		int j1 = -1;
		int k1 = -1;
		Animation animDef2 = null;
		if (super.animationId >= 0 && super.animationDelay == 0)
		{
			lastAnim1 = super.animationId;
			Animation animDef = Animation.get(super.animationId);
			if (animDef != null)
			{
				k = animDef.getFrame(super.anInt1527);
				//lastAnim1 = k;
				if (super.anInt1517 >= 0 && super.anInt1517 != super.standAnimation)
				{
					Animation childAnim = Animation.get(super.anInt1517);
					//lastAnim2 = super.anInt1517 * 100000;
					if (childAnim != null)
						i1 = childAnim.getFrame(super.anInt1518);

					//lastAnim2 = i1;
				}
				if (animDef.anInt360 >= 0)
				{
					j1 = animDef.anInt360;
					l += j1 - equipment[5] << 40;
				}
				if (animDef.anInt361 >= 0)
				{
					k1 = animDef.anInt361;
					l += k1 - equipment[3] << 48;
				}
			}
		}
		else if (super.anInt1517 >= 0)
		{
			lastAnim1 = super.anInt1517;
			animDef2 = Animation.get(super.anInt1517);
			if (animDef2 != null)
				k = animDef2.getFrame(super.anInt1518);

		}
		Model model_1 = (Model) mruNodes.insertFromCache(l);
		if (model_1 == null)
		{
			boolean flag = false;
			for (int i2 = 0; i2 < 12; i2++)
			{
				int k2 = equipment[i2];
				if (k1 >= 0 && i2 == 3)
					k2 = k1;
				if (j1 >= 0 && i2 == 5)
					k2 = j1;
				if (k2 >= 256 && k2 < 512 && !IdentityKit.cache[k2 - 256].method537())
					flag = true;
				if (k2 >= 512 && !ItemDef.forId(k2 - 512).method195(gender))
					flag = true;
			}

			if (flag)
			{
				if (aLong1697 != -1L)
					model_1 = (Model) mruNodes.insertFromCache(aLong1697);
				if (model_1 == null)
					return null;
			}
		}
		if (model_1 == null)
		{
			Model aclass30_sub2_sub4_sub6s[] = new Model[12];
			int j2 = 0;
			for (int l2 = 0; l2 < 12; l2++)
			{
				int i3 = equipment[l2];
				if (k1 >= 0 && l2 == 3)
					i3 = k1;
				if (j1 >= 0 && l2 == 5)
					i3 = j1;
				if (i3 >= 256 && i3 < 512)
				{
					Model model_3 = IdentityKit.cache[i3 - 256].method538();
					if (model_3 != null)
						aclass30_sub2_sub4_sub6s[j2++] = model_3;
				}
				if (i3 >= 512)
				{
					Model model_4 = ItemDef.forId(i3 - 512).method196(gender);
					if (model_4 != null)
						aclass30_sub2_sub4_sub6s[j2++] = model_4;
				}
			}

			model_1 = new Model(j2, aclass30_sub2_sub4_sub6s);
			for (int j3 = 0; j3 < 5; j3++)
				if (clotheColour[j3] != 0)
				{
					model_1.setColor((short) Client.VALID_CLOTHE_COLOUR[j3][0], (short) Client.VALID_CLOTHE_COLOUR[j3][clotheColour[j3]]);
					if (j3 == 1)
						model_1.setColor((short) Client.anIntArray1204[0], (short) Client.anIntArray1204[clotheColour[j3]]);
				}

			model_1.createBones();
			model_1.scale(132, 132, 132);
			model_1.setLighting(84, 1000, -90, -580, -90, true, true);
			//model_1.method478(133, 133, 133);
			mruNodes.removeFromCache(model_1, l);
			aLong1697 = l;
		}
		if (aBoolean1699)
			return model_1;

		Model model_2 = Model.aModel_1621;
		int flags = 0;
		i1 = -1;//NO SUPPORT FOR DOUBLE ANIMS!!!
		if (k != -1 && i1 != -1)
		{
			Animation animDef = Animation.get(super.animationId);
			if (animDef != null)
				flags |= Model.getFlags(animDef, i1, k, true);

		}
		else if (k != -1)
			flags |= Model.getFlags(k, animDef2);

		model_2.method464(model_1, flags);
		if (k != -1 && i1 != -1)
		{
			Animation animDef = Animation.get(super.animationId);
			if (animDef != null)
				model_2.method471(animDef, i1, k, true);

		}
		else if (k != -1)
			model_2.applyTransformation(k, animDef2);

		model_2.triangleSkin = null;
		model_2.vectorSkin = null;
		model_2.method466();
		return model_2;
	}

	public boolean isVisible()
	{
		return visible;
	}

	public String getInfo()
	{
		return "animationId: " + lastAnim1 + (lastAnim2 == -1 ? "":", " + lastAnim2) + (lastGFX == -1 ? "":", gfx: " + lastGFX);
	}

	public NPCDefinition desc()
	{
		return desc < 0 ? null:NPCDefinition.forId(desc);
	}

	public Model method453()
	{
		if (!visible)
			return null;
		NPCDefinition def = desc();
		if (def != null)
			return def.method160();
		boolean flag = false;
		for (int i = 0; i < 12; i++)
		{
			int j = equipment[i];
			if (j >= 256 && j < 512 && !IdentityKit.cache[j - 256].method539())
				flag = true;
			if (j >= 512 && !ItemDef.forId(j - 512).method192(gender))
				flag = true;
		}

		if (flag)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[12];
		int k = 0;
		for (int l = 0; l < 12; l++)
		{
			int i1 = equipment[l];
			if (i1 >= 256 && i1 < 512)
			{
				Model model_1 = IdentityKit.cache[i1 - 256].method540();
				if (model_1 != null)
					aclass30_sub2_sub4_sub6s[k++] = model_1;
			}
			if (i1 >= 512)
			{
				Model model_2 = ItemDef.forId(i1 - 512).method194(gender);
				if (model_2 != null)
					aclass30_sub2_sub4_sub6s[k++] = model_2;
			}
		}

		Model model = new Model(k, aclass30_sub2_sub4_sub6s);
		for (int j1 = 0; j1 < 5; j1++)
			if (clotheColour[j1] != 0)
			{
				model.setColor((short) Client.VALID_CLOTHE_COLOUR[j1][0], (short) Client.VALID_CLOTHE_COLOUR[j1][clotheColour[j1]]);
				if (j1 == 1)
					model.setColor((short) Client.anIntArray1204[0], (short) Client.anIntArray1204[clotheColour[j1]]);
			}

		return model;
	}

	public int rights;

	Player() {
		aLong1697 = -1L;
		aBoolean1699 = false;
		clotheColour = new int[5];
		visible = false;
		equipment = new int[14];
	}

	public int scalePlayerX = 132;
	public int scalePlayerY = 132;
	public int scalePlayerHeight = 132;
	private long aLong1697;
	public int desc = -1;
	boolean aBoolean1699;
	final int[] clotheColour;
	public int team;
	private int gender;
	public String name;
	static Cache mruNodes = new Cache(260);
	public int combatLevel;
	public int prayerId;
	public int skullId;
	public int hintIcon;
	public int secondaryModel;
	int primaryModel;
	int anInt1709;
	boolean visible;
	int modelOffsetX;
	int modelOffsetZ;
	int modelOffsetY;
	Model objectTransformatiomModel;
	public final int[] equipment;
	public int title;
	private long aLong1718;
	int anInt1719;
	int anInt1720;
	int anInt1721;
	int anInt1722;
	int skill;

}