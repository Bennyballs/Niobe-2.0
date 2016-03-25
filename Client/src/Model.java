package src;


import src.sign.signlink;

public class Model extends Animable
{
	public static void nullLoader()
	{
		models = null;
		aBooleanArray1663 = null;
		aBooleanArray1664 = null;
		anIntArray1666 = null;
		anIntArray1667 = null;
		anIntArray1668 = null;
		anIntArray1669 = null;
		anIntArray1670 = null;
		anIntArray1671 = null;
		anIntArrayArray1672 = (int[][])null;
		anIntArray1673 = null;
		anIntArrayArray1674 = (int[][])null;
		anIntArray1675 = null;
		anIntArray1676 = null;
		anIntArray1677 = null;
		SINE = null;
		COSINE = null;
		hsl2rgb = null;
		lightDecay = null;
		updateManager = null;
	}

	private static byte[][] models;

	public static void nullLoaderSafe()
	{
		updateManager = null;
		models = null;
	}

	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	public static void method460(byte[] data, int id)
	{
		models[id] = data != null ? data:EMPTY_BYTE_ARRAY;
	}

	public static void method459(int count, OnDemandFetcherParent um)
	{
		updateManager = um;
		models = new byte[count][];
	}

	public static void unload(int id)
	{
		models[id] = null;
	}

	public static Model getModel(int id) {
		if (id < 0 || id >= models.length) {
			return null;
		}
		byte[] data = models[id];
		if (data == null) {
			updateManager.loadModel(id);
			return null;
		}
		return new Model(data);
	}
	public static boolean method463(int id)
	{
		if (models == null || id < 0 || id >= models.length)
			return false;

		if (models[id] != null)
			return true;

		updateManager.loadModel(id);
		return false;
	}

	byte trianglePriority;
	int numTriangles;
	int maxDepth; 
	int numVertices;
	int numTexTriangles;
	int version;
	int[] verticesZ;
	int[] verticesY;
	int[] anIntArray2584;
	int[] anIntArray2588;
	int[] anIntArray2593;
	int[] vertexSkins;
	int[] triangleSkinValues;
	int[] anIntArray2603;
	int[] anIntArray2615;
	int[] verticesX;
	int[] anIntArray2617;
	byte[] aByteArray2585;
	byte[] textureTypes;
	byte[] drawTypes;
	byte[] trianglePriorities;
	byte[] aByteArray2609;
	byte[] texturePointers;
	byte[] triangleAlphaValues;
	short[] aShortArray2575;
	short[] texTrianglePointsY;
	public short[] colorValues;
	short[] aShortArray2582;
	short[] trianglePoints2;
	short[] trianglePoints1;
	short[] textureBackgrounds;
	short[] texTrianglePointsZ;
	short[] texTrianglePointsX;
	short[] trianglePoints3;

	public Model()
	{
		aBoolean1659 = false;
		maxDepth = 0;
		numTexTriangles = 0;
		trianglePriority = (byte) 0;
		version = 12;
		numVertices = 0;
		numTriangles = 0;
	}

	public Model(boolean flag)
	{
		this();
	}

	public Model(byte[] data)
	{
		this();
		if (data != null && data.length > 1)
			if (data[data.length - 1] == -1 && data[data.length - 2] == -1)
				loadNewModel(data);
			else
				loadOldModel(data);


	}

	public Model(int i, int i_199_, int i_200_)
	{
		this();
		triangleSkinValues = new int[i_199_];
		textureBackgrounds = new short[i_199_];
		colorValues = new short[i_199_];
		trianglePoints2 = new short[i_199_];
		trianglePriorities = new byte[i_199_];
		drawTypes = new byte[i_199_];
		verticesY = new int[i];
		trianglePoints1 = new short[i_199_];
		if (i_200_ > 0)
		{
			texTrianglePointsY = new short[i_200_];
			anIntArray2603 = new int[i_200_];
			aByteArray2585 = new byte[i_200_];
			anIntArray2617 = new int[i_200_];
			texTrianglePointsX = new short[i_200_];
			texTrianglePointsZ = new short[i_200_];
			textureTypes = new byte[i_200_];
			anIntArray2593 = new int[i_200_];
			aByteArray2609 = new byte[i_200_];
			anIntArray2615 = new int[i_200_];
			anIntArray2584 = new int[i_200_];
			anIntArray2588 = new int[i_200_];
		}
		verticesX = new int[i];
		vertexSkins = new int[i];
		verticesZ = new int[i];
		texturePointers = new byte[i_199_];
		triangleAlphaValues = new byte[i_199_];
		trianglePoints3 = new short[i_199_];
	}

	public void setColor(short s, short s_134_)
	{
		for (int i_135_ = 0; i_135_ < numTriangles; i_135_++)
			if (colorValues[i_135_] == s)
				colorValues[i_135_] = s_134_;


	}

	public void setTexture(short s, short s_134_)
	{
		if (textureBackgrounds != null)
			for (int i_135_ = 0; i_135_ < numTriangles; i_135_++)
				if (textureBackgrounds[i_135_] == s)
					textureBackgrounds[i_135_] = s_134_;



	}

	public int addVertex(int i_0_, int i_1_, int i_2_)
	{
		for (int i_3_ = 0; i_3_ < numVertices; i_3_++)
			if (i_2_ == verticesX[i_3_] && i_1_ == verticesY[i_3_] && i_0_ == verticesZ[i_3_])
				return i_3_;


		verticesX[numVertices] = i_2_;
		verticesY[numVertices] = i_1_;
		verticesZ[numVertices] = i_0_;
		maxDepth++;
		return numVertices++;
	}

	public int addTriangle(int i, int i_4_, int i_5_, short s, short s_6_, byte b, byte b_7_, byte b_8_)
	{
		trianglePoints1[numTriangles] = (short) i;
		trianglePoints2[numTriangles] = (short) i_5_;
		trianglePoints3[numTriangles] = (short) i_4_;
		drawTypes[numTriangles] = b_7_;
		texturePointers[numTriangles] = b_8_;
		colorValues[numTriangles] = s;
		triangleAlphaValues[numTriangles] = b;
		textureBackgrounds[numTriangles] = s_6_;
		return numTriangles++;
	}

	public void upscale(int i)
	{
		for (int i_10_ = 0; i_10_ < numVertices; i_10_++)
		{
			verticesX[i_10_] <<= i;
			verticesY[i_10_] <<= i;
			verticesZ[i_10_] <<= i;
		}

		if (numTexTriangles > 0 && anIntArray2603 != null)
			for (int i_11_ = 0; i_11_ < anIntArray2603.length; i_11_++)
			{
				anIntArray2603[i_11_] <<= i;
				anIntArray2588[i_11_] <<= i;
				if (textureTypes[i_11_] != 1)
					anIntArray2593[i_11_] <<= i;

			}


	}

	public void translate(int i, int i_12_, int i_14_)
	{
		for (int i_16_ = 0; i_16_ < numVertices; i_16_++)
		{
			verticesX[i_16_] += i << 2;
			verticesY[i_16_] += i_12_ << 2;
			verticesZ[i_16_] += i_14_ << 2;
		}

	}

	public int[][] getVertexSkins(boolean bool)
	{
		int[] is = new int[256];
		int i_17_ = 0;
		int i_18_ = bool ? numVertices:maxDepth;
		for (int i_19_ = 0; i_19_ < i_18_; i_19_++)
		{
			int i_20_ = vertexSkins[i_19_];
			if (i_20_ >= 0)
			{
				if (i_17_ < i_20_)
					i_17_ = i_20_;

				is[i_20_]++;
			}

		}

		int[][] is_21_ = new int[1 + i_17_][];
		for (int i_22_ = 0; i_22_ <= i_17_; i_22_++)
		{
			is_21_[i_22_] = new int[is[i_22_]];
			is[i_22_] = 0;
		}

		for (int i_23_ = 0; i_23_ < i_18_; i_23_++) 
		{
			int i_24_ = vertexSkins[i_23_];
			if (i_24_ >= 0)
				is_21_[i_24_][is[i_24_]++] = i_23_;

		}

		return is_21_;
	}

	public void rotate(int i, int i_102_, int i_104_)
	{
		if (i != 0)
		{
			int i_105_ = ANIM_SINE[i];
			int i_106_ = ANIM_COSINE[i];
			for (int i_107_ = 0; i_107_ < numVertices; i_107_++)
			{
				int i_108_ = i_106_ * verticesX[i_107_] + i_105_ * verticesY[i_107_] >> 14;
				verticesY[i_107_] = i_106_ * verticesY[i_107_] - verticesX[i_107_] * i_105_ >> 14;
				verticesX[i_107_] = i_108_;
			}

		}
		if (i_102_ != 0)
		{
			int i_109_ = ANIM_SINE[i_102_];
			int i_110_ = ANIM_COSINE[i_102_];
			for (int i_111_ = 0; i_111_ < numVertices; i_111_++)
			{
				int i_112_ = i_110_ * verticesY[i_111_] - i_109_ * verticesZ[i_111_] >> 14;
				verticesZ[i_111_] = i_109_ * verticesY[i_111_] + verticesZ[i_111_] * i_110_ >> 14;
				verticesY[i_111_] = i_112_;
			}

		}
		if (i_104_ != 0)
		{
			int i_113_ = ANIM_SINE[i_104_];
			int i_114_ = ANIM_COSINE[i_104_];
			for (int i_115_ = 0; i_115_ < numVertices; i_115_++)
			{
				int i_116_ = i_113_ * verticesZ[i_115_] + i_114_ * verticesX[i_115_] >> 14;
				verticesZ[i_115_] = verticesZ[i_115_] * i_114_ - i_113_ * verticesX[i_115_] >> 14;
				verticesX[i_115_] = i_116_;
			}

		}
	}

	public byte addTexture(byte b, byte b_117_, short s, short s_119_, short s_120_, short s_121_, short s_122_, short s_123_, byte b_124_)
	{
		if (numTexTriangles >= 255)
			throw new IllegalStateException();

		textureTypes[numTexTriangles] = (byte) 3;
		texTrianglePointsX[numTexTriangles] = s_119_;
		texTrianglePointsY[numTexTriangles] = s_121_;
		texTrianglePointsZ[numTexTriangles] = s;
		anIntArray2603[numTexTriangles] = s_120_;
		anIntArray2588[numTexTriangles] = s_122_;
		anIntArray2593[numTexTriangles] = s_123_;
		aByteArray2609[numTexTriangles] = b;
		aByteArray2585[numTexTriangles] = b_117_;
		anIntArray2617[numTexTriangles] = b_124_;
		return (byte) numTexTriangles++;
	}

	public int[][] getTriangleSkins()
	{
		int[] is = new int[256];
		int i = 0;
		for (int i_137_ = 0; numTriangles > i_137_; i_137_++)
		{
			int i_138_ = triangleSkinValues[i_137_];
			if (i_138_ >= 0)
			{
				is[i_138_]++;
				if (i_138_ > i)
					i = i_138_;

			}
		}

		int[][] is_139_ = new int[1 + i][];
		for (int i_140_ = 0; i_140_ <= i; i_140_++)
		{
			is_139_[i_140_] = new int[is[i_140_]];
			is[i_140_] = 0;
		}

		for (int i_141_ = 0; i_141_ < numTriangles; i_141_++)
		{
			int i_142_ = triangleSkinValues[i_141_];
			if (i_142_ >= 0)
				is_139_[i_142_][is[i_142_]++] = i_141_;

		}
		return is_139_;
	}

