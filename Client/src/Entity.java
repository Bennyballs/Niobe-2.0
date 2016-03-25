package src;

public class Entity extends Animable {
	
	public final void setPosition(int localX, int localY, boolean teleport) {
		if (animationId != -1 && Animation.anims[animationId].anInt364 == 1)
			animationId = -1;
		if (!teleport) {
			int k = localX - smallX[0];
			int l = localY - smallY[0];
			if (k >= -8 && k <= 8 && l >= -8 && l <= 8)
			{
				if (smallXYIndex < 9)
					smallXYIndex++;
				for (int i1 = smallXYIndex; i1 > 0; i1--)
				{
					smallX[i1] = smallX[i1 - 1];
					smallY[i1] = smallY[i1 - 1];
					aBooleanArray1553[i1] = aBooleanArray1553[i1 - 1];
				}

				smallX[0] = localX;
				smallY[0] = localY;
				aBooleanArray1553[0] = false;
				return;
			}
		}
		smallXYIndex = 0;
		anInt1542 = 0;
		anInt1503 = 0;
		smallX[0] = localX;
		smallY[0] = localY;
		x = smallX[0] * 128 + size * 64;
		y = smallY[0] * 128 + size * 64;
	}

	public final void resetRegionPosition() {
		smallXYIndex = 0;
		anInt1542 = 0;
	}

	public final void updateHitData(int type, int icon, int damage, int absorbed, boolean maxHit, String owner, String victim, int loopCycle) {
		for (int hit = 0; hit < 4; hit++) {
			if (hitsLoopCycle[hit] <= loopCycle) {
				hitArray[hit] = damage;
				hitMarkTypes[hit] = type;
				hitsLoopCycle[hit] = loopCycle + 70;
				hitAlpha[hit] = 256;
				hitPosY[hit] = 0;
				hitMax[hit] = maxHit;
				hitIcon[hit] = icon;
				hitAttacker[hit] = owner;
				hitVictim[hit] = victim;
				hitSoak[hit] = absorbed;
				return;
			}
		}
	}

	public final void moveInDir(boolean flag, int i)
	{
		int j = smallX[0];
		int k = smallY[0];
		if (i == 0)
		{
			j--;
			k++;
		}
		if (i == 1)
			k++;
		if (i == 2)
		{
			j++;
			k++;
		}
		if (i == 3)
			j--;
		if (i == 4)
			j++;
		if (i == 5)
		{
			j--;
			k--;
		}
		if (i == 6)
			k--;
		if (i == 7)
		{
			j++;
			k--;
		}
		if (animationId != -1 && Animation.anims[animationId].anInt364 == 1)
			animationId = -1;
		if (smallXYIndex < 9)
			smallXYIndex++;
		for (int l = smallXYIndex; l > 0; l--)
		{
			smallX[l] = smallX[l - 1];
			smallY[l] = smallY[l - 1];
			aBooleanArray1553[l] = aBooleanArray1553[l - 1];
		}
		smallX[0] = j;
		smallY[0] = k;
		aBooleanArray1553[0] = flag;
	}

	public int entScreenX;
	public int entScreenY;
	public final int index = -1;
	public int headIcon = -1;
	public int hitpointsConfig = 0;

	public boolean isVisible()
	{
		return false;
	}

	Entity()
	{
		smallX = new int[10];
		smallY = new int[10];
		interactingEntity = -1;
		turnSpeed = 32;
		runAnimation = -1;
		height = 200;
		standAnimation = -1;
		spinAnimation = -1;
		hitArray = new int[4];
		hitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		hitAlpha = new int[]{ 256, 256, 256, 256 };
		hitPosY = new int[]{ 0, 0, 0, 0 };
		hitMax = new boolean[]{ false, false, false, false };
		hitAttacker = new String[]{ "", "", "", "" };
		hitVictim = new String[]{ "", "", "", "" };
		hitIcon = new int[]{ -1, -1, -1, -1 };
		hitSoak = new int[]{ -1, -1, -1, -1 };
		anInt1517 = -1;
		graphicId = -1;
		animationId = -1;
		loopCycleStatus = -1000;
		textCycle = 100;
		size = 1;
		aBoolean1541 = false;
		aBooleanArray1553 = new boolean[10];
		walkAnimation = -1;
		reverseAnimation = -1;
		turnLeftAnimation = -1;
		turnRightAnimation = -1;
	}

	public void turnDirection(int dir)
	{
		if (lastTurnDirection == dir)
			return;

		lastTurnDirection = turnDirection = dir;
	}

	public final void updateHitData(int damage, int hitMark, int combatStyle, boolean maxHit, int loopCycle, boolean dullHitMark) {
		for (int i1 = 0; i1 < 4; i1++) {
			if (hitsLoopCycle[i1] <= loopCycle) {
				hitArray[i1] = damage;
				hitMarkTypes[i1] = hitMark;
				hitsLoopCycle[i1] = loopCycle + 70;
				hitMarkAlpha = 256;
				hitMarkPositionY = 0;
				hitMarkMax = maxHit;
				combatIcon = combatStyle;
				hitMarkDull = dullHitMark;
				return;
			}
		}
	}
	
	int hitMarkPositionY;
	boolean hitMarkMax;
	int combatIcon;
	boolean hitMarkDull;

	public final int[] smallX;
	public final int[] smallY;
	public int interactingEntity;
	int anInt1503;
	int turnSpeed;
	int runAnimation;
	public String textSpoken;
	public int height;
	public int turnDirection;
	int standAnimation;
	int spinAnimation;
	int textColour;
	final int[] hitArray;
	final int[] hitMarkTypes;
	final int[] hitsLoopCycle;
	int[] hitAlpha;
	int[] hitPosY;
	boolean[] hitMax;
	int[] hitIcon;
	String[] hitAttacker;
	String[] hitVictim;
	int[] hitSoak;
	int anInt1517;
	int anInt1518;
	int anInt1519;
	int graphicId;
	int anInt1521;
	int anInt1522;
	int graphicDelay;
	int graphicHeight;
	int smallXYIndex;
	public int animationId;
	int anInt1527;
	int anInt1528;
	int animationDelay;
	int anInt1530;
	int textEffect;
	public int loopCycleStatus;
	public int currentHealth;
	public int maxHealth;
	int textCycle;
	int anInt1537;
	int facePositionX;
	int facePositionY;
	int size;
	boolean aBoolean1541;
	int anInt1542;
	int anInt1543;
	int anInt1544;
	int anInt1545;
	int anInt1546;
	int anInt1547;
	int anInt1548;
	int anInt1549;
	int lastTurnDirection;
	public int x;
	public int y;
	int anInt1552;
	final boolean[] aBooleanArray1553;
	int walkAnimation;
	int reverseAnimation;
	int turnLeftAnimation;
	int turnRightAnimation;
	int hitMarkAlpha;
	
}