	private void loadNewModel(byte[] bs)
	{
		Buffer buffer = new Buffer(bs);
		Buffer buffer_25_ = new Buffer(bs);
		Buffer buffer_26_ = new Buffer(bs);
		Buffer buffer_27_ = new Buffer(bs);
		Buffer buffer_28_ = new Buffer(bs);
		Buffer buffer_29_ = new Buffer(bs);
		Buffer buffer_30_ = new Buffer(bs);
		buffer.currentOffset = bs.length - 23;
		numVertices = buffer.getUnsignedShort();
		numTriangles = buffer.getUnsignedShort();
		numTexTriangles = buffer.getUnsigned();
		int i = buffer.getUnsigned();
		boolean bool_31_ = (i & 0x1) != 0;
		boolean bool_32_ = (i & 0x2) != 0;
		boolean bool_33_ = (i & 0x4) != 0;
		boolean bool_34_ = (i & 0x8) != 0;
		if (bool_34_)
		{
			buffer.currentOffset -= 7;
			version = buffer.getUnsigned();
			buffer.currentOffset += 6;
		}
		int i_35_ = buffer.getUnsigned();
		if (i_35_ == 0xff)
			i_35_ = -1;

		int i_36_ = buffer.getUnsigned();
		int i_37_ = buffer.getUnsigned();
		int i_38_ = buffer.getUnsigned();
		int i_39_ = buffer.getUnsigned();
		int i_40_ = buffer.getUnsignedShort();
		int i_41_ = buffer.getUnsignedShort();
		int i_42_ = buffer.getUnsignedShort();
		int i_43_ = buffer.getUnsignedShort();
		int i_44_ = buffer.getUnsignedShort();
		int i_45_ = 0;
		int i_46_ = 0;
		int i_47_ = 0;
		if (numTexTriangles > 0)
		{
			textureTypes = new byte[numTexTriangles];
			buffer.currentOffset = 0;
			for (int i_48_ = 0; i_48_ < numTexTriangles; i_48_++)
			{
				byte b = textureTypes[i_48_] = buffer.get();
				if (b == 0)
					i_45_++;

				if (b == 2)
					i_47_++;

				if (b >= 1 && b <= 3)
					i_46_++;

			}

		}
		int i_49_ = numTexTriangles;
		int i_50_ = i_49_;
		i_49_ += numVertices;
		int i_51_ = i_49_;
		if (bool_31_)
			i_49_ += numTriangles;

		int i_52_ = i_49_;
		i_49_ += numTriangles;
		int i_53_ = i_49_;
		if (i_35_ == -1)
			i_49_ += numTriangles;

		int i_54_ = i_49_;
		if (i_37_ == 1)
			i_49_ += numTriangles;

		int i_55_ = i_49_;
		if (i_39_ == 1)
			i_49_ += numVertices;

		int i_56_ = i_49_;
		if (i_36_ == 1)
			i_49_ += numTriangles;

		int i_57_ = i_49_;
		i_49_ += i_43_;
		int i_58_ = i_49_;
		if (i_38_ == 1)
			i_49_ += numTriangles * 2;

		int i_59_ = i_49_;
		i_49_ += i_44_;
		int i_60_ = i_49_;
		i_49_ += 2 * numTriangles;
		int i_61_ = i_49_;
		i_49_ += i_40_;
		int i_62_ = i_49_;
		i_49_ += i_41_;
		int i_63_ = i_49_;
		i_49_ += i_42_;
		int i_64_ = i_49_;
		i_49_ += 6 * i_45_;
		int i_65_ = i_49_;
		i_49_ += i_46_ * 6;
		int i_66_ = 6;
		if (version == 14)
			i_66_ = 7;
		else if (version >= 15)
			i_66_ = 9;

		int i_67_ = i_49_;
		i_49_ += i_46_ * i_66_;
		int i_68_ = i_49_;
		i_49_ += i_46_;
		int i_69_ = i_49_;
		i_49_ += i_46_;
		int i_70_ = i_49_;
		i_49_ += i_46_ + i_47_ * 2;
		colorValues = new short[numTriangles];
		buffer.currentOffset = i_50_;
		if (bool_31_)
			drawTypes = new byte[numTriangles];

		if (numTexTriangles > 0)
		{
			texTrianglePointsX = new short[numTexTriangles];
			texTrianglePointsY = new short[numTexTriangles];
			texTrianglePointsZ = new short[numTexTriangles];
			if (i_46_ > 0)
			{
				anIntArray2593 = new int[i_46_];
				aByteArray2585 = new byte[i_46_];
				anIntArray2603 = new int[i_46_];
				aByteArray2609 = new byte[i_46_];
				anIntArray2617 = new int[i_46_];
				anIntArray2588 = new int[i_46_];
			}
			if (i_47_ > 0)
			{
				anIntArray2615 = new int[i_47_];
				anIntArray2584 = new int[i_47_];
			}
		}
		verticesX = new int[numVertices];
		verticesY = new int[numVertices];
		verticesZ = new int[numVertices];
		if (i_39_ == 1)
			vertexSkins = new int[numVertices];

		trianglePoints1 = new short[numTriangles];
		trianglePoints2 = new short[numTriangles];
		trianglePoints3 = new short[numTriangles];
		if (i_38_ == 1)
			textureBackgrounds = new short[numTriangles];

		trianglePriority = (byte) i_35_;
		if (i_35_ == -1)
			trianglePriorities = new byte[numTriangles];

		if (i_36_ == 1)
			triangleAlphaValues = new byte[numTriangles];

		if (i_38_ == 1 && numTexTriangles > 0)
			texturePointers = new byte[numTriangles];

		if (i_37_ == 1)
			triangleSkinValues = new int[numTriangles];

		int i_71_ = i_49_;
		buffer_25_.currentOffset = i_61_;
		buffer_26_.currentOffset = i_62_;
		buffer_27_.currentOffset = i_63_;
		buffer_28_.currentOffset = i_55_;
		int i_72_ = 0;
		int i_73_ = 0;
		int i_74_ = 0;
		for (int i_75_ = 0; i_75_ < numVertices; i_75_++)
		{
			int i_76_ = buffer.getUnsigned();
			int i_77_ = 0;
			if ((i_76_ & 0x1) != 0)
				i_77_ = buffer_25_.getSignedSmart();

			int i_78_ = 0;
			if ((i_76_ & 0x2) != 0)
				i_78_ = buffer_26_.getSignedSmart();

			int i_79_ = 0;
			if ((i_76_ & 0x4) != 0)
				i_79_ = buffer_27_.getSignedSmart();

			verticesX[i_75_] = i_77_ + i_72_;
			verticesY[i_75_] = i_78_ + i_73_;
			verticesZ[i_75_] = i_79_ + i_74_;
			i_72_ = verticesX[i_75_];
			i_73_ = verticesY[i_75_];
			i_74_ = verticesZ[i_75_];
			if (i_39_ == 1)
				vertexSkins[i_75_] = buffer_28_.getUnsigned();

		}

		buffer.currentOffset = i_60_;
		buffer_25_.currentOffset = i_51_;
		buffer_26_.currentOffset = i_53_;
		buffer_27_.currentOffset = i_56_;
		buffer_28_.currentOffset = i_54_;
		buffer_29_.currentOffset = i_58_;
		buffer_30_.currentOffset = i_59_;
		for (int i_80_ = 0; i_80_ < numTriangles; i_80_++)
		{
			colorValues[i_80_] = (short) buffer.getUnsignedShort();
			if (bool_31_)
				drawTypes[i_80_] = buffer_25_.get();

			if (i_35_ == -1) {
				trianglePriorities[i_80_] = buffer_26_.get();
				//trianglePriorities[i_80_] = 10;//Model Priorites fix
			}

			if (i_36_ == 1) {
				triangleAlphaValues[i_80_] = buffer_27_.get();
			}
			if (i_37_ == 1) {
				triangleSkinValues[i_80_] = buffer_28_.getUnsigned();
			}
			if (i_38_ == 1) {
				textureBackgrounds[i_80_] = (short) (buffer_29_.getUnsignedShort() - 1);
		}
			if (texturePointers != null)
				if (textureBackgrounds[i_80_] == -1)
					texturePointers[i_80_] = -1;
				else
					texturePointers[i_80_] = (byte) (buffer_30_.getUnsigned() - 1);


		}

		maxDepth = -1;
		buffer.currentOffset = i_57_;
		buffer_25_.currentOffset = i_52_;
		short s = 0;
		short s_81_ = 0;
		short s_82_ = 0;
		int i_83_ = 0;
		for (int i_84_ = 0; i_84_ < numTriangles; i_84_++)
		{
			int i_85_ = buffer_25_.getUnsigned();
			if (i_85_ == 1)
			{
				s = (short) (i_83_ + buffer.getSignedSmart());
				i_83_ = s;
				s_81_ = (short) (i_83_ + buffer.getSignedSmart());
				i_83_ = s_81_;
				s_82_ = (short) (i_83_ + buffer.getSignedSmart());
				i_83_ = s_82_;
				trianglePoints1[i_84_] = s;
				trianglePoints2[i_84_] = s_81_;
				trianglePoints3[i_84_] = s_82_;
				if (maxDepth < s)
					maxDepth = s;

				if (maxDepth < s_81_)
					maxDepth = s_81_;

				if (s_82_ > maxDepth)
					maxDepth = s_82_;

			}
			if (i_85_ == 2)
			{
				s_81_ = s_82_;
				s_82_ = (short) (buffer.getSignedSmart() + i_83_);
				i_83_ = s_82_;
				trianglePoints1[i_84_] = s;
				trianglePoints2[i_84_] = s_81_;
				trianglePoints3[i_84_] = s_82_;
				if (maxDepth < s_82_)
					maxDepth = s_82_;

			}
			if (i_85_ == 3)
			{
				s = s_82_;
				s_82_ = (short) (buffer.getSignedSmart() + i_83_);
				i_83_ = s_82_;
				trianglePoints1[i_84_] = s;
				trianglePoints2[i_84_] = s_81_;
				trianglePoints3[i_84_] = s_82_;
				if (maxDepth < s_82_)
					maxDepth = s_82_;

			}
			if (i_85_ == 4)
			{
				short s_86_ = s;
				s = s_81_;
				s_82_ = (short) (buffer.getSignedSmart() + i_83_);
				s_81_ = s_86_;
				i_83_ = s_82_;
				trianglePoints1[i_84_] = s;
				trianglePoints2[i_84_] = s_81_;
				trianglePoints3[i_84_] = s_82_;
				if (maxDepth < s_82_)
					maxDepth = s_82_;

			}
		}

		buffer.currentOffset = i_64_;
		maxDepth++;
		buffer_25_.currentOffset = i_65_;
		buffer_26_.currentOffset = i_67_;
		buffer_27_.currentOffset = i_68_;
		buffer_28_.currentOffset = i_69_;
		buffer_29_.currentOffset = i_70_;
		for (int i_87_ = 0; i_87_ < numTexTriangles; i_87_++)
		{
			int i_88_ = textureTypes[i_87_];
			if (i_88_ == 0)
			{
				texTrianglePointsX[i_87_] = (short) buffer.getUnsignedShort();
				texTrianglePointsY[i_87_] = (short) buffer.getUnsignedShort();
				texTrianglePointsZ[i_87_] = (short) buffer.getUnsignedShort();
			}
			if (i_88_ == 1)
			{
				texTrianglePointsX[i_87_] = (short) buffer_25_.getUnsignedShort();
				texTrianglePointsY[i_87_] = (short) buffer_25_.getUnsignedShort();
				texTrianglePointsZ[i_87_] = (short) buffer_25_.getUnsignedShort();
				if (version < 15)
				{
					anIntArray2603[i_87_] = buffer_26_.getUnsignedShort();
					if (version < 14)
						anIntArray2588[i_87_] = buffer_26_.getUnsignedShort();
					else
						anIntArray2588[i_87_] = buffer_26_.getMedium();

					anIntArray2593[i_87_] = buffer_26_.getUnsignedShort();
				}
				else
				{
					anIntArray2603[i_87_] = buffer_26_.getMedium();
					anIntArray2588[i_87_] = buffer_26_.getMedium();
					anIntArray2593[i_87_] = buffer_26_.getMedium();
				}
				aByteArray2609[i_87_] = buffer_27_.get();
				aByteArray2585[i_87_] = buffer_28_.get();
				anIntArray2617[i_87_] = buffer_29_.getUnsigned();
			}
			if (i_88_ == 2)
			{
				texTrianglePointsX[i_87_] = (short) buffer_25_.getUnsignedShort();
				texTrianglePointsY[i_87_] = (short) buffer_25_.getUnsignedShort();
				texTrianglePointsZ[i_87_] = (short) buffer_25_.getUnsignedShort();
				if (version < 15)
				{
					anIntArray2603[i_87_] = buffer_26_.getUnsignedShort();
					if (version >= 14)
						anIntArray2588[i_87_] = buffer_26_.getMedium();
					else
						anIntArray2588[i_87_] = buffer_26_.getUnsignedShort();

					anIntArray2593[i_87_] = buffer_26_.getUnsignedShort();
				}
				else
				{
					anIntArray2603[i_87_] = buffer_26_.getMedium();
					anIntArray2588[i_87_] = buffer_26_.getMedium();
					anIntArray2593[i_87_] = buffer_26_.getMedium();
				}
				aByteArray2609[i_87_] = buffer_27_.get();
				aByteArray2585[i_87_] = buffer_28_.get();
				anIntArray2617[i_87_] = buffer_29_.get();
				anIntArray2615[i_87_] = buffer_29_.get();
				anIntArray2584[i_87_] = buffer_29_.get();
			}
			if (i_88_ == 3)
			{
				texTrianglePointsX[i_87_] = (short) buffer_25_.getUnsignedShort();
				texTrianglePointsY[i_87_] = (short) buffer_25_.getUnsignedShort();
				texTrianglePointsZ[i_87_] = (short) buffer_25_.getUnsignedShort();
				if (version < 15)
				{
					anIntArray2603[i_87_] = buffer_26_.getUnsignedShort();
					if (version < 14)
						anIntArray2588[i_87_] = buffer_26_.getUnsignedShort();
					else
						anIntArray2588[i_87_] = buffer_26_.getMedium();

					anIntArray2593[i_87_] = buffer_26_.getUnsignedShort();
				}
				else
				{
					anIntArray2603[i_87_] = buffer_26_.getMedium();
					anIntArray2588[i_87_] = buffer_26_.getMedium();
					anIntArray2593[i_87_] = buffer_26_.getMedium();
				}
				aByteArray2609[i_87_] = buffer_27_.get();
				aByteArray2585[i_87_] = buffer_28_.get();
				anIntArray2617[i_87_] = buffer_29_.get();
			}
		}

		buffer.currentOffset = i_71_;
		if (bool_32_)
		{
			int i_89_ = buffer.getUnsigned();
			if (i_89_ > 0)
			{
				for (int i_90_ = 0; i_90_ < i_89_; i_90_++)
				{
					buffer.getUnsignedShort();
					if (i_35_ == -1) {
					} else {
					}

				}

			}
			int i_93_ = buffer.getUnsigned();
			if (i_93_ > 0)
			{
				for (int i_94_ = 0; i_94_ < i_93_; i_94_++)
				{
					buffer.getUnsignedShort();
					buffer.getUnsignedShort();
				}

			}
		}
		if (bool_33_)
		{
			int i_97_ = buffer.getUnsigned();
			if (i_97_ > 0)
			{
				for (int i_98_ = 0; i_98_ < i_97_; i_98_++)
				{
					buffer.getUnsignedShort();
					buffer.getUnsignedShort();
					buffer.getUnsigned();
					buffer.get();
				}

			}
		}
		if (version > 13)
		{
			//method475(0, 6, 0);
			if (version >= 15)
			{
				if (trianglePriorities != null)
					for (int i1 = 0; i1 != numTriangles; ++i1)
						if (trianglePriorities[i1] >= 10)
						{
							trianglePriorities = null;
							break;
						}

				if (trianglePriority > 10 || (trianglePriority == -1 && trianglePriorities == null))
					trianglePriority = 10;

			}
		}
		else
			upscale(2);

		//numTexTriangles = 0;
	}

	private void loadOldModel(byte[] bs)
	{
		version = 12;
		boolean bool = false;
		boolean bool_143_ = false;
		Buffer buffer = new Buffer(bs);
		Buffer buffer_144_ = new Buffer(bs);
		Buffer buffer_145_ = new Buffer(bs);
		Buffer buffer_146_ = new Buffer(bs);
		Buffer buffer_147_ = new Buffer(bs);
		buffer.currentOffset = bs.length - 18;
		numVertices = buffer.getUnsignedShort();
		numTriangles = buffer.getUnsignedShort();
		numTexTriangles = buffer.getUnsigned();
		int i_148_ = buffer.getUnsigned();
		int i_149_ = buffer.getUnsigned();
		if (i_149_ == 0xff)
			i_149_ = -1;

		int i_150_ = buffer.getUnsigned();
		int i_151_ = buffer.getUnsigned();
		int i_152_ = buffer.getUnsigned();
		int i_153_ = buffer.getUnsignedShort();
		int i_154_ = buffer.getUnsignedShort();
		int i_155_ = buffer.getUnsignedShort();
		int i_156_ = buffer.getUnsignedShort();
		int i_157_ = 0;
		int i_158_ = i_157_;
		i_157_ += numVertices;
		int i_159_ = i_157_;
		i_157_ += numTriangles;
		int i_160_ = i_157_;
		if (i_149_ == -1)
			i_157_ += numTriangles;

		int i_161_ = i_157_;
		if (i_151_ == 1)
			i_157_ += numTriangles;

		int i_162_ = i_157_;
		if (i_148_ == 1)
			i_157_ += numTriangles;

		int i_163_ = i_157_;
		if (i_152_ == 1)
			i_157_ += numVertices;

		int i_164_ = i_157_;
		if (i_150_ == 1)
			i_157_ += numTriangles;

		int i_165_ = i_157_;
		i_157_ += i_156_;
		int i_166_ = i_157_;
		i_157_ += numTriangles * 2;
		int i_167_ = i_157_;
		i_157_ += numTexTriangles * 6;
		int i_168_ = i_157_;
		i_157_ += i_153_;
		int i_169_ = i_157_;
		i_157_ += i_154_;
		int i_170_ = i_157_;
		i_157_ += i_155_;
		trianglePriority = (byte) i_149_;
		if (i_149_ == -1)
			trianglePriorities = new byte[numTriangles];

		if (i_151_ == 1)
			triangleSkinValues = new int[numTriangles];

		if (i_152_ == 1)
			vertexSkins = new int[numVertices];

		verticesX = new int[numVertices];
		verticesY = new int[numVertices];
		verticesZ = new int[numVertices];
		trianglePoints1 = new short[numTriangles];
		trianglePoints2 = new short[numTriangles];
		trianglePoints3 = new short[numTriangles];
		colorValues = new short[numTriangles];
		if (numTexTriangles > 0)
		{
			texTrianglePointsY = new short[numTexTriangles];
			texTrianglePointsZ = new short[numTexTriangles];
			texTrianglePointsX = new short[numTexTriangles];
			textureTypes = new byte[numTexTriangles];
		}
		if (i_148_ == 1)
		{
			drawTypes = new byte[numTriangles];
			textureBackgrounds = new short[numTriangles];
			texturePointers = new byte[numTriangles];
		}
		if (i_150_ == 1)
			triangleAlphaValues = new byte[numTriangles];

		buffer.currentOffset = i_158_;
		buffer_144_.currentOffset = i_168_;
		buffer_145_.currentOffset = i_169_;
		buffer_146_.currentOffset = i_170_;
		buffer_147_.currentOffset = i_163_;
		int i_171_ = 0;
		int i_172_ = 0;
		int i_173_ = 0;
		for (int i_174_ = 0; i_174_ < numVertices; i_174_++)
		{
			int i_175_ = buffer.getUnsigned();
			int i_176_ = 0;
			if ((i_175_ & 0x1) != 0)
				i_176_ = buffer_144_.getSignedSmart();

			int i_177_ = 0;
			if ((i_175_ & 0x2) != 0)
				i_177_ = buffer_145_.getSignedSmart();

			int i_178_ = 0;
			if ((i_175_ & 0x4) != 0)
				i_178_ = buffer_146_.getSignedSmart();

			verticesX[i_174_] = i_176_ + i_171_;
			verticesY[i_174_] = i_172_ + i_177_;
			verticesZ[i_174_] = i_173_ + i_178_;
			i_171_ = verticesX[i_174_];
			i_172_ = verticesY[i_174_];
			i_173_ = verticesZ[i_174_];
			if (i_152_ == 1)
				vertexSkins[i_174_] = buffer_147_.getUnsigned();

		}

		buffer.currentOffset = i_166_;
		buffer_144_.currentOffset = i_162_;
		buffer_145_.currentOffset = i_160_;
		buffer_146_.currentOffset = i_164_;
		buffer_147_.currentOffset = i_161_;
		for (int i_179_ = 0; i_179_ < numTriangles; i_179_++)
		{
			colorValues[i_179_] = (short) buffer.getUnsignedShort();
			if (i_148_ == 1)
			{
				int i_180_ = buffer_144_.getUnsigned();
				if ((i_180_ & 0x1) == 0)
					drawTypes[i_179_] = 0;

				else
				{
					drawTypes[i_179_] = (byte) 1;
					bool = true;
				}
				if ((i_180_ & 0x2) != 0)
				{
					texturePointers[i_179_] = (byte) (i_180_ >> 2);
					textureBackgrounds[i_179_] = colorValues[i_179_];
					colorValues[i_179_] = (short) 127;
					if (textureBackgrounds[i_179_] != 0xffff)
						bool_143_ = true;

				}
				else
				{
					texturePointers[i_179_] = -1;
					textureBackgrounds[i_179_] = -1;
				}
			}
			if (i_149_ == -1)
				trianglePriorities[i_179_] = buffer_145_.get();

			if (i_150_ == 1)
				triangleAlphaValues[i_179_] = buffer_146_.get();

			if (i_151_ == 1)
				triangleSkinValues[i_179_] = buffer_147_.getUnsigned();

		}

		maxDepth = -1;
		buffer.currentOffset = i_165_;
		buffer_144_.currentOffset = i_159_;
		short s = 0;
		short s_181_ = 0;
		short s_182_ = 0;
		int i_183_ = 0;
		for (int i_184_ = 0; i_184_ < numTriangles; i_184_++)
		{
			int i_185_ = buffer_144_.getUnsigned();
			if (i_185_ == 1)
			{
				s = (short) (buffer.getSignedSmart() + i_183_);
				i_183_ = s;
				s_181_ = (short) (buffer.getSignedSmart() + i_183_);
				i_183_ = s_181_;
				s_182_ = (short) (buffer.getSignedSmart() + i_183_);
				i_183_ = s_182_;
				trianglePoints1[i_184_] = s;
				trianglePoints2[i_184_] = s_181_;
				trianglePoints3[i_184_] = s_182_;
				if (maxDepth < s)
					maxDepth = s;

				if (maxDepth < s_181_)
					maxDepth = s_181_;

				if (maxDepth < s_182_)
					maxDepth = s_182_;

			}
			if (i_185_ == 2)
			{
				s_181_ = s_182_;
				s_182_ = (short) (buffer.getSignedSmart() + i_183_);
				i_183_ = s_182_;
				trianglePoints1[i_184_] = s;
				trianglePoints2[i_184_] = s_181_;
				trianglePoints3[i_184_] = s_182_;
				if (maxDepth < s_182_)
					maxDepth = s_182_;

			}
			if (i_185_ == 3)
			{
				s = s_182_;
				s_182_ = (short) (buffer.getSignedSmart() + i_183_);
				i_183_ = s_182_;
				trianglePoints1[i_184_] = s;
				trianglePoints2[i_184_] = s_181_;
				trianglePoints3[i_184_] = s_182_;
				if (maxDepth < s_182_)
					maxDepth = s_182_;

			}
			if (i_185_ == 4)
			{
				short s_186_ = s;
				s = s_181_;
				s_182_ = (short) (buffer.getSignedSmart() + i_183_);
				s_181_ = s_186_;
				i_183_ = s_182_;
				trianglePoints1[i_184_] = s;
				trianglePoints2[i_184_] = s_181_;
				trianglePoints3[i_184_] = s_182_;
				if (maxDepth < s_182_)
					maxDepth = s_182_;

			}
		}

		buffer.currentOffset = i_167_;
		maxDepth++;
		for (int i_187_ = 0; i_187_ < numTexTriangles; i_187_++)
		{
			textureTypes[i_187_] = 0;
			texTrianglePointsX[i_187_] = (short) buffer.getUnsignedShort();
			texTrianglePointsY[i_187_] = (short) buffer.getUnsignedShort();
			texTrianglePointsZ[i_187_] = (short) buffer.getUnsignedShort();
		}

		if (texturePointers != null)
		{
			boolean bool_188_ = false;
			for (int i_189_ = 0; i_189_ < numTriangles; i_189_++)
			{
				int i_190_ = texturePointers[i_189_] & 0xff;
				if (i_190_ != 0xff)
					if (texTrianglePointsX[i_190_] != trianglePoints1[i_189_] || trianglePoints2[i_189_] != texTrianglePointsY[i_190_] || trianglePoints3[i_189_] != texTrianglePointsZ[i_190_])
						bool_188_ = true;
					else
						texturePointers[i_189_] = -1;


			}

			if (!bool_188_)
				texturePointers = null;

		}
		if (!bool)
			drawTypes = null;

		if (!bool_143_)
			textureBackgrounds = null;

		upscale(2);
	}


	public Model(Model[] models)
	{
		this(models, 2, true);
		method466();
	}

	public Model(int i, Model[] models)
	{
		this(models, i, false);
	}

	public Model(Model[] models, int count, boolean ex)
	{
		this();
		boolean hasDrawTypes = false;
		boolean hasPriorities = false;
		boolean hasAlphaValues = false;
		boolean hasTexturePointers = false;
		boolean hasTextures = false;
		boolean hasTriangleSkins = false;
		trianglePriority = (byte) -1;
		for (int i = 0; i != count; ++i)
		{
			Model model = models[i];
			if (model != null)
			{
				numTriangles += model.numTriangles;
				numTexTriangles += model.numTexTriangles;
				numVertices += model.numVertices;
				hasDrawTypes = hasDrawTypes | model.drawTypes != null;
				hasAlphaValues = hasAlphaValues | model.triangleAlphaValues != null;
				if (model.trianglePriorities != null)
					hasPriorities = true;

				else
				{
					if (trianglePriority == 1)
						trianglePriority = model.trianglePriority;

					if (model.trianglePriority != trianglePriority)
						hasPriorities = true;

				}
				hasTexturePointers = hasTexturePointers | model.texturePointers != null;
				hasTriangleSkins = hasTriangleSkins | model.triangleSkinValues != null;
				hasTextures = hasTextures | model.textureBackgrounds != null;
			}
		}

		colorValues = new short[numTriangles];
		verticesX = new int[numVertices];
		verticesY = new int[numVertices];
		verticesZ = new int[numVertices];
		trianglePoints1 = new short[numTriangles];
		trianglePoints2 = new short[numTriangles];
		trianglePoints3 = new short[numTriangles];
		vertexSkins = new int[numVertices];
		if (hasDrawTypes)
			drawTypes = new byte[numTriangles];

		//aShortArray2582 = new short[numVertices];
		//aShortArray2575 = new short[numTriangles];
		if (hasTriangleSkins)
			triangleSkinValues = new int[numTriangles];

		if (ex)
		{
			anIntArray1634 = new int[numTriangles];
			anIntArray1635 = new int[numTriangles];
			anIntArray1636 = new int[numTriangles];
		}
		if (numTexTriangles > 0)
		{
			anIntArray2603 = new int[numTexTriangles];
			anIntArray2588 = new int[numTexTriangles];
			anIntArray2593 = new int[numTexTriangles];
			aByteArray2609 = new byte[numTexTriangles];
			aByteArray2585 = new byte[numTexTriangles];
			anIntArray2617 = new int[numTexTriangles];
			anIntArray2615 = new int[numTexTriangles];
			anIntArray2584 = new int[numTexTriangles];
			texTrianglePointsX = new short[numTexTriangles];
			texTrianglePointsY = new short[numTexTriangles];
			texTrianglePointsZ = new short[numTexTriangles];
			textureTypes = new byte[numTexTriangles];
		}
		if (hasTexturePointers)
			texturePointers = new byte[numTriangles];

		if (hasTextures)
			textureBackgrounds = new short[numTriangles];

		if (hasAlphaValues)
			triangleAlphaValues = new byte[numTriangles];

		if (hasPriorities)
			trianglePriorities = new byte[numTriangles];

		numTexTriangles = 0;
		numTriangles = 0;
		numVertices = 0;
		int[] offsets = null;//ex ? new int[count]:null;
		for (int i = 0; i != count; ++i)
		{
			short flag = (short) (1 << i);
			Model model = models[i];
			if (model != null)
			{
				int start = -1;
				if (offsets != null)
				{
					start = offsets[i] = numVertices;
					for (int i1 = 0; i1 != model.numVertices; ++i1)
					{
						verticesX[numVertices] = model.verticesX[i1];
						verticesY[numVertices] = model.verticesY[i1];
						verticesZ[numVertices] = model.verticesZ[i1];
						//aShortArray2582[numVertices] = flag;
						vertexSkins[numVertices] = model.vertexSkins != null ? model.vertexSkins[i1]:-1;
						numVertices++;
					}

				}
				for (int i1 = 0; i1 != model.numTriangles; ++i1)
				{
					if (hasDrawTypes && model.drawTypes != null)
						drawTypes[numTriangles] = model.drawTypes[i1];

					if (hasPriorities)
						if (model.trianglePriorities != null)
							trianglePriorities[numTriangles] = model.trianglePriorities[i1];
						else
							trianglePriorities[numTriangles] = model.trianglePriority;


					if (hasAlphaValues && model.triangleAlphaValues != null)
						triangleAlphaValues[numTriangles] = model.triangleAlphaValues[i1];

					if (hasTextures)
						if (model.textureBackgrounds == null)
							textureBackgrounds[numTriangles] = (short) -1;
						else 
							textureBackgrounds[numTriangles] = model.textureBackgrounds[i1];


					if (hasTriangleSkins)
						if (model.triangleSkinValues == null)
							triangleSkinValues[numTriangles] = -1;
						else
							triangleSkinValues[numTriangles] = model.triangleSkinValues[i1];


					if (ex)
					{
						anIntArray1634[numTriangles] = model.anIntArray1634[i1];
						anIntArray1635[numTriangles] = model.anIntArray1635[i1];
						anIntArray1636[numTriangles] = model.anIntArray1636[i1];
					}
					if (start != -1)
					{
						trianglePoints1[numTriangles] = (short) ((model.trianglePoints1[i1] & 0xffff) + start);
						trianglePoints2[numTriangles] = (short) ((model.trianglePoints2[i1] & 0xffff) + start);
						trianglePoints3[numTriangles] = (short) ((model.trianglePoints3[i1] & 0xffff) + start);
					}
					else
					{
						trianglePoints1[numTriangles] = (short) copyVertex(model, model.trianglePoints1[i1] & 0xffff, flag);
						trianglePoints2[numTriangles] = (short) copyVertex(model, model.trianglePoints2[i1] & 0xffff, flag);
						trianglePoints3[numTriangles] = (short) copyVertex(model, model.trianglePoints3[i1] & 0xffff, flag);
					}
					//aShortArray2575[numTriangles] = flag;
					colorValues[numTriangles] = model.colorValues[i1];
					numTriangles++;
				}


			}
		}

		int triangle = 0;
		for (int i = 0; i != count; ++i)
		{
			short flag = (short) (1 << i);
			Model model = models[i];
			if (model != null)
			{
				if (hasTexturePointers)
					for (int i1 = 0; i1 != model.numTriangles; ++i1)
						texturePointers[triangle++] = (byte) (model.texturePointers == null || model.texturePointers[i1] == -1 ? -1:(model.texturePointers[i1] & 0xff) + numTexTriangles);


				int start = offsets != null ? offsets[i]:-1;
				for (int i1 = 0; i1 != model.numTexTriangles; ++i1)
				{
					byte b = textureTypes[numTexTriangles] = model.textureTypes[i1];
					if (b == 0)
					{
						if (start != -1)
						{
							texTrianglePointsX[numTexTriangles] = (short) ((model.texTrianglePointsX[i1] & 0xffff) + start);
							texTrianglePointsY[numTexTriangles] = (short) ((model.texTrianglePointsY[i1] & 0xffff) + start);
							texTrianglePointsZ[numTexTriangles] = (short) ((model.texTrianglePointsZ[i1] & 0xffff) + start);
						}
						else
						{
							texTrianglePointsX[numTexTriangles] = (short) copyVertex(model, model.texTrianglePointsX[i1] & 0xffff, flag);
							texTrianglePointsY[numTexTriangles] = (short) copyVertex(model, model.texTrianglePointsY[i1] & 0xffff, flag);
							texTrianglePointsZ[numTexTriangles] = (short) copyVertex(model, model.texTrianglePointsZ[i1] & 0xffff, flag);
						}
					}
					if (b >= 1 && b <= 3)
					{
						/*if (start != -1)
						{
							//Should I do this?
							texTrianglePointsX[numTexTriangles] = (short) ((model.texTrianglePointsX[i1] & 0xffff) + start);
							texTrianglePointsY[numTexTriangles] = (short) ((model.texTrianglePointsY[i1] & 0xffff) + start);
							texTrianglePointsZ[numTexTriangles] = (short) ((model.texTrianglePointsZ[i1] & 0xffff) + start);
						}
						else
						{*/
							texTrianglePointsX[numTexTriangles] = model.texTrianglePointsX[i1];
							texTrianglePointsY[numTexTriangles] = model.texTrianglePointsY[i1];
							texTrianglePointsZ[numTexTriangles] = model.texTrianglePointsZ[i1];
						//}
						anIntArray2603[numTexTriangles] = model.anIntArray2603[i1];
						anIntArray2588[numTexTriangles] = model.anIntArray2588[i1];
						anIntArray2593[numTexTriangles] = model.anIntArray2593[i1];
						aByteArray2609[numTexTriangles] = model.aByteArray2609[i1];
						aByteArray2585[numTexTriangles] = model.aByteArray2585[i1];
						anIntArray2617[numTexTriangles] = model.anIntArray2617[i1];
					}
					if (b == 2)
					{
						anIntArray2615[numTexTriangles] = model.anIntArray2615[i1];
						anIntArray2584[numTexTriangles] = model.anIntArray2584[i1];
					}
					numTexTriangles++;
				}

			}
		}

		maxDepth = numVertices;
	}
	
	private int copyVertex(Model model, int index, short id)
	{
		int x = model.verticesX[index];
		int y = model.verticesY[index];
		int z = model.verticesZ[index];
		for (int i = 0; numVertices > i; i++)
			if (verticesX[i] == x && verticesY[i] == y && z == verticesZ[i])
			{
				//aShortArray2582[i] |= id;
				return i;
			}

		verticesX[numVertices] = x;
		verticesY[numVertices] = y;
		verticesZ[numVertices] = z;
		//aShortArray2582[numVertices] = id;
		vertexSkins[numVertices] = model.vertexSkins != null ? model.vertexSkins[index] : -1;
		return numVertices++;
	}

	public Model(boolean flag, boolean flag1, Model model)
	{
		this();
		version = model.version;
		numVertices = model.numVertices;
		verticesX = model.verticesX;
		if (flag)
		{
			verticesY = new int[numVertices];
			for (int i = 0; i != numVertices; ++i)
				verticesY[i] = model.verticesY[i];

		}
		else
		{
			verticesY = model.verticesY;
		}
		verticesZ = model.verticesZ;
		maxDepth = model.maxDepth;
		vertexSkins = model.vertexSkins;
		triangleSkinValues = model.triangleSkinValues;
		numTexTriangles = model.numTexTriangles;
		texTrianglePointsX = model.texTrianglePointsX;
		texTrianglePointsY = model.texTrianglePointsY;
		texTrianglePointsZ = model.texTrianglePointsZ;
		textureBackgrounds = model.textureBackgrounds;
		textureTypes = model.textureTypes;
		numTriangles = model.numTriangles;
		trianglePoints1 = model.trianglePoints1;
		trianglePoints2 = model.trianglePoints2;
		trianglePoints3 = model.trianglePoints3;
		colorValues = model.colorValues;
		trianglePriority = model.trianglePriority;
		trianglePriorities = model.trianglePriorities;
		triangleAlphaValues = model.triangleAlphaValues;
		if (flag1)
		{
			anIntArray1634 = new int[numTriangles];
			anIntArray1635 = new int[numTriangles];
			anIntArray1636 = new int[numTriangles];
			for(int i = 0; i != numTriangles; ++i)
			{
				anIntArray1634[i] = model.anIntArray1634[i];
				anIntArray1635[i] = model.anIntArray1635[i];
				anIntArray1636[i] = model.anIntArray1636[i];
			}

			drawTypes = new byte[numTriangles];
			if (model.drawTypes != null)
				for (int i = 0; i != numTriangles; ++i)
					drawTypes[i] = model.drawTypes[i];


			if (model.aClass33Array1425 != null)
			{
				aClass33Array1425 = new Class33[numVertices];
				for (int i = 0; i != numVertices; ++i)
				{
					Class33 class33 = aClass33Array1425[i] = new Class33();
					Class33 class33_1 = model.aClass33Array1425[i];
					class33.anInt602 = class33_1.anInt602;
					class33.anInt603 = class33_1.anInt603;
					class33.anInt604 = class33_1.anInt604;
					class33.anInt605 = class33_1.anInt605;
				}

				aClass33Array1660 = model.aClass33Array1660;
			}
		}
		else
		{
			anIntArray1634 = model.anIntArray1634;
			anIntArray1635 = model.anIntArray1635;
			anIntArray1636 = model.anIntArray1636;
			drawTypes = model.drawTypes;
		}
		anIntArray2603 = model.anIntArray2603;
		aByteArray2609 = model.aByteArray2609;
		anIntArray2615 = model.anIntArray2615;
		anIntArray2584 = model.anIntArray2584;
		anIntArray2588 = model.anIntArray2588;
		anIntArray2593 = model.anIntArray2593;
		anIntArray2617 = model.anIntArray2617;
		aByteArray2585 = model.aByteArray2585;
		aShortArray2575 = model.aShortArray2575;
		aShortArray2582 = model.aShortArray2582;
		super.modelHeight = model.modelHeight;
		shadowIntensity = model.shadowIntensity;
		anInt1653 = model.anInt1653;
		anInt1652 = model.anInt1652;
		anInt1646 = model.anInt1646;
		anInt1648 = model.anInt1648;
		anInt1649 = model.anInt1649;
		anInt1647 = model.anInt1647;
	}

	//TODO: redo null vertex array copying differently
	public Model(int flags, Model model)
	{
		this();
		version = model.version;
		numVertices = model.numVertices;
		if ((flags & 0x4) == 0)
		{
			verticesX = model.verticesX;
			if ((flags & 0x10) == 0)
				verticesY = model.verticesY;

			else
			{
				verticesY = new int[numVertices];
				if (model.verticesY != null)
					for (int i = 0; i != numVertices; ++i)
						verticesY[i] = model.verticesY[i];


			}
			verticesZ = model.verticesZ;
		}
		else
		{
			verticesX = new int[numVertices];
			verticesY = new int[numVertices];
			verticesZ = new int[numVertices];
			for(int i = 0; i != numVertices; ++i)
			{
				if (model.verticesX != null)
					verticesX[i] = model.verticesX[i];

				if (model.verticesY != null)
					verticesY[i] = model.verticesY[i];

				if (model.verticesZ != null)
					verticesZ[i] = model.verticesZ[i];

			}

		}
		maxDepth = model.maxDepth;
		triangleSkinValues = model.triangleSkinValues;
		vertexSkins = model.vertexSkins;
		numTexTriangles = model.numTexTriangles;
		texTrianglePointsX = model.texTrianglePointsX;
		texTrianglePointsY = model.texTrianglePointsY;
		texTrianglePointsZ = model.texTrianglePointsZ;
		textureBackgrounds = model.textureBackgrounds;
		textureTypes = model.textureTypes;
		numTriangles = model.numTriangles;
		trianglePoints1 = model.trianglePoints1;
		trianglePoints2 = model.trianglePoints2;
		trianglePoints3 = model.trianglePoints3;
		if ((flags & 0x2) == 0)
			colorValues = model.colorValues;

		else
		{
			colorValues = new short[numTriangles];
			if (model.colorValues != null)
				for(int i = 0; i != numTriangles; ++i)
					colorValues[i] = model.colorValues[i];


		}
		if ((flags & 0x8) == 0) //TODO: check for this flag. -> textureBgs 0x8
			textureBackgrounds = model.textureBackgrounds;

		else
		{
			textureBackgrounds = new short[numTriangles];
			if (model.textureBackgrounds != null)
				for (int i = 0; i != numTriangles; ++i)
					textureBackgrounds[i] = model.textureBackgrounds[i];


		}
		if ((flags & 0x1) == 0)
			triangleAlphaValues = model.triangleAlphaValues;

		else
		{
			triangleAlphaValues = new byte[numTriangles];
			if (model.triangleAlphaValues != null)
				for(int i = 0; i != numTriangles; ++i)
					triangleAlphaValues[i] = model.triangleAlphaValues[i];


		}
		trianglePriority = model.trianglePriority;
		trianglePriorities = model.trianglePriorities;
		drawTypes = model.drawTypes;
		anIntArray2603 = model.anIntArray2603;
		anIntArray2615 = model.anIntArray2615;
		aByteArray2609 = model.aByteArray2609;
		anIntArray2584 = model.anIntArray2584;
		anIntArray2588 = model.anIntArray2588;
		anIntArray2593 = model.anIntArray2593;
		anIntArray2617 = model.anIntArray2617;
		aByteArray2585 = model.aByteArray2585;
		aShortArray2575 = model.aShortArray2575;
		aShortArray2582 = model.aShortArray2582;
	}

	public void method464(Model model, int flags)
	{
		version = model.version;
		numVertices = model.numVertices;
		if ((flags & 0x4) == 0)
		{
			verticesX = model.verticesX;
			if ((flags & 0x10) == 0)
				verticesY = model.verticesY;

			else
			{
				if(anIntArray1623.length < numVertices)
					anIntArray1623 = new int[Math.max(anIntArray1623.length * 2, numTriangles)];

				verticesY = anIntArray1623;
				if (model.verticesY != null)
					for (int i = 0; i != numVertices; ++i)
						verticesY[i] = model.verticesY[i];


			}
			verticesZ = model.verticesZ;
		}
		else
		{
			if(anIntArray1622.length < numVertices)
			{
				anIntArray1622 = new int[Math.max(anIntArray1622.length * 2, numTriangles)];
				anIntArray1623 = new int[Math.max(anIntArray1623.length * 2, numTriangles)];
				anIntArray1624 = new int[Math.max(anIntArray1624.length * 2, numTriangles)];
			}
			verticesX = anIntArray1622;
			verticesY = anIntArray1623;
			verticesZ = anIntArray1624;
			for (int i = 0; i != numVertices; ++i)
			{
				if (model.verticesX != null)
					verticesX[i] = model.verticesX[i];

				if (model.verticesY != null)
					verticesY[i] = model.verticesY[i];

				if (model.verticesZ != null)
					verticesZ[i] = model.verticesZ[i];

			}

		}
		maxDepth = model.maxDepth;
		vertexSkins = model.vertexSkins;
		triangleSkinValues = model.triangleSkinValues;
		numTexTriangles = model.numTexTriangles;
		texTrianglePointsX = model.texTrianglePointsX;
		texTrianglePointsY = model.texTrianglePointsY;
		texTrianglePointsZ = model.texTrianglePointsZ;
		textureBackgrounds = model.textureBackgrounds;
		textureTypes = model.textureTypes;
		numTriangles = model.numTriangles;
		trianglePoints1 = model.trianglePoints1;
		trianglePoints2 = model.trianglePoints2;
		trianglePoints3 = model.trianglePoints3;
		if ((flags & 0x1) == 0)
			triangleAlphaValues = model.triangleAlphaValues;

		else
		{
			if(anIntArray1625.length < numTriangles)
				anIntArray1625 = new byte[Math.max(anIntArray1625.length * 2, numTriangles)];

			triangleAlphaValues = anIntArray1625;
			if (model.triangleAlphaValues != null)
				for (int i = 0; i != numTriangles; ++i)
					triangleAlphaValues[i] = model.triangleAlphaValues[i];


		}
		if ((flags & 0x2) == 0)
			colorValues = model.colorValues;

		else
		{
			if(anIntArray1626.length < numTriangles)
				anIntArray1626 = new short[Math.max(anIntArray1625.length * 2, numTriangles)];

			colorValues = anIntArray1626;
			if (model.colorValues != null)
				for (int i = 0; i != numTriangles; ++i)
					colorValues[i] = model.colorValues[i];


		}
		trianglePriority = model.trianglePriority;
		trianglePriorities = model.trianglePriorities;
		drawTypes = model.drawTypes;
		anIntArray2603 = model.anIntArray2603;
		aByteArray2609 = model.aByteArray2609;
		anIntArray2615 = model.anIntArray2615;
		anIntArray2584 = model.anIntArray2584;
		anIntArray2588 = model.anIntArray2588;
		anIntArray2593 = model.anIntArray2593;
		anIntArray2617 = model.anIntArray2617;
		aByteArray2585 = model.aByteArray2585;
		aShortArray2575 = model.aShortArray2575;
		aShortArray2582 = model.aShortArray2582;
		anIntArray1634 = model.anIntArray1634;
		anIntArray1635 = model.anIntArray1635;
		anIntArray1636 = model.anIntArray1636;
		triangleSkin = model.triangleSkin;
		vectorSkin = model.vectorSkin;
	}

	public void method466()
	{
		super.modelHeight = 0;
		shadowIntensity = 0;
		anInt1651 = 0;
		for(int i = 0; i < numVertices; i++)
		{
			int j = (verticesX[i] + 1) >> 2;
			int k = (verticesY[i] + 1) >> 2;
			int l = (verticesZ[i] + 1) >> 2;
			if(-k > super.modelHeight)
				super.modelHeight = -k;

			if(k > anInt1651)
				anInt1651 = k;

			int i1 = j * j + l * l;
			if(i1 > shadowIntensity)
				shadowIntensity = i1;

		}

		shadowIntensity = (int)(Math.sqrt(shadowIntensity) + 0.98999999999999999D);
		anInt1653 = (int)(Math.sqrt(shadowIntensity * shadowIntensity + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
		anInt1652 = anInt1653 + (int)(Math.sqrt(shadowIntensity * shadowIntensity + anInt1651 * anInt1651) + 0.98999999999999999D);
	}

	public void normalize()
	{
		super.modelHeight = 0;
		anInt1651 = 0;
		for(int i = 0; i < numVertices; i++)
		{
			int j = (verticesY[i] + 1) >> 2;
			if(-j > super.modelHeight)
				super.modelHeight = -j;

			if(j > anInt1651)
				anInt1651 = j;

		}

		anInt1653 = (int)(Math.sqrt(shadowIntensity * shadowIntensity + super.modelHeight * super.modelHeight) + 0.98999999999999999D);
		anInt1652 = anInt1653 + (int)(Math.sqrt(shadowIntensity * shadowIntensity + anInt1651 * anInt1651) + 0.98999999999999999D);
	}

	public void method468(int i)
	{
		super.modelHeight = 0;
		shadowIntensity = 0;
		anInt1651 = 0;
		anInt1646 = 0xf423f;
		anInt1647 = 0xfff0bdc1;
		anInt1648 = 0xfffe7961;
		anInt1649 = 0x1869f;
		for(int j = 0; j < numVertices; j++)
		{
			int k = (verticesX[j] + 1) >> 2;
			int l = (verticesY[j] + 1) >> 2;
			int i1 = (verticesZ[j] + 1) >> 2;
			if(k < anInt1646)
				anInt1646 = k;
			if(k > anInt1647)
				anInt1647 = k;
			if(i1 < anInt1649)
				anInt1649 = i1;
			if(i1 > anInt1648)
				anInt1648 = i1;
			if(-l > super.modelHeight)
				super.modelHeight = -l;
			if(l > anInt1651)
				anInt1651 = l;
			int j1 = k * k + i1 * i1;
			if(j1 > shadowIntensity)
				shadowIntensity = j1;
		}

		shadowIntensity = (int)Math.sqrt(shadowIntensity);
		anInt1653 = (int)Math.sqrt(shadowIntensity * shadowIntensity + super.modelHeight * super.modelHeight);
		if(i != 21073)
		{
			return;
		} else
		{
			anInt1652 = anInt1653 + (int)Math.sqrt(shadowIntensity * shadowIntensity + anInt1651 * anInt1651);
			return;
		}
	}

	public void createBones()
	{
		if (vertexSkins != null)
			vectorSkin = getVertexSkins(true);
		else
			vectorSkin = null;

		if (triangleSkinValues != null)
			triangleSkin = getTriangleSkins();
		else
			triangleSkin = null;

	}

	public void applyTransformation(int i, Animation def)
	{
		method634(1, i, 0, null, def != null ? def.anIntArray355[0]:0, def != null ? def.aBoolean691:false, -1, 0);
	}

	public static int getFlags(int i, Animation def)
	{
		return getFlags(1, i, 0, null, def != null ? def.anIntArray355[0]:0, def != null ? def.aBoolean691:false, -1, 0);
	}

	private boolean upscaled;
	private boolean aBoolean5734;

	private void downscale()//where is your seq I don't know galkon names h
	{
		if (!upscaled)
			return;

		upscaled = false;
		for (int i = 0; i != numVertices; ++i)
		{
			verticesX[i] = (verticesX[i] + 7) >> 4;
			verticesY[i] = (verticesY[i] + 7) >> 4;
			verticesZ[i] = (verticesZ[i] + 7) >> 4;
		}

	}

	//error causing invisble frames when trying to animate both
	public void method471(Animation def, int i, int j, boolean carry)
	{
		int[] ai = carry && def != null ? def.anIntArray357:null;
		method634(1, j, 0, ai, def != null ? def.anIntArray355[0]:0, def != null ? def.aBoolean691:false, i, 0);
	}

	public static int getFlags(Animation def, int i, int j, boolean carry)
	{
		int[] ai = carry && def != null ? def.anIntArray357:null;
		return getFlags(1, j, 0, ai, def != null ? def.anIntArray355[0]:0, def != null ? def.aBoolean691:false, i, 0);
	}

	public static int getFlags(int i, int i_101_, int i_102_, int[] is, int i_103_, boolean bool, int i_104_, int i_105_)
	{
		if (i_101_ == -1)
			return 0;

		FrameReader skeleton = FrameReader.get(i_101_);
		if (skeleton == null)
			return 0;

		SkinList skin = skeleton.skin;
		FrameReader FrameReader_106_ = null;
		if (i_104_ != -1)
		{
			FrameReader_106_ = FrameReader.get(i_104_);
			if (FrameReader_106_ != null && FrameReader_106_.skin != skin)
				FrameReader_106_ = null;

		}
		return getFlags(FrameReader_106_, null, i, false, is, i_102_, i_103_, skeleton, skin, i_105_, bool);
	}

	//0: ?
	//1: index1
	//2: ?
	//3: sorting
	//4: ?
	//5: ?
	//6: index2
	//7: ?
	public void method634(int i, int i_101_, int i_102_, int[] is, int i_103_, boolean bool, int i_104_, int i_105_)
	{
		if (i_101_ == -1)
			return;

		if (vectorSkin == null)
			return;

		FrameReader skeleton = FrameReader.get(i_101_);
		if (skeleton == null)
			return;

		SkinList skin = skeleton.skin;
		FrameReader FrameReader_106_ = null;
		if (i_104_ != -1)
		{
			FrameReader_106_ = FrameReader.get(i_104_);
			if (FrameReader_106_ != null && FrameReader_106_.skin != skin)
				FrameReader_106_ = null;

		}
		anInt1681 = 0;
		anInt1682 = 0;
		anInt1683 = 0;
		method635(FrameReader_106_, null, i, false, is, i_102_, i_103_, skeleton, skin, i_105_, bool);
		downscale();
	}

	private static int getFlags(int type)
	{
		if (type == 5)
			return 0x1;

		if (type == 7)
			return 0x2;

		if (type == 0 || type == 1 || type == 2 || type == 3)
			return 0x4;

		return 0;
	}

	//DISABLE DOUBLE ANIMS!!!!
	private static int getFlags(FrameReader FrameReader, boolean[] bools, int i, boolean bool, int[] is, int i_107_, int i_108_, FrameReader FrameReader_109_, SkinList skin, int i_110_, boolean bool_111_)
	{
		int flags = 0;
		if (FrameReader == null || i == 0)
		{
			for (int i_112_ = 0; i_112_ < FrameReader_109_.vertexCount; i_112_++)
			{
				short s = FrameReader_109_.vertexIndices[i_112_];
				if (bools == null || !bool != bools[s] || skin.anIntArray342[s] == 0)
				{
					if (FrameReader_109_.vertexSkins[i_112_] != -1)
						flags |= getFlags(0);

					flags |= getFlags(skin.anIntArray342[s]);
				}
			}

		}
		else
		{
			if (true)
				return 0;

			@SuppressWarnings("unused")
			int i_114_ = 0;
			int i_115_ = 0;
			for (int i_116_ = 0; i_116_ < skin.anIntArray342.length; i_116_++)
			{
				boolean bool_117_ = false;
				if (FrameReader_109_.vertexCount > i_114_ && i_116_ == FrameReader_109_.vertexIndices[i_114_])
					bool_117_ = true;

				boolean bool_118_ = false;
				if (FrameReader.vertexCount > i_115_ && i_116_ == FrameReader.vertexIndices[i_115_])
					bool_118_ = true;

				if (bool_117_ || bool_118_)
				{
					if (bools != null && !bools[i_116_] == bool && skin.anIntArray342[i_116_] != 0)
					{
						if (bool_118_) 
							i_115_++;

						if (bool_117_)
							i_114_++;

					}
					else
					{
						int i_119_ = 0;
						int i_120_ = skin.anIntArray342[i_116_];
						if (i_120_ == 3 || i_120_ == 10)
							i_119_ = 128;

						short s;
						if (bool_117_)
						{
							s = FrameReader_109_.vertexSkins[i_114_];
							i_114_++;
						}
						else
						{
							s = (short) -1;
						}
						short s_128_;
						if (bool_118_)
						{
							s_128_ = FrameReader.vertexSkins[i_115_];
							i_115_++;
						}
						else
						{
							s_128_ = (short) -1;
						}
						if (s == -1)
						{
							if (s_128_ != -1)
								flags |= getFlags(0);

						}
						else
							flags |= getFlags(0);

						flags |= getFlags(i_120_);
					}
				}
			}
		}
		return flags;
	}
	
	private final void method635(FrameReader FrameReader, boolean[] bools, int i, boolean bool, int[] is, int i_107_, int i_108_, FrameReader FrameReader_109_, SkinList skin, int i_110_, boolean bool_111_)
	{
		if (FrameReader == null || i == 0)
		{
			for (int i_112_ = 0; i_112_ < FrameReader_109_.vertexCount; i_112_++)
			{
				short s = FrameReader_109_.vertexIndices[i_112_];
				if (bools == null || !bool != bools[s] || skin.anIntArray342[s] == 0)
				{
					short s_113_ = FrameReader_109_.vertexSkins[i_112_];
					if (s_113_ != -1)
						method613(0, 0, i_110_ & skin.anIntArray7510[s_113_], skin.anIntArrayArray343[s_113_], is, bool_111_, i_107_, 0, 0);

					method613(FrameReader_109_.vertexZ[i_112_], skin.anIntArray342[s], i_110_ & skin.anIntArray7510[s], skin.anIntArrayArray343[s], is, bool_111_, i_107_, FrameReader_109_.vertexX[i_112_], FrameReader_109_.vertexY[i_112_]);
				}
			}

		}
		else
		{
			if (true)
				return;

			@SuppressWarnings("unused")
			int i_114_ = 0;
			int i_115_ = 0;
			for (int i_116_ = 0; i_116_ < skin.anIntArray342.length; i_116_++)
			{
				boolean bool_117_ = false;
				if (FrameReader_109_.vertexCount > i_114_ && i_116_ == FrameReader_109_.vertexIndices[i_114_])
					bool_117_ = true;

				boolean bool_118_ = false;
				if (FrameReader.vertexCount > i_115_ && i_116_ == FrameReader.vertexIndices[i_115_])
					bool_118_ = true;

				if (bool_117_ || bool_118_)
				{
					if (bools != null && !bools[i_116_] == bool && skin.anIntArray342[i_116_] != 0)
					{
						if (bool_118_) 
							i_115_++;

						if (bool_117_)
							i_114_++;

					}
					else
					{
						int i_119_ = 0;
						int i_120_ = skin.anIntArray342[i_116_];
						if (i_120_ == 3 || i_120_ == 10)
							i_119_ = 128;

						byte b_121_;
						int i_122_;
						int i_123_;
						short s;
						int i_124_;
						if (bool_117_)
						{
							i_122_ = FrameReader_109_.vertexZ[i_114_];
							s = FrameReader_109_.vertexSkins[i_114_];
							i_124_ = FrameReader_109_.vertexY[i_114_];
							i_123_ = FrameReader_109_.vertexX[i_114_];
							b_121_ = FrameReader_109_.vertexFlags[i_114_];
							i_114_++;
						}
						else
						{
							b_121_ = (byte) 0;
							i_122_ = i_119_;
							i_123_ = i_119_;
							s = (short) -1;
							i_124_ = i_119_;
						}
						int i_125_;
						int i_126_;
						byte b_127_;
						short s_128_;
						int i_129_;
						if (bool_118_)
						{
							i_129_ = FrameReader.vertexX[i_115_];
							i_126_ = FrameReader.vertexZ[i_115_];
							b_127_ = FrameReader.vertexFlags[i_115_];
							i_125_ = FrameReader.vertexY[i_115_];
							s_128_ = FrameReader.vertexSkins[i_115_];
							i_115_++;
						}
						else
						{
							i_125_ = i_119_;
							i_126_ = i_119_;
							b_127_ = (byte) 0;
							s_128_ = (short) -1;
							i_129_ = i_119_;
						}
						if (s == -1)
						{
							if (s_128_ != -1)
								method613(0, 0, i_110_ & skin.anIntArray7510[s_128_], skin.anIntArrayArray343[s_128_], is, bool_111_, i_107_, 0, 0);

						}
						else
							method613(0, 0, i_110_ & skin.anIntArray7510[s], skin.anIntArrayArray343[s], is, bool_111_, i_107_, 0, 0);

						int i_130_;
						int i_131_;
						int i_132_;
						if ((b_121_ & 0x2) == 0 && (0x1 & b_127_) == 0)
						{
							if (i_120_ == 2)
							{
								int i_133_ = i_129_ - i_123_ & 0x3fff;
								int i_134_ = i_125_ - i_124_ & 0x3fff;
								int i_135_ = i_126_ - i_122_ & 0x3fff;
								if (i_133_ >= 8192)
									i_133_ -= 16384;

								if (i_134_ >= 8192)
									i_134_ -= 16384;

								i_130_ = i_134_ * i / i_108_ + i_124_ & 0x3fff;
								i_131_ = i_133_ * i / i_108_ + i_123_ & 0x3fff;
								if (i_135_ >= 8192)
									i_135_ -= 16384;

								i_132_ = i * i_135_ / i_108_ + i_122_ & 0x3fff;
							}
							else if (i_120_ != 9)
							{
								if (i_120_ != 7)
								{
									i_132_ = (i_126_ - i_122_) * i / i_108_ + i_122_;
									i_130_ = (i_125_ - i_124_) * i / i_108_ + i_124_;
									i_131_ = (i_129_ - i_123_) * i / i_108_ + i_123_;
								}
								else
								{
									int i_136_ = i_129_ - i_123_ & 0x3f;
									if (i_136_ >= 32)
										i_136_ -= 64;

									i_132_ = (i_126_ - i_122_) * i / i_108_ + i_122_;
									i_130_ = (i_125_ - i_124_) * i / i_108_ + i_124_;
									i_131_ = (i_136_ / i_108_) * i + i_123_ & 0x3f;
								}
							}
							else
							{
								int i_137_ = (i_129_ - i_123_) & 0x3fff;
								if (i_137_ >= 8192)
									i_137_ -= 16384;

								i_130_ = i_132_ = 0;
								i_131_ = i_123_ + (i * i_137_ / i_108_) & 0x3fff;
							}
						}
						else
						{
							i_130_ = i_124_;
							i_131_ = i_123_;
							i_132_ = i_122_;
						}
						method613(i_132_, i_120_, skin.anIntArray7510[i_116_] & i_110_, skin.anIntArrayArray343[i_116_], is, bool_111_, i_107_, i_131_, i_130_);
					}
				}
			}
		}
	}

	private void method613(int i, int i_5_, int i_6_, int[] is, int[] is_7_, boolean bool, int i_9_, int i_10_, int i_11_)
	{
		if (i_9_ == 1)
		{
			if (i_5_ == 0 || i_5_ == 1)
			{
				int i_12_ = -i_10_;
				i_10_ = i;
				i = i_12_;
			}
			else if (i_5_ == 3)
			{
				int i_13_ = i_10_;
				i_10_ = i;
				i = i_13_;
			}
			else if (i_5_ == 2)
			{
				int i_14_ = i_10_;
				i_10_ = -i & 0x3fff;
				i = i_14_ & 0x3fff;
			}
		}
		else if (i_9_ == 2)
		{
			if (i_5_ != 0 && i_5_ != 1)
			{
				if (i_5_ == 2)
				{
					i = -i & 0x3fff;
					i_10_ = -i_10_ & 0x3fff;
				}
			}
			else
			{
				i_10_ = -i_10_;
				i = -i;
			}
		}
		else if (i_9_ == 3)
		{
			if (i_5_ == 0 || i_5_ == 1)
			{
				int i_15_ = i_10_;
				i_10_ = -i;
				i = i_15_;
			}
			else if (i_5_ != 3)
			{
				if (i_5_ == 2)
				{
					int i_16_ = i_10_;
					i_10_ = i & 0x3fff;
					i = -i_16_ & 0x3fff;
				}
			}
			else
			{
				int i_17_ = i_10_;
				i_10_ = i;
				i = i_17_;
			}
		}
		if (i_6_ == 0xffff)
			method637(i_5_, is, i_10_, i_11_, i, i_9_, bool);
		else
			I(i_5_, is, i_10_, i_11_, i, bool, i_6_, is_7_);

	}

	private void method637(int i, int[] is, int i_300_, int i_301_, int i_302_, int i_303_, boolean bool)
	{
		int i_304_ = is.length;
		if (i == 0) {
			i_300_ <<= 4;
			i_301_ <<= 4;
			i_302_ <<= 4;
			if (!upscaled) {
				for (int i_305_ = 0; i_305_ < numVertices; i_305_++) {
					verticesX[i_305_] <<= 4;
					verticesY[i_305_] <<= 4;
					verticesZ[i_305_] <<= 4;
				}
				upscaled = true;
			}
			int i_306_ = 0;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			for (int i_307_ = 0; i_307_ < i_304_; i_307_++) {
				int i_308_ = is[i_307_];
				if (i_308_ < vectorSkin.length) {
					int[] is_309_ = vectorSkin[i_308_];
					for (int i_310_ = 0; i_310_ < is_309_.length; i_310_++) {
						int i_311_ = is_309_[i_310_];
						anInt1681 += verticesX[i_311_];
						anInt1682 += verticesY[i_311_];
						anInt1683 += verticesZ[i_311_];
						i_306_++;
					}
				}
			}
			if (i_306_ > 0) {
				anInt1681 = anInt1681 / i_306_ + i_300_;
				anInt1682 = anInt1682 / i_306_ + i_301_;
				anInt1683 = anInt1683 / i_306_ + i_302_;
			} else {
				anInt1681 = i_300_;
				anInt1682 = i_301_;
				anInt1683 = i_302_;
			}
		} else if (i == 1) {
			i_300_ <<= 4;
			i_301_ <<= 4;
			i_302_ <<= 4;
			if (!upscaled) {
				for (int i_312_ = 0; i_312_ < numVertices; i_312_++) {
					verticesX[i_312_] <<= 4;
					verticesY[i_312_] <<= 4;
					verticesZ[i_312_] <<= 4;
				}
				upscaled = true;
			}
			for (int i_313_ = 0; i_313_ < i_304_; i_313_++) {
				int i_314_ = is[i_313_];
				if (i_314_ < vectorSkin.length) {
					int[] is_315_ = vectorSkin[i_314_];
					for (int i_316_ = 0; i_316_ < is_315_.length; i_316_++) {
						int i_317_ = is_315_[i_316_];
						verticesX[i_317_] += i_300_;
						verticesY[i_317_] += i_301_;
						verticesZ[i_317_] += i_302_;
					}
				}
			}
		} else if (i == 2) {
			for (int i_318_ = 0; i_318_ < i_304_; i_318_++) {
				int i_319_ = is[i_318_];
				if (i_319_ < vectorSkin.length) {
					int[] is_320_ = vectorSkin[i_319_];
					if ((i_303_ & 0x1) == 0) {
						for (int i_321_ = 0; i_321_ < is_320_.length; i_321_++) {
							int i_322_ = is_320_[i_321_];
							verticesX[i_322_] -= anInt1681;
							verticesY[i_322_] -= anInt1682;
							verticesZ[i_322_] -= anInt1683;
							if (i_302_ != 0) {
								int i_323_ = ANIM_SINE[i_302_];
								int i_324_ = ANIM_COSINE[i_302_];
								int i_325_ = verticesY[i_322_] * i_323_ + verticesX[i_322_] * i_324_ + 16383 >> 14;
								verticesY[i_322_] = verticesY[i_322_] * i_324_ - verticesX[i_322_] * i_323_ + 16383 >> 14;
								verticesX[i_322_] = i_325_;
							}
							if (i_300_ != 0) {
								int i_326_ = ANIM_SINE[i_300_];
								int i_327_ = ANIM_COSINE[i_300_];
								int i_328_ = verticesY[i_322_] * i_327_ - verticesZ[i_322_] * i_326_ + 16383 >> 14;
								verticesZ[i_322_] = verticesY[i_322_] * i_326_ + verticesZ[i_322_] * i_327_ + 16383 >> 14;
								verticesY[i_322_] = i_328_;
							}
							if (i_301_ != 0) {
								int i_329_ = ANIM_SINE[i_301_];
								int i_330_ = ANIM_COSINE[i_301_];
								int i_331_ = verticesZ[i_322_] * i_329_ + verticesX[i_322_] * i_330_ + 16383 >> 14;
								verticesZ[i_322_] = verticesZ[i_322_] * i_330_ - verticesX[i_322_] * i_329_ + 16383 >> 14;
								verticesX[i_322_] = i_331_;
							}
							verticesX[i_322_] += anInt1681;
							verticesY[i_322_] += anInt1682;
							verticesZ[i_322_] += anInt1683;
						}
					} else {
						for (int i_332_ = 0; i_332_ < is_320_.length; i_332_++) {
							int i_333_ = is_320_[i_332_];
							verticesX[i_333_] -= anInt1681;
							verticesY[i_333_] -= anInt1682;
							verticesZ[i_333_] -= anInt1683;
							if (i_300_ != 0) {
								int i_334_ = ANIM_SINE[i_300_];
								int i_335_ = ANIM_COSINE[i_300_];
								int i_336_ = verticesY[i_333_] * i_335_ - verticesZ[i_333_] * i_334_ + 16383 >> 14;
								verticesZ[i_333_] = verticesY[i_333_] * i_334_ + verticesZ[i_333_] * i_335_ + 16383 >> 14;
								verticesY[i_333_] = i_336_;
							}
							if (i_302_ != 0) {
								int i_337_ = ANIM_SINE[i_302_];
								int i_338_ = ANIM_COSINE[i_302_];
								int i_339_ = verticesY[i_333_] * i_337_ + verticesX[i_333_] * i_338_ + 16383 >> 14;
								verticesY[i_333_] = verticesY[i_333_] * i_338_ - verticesX[i_333_] * i_337_ + 16383 >> 14;
								verticesX[i_333_] = i_339_;
							}
							if (i_301_ != 0) {
								int i_340_ = ANIM_SINE[i_301_];
								int i_341_ = ANIM_COSINE[i_301_];
								int i_342_ = verticesZ[i_333_] * i_340_ + verticesX[i_333_] * i_341_ + 16383 >> 14;
								verticesZ[i_333_] = verticesZ[i_333_] * i_341_ - verticesX[i_333_] * i_340_ + 16383 >> 14;
								verticesX[i_333_] = i_342_;
							}
							verticesX[i_333_] += anInt1681;
							verticesY[i_333_] += anInt1682;
							verticesZ[i_333_] += anInt1683;
						}
					}
				}
			}
		} else if (i == 3) {
			for (int i_343_ = 0; i_343_ < i_304_; i_343_++) {
				int i_344_ = is[i_343_];
				if (i_344_ < vectorSkin.length) {
					int[] is_345_ = vectorSkin[i_344_];
					for (int i_346_ = 0; i_346_ < is_345_.length; i_346_++) {
						int i_347_ = is_345_[i_346_];
						verticesX[i_347_] -= anInt1681;
						verticesY[i_347_] -= anInt1682;
						verticesZ[i_347_] -= anInt1683;
						verticesX[i_347_] = verticesX[i_347_] * i_300_ / 128;
						verticesY[i_347_] = verticesY[i_347_] * i_301_ / 128;
						verticesZ[i_347_] = verticesZ[i_347_] * i_302_ / 128;
						verticesX[i_347_] += anInt1681;
						verticesY[i_347_] += anInt1682;
						verticesZ[i_347_] += anInt1683;
					}
				}
			}
		} else if (i == 5) {
			if (triangleSkin != null && triangleAlphaValues != null) {
				for (int i_348_ = 0; i_348_ < i_304_; i_348_++) {
					int i_349_ = is[i_348_];
					if (i_349_ < triangleSkin.length) {
						int[] is_350_ = triangleSkin[i_349_];
						for (int i_351_ = 0; i_351_ < is_350_.length; i_351_++) {
							int i_352_ = is_350_[i_351_];
							int i_353_ = (triangleAlphaValues[i_352_] & 0xff) + i_300_ * 8;
							if (i_353_ < 0) {
								i_353_ = 0;
							} else if (i_353_ > 255) {
								i_353_ = 255;
							}
							triangleAlphaValues[i_352_] = (byte) i_353_;
						}
					}
				}
			}
		} else if (i == 7) {
			if (triangleSkin != null) {
				for (int i_355_ = 0; i_355_ < i_304_; i_355_++) {
					int i_356_ = is[i_355_];
					if (i_356_ < triangleSkin.length) {
						int[] is_357_ = triangleSkin[i_356_];
						for (int i_358_ = 0; i_358_ < is_357_.length; i_358_++) {
							int i_359_ = is_357_[i_358_];
							int i_360_ = colorValues[i_359_] & 0xffff;
							int i_361_ = i_360_ >> 10 & 0x3f;
							int i_362_ = i_360_ >> 7 & 0x7;
							int i_363_ = i_360_ & 0x7f;
							i_361_ = i_361_ + i_300_ & 0x3f;
							i_362_ += i_301_;
							if (i_362_ < 0) {
								i_362_ = 0;
							} else if (i_362_ > 7) {
								i_362_ = 7;
							}
							i_363_ += i_302_;
							if (i_363_ < 0) {
								i_363_ = 0;
							} else if (i_363_ > 127) {
								i_363_ = 127;
							}
							colorValues[i_359_] = (short) (i_361_ << 10 | i_362_ << 7 | i_363_);
						}
					}
				}
			}
		} else if (i == 8) {
		} else if (i == 10) {
		} else if (i == 9) {
		}
	}

	private void I(int i, int[] is, int i_31_, int i_32_, int i_33_, boolean bool, int i_34_, int[] is_35_) {
		int i_36_ = is.length;
		if (i == 0) {
			i_31_ <<= 4;
			i_32_ <<= 4;
			i_33_ <<= 4;
			if (!upscaled) {
				for (int i_37_ = 0; i_37_ < numVertices; i_37_++) {
					verticesX[i_37_] <<= 4;
					verticesY[i_37_] <<= 4;
					verticesZ[i_37_] <<= 4;
				}
				upscaled = true;
			}
			int i_38_ = 0;
			anInt1681 = 0;
			anInt1682 = 0;
			anInt1683 = 0;
			for (int i_39_ = 0; i_39_ < i_36_; i_39_++) {
				int i_40_ = is[i_39_];
				if (i_40_ < vectorSkin.length) {
					int[] is_41_ = vectorSkin[i_40_];
					for (int i_42_ = 0; i_42_ < is_41_.length; i_42_++) {
						int i_43_ = is_41_[i_42_];
						if (aShortArray2582 == null || (i_34_ & aShortArray2582[i_43_]) != 0) {
							anInt1681 += verticesX[i_43_];
							anInt1682 += verticesY[i_43_];
							anInt1683 += verticesZ[i_43_];
							i_38_++;
						}
					}
				}
			}
			if (i_38_ > 0) {
				anInt1681 = anInt1681 / i_38_ + i_31_;
				anInt1682 = anInt1682 / i_38_ + i_32_;
				anInt1683 = anInt1683 / i_38_ + i_33_;
				aBoolean5734 = true;
			} else {
				anInt1681 = i_31_;
				anInt1682 = i_32_;
				anInt1683 = i_33_;
			}
		} else if (i == 1) {
			if (is_35_ != null) {
				int i_44_ = is_35_[0] * i_31_ + is_35_[1] * i_32_ + is_35_[2] * i_33_ + 8192 >> 14;
				int i_45_ = is_35_[3] * i_31_ + is_35_[4] * i_32_ + is_35_[5] * i_33_ + 8192 >> 14;
				int i_46_ = is_35_[6] * i_31_ + is_35_[7] * i_32_ + is_35_[8] * i_33_ + 8192 >> 14;
				i_31_ = i_44_;
				i_32_ = i_45_;
				i_33_ = i_46_;
			}
			i_31_ <<= 4;
			i_32_ <<= 4;
			i_33_ <<= 4;
			if (!upscaled) {
				for (int i_47_ = 0; i_47_ < numVertices; i_47_++) {
					verticesX[i_47_] <<= 4;
					verticesY[i_47_] <<= 4;
					verticesZ[i_47_] <<= 4;
				}
				upscaled = true;
			}
			for (int i_48_ = 0; i_48_ < i_36_; i_48_++) {
				int i_49_ = is[i_48_];
				if (i_49_ < vectorSkin.length) {
					int[] is_50_ = vectorSkin[i_49_];
					for (int i_51_ = 0; i_51_ < is_50_.length; i_51_++) {
						int i_52_ = is_50_[i_51_];
						if (aShortArray2582 == null || (i_34_ & aShortArray2582[i_52_]) != 0) {
							verticesX[i_52_] += i_31_;
							verticesY[i_52_] += i_32_;
							verticesZ[i_52_] += i_33_;
						}
					}
				}
			}
		} else if (i == 2) {
			if (is_35_ != null) {
				if (!upscaled) {
					for (int i_53_ = 0; i_53_ < numVertices; i_53_++) {
						verticesX[i_53_] <<= 4;
						verticesY[i_53_] <<= 4;
						verticesZ[i_53_] <<= 4;
					}
					upscaled = true;
				}
				int i_54_ = is_35_[9] << 4;
				int i_55_ = is_35_[10] << 4;
				int i_56_ = is_35_[11] << 4;
				int i_57_ = is_35_[12] << 4;
				int i_58_ = is_35_[13] << 4;
				int i_59_ = is_35_[14] << 4;
				if (aBoolean5734) {
					int i_60_ = is_35_[0] * anInt1681 + is_35_[3] * anInt1682 + is_35_[6] * anInt1683 + 8192 >> 14;
					int i_61_ = is_35_[1] * anInt1681 + is_35_[4] * anInt1682 + is_35_[7] * anInt1683 + 8192 >> 14;
					int i_62_ = is_35_[2] * anInt1681 + is_35_[5] * anInt1682 + is_35_[8] * anInt1683 + 8192 >> 14;
					i_60_ += i_57_;
					i_61_ += i_58_;
					i_62_ += i_59_;
					anInt1681 = i_60_;
					anInt1682 = i_61_;
					anInt1683 = i_62_;
					aBoolean5734 = false;
				}
				int[] is_63_ = new int[9];
				int i_64_ = ANIM_COSINE[i_31_];
				int i_65_ = ANIM_SINE[i_31_];
				int i_66_ = ANIM_COSINE[i_32_];
				int i_67_ = ANIM_SINE[i_32_];
				int i_68_ = ANIM_COSINE[i_33_];
				int i_69_ = ANIM_SINE[i_33_];
				int i_70_ = i_65_ * i_68_ + 8192 >> 14;
				int i_71_ = i_65_ * i_69_ + 8192 >> 14;
				is_63_[0] = i_66_ * i_68_ + i_67_ * i_71_ + 8192 >> 14;
				is_63_[1] = -i_66_ * i_69_ + i_67_ * i_70_ + 8192 >> 14;
				is_63_[2] = i_67_ * i_64_ + 8192 >> 14;
				is_63_[3] = i_64_ * i_69_ + 8192 >> 14;
				is_63_[4] = i_64_ * i_68_ + 8192 >> 14;
				is_63_[5] = -i_65_;
				is_63_[6] = -i_67_ * i_68_ + i_66_ * i_71_ + 8192 >> 14;
				is_63_[7] = i_67_ * i_69_ + i_66_ * i_70_ + 8192 >> 14;
				is_63_[8] = i_66_ * i_64_ + 8192 >> 14;
				int i_72_ = is_63_[0] * -anInt1681 + is_63_[1] * -anInt1682 + is_63_[2] * -anInt1683 + 8192 >> 14;
				int i_73_ = is_63_[3] * -anInt1681 + is_63_[4] * -anInt1682 + is_63_[5] * -anInt1683 + 8192 >> 14;
				int i_74_ = is_63_[6] * -anInt1681 + is_63_[7] * -anInt1682 + is_63_[8] * -anInt1683 + 8192 >> 14;
				int i_75_ = i_72_ + anInt1681;
				int i_76_ = i_73_ + anInt1682;
				int i_77_ = i_74_ + anInt1683;
				int[] is_78_ = new int[9];
				for (int i_79_ = 0; i_79_ < 3; i_79_++) {
					for (int i_80_ = 0; i_80_ < 3; i_80_++) {
						int i_81_ = 0;
						for (int i_82_ = 0; i_82_ < 3; i_82_++)
							i_81_ += is_63_[i_79_ * 3 + i_82_] * is_35_[i_80_ * 3 + i_82_];
						is_78_[i_79_ * 3 + i_80_] = i_81_ + 8192 >> 14;
					}
				}
				int i_83_ = is_63_[0] * i_57_ + is_63_[1] * i_58_ + is_63_[2] * i_59_ + 8192 >> 14;
				int i_84_ = is_63_[3] * i_57_ + is_63_[4] * i_58_ + is_63_[5] * i_59_ + 8192 >> 14;
				int i_85_ = is_63_[6] * i_57_ + is_63_[7] * i_58_ + is_63_[8] * i_59_ + 8192 >> 14;
				i_83_ += i_75_;
				i_84_ += i_76_;
				i_85_ += i_77_;
				int[] is_86_ = new int[9];
				for (int i_87_ = 0; i_87_ < 3; i_87_++) {
					for (int i_88_ = 0; i_88_ < 3; i_88_++) {
						int i_89_ = 0;
						for (int i_90_ = 0; i_90_ < 3; i_90_++)
							i_89_ += is_35_[i_87_ * 3 + i_90_] * is_78_[i_88_ + i_90_ * 3];
						is_86_[i_87_ * 3 + i_88_] = i_89_ + 8192 >> 14;
					}
				}
				int i_91_ = is_35_[0] * i_83_ + is_35_[1] * i_84_ + is_35_[2] * i_85_ + 8192 >> 14;
				int i_92_ = is_35_[3] * i_83_ + is_35_[4] * i_84_ + is_35_[5] * i_85_ + 8192 >> 14;
				int i_93_ = is_35_[6] * i_83_ + is_35_[7] * i_84_ + is_35_[8] * i_85_ + 8192 >> 14;
				i_91_ += i_54_;
				i_92_ += i_55_;
				i_93_ += i_56_;
				for (int i_94_ = 0; i_94_ < i_36_; i_94_++) {
					int i_95_ = is[i_94_];
					if (i_95_ < vectorSkin.length) {
						int[] is_96_ = vectorSkin[i_95_];
						for (int i_97_ = 0; i_97_ < is_96_.length; i_97_++) {
							int i_98_ = is_96_[i_97_];
							if (aShortArray2582 == null || (i_34_ & aShortArray2582[i_98_]) != 0) {
								int i_99_ = is_86_[0] * verticesX[i_98_] + is_86_[1] * verticesY[i_98_] + is_86_[2] * verticesZ[i_98_] + 8192 >> 14;
								int i_100_ = is_86_[3] * verticesX[i_98_] + is_86_[4] * verticesY[i_98_] + is_86_[5] * verticesZ[i_98_] + 8192 >> 14;
								int i_101_ = is_86_[6] * verticesX[i_98_] + is_86_[7] * verticesY[i_98_] + is_86_[8] * verticesZ[i_98_] + 8192 >> 14;
								i_99_ += i_91_;
								i_100_ += i_92_;
								i_101_ += i_93_;
								verticesX[i_98_] = i_99_;
								verticesY[i_98_] = i_100_;
								verticesZ[i_98_] = i_101_;
							}
						}
					}
				}
			} else {
				for (int i_102_ = 0; i_102_ < i_36_; i_102_++) {
					int i_103_ = is[i_102_];
					if (i_103_ < vectorSkin.length) {
						int[] is_104_ = vectorSkin[i_103_];
						for (int i_105_ = 0; i_105_ < is_104_.length; i_105_++) {
							int i_106_ = is_104_[i_105_];
							if (aShortArray2582 == null || (i_34_ & aShortArray2582[i_106_]) != 0) {
								verticesX[i_106_] -= anInt1681;
								verticesY[i_106_] -= anInt1682;
								verticesZ[i_106_] -= anInt1683;
								if (i_33_ != 0) {
									int i_107_ = ANIM_SINE[i_33_];
									int i_108_ = ANIM_COSINE[i_33_];
									int i_109_ = verticesY[i_106_] * i_107_ + verticesX[i_106_] * i_108_ + 16383 >> 14;
									verticesY[i_106_] = verticesY[i_106_] * i_108_ - verticesX[i_106_] * i_107_ + 16383 >> 14;
									verticesX[i_106_] = i_109_;
								}
								if (i_31_ != 0) {
									int i_110_ = ANIM_SINE[i_31_];
									int i_111_ = ANIM_COSINE[i_31_];
									int i_112_ = verticesY[i_106_] * i_111_ - verticesZ[i_106_] * i_110_ + 16383 >> 14;
									verticesZ[i_106_] = verticesY[i_106_] * i_110_ + verticesZ[i_106_] * i_111_ + 16383 >> 14;
									verticesY[i_106_] = i_112_;
								}
								if (i_32_ != 0) {
									int i_113_ = ANIM_SINE[i_32_];
									int i_114_ = ANIM_COSINE[i_32_];
									int i_115_ = verticesZ[i_106_] * i_113_ + verticesX[i_106_] * i_114_ + 16383 >> 14;
									verticesZ[i_106_] = verticesZ[i_106_] * i_114_ - verticesX[i_106_] * i_113_ + 16383 >> 14;
									verticesX[i_106_] = i_115_;
								}
								verticesX[i_106_] += anInt1681;
								verticesY[i_106_] += anInt1682;
								verticesZ[i_106_] += anInt1683;
							}
						}
					}
				}
			}
		} else if (i == 3) {
			if (is_35_ != null) {
				if (!upscaled) {
					for (int i_116_ = 0; i_116_ < numVertices; i_116_++) {
						verticesX[i_116_] <<= 4;
						verticesY[i_116_] <<= 4;
						verticesZ[i_116_] <<= 4;
					}
					upscaled = true;
				}
				int i_117_ = is_35_[9] << 4;
				int i_118_ = is_35_[10] << 4;
				int i_119_ = is_35_[11] << 4;
				int i_120_ = is_35_[12] << 4;
				int i_121_ = is_35_[13] << 4;
				int i_122_ = is_35_[14] << 4;
				if (aBoolean5734) {
					int i_123_ = is_35_[0] * anInt1681 + is_35_[3] * anInt1682 + is_35_[6] * anInt1683 + 8192 >> 14;
					int i_124_ = is_35_[1] * anInt1681 + is_35_[4] * anInt1682 + is_35_[7] * anInt1683 + 8192 >> 14;
					int i_125_ = is_35_[2] * anInt1681 + is_35_[5] * anInt1682 + is_35_[8] * anInt1683 + 8192 >> 14;
					i_123_ += i_120_;
					i_124_ += i_121_;
					i_125_ += i_122_;
					anInt1681 = i_123_;
					anInt1682 = i_124_;
					anInt1683 = i_125_;
					aBoolean5734 = false;
				}
				int i_126_ = i_31_ << 15 >> 7;
				int i_127_ = i_32_ << 15 >> 7;
				int i_128_ = i_33_ << 15 >> 7;
				int i_129_ = i_126_ * -anInt1681 + 8192 >> 14;
				int i_130_ = i_127_ * -anInt1682 + 8192 >> 14;
				int i_131_ = i_128_ * -anInt1683 + 8192 >> 14;
				int i_132_ = i_129_ + anInt1681;
				int i_133_ = i_130_ + anInt1682;
				int i_134_ = i_131_ + anInt1683;
				int[] is_135_ = new int[9];
				is_135_[0] = i_126_ * is_35_[0] + 8192 >> 14;
				is_135_[1] = i_126_ * is_35_[3] + 8192 >> 14;
				is_135_[2] = i_126_ * is_35_[6] + 8192 >> 14;
				is_135_[3] = i_127_ * is_35_[1] + 8192 >> 14;
				is_135_[4] = i_127_ * is_35_[4] + 8192 >> 14;
				is_135_[5] = i_127_ * is_35_[7] + 8192 >> 14;
				is_135_[6] = i_128_ * is_35_[2] + 8192 >> 14;
				is_135_[7] = i_128_ * is_35_[5] + 8192 >> 14;
				is_135_[8] = i_128_ * is_35_[8] + 8192 >> 14;
				int i_136_ = i_126_ * i_120_ + 8192 >> 14;
				int i_137_ = i_127_ * i_121_ + 8192 >> 14;
				int i_138_ = i_128_ * i_122_ + 8192 >> 14;
				i_136_ += i_132_;
				i_137_ += i_133_;
				i_138_ += i_134_;
				int[] is_139_ = new int[9];
				for (int i_140_ = 0; i_140_ < 3; i_140_++) {
					for (int i_141_ = 0; i_141_ < 3; i_141_++) {
						int i_142_ = 0;
						for (int i_143_ = 0; i_143_ < 3; i_143_++)
							i_142_ += is_35_[i_140_ * 3 + i_143_] * is_135_[i_141_ + i_143_ * 3];
						is_139_[i_140_ * 3 + i_141_] = i_142_ + 8192 >> 14;
					}
				}
				int i_144_ = is_35_[0] * i_136_ + is_35_[1] * i_137_ + is_35_[2] * i_138_ + 8192 >> 14;
				int i_145_ = is_35_[3] * i_136_ + is_35_[4] * i_137_ + is_35_[5] * i_138_ + 8192 >> 14;
				int i_146_ = is_35_[6] * i_136_ + is_35_[7] * i_137_ + is_35_[8] * i_138_ + 8192 >> 14;
				i_144_ += i_117_;
				i_145_ += i_118_;
				i_146_ += i_119_;
				for (int i_147_ = 0; i_147_ < i_36_; i_147_++) {
					int i_148_ = is[i_147_];
					if (i_148_ < vectorSkin.length) {
						int[] is_149_ = vectorSkin[i_148_];
						for (int i_150_ = 0; i_150_ < is_149_.length; i_150_++) {
							int i_151_ = is_149_[i_150_];
							if (aShortArray2582 == null || (i_34_ & aShortArray2582[i_151_]) != 0) {
								int i_152_ = is_139_[0] * verticesX[i_151_] + is_139_[1] * verticesY[i_151_] + is_139_[2] * verticesZ[i_151_] + 8192 >> 14;
								int i_153_ = is_139_[3] * verticesX[i_151_] + is_139_[4] * verticesY[i_151_] + is_139_[5] * verticesZ[i_151_] + 8192 >> 14;
								int i_154_ = is_139_[6] * verticesX[i_151_] + is_139_[7] * verticesY[i_151_] + is_139_[8] * verticesZ[i_151_] + 8192 >> 14;
								i_152_ += i_144_;
								i_153_ += i_145_;
								i_154_ += i_146_;
								verticesX[i_151_] = i_152_;
								verticesY[i_151_] = i_153_;
								verticesZ[i_151_] = i_154_;
							}
						}
					}
				}
			} else {
				for (int i_155_ = 0; i_155_ < i_36_; i_155_++) {
					int i_156_ = is[i_155_];
					if (i_156_ < vectorSkin.length) {
						int[] is_157_ = vectorSkin[i_156_];
						for (int i_158_ = 0; i_158_ < is_157_.length; i_158_++) {
							int i_159_ = is_157_[i_158_];
							if (aShortArray2582 == null || (i_34_ & aShortArray2582[i_159_]) != 0) {
								verticesX[i_159_] -= anInt1681;
								verticesY[i_159_] -= anInt1682;
								verticesZ[i_159_] -= anInt1683;
								verticesX[i_159_] = verticesX[i_159_] * i_31_ / 128;
								verticesY[i_159_] = verticesY[i_159_] * i_32_ / 128;
								verticesZ[i_159_] = verticesZ[i_159_] * i_33_ / 128;
								verticesX[i_159_] += anInt1681;
								verticesY[i_159_] += anInt1682;
								verticesZ[i_159_] += anInt1683;
							}
						}
					}
				}
			}
		} else if (i == 5) {
			if (triangleSkin != null && triangleAlphaValues != null) {
				for (int i_160_ = 0; i_160_ < i_36_; i_160_++) {
					int i_161_ = is[i_160_];
					if (i_161_ < triangleSkin.length) {
						int[] is_162_ = triangleSkin[i_161_];
						for (int i_163_ = 0; i_163_ < is_162_.length; i_163_++) {
							int i_164_ = is_162_[i_163_];
							if (aShortArray2575 == null || (i_34_ & aShortArray2575[i_164_]) != 0) {
								int i_165_ = (triangleAlphaValues[i_164_] & 0xff) + i_31_ * 8;
								if (i_165_ < 0) {
									i_165_ = 0;
								} else if (i_165_ > 255) {
									i_165_ = 255;
								}
								triangleAlphaValues[i_164_] = (byte) i_165_;
							}
						}
					}
				}
			}
		} else if (i == 7) {
			if (triangleSkin != null) {
				for (int i_167_ = 0; i_167_ < i_36_; i_167_++) {
					int i_168_ = is[i_167_];
					if (i_168_ < triangleSkin.length) {
						int[] is_169_ = triangleSkin[i_168_];
						for (int i_170_ = 0; i_170_ < is_169_.length; i_170_++) {
							int i_171_ = is_169_[i_170_];
							if (aShortArray2575 == null || (i_34_ & aShortArray2575[i_171_]) != 0) {
								int i_172_ = colorValues[i_171_] & 0xffff;
								int i_173_ = i_172_ >> 10 & 0x3f;
								int i_174_ = i_172_ >> 7 & 0x7;
								int i_175_ = i_172_ & 0x7f;
								i_173_ = i_173_ + i_31_ & 0x3f;
								i_174_ += i_32_;
								if (i_174_ < 0) {
									i_174_ = 0;
								} else if (i_174_ > 7) {
									i_174_ = 7;
								}
								i_175_ += i_33_;
								if (i_175_ < 0) {
									i_175_ = 0;
								} else if (i_175_ > 127) {
									i_175_ = 127;
								}
								colorValues[i_171_] = (short) (i_173_ << 10 | i_174_ << 7 | i_175_);
							}
						}
					}
				}
			}
		} else if (i == 8) {
		} else if (i == 10) {
		} else if (i == 9) {
		}
	}

	public void rotate90Degrees()
	{
		for(int i = 0; i < numVertices; i++)
		{
			int j = verticesX[i];
			verticesX[i] = verticesZ[i];
			verticesZ[i] = -j;
		}

	}

	public void method474(int i)
	{
		int j = SINE[i];
		int k = COSINE[i];
		for(int l = 0; l < numVertices; l++)
		{
			int i1 = verticesY[l] * k - verticesZ[l] * j >> 16;
			verticesZ[l] = verticesY[l] * j + verticesZ[l] * k >> 16;
			verticesY[l] = i1;
		}

	}

	public void method477()
	{
		for(int i = 0; i < numVertices; i++)
			verticesZ[i] = -verticesZ[i];

		for(int j = 0; j < numTriangles; j++)
		{
			short k = trianglePoints1[j];
			trianglePoints1[j] = trianglePoints3[j];
			trianglePoints3[j] = k;
		}

	}

	public void scale(int i, int j, int k)
	{
		for(int l = 0; l < numVertices; l++)
		{
			verticesX[l] = (verticesX[l] * i) / 128;
			verticesY[l] = (verticesY[l] * k) / 128;
			verticesZ[l] = (verticesZ[l] * j) / 128;
		}

	}

	public final void setLighting(int i, int j, int k, int l, int i1, boolean flag, boolean fixPriorities)
	{
		int j1 = (int)Math.sqrt(k * k + l * l + i1 * i1);
		int k1 = j * j1 >> 8;
		if(anIntArray1634 == null)
		{
			anIntArray1634 = new int[numTriangles];
			anIntArray1635 = new int[numTriangles];
			anIntArray1636 = new int[numTriangles];
		}
		if(super.aClass33Array1425 == null)
		{
			super.aClass33Array1425 = new Class33[numVertices];
			for(int l1 = 0; l1 < numVertices; l1++)
				super.aClass33Array1425[l1] = new Class33();

		}
		for(int i2 = 0; i2 < numTriangles; i2++) {
			if (trianglePriorities != null && fixPriorities) {
				trianglePriorities[i2] = 10;
			}
			int k2 = trianglePoints1[i2] & 0xffff;
			int l2 = trianglePoints2[i2] & 0xffff;
			int i3 = trianglePoints3[i2] & 0xffff;
			int j3 = (verticesX[l2] - verticesX[k2] + 1) >> 2;
			int k3 = (verticesY[l2] - verticesY[k2] + 1) >> 2;
			int l3 = (verticesZ[l2] - verticesZ[k2] + 1) >> 2;
			int i4 = (verticesX[i3] - verticesX[k2] + 1) >> 2;
			int j4 = (verticesY[i3] - verticesY[k2] + 1) >> 2;
			int k4 = (verticesZ[i3] - verticesZ[k2] + 1) >> 2;
			int l4 = k3 * k4 - j4 * l3;
			int i5 = l3 * i4 - k4 * j3;
			int j5;
			for(j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192 || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1)
			{
				l4 >>= 1;
				i5 >>= 1;
			}

			int k5 = (int)Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
			if(k5 <= 0)
				k5 = 1;
			l4 = (l4 * 256) / k5;
			i5 = (i5 * 256) / k5;
			j5 = (j5 * 256) / k5;
			if(drawTypes == null || (drawTypes[i2] & 1) == 0)
			{
				Class33 class33_2 = super.aClass33Array1425[k2];
				class33_2.anInt602 += l4;
				class33_2.anInt603 += i5;
				class33_2.anInt604 += j5;
				class33_2.anInt605++;
				class33_2 = super.aClass33Array1425[l2];
				class33_2.anInt602 += l4;
				class33_2.anInt603 += i5;
				class33_2.anInt604 += j5;
				class33_2.anInt605++;
				class33_2 = super.aClass33Array1425[i3];
				class33_2.anInt602 += l4;
				class33_2.anInt603 += i5;
				class33_2.anInt604 += j5;
				class33_2.anInt605++;
			} else
			{
				int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
				anIntArray1634[i2] = method481(colorValues[i2] & 0xffff, l5, 0);
			}
		}

		if(flag)
		{
			method480(i, k1, k, l, i1);
		} else
		{
			aClass33Array1660 = new Class33[numVertices];
			for(int j2 = 0; j2 < numVertices; j2++)
			{
				Class33 class33 = super.aClass33Array1425[j2];
				Class33 class33_1 = aClass33Array1660[j2] = new Class33();
				class33_1.anInt602 = class33.anInt602;
				class33_1.anInt603 = class33.anInt603;
				class33_1.anInt604 = class33.anInt604;
				class33_1.anInt605 = class33.anInt605;
			}

		}
		if(flag)
		{
			method466();
			return;
		} else
		{
			method468(21073);
			return;
		}
	}

	public final void method480(int i, int j, int k, int l, int i1)
	{
		for(int j1 = 0; j1 < numTriangles; j1++)
		{
			int l1 = trianglePoints1[j1] & 0xffff;
			int i2 = trianglePoints2[j1] & 0xffff;
			int j2 = trianglePoints3[j1] & 0xffff;
			if(drawTypes == null)
			{
				int k2 = colorValues[j1];
				Class33 class33 = super.aClass33Array1425[l1];
				int j3 = i + (k * class33.anInt602 + l * class33.anInt603 + i1 * class33.anInt604) / (j * class33.anInt605);
				anIntArray1634[j1] = method481(k2, j3, 0);
				class33 = super.aClass33Array1425[i2];
				j3 = i + (k * class33.anInt602 + l * class33.anInt603 + i1 * class33.anInt604) / (j * class33.anInt605);
				anIntArray1635[j1] = method481(k2, j3, 0);
				class33 = super.aClass33Array1425[j2];
				j3 = i + (k * class33.anInt602 + l * class33.anInt603 + i1 * class33.anInt604) / (j * class33.anInt605);
				anIntArray1636[j1] = method481(k2, j3, 0);
				continue;
			}
			if((drawTypes[j1] & 0x1) == 0)
			{
				int l2 = colorValues[j1];
				int i3 = 0;
				Class33 class33_1 = super.aClass33Array1425[l1];
				int k3 = i + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1 * class33_1.anInt604) / (j * class33_1.anInt605);
				anIntArray1634[j1] = method481(l2, k3, i3);
				class33_1 = super.aClass33Array1425[i2];
				k3 = i + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1 * class33_1.anInt604) / (j * class33_1.anInt605);
				anIntArray1635[j1] = method481(l2, k3, i3);
				class33_1 = super.aClass33Array1425[j2];
				k3 = i + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1 * class33_1.anInt604) / (j * class33_1.anInt605);
				anIntArray1636[j1] = method481(l2, k3, i3);
			}
		}

		super.aClass33Array1425 = null;
		aClass33Array1660 = null;
		vertexSkins = null;
		triangleSkinValues = null;
	}

	public static final int method481(int i, int j, int k)
	{
		if(i == 65535)
			return 0;
		if((k & 2) == 2)
		{
			if(j < 0)
				j = 0;
			else
			if(j > 127)
				j = 127;
			j = 127 - j;
			return j;
		}
		j = j * (i & 0x7f) >> 7;
		if(j < 2)
			j = 2;
		else
		if(j > 126)
			j = 126;
		return (i & 0xff80) + j;
	}

	public final boolean method482(int i, int j, int k, int l, int i1, int j1)
	{
		int k1 = 0;
		int l1 = Rasterizer.centerX;
		int i2 = Rasterizer.centerY;
		int j2 = SINE[k1];
		int k2 = COSINE[k1];
		int l2 = SINE[i];
		int i3 = COSINE[i];
		int j3 = SINE[j];
		int k3 = COSINE[j];
		int l3 = SINE[k];
		int i4 = COSINE[k];
		int j4 = i1 * l3 + j1 * i4 >> 16;
		for(int k4 = 0; k4 < numVertices; k4++)
		{
			int l4 = (verticesX[k4] + 1) >> 2;
			int i5 = (verticesY[k4] + 1) >> 2;
			int j5 = (verticesZ[k4] + 1) >> 2;
			if(j != 0)
			{
				int k5 = i5 * j3 + l4 * k3 >> 16;
				i5 = i5 * k3 - l4 * j3 >> 16;
				l4 = k5;
			}
			if(k1 != 0)
			{
				int l5 = i5 * k2 - j5 * j2 >> 16;
				j5 = i5 * j2 + j5 * k2 >> 16;
				i5 = l5;
			}
			if(i != 0)
			{
				int i6 = j5 * l2 + l4 * i3 >> 16;
				j5 = j5 * i3 - l4 * l2 >> 16;
				l4 = i6;
			}
			l4 += l;
			i5 += i1;
			j5 += j1;
			int j6 = i5 * i4 - j5 * l3 >> 16;
			j5 = i5 * l3 + j5 * i4 >> 16;
			i5 = j6;
			anIntArray1667[k4] = j5 - j4;
			anIntArray1665[k4] = l1 + (l4 << 9) / j5;
			anIntArray1666[k4] = i2 + (i5 << 9) / j5;
			//if(numTexTriangles > 0)
			//{
				anIntArray1668[k4] = l4;
				anIntArray1669[k4] = i5;
				anIntArray1670[k4] = j5;
			//}
		}

		try
		{
			return method483(false, false, 0, -1);
		}
		catch(Exception exception)
		{
			if (signlink.storeid != 32)
				exception.printStackTrace();

			return false;
		}
	}

	public final boolean method443(int i, int j, int k, int l, int i1, int j1, int k1, 
			int l1, int i2, int id)
	{
		int j2 = l1 * i1 - j1 * l >> 16;
		int k2 = k1 * j + j2 * k >> 16;
		int l2 = shadowIntensity * k >> 16;
		int i3 = k2 + l2;
		if(i3 <= 50 || k2 >= 3500)
			return true;
		int j3 = l1 * l + j1 * i1 >> 16;
		int k3 = j3 - shadowIntensity << 9;
		if(k3 / i3 >= DrawingArea.centerY)
			return true;
		int l3 = j3 + shadowIntensity << 9;
		if(l3 / i3 <= -DrawingArea.centerY)
			return true;
		int i4 = k1 * k - j2 * j >> 16;
		int j4 = shadowIntensity * j >> 16;
		int k4 = i4 + j4 << 9;
		if(k4 / i3 <= -DrawingArea.anInt1387)
			return true;
		int l4 = j4 + (super.modelHeight * k >> 16);
		int i5 = i4 - l4 << 9;
		if(i5 / i3 >= DrawingArea.anInt1387)
			return true;
		int j5 = l2 + (super.modelHeight * j >> 16);
		boolean flag = false;
		if(k2 - j5 <= 50)
			flag = true;
		boolean flag1 = false;
		if(i2 > 0 && aBoolean1684)
		{
			int k5 = k2 - l2;
			if(k5 <= 50)
				k5 = 50;
			if(j3 > 0)
			{
				k3 /= i3;
				l3 /= k5;
			} else
			{
				l3 /= i3;
				k3 /= k5;
			}
			if(i4 > 0)
			{
				i5 /= i3;
				k4 /= k5;
			} else
			{
				k4 /= i3;
				i5 /= k5;
			}
			int i6 = anInt1685 - Rasterizer.centerX;
			int k6 = anInt1686 - Rasterizer.centerY;
			if(i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4)
				if(aBoolean1659)
				{
					mapObjectIds[anInt1687] = id;
					anIntArray1688[anInt1687++] = i2;
				}
				else
					flag1 = true;
		}
		int l5 = Rasterizer.centerX;
		int j6 = Rasterizer.centerY;
		int l6 = 0;
		int i7 = 0;
		if(i != 0)
		{
			l6 = SINE[i];
			i7 = COSINE[i];
		}
		for(int j7 = 0; j7 < numVertices; j7++)
		{
			int k7 = (verticesX[j7] + 1) >> 2;
			int l7 = (verticesY[j7] + 1) >> 2;
			int i8 = (verticesZ[j7] + 1) >> 2;
			if(i != 0)
			{
				int j8 = i8 * l6 + k7 * i7 >> 16;
				i8 = i8 * i7 - k7 * l6 >> 16;
				k7 = j8;
			}
			k7 += j1;
			l7 += k1;
			i8 += l1;
			int k8 = i8 * l + k7 * i1 >> 16;
			i8 = i8 * i1 - k7 * l >> 16;
			k7 = k8;
			k8 = l7 * k - i8 * j >> 16;
			i8 = l7 * j + i8 * k >> 16;
			l7 = k8;
			anIntArray1667[j7] = i8 - k2;
			if(i8 >= 50)
			{
				anIntArray1665[j7] = l5 + (k7 << 9) / i8;
				anIntArray1666[j7] = j6 + (l7 << 9) / i8;
			} else
			{
				anIntArray1665[j7] = -5000;
				flag = true;
			}
			//if(flag || numTexTriangles > 0)
			//{
				anIntArray1668[j7] = k7;
				anIntArray1669[j7] = l7;
				anIntArray1670[j7] = i8;
			///}
		}

		try
		{
			return method483(flag, flag1, i2, id);
		}
		catch(Exception exception)
		{
			if (signlink.storeid != 32)
				exception.printStackTrace();

			return false;
		}
	}

	private final boolean method483(boolean flag, boolean flag1, int i, int id)
	{
		boolean success = true;
		if (anInt1652 > 1500)//handle large objects?
			return false;

		for(int j = 0; j < anInt1652; j++)
			anIntArray1671[j] = 0;

		for(int k = 0; k < numTriangles; k++)
		{
			if(drawTypes != null && drawTypes[k] == -1)
				continue;
			int l1 = trianglePoints1[k] & 0xffff;
			int l2 = trianglePoints2[k] & 0xffff;
			int j3 = trianglePoints3[k] & 0xffff;
			int j4 = anIntArray1665[l1];
			int i5 = anIntArray1665[l2];
			int k5 = anIntArray1665[j3];
			if(flag && (j4 == -5000 || i5 == -5000 || k5 == -5000))
			{
				aBooleanArray1664[k] = true;
				int i6 = (anIntArray1667[l1] + anIntArray1667[l2] + anIntArray1667[j3]) / 3 + anInt1653;
				anIntArrayArray1672[i6][anIntArray1671[i6]++] = k;
				continue;
			}
			if(flag1 && method486(anInt1685, anInt1686, anIntArray1666[l1], anIntArray1666[l2], anIntArray1666[j3], j4, i5, k5))
			{
				mapObjectIds[anInt1687] = id;
				anIntArray1688[anInt1687++] = i;
				flag1 = false;
			}
			if((j4 - i5) * (anIntArray1666[j3] - anIntArray1666[l2]) - (anIntArray1666[l1] - anIntArray1666[l2]) * (k5 - i5) <= 0)
				continue;
			aBooleanArray1664[k] = false;
			if(j4 < 0 || i5 < 0 || k5 < 0 || j4 > DrawingArea.centerX || i5 > DrawingArea.centerX || k5 > DrawingArea.centerX)
				aBooleanArray1663[k] = true;
			else
				aBooleanArray1663[k] = false;
			int j6 = (anIntArray1667[l1] + anIntArray1667[l2] + anIntArray1667[j3]) / 3 + anInt1653;
			anIntArrayArray1672[j6][anIntArray1671[j6]++] = k;
		}

		if(trianglePriorities == null && trianglePriority == -1)
		{
			for(int l = anInt1652 - 1; l >= 0; l--)
			{
				int i2 = anIntArray1671[l];
				if(i2 <= 0)
					continue;
				int ai[] = anIntArrayArray1672[l];
				for(int k3 = 0; k3 < i2; k3++)
					success &= method484(ai[k3]);

			}

			return success;
		}
		for(int i1 = 0; i1 < 12; i1++)
		{
			anIntArray1673[i1] = 0;
			anIntArray1677[i1] = 0;
		}

		for(int j1 = anInt1652 - 1; j1 >= 0; j1--)
		{
			int j2 = anIntArray1671[j1];
			if(j2 <= 0)
				continue;
			int ai1[] = anIntArrayArray1672[j1];
			for(int l3 = 0; l3 < j2; l3++)
			{
				int k4 = ai1[l3];
				int j5 = (trianglePriorities != null ? trianglePriorities[k4]:trianglePriority) & 0xff;
				int l5 = anIntArray1673[j5]++;
				anIntArrayArray1674[j5][l5] = k4;
				if(j5 < 10)
				{
					anIntArray1677[j5] += j1;
					continue;
				}
				if(j5 == 10)
					anIntArray1675[l5] = j1;
				else
					anIntArray1676[l5] = j1;
			}

		}

		int k1 = 0;
		if(anIntArray1673[1] > 0 || anIntArray1673[2] > 0)
			k1 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
		int k2 = 0;
		if(anIntArray1673[3] > 0 || anIntArray1673[4] > 0)
			k2 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
		int i3 = 0;
		if(anIntArray1673[6] > 0 || anIntArray1673[8] > 0)
			i3 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);
		int i4 = 0;
		int l4 = anIntArray1673[10];
		int ai2[] = anIntArrayArray1674[10];
		int ai3[] = anIntArray1675;
		if(i4 == l4)
		{
			i4 = 0;
			l4 = anIntArray1673[11];
			ai2 = anIntArrayArray1674[11];
			ai3 = anIntArray1676;
		}
		int k6;
		if(i4 < l4)
			k6 = ai3[i4];
		else
			k6 = -1000;
		for(int l6 = 0; l6 < 10; l6++)
		{
			while(l6 == 0 && k6 > k1) 
			{
				success &= method484(ai2[i4++]);
				if(i4 == l4 && ai2 != anIntArrayArray1674[11])
				{
					i4 = 0;
					l4 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if(i4 < l4)
					k6 = ai3[i4];
				else
					k6 = -1000;
			}
			while(l6 == 3 && k6 > k2) 
			{
				success &= method484(ai2[i4++]);
				if(i4 == l4 && ai2 != anIntArrayArray1674[11])
				{
					i4 = 0;
					l4 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if(i4 < l4)
					k6 = ai3[i4];
				else
					k6 = -1000;
			}
			while(l6 == 5 && k6 > i3) 
			{
				success &= method484(ai2[i4++]);
				if(i4 == l4 && ai2 != anIntArrayArray1674[11])
				{
					i4 = 0;
					l4 = anIntArray1673[11];
					ai2 = anIntArrayArray1674[11];
					ai3 = anIntArray1676;
				}
				if(i4 < l4)
					k6 = ai3[i4];
				else
					k6 = -1000;
			}
			int i7 = anIntArray1673[l6];
			int ai4[] = anIntArrayArray1674[l6];
			for(int j7 = 0; j7 < i7; j7++)
				success &= method484(ai4[j7]);

		}

		while(k6 != -1000) 
		{
			success &= method484(ai2[i4++]);
			if(i4 == l4 && ai2 != anIntArrayArray1674[11])
			{
				i4 = 0;
				ai2 = anIntArrayArray1674[11];
				l4 = anIntArray1673[11];
				ai3 = anIntArray1676;
			}
			if(i4 < l4)
				k6 = ai3[i4];
			else
				k6 = -1000;
		}
		return success;
	}

	private final boolean method484(int i)
	{
		if(aBooleanArray1664[i])
			return method485(i);

		boolean success = true;
		int j = trianglePoints1[i] & 0xffff;
		int k = trianglePoints2[i] & 0xffff;
		int l = trianglePoints3[i] & 0xffff;
		Rasterizer.restrict_edges = aBooleanArray1663[i];
		if(colorValues != null && colorValues[i] == -1) //?
			return true;

		if(triangleAlphaValues == null)
			Rasterizer.alpha = 0;
		else
			Rasterizer.alpha = triangleAlphaValues[i] & 0xff;

		int type = drawTypes == null ? 0:drawTypes[i];
		boolean drawn = false;
		if (type == -1)
		{
			drawn = true;
			success = true;
		}
		if (!drawn && textureBackgrounds != null && textureBackgrounds[i] != -1)
		{
			if (type != 1)
			{
				if (texturePointers != null && texturePointers[i] != -1)
				{
					int j1 = texturePointers[i] & 0xff;
					byte texType = textureTypes == null ? 0:textureTypes[j1];
					if (texType == 0)
					{
						int l1 = texTrianglePointsX[j1] & 0xffff;
						int j2 = texTrianglePointsY[j1] & 0xffff;
						int l2 = texTrianglePointsZ[j1] & 0xffff;
						success &= Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1635[i], anIntArray1636[i], anIntArray1668[l1], anIntArray1668[j2], anIntArray1668[l2], anIntArray1669[l1], anIntArray1669[j2], anIntArray1669[l2], anIntArray1670[l1], anIntArray1670[j2], anIntArray1670[l2], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
						drawn = true;
					}
				}
				else
				{
					int l1 = j;
					int j2 = k;
					int l2 = l;
					success &= Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1635[i], anIntArray1636[i], anIntArray1668[l1], anIntArray1668[j2], anIntArray1668[l2], anIntArray1669[l1], anIntArray1669[j2], anIntArray1669[l2], anIntArray1670[l1], anIntArray1670[j2], anIntArray1670[l2], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
					drawn = true;
				}
			}
			else
			{
				if (texturePointers != null && texturePointers[i] != -1)
				{
					int k1 = texturePointers[i] & 0xff;
					byte texType = textureTypes == null ? 0:textureTypes[k1];
					if (texType == 0)
					{
						int i2 = texTrianglePointsX[k1] & 0xffff;
						int k2 = texTrianglePointsY[k1] & 0xffff;
						int i3 = texTrianglePointsZ[k1] & 0xffff;
						success &= Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[i2], anIntArray1668[k2], anIntArray1668[i3], anIntArray1669[i2], anIntArray1669[k2], anIntArray1669[i3], anIntArray1670[i2], anIntArray1670[k2], anIntArray1670[i3], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
						drawn = true;
					}
				}
				else
				{
					int i2 = j;
					int k2 = k;
					int i3 = l;
					success &= Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[i2], anIntArray1668[k2], anIntArray1668[i3], anIntArray1669[i2], anIntArray1669[k2], anIntArray1669[i3], anIntArray1670[i2], anIntArray1670[k2], anIntArray1670[i3], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
					drawn = true;
				}
			}
		}
		if (!drawn)
		{
			if (type != 1)
				Rasterizer.drawShadedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1635[i], anIntArray1636[i]);
			else
				Rasterizer.drawFlatTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], hsl2rgb[anIntArray1634[i]]);

		}
		return success;
		/*int i1;
		if(drawTypes == null)
			i1 = 0;
		else
			i1 = drawTypes[i] & 3;
		if(i1 == 0)
		{
			Rasterizer.drawShadedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1635[i], anIntArray1636[i]);
			return;
		}
		if(i1 == 1)
		{
			Rasterizer.drawFlatTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], hslToRgbLookupTable[anIntArray1634[i]]);
			return;
		}
		if(i1 == 2)
		{
			int j1 = drawTypes[i];
			int l1 = texTrianglePointsX[j1] & 0xffff;
			int j2 = texTrianglePointsY[j1] & 0xffff;
			int l2 = texTrianglePointsZ[j1] & 0xffff;
			Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1635[i], anIntArray1636[i], anIntArray1668[l1], anIntArray1668[j2], anIntArray1668[l2], anIntArray1669[l1], anIntArray1669[j2], anIntArray1669[l2], anIntArray1670[l1], anIntArray1670[j2], anIntArray1670[l2], colorValues[i] & 0xffff);
			return;
		}
		if(i1 == 3)
		{
			int k1 = drawTypes[i];
			int i2 = texTrianglePointsX[k1] & 0xffff;
			int k2 = texTrianglePointsY[k1] & 0xffff;
			int i3 = texTrianglePointsZ[k1] & 0xffff;
			Rasterizer.drawTexturedTriangle(anIntArray1666[j], anIntArray1666[k], anIntArray1666[l], anIntArray1665[j], anIntArray1665[k], anIntArray1665[l], anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[i2], anIntArray1668[k2], anIntArray1668[i3], anIntArray1669[i2], anIntArray1669[k2], anIntArray1669[i3], anIntArray1670[i2], anIntArray1670[k2], anIntArray1670[i3], colorValues[i] & 0xffff);
		}*/
	}

	private final boolean method485(int i)
	{
		if(colorValues != null && colorValues[i] == -1)
			return true;

		if(triangleAlphaValues == null)
			Rasterizer.alpha = 0;
		else
			Rasterizer.alpha = triangleAlphaValues[i] & 0xff; //?

		boolean success = true;
		int j = Rasterizer.centerX;
		int k = Rasterizer.centerY;
		int l = 0;
		int i1 = trianglePoints1[i] & 0xffff;
		int j1 = trianglePoints2[i] & 0xffff;
		int k1 = trianglePoints3[i] & 0xffff;
		int l1 = anIntArray1670[i1];
		int i2 = anIntArray1670[j1];
		int j2 = anIntArray1670[k1];
		if(l1 >= 50)
		{
			anIntArray1678[l] = anIntArray1665[i1];
			anIntArray1679[l] = anIntArray1666[i1];
			anIntArray1680[l++] = anIntArray1634[i];
		} else
		{
			int k2 = anIntArray1668[i1];
			int k3 = anIntArray1669[i1];
			int k4 = anIntArray1634[i];
			if(j2 >= 50)
			{
				int k5 = (50 - l1) * lightDecay[j2 - l1];
				anIntArray1678[l] = j + (k2 + ((anIntArray1668[k1] - k2) * k5 >> 16) << 9) / 50;
				anIntArray1679[l] = k + (k3 + ((anIntArray1669[k1] - k3) * k5 >> 16) << 9) / 50;
				anIntArray1680[l++] = k4 + ((anIntArray1636[i] - k4) * k5 >> 16);
			}
			if(i2 >= 50)
			{
				int l5 = (50 - l1) * lightDecay[i2 - l1];
				anIntArray1678[l] = j + (k2 + ((anIntArray1668[j1] - k2) * l5 >> 16) << 9) / 50;
				anIntArray1679[l] = k + (k3 + ((anIntArray1669[j1] - k3) * l5 >> 16) << 9) / 50;
				anIntArray1680[l++] = k4 + ((anIntArray1635[i] - k4) * l5 >> 16);
			}
		}
		if(i2 >= 50)
		{
			anIntArray1678[l] = anIntArray1665[j1];
			anIntArray1679[l] = anIntArray1666[j1];
			anIntArray1680[l++] = anIntArray1635[i];
		} else
		{
			int l2 = anIntArray1668[j1];
			int l3 = anIntArray1669[j1];
			int l4 = anIntArray1635[i];
			if(l1 >= 50)
			{
				int i6 = (50 - i2) * lightDecay[l1 - i2];
				anIntArray1678[l] = j + (l2 + ((anIntArray1668[i1] - l2) * i6 >> 16) << 9) / 50;
				anIntArray1679[l] = k + (l3 + ((anIntArray1669[i1] - l3) * i6 >> 16) << 9) / 50;
				anIntArray1680[l++] = l4 + ((anIntArray1634[i] - l4) * i6 >> 16);
			}
			if(j2 >= 50)
			{
				int j6 = (50 - i2) * lightDecay[j2 - i2];
				anIntArray1678[l] = j + (l2 + ((anIntArray1668[k1] - l2) * j6 >> 16) << 9) / 50;
				anIntArray1679[l] = k + (l3 + ((anIntArray1669[k1] - l3) * j6 >> 16) << 9) / 50;
				anIntArray1680[l++] = l4 + ((anIntArray1636[i] - l4) * j6 >> 16);
			}
		}
		if(j2 >= 50)
		{
			anIntArray1678[l] = anIntArray1665[k1];
			anIntArray1679[l] = anIntArray1666[k1];
			anIntArray1680[l++] = anIntArray1636[i];
		} else
		{
			int i3 = anIntArray1668[k1];
			int i4 = anIntArray1669[k1];
			int i5 = anIntArray1636[i];
			if(i2 >= 50)
			{
				int k6 = (50 - j2) * lightDecay[i2 - j2];
				anIntArray1678[l] = j + (i3 + ((anIntArray1668[j1] - i3) * k6 >> 16) << 9) / 50;
				anIntArray1679[l] = k + (i4 + ((anIntArray1669[j1] - i4) * k6 >> 16) << 9) / 50;
				anIntArray1680[l++] = i5 + ((anIntArray1635[i] - i5) * k6 >> 16);
			}
			if(l1 >= 50)
			{
				int l6 = (50 - j2) * lightDecay[l1 - j2];
				anIntArray1678[l] = j + (i3 + ((anIntArray1668[i1] - i3) * l6 >> 16) << 9) / 50;
				anIntArray1679[l] = k + (i4 + ((anIntArray1669[i1] - i4) * l6 >> 16) << 9) / 50;
				anIntArray1680[l++] = i5 + ((anIntArray1634[i] - i5) * l6 >> 16);
			}
		}
		int j3 = anIntArray1678[0];
		int j4 = anIntArray1678[1];
		int j5 = anIntArray1678[2];
		int i7 = anIntArray1679[0];
		int j7 = anIntArray1679[1];
		int k7 = anIntArray1679[2];
		if((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0)
		{
			Rasterizer.restrict_edges = false;
			if(l == 3)
			{
				if(j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.centerX || j4 > DrawingArea.centerX || j5 > DrawingArea.centerX)
					Rasterizer.restrict_edges = true;

				if(triangleAlphaValues == null)
					Rasterizer.alpha = 0;
				else
					Rasterizer.alpha = triangleAlphaValues[i] & 0xff;

				boolean drawn = false;
				int type = drawTypes == null ? 0:drawTypes[i];
				if (type == -1)
				{
					drawn = true;
					success = true;
				}
				if (!drawn && textureBackgrounds != null && textureBackgrounds[i] != -1)
				{
					if (type != 1)
					{
						if (texturePointers != null && texturePointers[i] != -1)
						{
							int j8 = texturePointers[i] & 0xff;
							byte texType = textureTypes == null ? 0:textureTypes[j8];
							if (texType == 0)
							{
								int k9 = texTrianglePointsX[j8] & 0xffff;
								int k10 = texTrianglePointsY[j8] & 0xffff;
								int k11 = texTrianglePointsZ[j8] & 0xffff;
								success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[k9], anIntArray1668[k10], anIntArray1668[k11], anIntArray1669[k9], anIntArray1669[k10], anIntArray1669[k11], anIntArray1670[k9], anIntArray1670[k10], anIntArray1670[k11], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
								drawn = true;
							}
						}
						else
						{//fucking non standard code!
							int k9 = i1;
							int k10 = j1;
							int k11 = k1;
							success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[k9], anIntArray1668[k10], anIntArray1668[k11], anIntArray1669[k9], anIntArray1669[k10], anIntArray1669[k11], anIntArray1670[k9], anIntArray1670[k10], anIntArray1670[k11], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
							drawn = true;
						}
					}
					else
					{
						if (texturePointers != null && texturePointers[i] != -1)
						{
							int k8  = texturePointers[i] & 0xff;
							byte texType = textureTypes == null ? 0:textureTypes[k8];
							if (texType == 0)
							{
								int l9 = texTrianglePointsX[k8] & 0xffff;
								int l10 = texTrianglePointsY[k8] & 0xffff;
								int l11 = texTrianglePointsZ[k8] & 0xffff;
								success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[l9], anIntArray1668[l10], anIntArray1668[l11], anIntArray1669[l9], anIntArray1669[l10], anIntArray1669[l11], anIntArray1670[l9], anIntArray1670[l10], anIntArray1670[l11], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
								drawn = true;
							}
						}
						else
						{
							int l9 = i1;
							int l10 = j1;
							int l11 = k1;
							success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[l9], anIntArray1668[l10], anIntArray1668[l11], anIntArray1669[l9], anIntArray1669[l10], anIntArray1669[l11], anIntArray1670[l9], anIntArray1670[l10], anIntArray1670[l11], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
							drawn = true;
						}
					}
				}
				if (!drawn)
				{
					if (type != 1)
						Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);
					else
						Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, hsl2rgb[anIntArray1634[i]]);

				}
				/*int l7;
				if(drawTypes == null)
					l7 = 0;
				else
					l7 = drawTypes[i] & 3;
				if(l7 == 0)
					Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);
				else
				if(l7 == 1)
					Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, hslToRgbLookupTable[anIntArray1634[i]]);
				else
				if(l7 == 2)
				{
					int j8 = drawTypes[i];
					int k9 = texTrianglePointsX[j8] & 0xffff;
					int k10 = texTrianglePointsY[j8] & 0xffff;
					int k11 = texTrianglePointsZ[j8] & 0xffff;
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[k9], anIntArray1668[k10], anIntArray1668[k11], anIntArray1669[k9], anIntArray1669[k10], anIntArray1669[k11], anIntArray1670[k9], anIntArray1670[k10], anIntArray1670[k11], colorValues[i] & 0xffff);
				} else
				if(l7 == 3)
				{
					int k8 = drawTypes[i];
					int l9 = texTrianglePointsX[k8] & 0xffff;
					int l10 = texTrianglePointsY[k8] & 0xffff;
					int l11 = texTrianglePointsZ[k8] & 0xffff;
					Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[l9], anIntArray1668[l10], anIntArray1668[l11], anIntArray1669[l9], anIntArray1669[l10], anIntArray1669[l11], anIntArray1670[l9], anIntArray1670[l10], anIntArray1670[l11], colorValues[i] & 0xffff);
				}*/
			}
			if(l == 4)
			{
				if(j3 < 0 || j4 < 0 || j5 < 0 || j3 > DrawingArea.centerX || j4 > DrawingArea.centerX || j5 > DrawingArea.centerX || anIntArray1678[3] < 0 || anIntArray1678[3] > DrawingArea.centerX)
					Rasterizer.restrict_edges = true;
				if(triangleAlphaValues == null)
					Rasterizer.alpha = 0;
				else
					Rasterizer.alpha = triangleAlphaValues[i] & 0xff;
				boolean drawn = false;
				int type = drawTypes == null ? 0:drawTypes[i];
				if (type == -1)
				{
					drawn = true;
					success = true;
				}
				if (!drawn && textureBackgrounds != null && textureBackgrounds[i] != -1)
				{
					if (type != 1)
					{
						if (texturePointers != null && texturePointers[i] != -1)
						{
							int i9 = texturePointers[i] & 0xff;
							byte texType = textureTypes == null ? 0:textureTypes[i9];
							if (texType == 0)
							{
								int i10 = texTrianglePointsX[i9] & 0xffff;
								int i11 = texTrianglePointsY[i9] & 0xffff;
								int i12 = texTrianglePointsZ[i9] & 0xffff;
								success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[i10], anIntArray1668[i11], anIntArray1668[i12], anIntArray1669[i10], anIntArray1669[i11], anIntArray1669[i12], anIntArray1670[i10], anIntArray1670[i11], anIntArray1670[i12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
								success &= Rasterizer.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3], anIntArray1668[i10], anIntArray1668[i11], anIntArray1668[i12], anIntArray1669[i10], anIntArray1669[i11], anIntArray1669[i12], anIntArray1670[i10], anIntArray1670[i11], anIntArray1670[i12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
								drawn = true;
							}
						}
						else
						{
							int i10 = i1;
							int i11 = j1;
							int i12 = k1;
							success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2], anIntArray1668[i10], anIntArray1668[i11], anIntArray1668[i12], anIntArray1669[i10], anIntArray1669[i11], anIntArray1669[i12], anIntArray1670[i10], anIntArray1670[i11], anIntArray1670[i12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
							success &= Rasterizer.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3], anIntArray1668[i10], anIntArray1668[i11], anIntArray1668[i12], anIntArray1669[i10], anIntArray1669[i11], anIntArray1669[i12], anIntArray1670[i10], anIntArray1670[i11], anIntArray1670[i12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
							drawn = true;
						}
					}
					else
					{
						if (texturePointers != null && texturePointers[i] != -1)
						{
							int j9 = texturePointers[i] & 0xff;
							byte texType = textureTypes == null ? 0:textureTypes[j9];
							if (texType == 0)
							{
								int j10 = texTrianglePointsX[j9] & 0xffff;
								int j11 = texTrianglePointsY[j9] & 0xffff;
								int j12 = texTrianglePointsZ[j9] & 0xffff;
								success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[j10], anIntArray1668[j11], anIntArray1668[j12], anIntArray1669[j10], anIntArray1669[j11], anIntArray1669[j12], anIntArray1670[j10], anIntArray1670[j11], anIntArray1670[j12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
								success &= Rasterizer.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[j10], anIntArray1668[j11], anIntArray1668[j12], anIntArray1669[j10], anIntArray1669[j11], anIntArray1669[j12], anIntArray1670[j10], anIntArray1670[j11], anIntArray1670[j12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
								drawn = true;
							}
						}
						else
						{
							int j10 = i1;
							int j11 = j1;
							int j12 = k1;
							success &= Rasterizer.drawTexturedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[j10], anIntArray1668[j11], anIntArray1668[j12], anIntArray1669[j10], anIntArray1669[j11], anIntArray1669[j12], anIntArray1670[j10], anIntArray1670[j11], anIntArray1670[j12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
							success &= Rasterizer.drawTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1634[i], anIntArray1634[i], anIntArray1634[i], anIntArray1668[j10], anIntArray1668[j11], anIntArray1668[j12], anIntArray1669[j10], anIntArray1669[j11], anIntArray1669[j12], anIntArray1670[j10], anIntArray1670[j11], anIntArray1670[j12], textureBackgrounds[i] & 0xffff, colorValues[i] & 0xffff, false);
							drawn = true;
						}
					}
				}
				if (!drawn)
				{
					if (type != 1)
					{
						Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5, anIntArray1680[0], anIntArray1680[1], anIntArray1680[2]);
						Rasterizer.drawShadedTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], anIntArray1680[0], anIntArray1680[2], anIntArray1680[3]);
					}
					else
					{
						int l8 = hsl2rgb[anIntArray1634[i]];
						Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8);
						Rasterizer.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5, anIntArray1678[3], l8);
					}
				}
			}
		}
		return success;
	}

	private final boolean method486(int i, int j, int k, int l, int i1, int j1, int k1, 
			int l1)
	{
		if(j < k && j < l && j < i1)
			return false;
		if(j > k && j > l && j > i1)
			return false;
		if(i < j1 && i < k1 && i < l1)
			return false;
		else
			return i <= j1 || i <= k1 || i <= l1;
	}

	public static Model aModel_1621 = new Model(true);
	private static int anIntArray1622[] = new int[2000];
	private static int anIntArray1623[] = new int[2000];
	private static int anIntArray1624[] = new int[2000];
	private static byte anIntArray1625[] = new byte[2000];
	private static short anIntArray1626[] = new short[2000];
	public int anIntArray1634[];
	public int anIntArray1635[];
	public int anIntArray1636[];
	public int anInt1646;
	public int anInt1647;
	public int anInt1648;
	public int anInt1649;
	public int shadowIntensity;
	public int anInt1651;
	public int anInt1652;
	public int anInt1653;
	public int anInt1654;
	public int vectorSkin[][];
	public int triangleSkin[][];
	public boolean aBoolean1659;
	Class33 aClass33Array1660[];
	static OnDemandFetcherParent updateManager;
	static boolean aBooleanArray1663[] = new boolean[8000];
	static boolean aBooleanArray1664[] = new boolean[8000];
	static int anIntArray1665[] = new int[8000];
	static int anIntArray1666[] = new int[8000];
	static int anIntArray1667[] = new int[8000];
	static int anIntArray1668[] = new int[8000];
	static int anIntArray1669[] = new int[8000];
	static int anIntArray1670[] = new int[8000];
	static int anIntArray1671[] = new int[1500];
	static int anIntArrayArray1672[][] = new int[1500][512];
	static int anIntArray1673[] = new int[12];
	static int anIntArrayArray1674[][] = new int[12][2500];
	static int anIntArray1675[] = new int[8000];
	static int anIntArray1676[] = new int[8000];
	static int anIntArray1677[] = new int[12];
	static int anIntArray1678[] = new int[10];
	static int anIntArray1679[] = new int[10];
	static int anIntArray1680[] = new int[10];
	static int anInt1681;
	static int anInt1682;
	static int anInt1683;
	public static boolean aBoolean1684;
	public static int anInt1685;
	public static int anInt1686;
	public static int anInt1687;
	public static int anIntArray1688[] = new int[1000];
	public static int mapObjectIds[] = new int[1000];
	private static int[] ANIM_SINE = new int[16384];
	private static int[] ANIM_COSINE = new int[16384];
	public static int SINE[];
	public static int COSINE[];
	static int hsl2rgb[];
	static int lightDecay[];

	static 
	{
		SINE = Rasterizer.SINE;
		COSINE = Rasterizer.COSINE;
		hsl2rgb = Rasterizer.hslToRgbLookupTable;
		lightDecay = Rasterizer.lightDecay;
		double d = Math.PI / 8192.0D;
		for (int i = 0; i < 16384; i++) {
			ANIM_SINE[i] = (int) (16384.0D * Math.sin((double) i * d));
			ANIM_COSINE[i] = (int) (16384.0D * Math.cos((double) i * d));
		}
	}
}
