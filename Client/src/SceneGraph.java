package src;

public final class SceneGraph {
	
	public SceneGraph(byte abyte0[][][], int ai[][][]) {
		lowestPlane = 99;
		landscapeSizeX = 104;
		landscapeSizeY = 104;
		vertexHeights = ai;
		renderRuleFlags = abyte0;
		underlayFloorId = new byte[4][landscapeSizeX][landscapeSizeY];
		overlayFloorId = new byte[4][landscapeSizeX][landscapeSizeY];
		overlayClippingPaths = new byte[4][landscapeSizeX][landscapeSizeY];
		overlayClippingPathRotations = new byte[4][landscapeSizeX][landscapeSizeY];
		tileCullingBitsets = new int[4][landscapeSizeX + 1][landscapeSizeY + 1];
		tileShadowIntensity = new byte[4][landscapeSizeX + 1][landscapeSizeY + 1];
		tileLightingIntensity = new int[landscapeSizeX + 1][landscapeSizeY + 1];
		blendedHue = new int[landscapeSizeY];
		blendedSaturation = new int[landscapeSizeY];
		blendedLightness = new int[landscapeSizeY];
		blendedHueDivisor = new int[landscapeSizeY];
		blendDirectionTracker = new int[landscapeSizeY];
	}

	private static int getNoise(int x, int seed) {
		int n = x + seed * 57;
		n = n << 13 ^ n;
		int noise = n * (n * n * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
		return noise >> 19 & 0xff;
	}

	public final void createScene(LandscapeClippingPlane[] clippingPlane, LandscapeScene scene)
	{
		for (int plane = 0; plane < 4; plane++)
		{
			for (int x = 0; x < landscapeSizeX; x++)
			{
				for (int y = 0; y < landscapeSizeY; y++)
					if ((renderRuleFlags[plane][x][y] & 1) == 1)
					{
						int startPlane = plane;
						if ((renderRuleFlags[1][x][y] & 2) == 2)
							startPlane--;
						if (startPlane >= 0)
							clippingPlane[startPlane].markClip(y, x);
					}
			}
		}
		/*hueOffset += (int) (Math.random() * 5D) - 2;
		if (hueOffset < -8)
			hueOffset = -8;
		if (hueOffset > 8)
			hueOffset = 8;
		lightingOffset += (int) (Math.random() * 5D) - 2;
		if (lightingOffset < -16)
			lightingOffset = -16;
		if (lightingOffset > 16)
			lightingOffset = 16;*/
		for (int plane = 0; plane < 4; plane++)
		{
			byte[][] shadowIntensity = tileShadowIntensity[plane];
			byte byte0 = 96;
			char c = '\u0300';
			byte byte1 = -50;
			byte byte2 = -10;
			byte byte3 = -50;
			int j3 = (int) Math.sqrt(byte1 * byte1 + byte2 * byte2 + byte3 * byte3);
			int l3 = c * j3 >> 8;
			for (int y = 1; y < landscapeSizeY - 1; y++)
			{
				for (int x = 1; x < landscapeSizeX - 1; x++)
				{
					int k6 = vertexHeights[plane][x + 1][y] - vertexHeights[plane][x - 1][y];
					int l7 = vertexHeights[plane][x][y + 1] - vertexHeights[plane][x][y - 1];
					int j9 = (int) Math.sqrt(k6 * k6 + 0x10000 + l7 * l7);
					int k12 = (k6 << 8) / j9;
					int l13 = 0x10000 / j9;
					int j15 = (l7 << 8) / j9;
					int j16 = byte0 + (byte1 * k12 + byte2 * l13 + byte3 * j15) / l3;
					int j17 = (shadowIntensity[x - 1][y] >> 2) + (shadowIntensity[x + 1][y] >> 3) + (shadowIntensity[x][y - 1] >> 2) + (shadowIntensity[x][y + 1] >> 3) + (shadowIntensity[x][y] >> 1);
					tileLightingIntensity[x][y] = j16 - j17;
				}
			}

			for (int k5 = 0; k5 < landscapeSizeY; k5++)
			{
				blendedHue[k5] = 0;
				blendedSaturation[k5] = 0;
				blendedLightness[k5] = 0;
				blendedHueDivisor[k5] = 0;
				blendDirectionTracker[k5] = 0;
			}

			for (int l6 = -5; l6 < landscapeSizeX + 5; l6++)
			{
				for (int i8 = 0; i8 < landscapeSizeY; i8++)
				{
					int k9 = l6 + 5;
					if (k9 >= 0 && k9 < landscapeSizeX)
					{
						int l12 = underlayFloorId[plane][k9][i8] & 0xff;
						if (l12 > 0)
						{
							Floor floorDef = Floor.cache[l12 - 1];
							blendedHue[i8] += floorDef.hue;
							blendedSaturation[i8] += floorDef.saturation;
							blendedLightness[i8] += floorDef.lightness;
							blendedHueDivisor[i8] += floorDef.hueDivisor;
							blendDirectionTracker[i8]++;
						}
					}
					int i13 = l6 - 5;
					if (i13 >= 0 && i13 < landscapeSizeX)
					{
						int i14 = underlayFloorId[plane][i13][i8] & 0xff;
						if (i14 > 0)
						{
							Floor floorDef_1 = Floor.cache[i14 - 1];
							blendedHue[i8] -= floorDef_1.hue;
							blendedSaturation[i8] -= floorDef_1.saturation;
							blendedLightness[i8] -= floorDef_1.lightness;
							blendedHueDivisor[i8] -= floorDef_1.hueDivisor;
							blendDirectionTracker[i8]--;
						}
					}
				}

				if (l6 >= 1 && l6 < landscapeSizeX - 1)
				{
					int l9 = 0;
					int j13 = 0;
					int j14 = 0;
					int k15 = 0;
					int k16 = 0;
					for (int k17 = -5; k17 < landscapeSizeY + 5; k17++)
					{
						int j18 = k17 + 5;
						if (j18 >= 0 && j18 < landscapeSizeY)
						{
							l9 += blendedHue[j18];
							j13 += blendedSaturation[j18];
							j14 += blendedLightness[j18];
							k15 += blendedHueDivisor[j18];
							k16 += blendDirectionTracker[j18];
						}
						int k18 = k17 - 5;
						if (k18 >= 0 && k18 < landscapeSizeY)
						{
							l9 -= blendedHue[k18];
							j13 -= blendedSaturation[k18];
							j14 -= blendedLightness[k18];
							k15 -= blendedHueDivisor[k18];
							k16 -= blendDirectionTracker[k18];
						}
						boolean mem = true;
						if (k17 >= 1 && k17 < landscapeSizeY - 1 && (!mem || (renderRuleFlags[0][l6][k17] & 2) != 0 || (renderRuleFlags[plane][l6][k17] & 0x10) == 0 && getVisibilityPlaneFor(k17, plane, l6) == onBuildTimePlane))
						{
							if (plane < lowestPlane)
								lowestPlane = plane;
							int l18 = underlayFloorId[plane][l6][k17] & 0xff;//underlay
							int i19 = overlayFloorId[plane][l6][k17] & 0xff;//overlay
							if (l18 > 0 || i19 > 0)
							{
								int j19 = vertexHeights[plane][l6][k17];
								int k19 = vertexHeights[plane][l6 + 1][k17];
								int l19 = vertexHeights[plane][l6 + 1][k17 + 1];
								int i20 = vertexHeights[plane][l6][k17 + 1];
								int j20 = tileLightingIntensity[l6][k17];
								int k20 = tileLightingIntensity[l6 + 1][k17];
								int l20 = tileLightingIntensity[l6 + 1][k17 + 1];
								int i21 = tileLightingIntensity[l6][k17 + 1];
								int j21 = -1;
								int k21 = -1;
								if (l18 > 0)
								{
									if (k15 < 1)
										k15 = 1;
									int l21 = (l9 << 8) / k15;
									int j22 = j13 / k16;
									int l22 = j14 / k16;
									j21 = getHSLBitset(l21, j22, l22);
									/*l21 = l21 + hueOffset & 0xff;
									l22 += lightingOffset;
									if (l22 < 0)
										l22 = 0;
									else if (l22 > 255)
										l22 = 255;*/
									k21 = getHSLBitset(l21, j22, l22);
								}
								if (plane > 0)
								{
									boolean flag = true;
									if (l18 == 0 && overlayClippingPaths[plane][l6][k17] != 0)
										flag = false;
									if (i19 > 0 && Floor.cache.length > i19 - 2 && !Floor.cache[i19 - 1].occlude)
										flag = false;
									if (flag && j19 == k19 && j19 == l19 && j19 == i20)
										tileCullingBitsets[plane][l6][k17] |= 0x924;
								}
								int i22 = 0;
								if (j21 != -1)
									i22 = Rasterizer.hslToRgbLookupTable[trimHSLLightness(k21, 96)];
								if (i19 == 0)
								{
									scene.method279(plane, l6, k17, 0, 0, -1, j19, k19, l19, i20, trimHSLLightness(j21, j20), trimHSLLightness(j21, k20), trimHSLLightness(j21, l20), trimHSLLightness(j21, i21), 0, 0, 0, 0, i22, 0, j21);
								}
								else
								{
									int k22 = overlayClippingPaths[plane][l6][k17] + 1;
									byte byte4 = overlayClippingPathRotations[plane][l6][k17];
									if (i19 - 1 >= Floor.cache.length)
										i19 = 1;

									Floor floorOverlay = Floor.overlays[i19 - 1];// idk :o
									int overlayTexture = floorOverlay.textureId;
									int overlayHSL;
									int overlayRGB;
									int overlayColor = 0;
									if (floorOverlay.anInt3629 != -1)
										overlayColor = (Rasterizer.hslToRgbLookupTable[floorOverlay.anInt3629] != 1) ? Rasterizer.hslToRgbLookupTable[floorOverlay.anInt3629]
												: 0;
									//i23 = (int) (Math.random() * (double) Texture.getTotal());
									int color = -1;
									if (overlayTexture >= 0)
									{
										overlayHSL = -1;
										if (floorOverlay.anInt3631 != 0xff00ff)
										{
											overlayHSL = floorOverlay.anInt3631;
											overlayRGB = Rasterizer.hslToRgbLookupTable[overlayHSL];
											color = getRgbLookupTableId(floorOverlay.anInt3631, 96);
										}
										else
										{
											overlayRGB = 0;
											overlayHSL = -2;
											color = -1;
										}
									}
									else if (floorOverlay.anInt3631 == -1) {
										overlayRGB = overlayColor;
										overlayHSL = -2;
										overlayTexture = -1;
									} 
									else
									{
										color = getRgbLookupTableId(floorOverlay.anInt3631, 96);
										overlayHSL = floorOverlay.anInt3631;
										overlayRGB = Rasterizer.hslToRgbLookupTable[color];
									}
									scene.method279(plane, l6, k17, k22, byte4, overlayTexture, j19, k19, l19, i20, trimHSLLightness(j21, j20), trimHSLLightness(j21, k20), trimHSLLightness(j21, l20), trimHSLLightness(j21, i21), getRgbLookupTableId(overlayHSL, j20), getRgbLookupTableId(overlayHSL, k20), getRgbLookupTableId(overlayHSL, l20), getRgbLookupTableId(overlayHSL, i21), i22, overlayRGB, color);
								}
							}
						}
					}
				}
			}

			for (int y = 1; y < landscapeSizeY - 1; y++)
			{
				for (int x = 1; x < landscapeSizeX - 1; x++)
					scene.setVisiblePlanesFor(plane, x, y, getVisibilityPlaneFor(y, plane, x));
			}
		}

		scene.method305(-10, -50, -50);
		for (int j1 = 0; j1 < landscapeSizeX; j1++)
		{
			for (int l1 = 0; l1 < landscapeSizeY; l1++)
				if ((renderRuleFlags[1][j1][l1] & 2) == 2)
					scene.setBridgeMode(l1, j1);
		}

		int i2 = 1;
		int j2 = 2;
		int k2 = 4;
		for (int l2 = 0; l2 < 4; l2++)
		{
			if (l2 > 0)
			{
				i2 <<= 3;
				j2 <<= 3;
				k2 <<= 3;
			}
			for (int i3 = 0; i3 <= l2; i3++)
			{
				for (int k3 = 0; k3 <= landscapeSizeY; k3++)
				{
					for (int i4 = 0; i4 <= landscapeSizeX; i4++)
					{
						if ((tileCullingBitsets[i3][i4][k3] & i2) != 0)
						{
							int k4 = k3;
							int l5 = k3;
							int i7 = i3;
							int k8 = i3;
							for (; k4 > 0 && (tileCullingBitsets[i3][i4][k4 - 1] & i2) != 0; k4--)
								;
							for (; l5 < landscapeSizeY && (tileCullingBitsets[i3][i4][l5 + 1] & i2) != 0; l5++)
								;
							label0:
							for (; i7 > 0; i7--)
							{
								for (int j10 = k4; j10 <= l5; j10++)
									if ((tileCullingBitsets[i7 - 1][i4][j10] & i2) == 0)
										break label0;
							}

							label1:
							for (; k8 < l2; k8++)
							{
								for (int k10 = k4; k10 <= l5; k10++)
									if ((tileCullingBitsets[k8 + 1][i4][k10] & i2) == 0)
										break label1;
							}

							int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
							if (l10 >= 8)
							{
								char c1 = '\360';
								int k14 = vertexHeights[k8][i4][k4] - c1;
								int l15 = vertexHeights[i7][i4][k4];
								LandscapeScene.createCullingOcclussionBox(l2, i4 << 7, l15, i4 << 7, (l5 << 7) + 128, k14, k4 << 7, 1);
								for (int l16 = i7; l16 <= k8; l16++)
								{
									for (int l17 = k4; l17 <= l5; l17++)
										tileCullingBitsets[l16][i4][l17] &= ~i2;
								}
							}
						}
						if ((tileCullingBitsets[i3][i4][k3] & j2) != 0)
						{
							int l4 = i4;
							int i6 = i4;
							int j7 = i3;
							int l8 = i3;
							for (; l4 > 0 && (tileCullingBitsets[i3][l4 - 1][k3] & j2) != 0; l4--)
								;
							for (; i6 < landscapeSizeX && (tileCullingBitsets[i3][i6 + 1][k3] & j2) != 0; i6++)
								;
							label2:
							for (; j7 > 0; j7--)
							{
								for (int i11 = l4; i11 <= i6; i11++)
									if ((tileCullingBitsets[j7 - 1][i11][k3] & j2) == 0)
										break label2;
							}

							label3:
							for (; l8 < l2; l8++)
							{
								for (int j11 = l4; j11 <= i6; j11++)
									if ((tileCullingBitsets[l8 + 1][j11][k3] & j2) == 0)
										break label3;
							}

							int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
							if (k11 >= 8)
							{
								char c2 = '\360';
								int l14 = vertexHeights[l8][l4][k3] - c2;
								int i16 = vertexHeights[j7][l4][k3];
								LandscapeScene.createCullingOcclussionBox(l2, l4 << 7, i16, (i6 << 7) + 128, k3 << 7, l14, k3 << 7, 2);
								for (int i17 = j7; i17 <= l8; i17++)
								{
									for (int i18 = l4; i18 <= i6; i18++)
										tileCullingBitsets[i17][i18][k3] &= ~j2;
								}
							}
						}
						if ((tileCullingBitsets[i3][i4][k3] & k2) != 0)
						{
							int i5 = i4;
							int j6 = i4;
							int k7 = k3;
							int i9 = k3;
							for (; k7 > 0 && (tileCullingBitsets[i3][i4][k7 - 1] & k2) != 0; k7--)
								;
							for (; i9 < landscapeSizeY && (tileCullingBitsets[i3][i4][i9 + 1] & k2) != 0; i9++)
								;
							label4:
							for (; i5 > 0; i5--)
							{
								for (int l11 = k7; l11 <= i9; l11++)
									if ((tileCullingBitsets[i3][i5 - 1][l11] & k2) == 0)
										break label4;
							}

							label5:
							for (; j6 < landscapeSizeX; j6++)
							{
								for (int i12 = k7; i12 <= i9; i12++)
									if ((tileCullingBitsets[i3][j6 + 1][i12] & k2) == 0)
										break label5;
							}

							if (((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4)
							{
								int j12 = vertexHeights[i3][i5][k7];
								LandscapeScene.createCullingOcclussionBox(l2, i5 << 7, j12, (j6 << 7) + 128, (i9 << 7) + 128, j12, k7 << 7, 4);
								for (int k13 = i5; k13 <= j6; k13++)
								{
									for (int i15 = k7; i15 <= i9; i15++)
										tileCullingBitsets[i3][k13][i15] &= ~k2;
								}
							}
						}
					}
				}
			}
		}
	}
	
   /* public final void createLandscapeScene(LandscapeClippingPlane[] clipping_planes, LandscapeScene landscape_scene) {
        for (int plane = 0; plane < 4; plane++) {
            for (int x = 0; x < 104; x++) {
                for (int y = 0; y < 104; y++)
                    if ((renderRuleFlags[plane][x][y] & 1) == 1) {
                        int plane_ = plane;
                        if ((renderRuleFlags[1][x][y] & 2) == 2)
                            plane_--;
                        if (plane_ >= 0)
                            clipping_planes[plane_].setSolidFlag(y, x);
                    }
            }
        }
        hue_randomizer += (int) (Math.random() * 5D) - 2;
        if (hue_randomizer < -8)
            hue_randomizer = -8;
        if (hue_randomizer > 8)
            hue_randomizer = 8;
        lightness_randomizer += (int) (Math.random() * 5D) - 2;
        if (lightness_randomizer < -16)
            lightness_randomizer = -16;
        if (lightness_randomizer > 16)
            lightness_randomizer = 16;
        for (int plane = 0; plane < 4; plane++) {
            byte shadow_intensity[][] = tileShadowIntensity[plane];
            byte directional_light_initial_intensity = 96;
            char specular_distribution_factor = '\u0300'; 
            byte directional_light_x = -50; 
            byte directional_light_z = -10;
            byte directional_light_y = -50;
            int directional_light_length = 
            	(int) Math.sqrt(directional_light_x * directional_light_x 
            	+ directional_light_z * directional_light_z 
            	+ directional_light_y * directional_light_y);
            int specular_distribution = 
            	specular_distribution_factor * directional_light_length >> 8;
            for (int y = 1; y < landscapeSizeY - 1; y++) {
                for (int x = 1; x < landscapeSizeX - 1; x++) {
                    int x_height_difference = 
                    	vertexHeights[plane][x + 1][y] - vertexHeights[plane][x - 1][y];
                    int y_height_difference = 
                    	vertexHeights[plane][x][y + 1] - vertexHeights[plane][x][y - 1];
                    int normal_length = (int) Math.sqrt(x_height_difference * x_height_difference 
                    		+ 0x10000 + y_height_difference * y_height_difference);
                    int normalized_normal_x = (x_height_difference << 8) / normal_length;
                    int normalized_normal_z = 0x10000 / normal_length;
                    int normalized_normal_y = (y_height_difference << 8) / normal_length;
                    int directional_light_intensity = directional_light_initial_intensity 
                    	+ (directional_light_x * normalized_normal_x 
                    	+ directional_light_z * normalized_normal_z 
                    	+ directional_light_y * normalized_normal_y) / specular_distribution;
                    int weighted_shadow_intensity = 
                    	(shadow_intensity[x - 1][y] >> 2) 
                    	+ (shadow_intensity[x + 1][y] >> 3) 
                    	+ (shadow_intensity[x][y - 1] >> 2) 
                    	+ (shadow_intensity[x][y + 1] >> 3) 
                    	+ (shadow_intensity[x][y] >> 1);
                    tileLightingIntensity[x][y] = 
                    	directional_light_intensity - weighted_shadow_intensity;
                }
            }
            for (int y = 0; y < landscapeSizeY; y++) {
                blendedHue[y] = 0;
                blendedSaturation[y] = 0;
                blendedLightness[y] = 0;
                blendedHueDivisor[y] = 0;
                blendDirectionTracker[y] = 0;
            }
            for (int x = -5; x < landscapeSizeX + 5; x++) {
                for (int y = 0; y < landscapeSizeY; y++) {
                    int x_positive_offset = x + 5;
                    if (x_positive_offset >= 0 && x_positive_offset < landscapeSizeX) {
                        int floor_id = underlayFloorId[plane][x_positive_offset][y] & 0xff;
                        if (floor_id > 0) {
                            Floor floor = Floor.cache[floor_id - 1];
                            blendedHue[y] += floor.hue; // hue 0 - 99
                            blendedSaturation[y] += floor.saturation; // saturation 0 - 255
                            blendedLightness[y] += floor.lightness; // lightness 0 - 255
                            blendedHueDivisor[y] += floor.hueDivisor;
                            blendDirectionTracker[y]++;
                        }
                    }
                    int x_negative_offset = x - 5;
                    if (x_negative_offset >= 0 && x_negative_offset < landscapeSizeX) {
                        int floor_id = underlayFloorId[plane][x_negative_offset][y] & 0xff;
                        if (floor_id > 0) {
                            Floor floor = Floor.cache[floor_id - 1];
                            blendedHue[y] -= floor.hue; // hue
                            blendedSaturation[y] -= floor.saturation;
                            blendedLightness[y] -= floor.lightness;
                            blendedHueDivisor[y] -= floor.hueDivisor;
                            blendDirectionTracker[y]--;
                        }
                    }
                }
                if (x >= 1 && x < landscapeSizeX - 1) {
                    int blended_hue = 0;
                    int blended_saturation = 0;
                    int blended_lightness = 0;
                    int blended_hue_divisor = 0;
                    int blend_direction_tracker = 0;
                    for (int y = -5; y < landscapeSizeY + 5; y++) {
                        int y_positive_offset = y + 5;
                        if (y_positive_offset >= 0 && y_positive_offset < landscapeSizeY) {
                            blended_hue += blendedHue[y_positive_offset];
                            blended_saturation += blendedSaturation[y_positive_offset];
                            blended_lightness += blendedLightness[y_positive_offset];
                            blended_hue_divisor += blendedHueDivisor[y_positive_offset];
                            blend_direction_tracker += blendDirectionTracker[y_positive_offset];
                        }
                        int y_negative_offset = y - 5;
                        if (y_negative_offset >= 0 && y_negative_offset < landscapeSizeY) {
                            blended_hue -= blendedHue[y_negative_offset];
                            blended_saturation -= blendedSaturation[y_negative_offset];
                            blended_lightness -= blendedLightness[y_negative_offset];
                            blended_hue_divisor -= blendedHueDivisor[y_negative_offset];
                            blend_direction_tracker -= blendDirectionTracker[y_negative_offset];
                        }
                        if (y >= 1 && y < landscapeSizeY - 1 
                        		&& (!lowMem || (renderRuleFlags[0][x][y] & 2) != 0 
                        				|| (renderRuleFlags[plane][x][y] & 0x10) == 0 
                        				&& getVisibilityPlaneFor(y, plane, x) == onBuildTimePlane)) {
                            if (plane < lowestPlane)
                                lowestPlane = plane;
                            int underlay_floor_id = underlayFloorId[plane][x][y] & 0xff;
                            int overlay_floor_id = overlayFloorId[plane][x][y] & 0xff;
                            if (underlay_floor_id > 0 || overlay_floor_id > 0) {
                                int v_sw = vertexHeights[plane][x][y];
                                int v_se = vertexHeights[plane][x + 1][y];
                                int v_ne = vertexHeights[plane][x + 1][y + 1];
                                int v_nw = vertexHeights[plane][x][y + 1];
                                int l_sw = tileLightingIntensity[x][y];
                                int l_se = tileLightingIntensity[x + 1][y];
                                int l_ne = tileLightingIntensity[x + 1][y + 1];
                                int l_nw = tileLightingIntensity[x][y + 1];
                                int hsl_bitset_unmodified = -1;
                                int hsl_bitset_randomized = -1;
                                if (underlay_floor_id > 0) {
                                    int hue = (blended_hue * 256) / blended_hue_divisor;
                                    int saturation = blended_saturation / blend_direction_tracker;
                                    int lightness = blended_lightness / blend_direction_tracker;
                                    hsl_bitset_unmodified = getHSLBitset(hue, saturation, lightness);
                                    hue = hue + hue_randomizer & 0xff;
                                    lightness += lightness_randomizer;
                                    if (lightness < 0)
                                        lightness = 0;
                                    else if (lightness > 255)
                                        lightness = 255;
                                    hsl_bitset_randomized = getHSLBitset(hue, saturation, lightness);
                                }
                                if (plane > 0) {
                                    boolean hide_underlay = true;
                                    if (underlay_floor_id == 0 && overlayClippingPaths[plane][x][y] != 0)
                                        hide_underlay = false;
                                    if (overlay_floor_id > 0 && !Floor.cache[overlay_floor_id - 1].occlude)
                                        hide_underlay = false;
                                    if (hide_underlay && v_sw == v_se && v_sw == v_ne && v_sw == v_nw)
                                        tileCullingBitsets[plane][x][y] |= 0x924;
                                }
                                int rgb_bitset_randomized = 0;
                                if (hsl_bitset_unmodified != -1)
                                    rgb_bitset_randomized = 
                                    	Rasterizer.hslToRgbLookupTable[trimHSLLightness(hsl_bitset_randomized, 96)];
                                if (overlay_floor_id == 0) { // perlin noise generated area drawing
                                    landscape_scene.method279(plane, x, y, 0, 0, -1, 
                                    		v_sw, v_se, v_ne, v_nw, 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_sw), 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_se), 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_ne), 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_nw), 
                                    		0, 0, 0, 0, 
                                    		rgb_bitset_randomized, 0);
                                } else { // overlay area drawing
                                    int clipping_path = overlayClippingPaths[plane][x][y] + 1;
                                    byte clipping_path_rotation = overlayClippingPathRotations[plane][x][y];
                                    Floor floor = Floor.overlays[overlay_floor_id - 1];
                                    int textureId = floor.textureId;
                                    int hsl_bitset;
                                    int rgb_bitset;
                                    if (textureId >= 0) {
                                        rgb_bitset = Texture.getAverageRgbColourForTexture(textureId);
                                        hsl_bitset = -1;
                                    } else if (floor.rgbColour2 == 0xff00ff) {
                                        rgb_bitset = 0;
                                        hsl_bitset = -2;
                                        textureId = -1;
                                    } else {
                                        hsl_bitset = getHSLBitset(floor.hue2, floor.saturation, floor.lightness);
                                        rgb_bitset = Texture.hslToRgbLookupTable[getRgbLookupTableId(floor.hslColour2, 96)];
                                    }
                                    landscape_scene.method279(plane, x, y, clipping_path, clipping_path_rotation, textureId, 
                                    		v_sw, v_se, v_ne, v_nw, 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_sw), 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_se), 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_ne), 
                                    		trimHSLLightness(hsl_bitset_unmodified, l_nw), 
                                    		getRgbLookupTableId(hsl_bitset, l_sw), getRgbLookupTableId(hsl_bitset, l_se), 
                                    		getRgbLookupTableId(hsl_bitset, l_ne), getRgbLookupTableId(hsl_bitset, l_nw), 
                                    		rgb_bitset_randomized, rgb_bitset);
                                }
                            }
                        }
                    }
                }
            }
            for (int y = 1; y < landscapeSizeY - 1; y++) {
                for (int x = 1; x < landscapeSizeX - 1; x++)
                    landscape_scene.setVisiblePlanesFor(plane, x, y, getVisibilityPlaneFor(y, plane, x));
            }
        }
        landscape_scene.applyDLNonTexturedObjects(-10, -50, -50);
        for (int j1 = 0; j1 < landscapeSizeX; j1++) {
            for (int l1 = 0; l1 < landscapeSizeY; l1++)
                if ((renderRuleFlags[1][j1][l1] & 2) == 2)
                    landscape_scene.setBridgeMode(l1, j1);
        }
        int render_rule1 = 1;
        int render_rule2 = 2;
        int render_rule3 = 4;
        for (int current_plane = 0; current_plane < 4; current_plane++) {
            if (current_plane > 0) {
                render_rule1 <<= 3; // 0x8
                render_rule2 <<= 3; // 0x10
                render_rule3 <<= 3; // 0x20
            }
            for (int plane = 0; plane <= current_plane; plane++) {
                for (int y = 0; y <= landscapeSizeY; y++) {
                    for (int x = 0; x <= landscapeSizeX; x++) {
                        if ((tileCullingBitsets[plane][x][y] & render_rule1) != 0) {
                            int lowest_occlussion_y = y;
                            int highest_occlussion_y = y;
                            int lowest_occlussion_plane = plane;
                            int highest_occlussion_plane = plane;
                            for (; lowest_occlussion_y > 0 
                            	&& (tileCullingBitsets[plane][x][lowest_occlussion_y - 1] & render_rule1) != 0; 
                            		lowest_occlussion_y--);
                            for (; highest_occlussion_y < landscapeSizeY 
                            	&& (tileCullingBitsets[plane][x][highest_occlussion_y + 1] & render_rule1) != 0; 
                            		highest_occlussion_y++);
                            find_lowest_occlussion_plane:
                            for (; lowest_occlussion_plane > 0; lowest_occlussion_plane--) {
                                for (int occluded_y = lowest_occlussion_y; occluded_y <= highest_occlussion_y; occluded_y++)
                                    if ((tileCullingBitsets[lowest_occlussion_plane - 1][x][occluded_y] & render_rule1) == 0)
                                        break find_lowest_occlussion_plane;
                            }
                            find_highest_occlussion_plane:
                            for (; highest_occlussion_plane < current_plane; highest_occlussion_plane++) {
                                for (int occluded_y = lowest_occlussion_y; occluded_y <= highest_occlussion_y; occluded_y++)
                                    if ((tileCullingBitsets[highest_occlussion_plane + 1][x][occluded_y] & render_rule1) == 0)
                                        break find_highest_occlussion_plane;
                            }
                            int occlussion_surface = ((highest_occlussion_plane + 1) - lowest_occlussion_plane) 
                            	* ((highest_occlussion_y - lowest_occlussion_y) + 1);
                            if (occlussion_surface >= 8) {
                                char c1 = '\360'; // 240
                                int highest_occlussion_vertex_height = 
                                	vertexHeights[highest_occlussion_plane][x][lowest_occlussion_y] - c1;
                                int lowest_occlussion_vertex_height = 
                                	vertexHeights[lowest_occlussion_plane][x][lowest_occlussion_y];
                                LandscapeScene.createCullingOcclussionBox(current_plane, x * 128, 
                                		lowest_occlussion_vertex_height, x * 128, highest_occlussion_y * 128 + 128, 
                                		highest_occlussion_vertex_height, lowest_occlussion_y * 128, 1);
                                for (int occluded_plane = lowest_occlussion_plane; 
                                	occluded_plane <= highest_occlussion_plane; occluded_plane++) {
                                    for (int occluded_y = lowest_occlussion_y; occluded_y <= highest_occlussion_y; occluded_y++)
                                        tileCullingBitsets[occluded_plane][x][occluded_y] &= ~render_rule1;
                                }
                            }
                        }
                        if ((tileCullingBitsets[plane][x][y] & render_rule2) != 0) {
                            int lowest_occlussion_x = x;
                            int highest_occlussion_x = x;
                            int lowest_occlussion_plane = plane;
                            int highest_occlussion_plane = plane;
                            for (; lowest_occlussion_x > 0 
                            	&& (tileCullingBitsets[plane][lowest_occlussion_x - 1][y] & render_rule2) != 0; 
                            		lowest_occlussion_x--) ;
                            for (; highest_occlussion_x < landscapeSizeX 
                            	&& (tileCullingBitsets[plane][highest_occlussion_x + 1][y] & render_rule2) != 0; 
                            		highest_occlussion_x++) ;
                            find_lowest_occlussion_plane:
                            for (; lowest_occlussion_plane > 0; lowest_occlussion_plane--) {
                                for (int occluded_x = lowest_occlussion_x; occluded_x <= highest_occlussion_x; occluded_x++)
                                    if ((tileCullingBitsets[lowest_occlussion_plane - 1][occluded_x][y] & render_rule2) == 0)
                                        break find_lowest_occlussion_plane;
                            }
                            find_highest_occlussion_plane:
                            for (; highest_occlussion_plane < current_plane; highest_occlussion_plane++) {
                                for (int ocluded_x = lowest_occlussion_x; ocluded_x <= highest_occlussion_x; ocluded_x++)
                                    if ((tileCullingBitsets[highest_occlussion_plane + 1][ocluded_x][y] & render_rule2) == 0)
                                        break find_highest_occlussion_plane;
                            }
                            int occlussion_surface = ((highest_occlussion_plane + 1) - lowest_occlussion_plane) 
                            	* ((highest_occlussion_x - lowest_occlussion_x) + 1);
                            if (occlussion_surface >= 8) {
                                char c2 = '\360'; // 240
                                int highest_occlussion_vertex_height = 
                                	vertexHeights[highest_occlussion_plane][lowest_occlussion_x][y] - c2;
                                int lowest_occlussion_vertex_height = 
                                	vertexHeights[lowest_occlussion_plane][lowest_occlussion_x][y];
                                LandscapeScene.createCullingOcclussionBox(current_plane, lowest_occlussion_x * 128, 
                                		lowest_occlussion_vertex_height, highest_occlussion_x * 128 + 128, y * 128, 
                                		highest_occlussion_vertex_height, y * 128, 2);
                                for (int occluded_plane = lowest_occlussion_plane; 
                                	occluded_plane <= highest_occlussion_plane; occluded_plane++) {
                                    for (int occluded_x = lowest_occlussion_x; occluded_x <= highest_occlussion_x; occluded_x++)
                                        tileCullingBitsets[occluded_plane][occluded_x][y] &= ~render_rule2;
                                }
                            }
                        }
                        if ((tileCullingBitsets[plane][x][y] & render_rule3) != 0) {
                            int lowest_occlusion_x = x;
                            int highest_occlusion_x = x;
                            int lowest_occlusion_y = y;
                            int highest_occlussion_y = y;
                            for (; lowest_occlusion_y > 0 
                            	&& (tileCullingBitsets[plane][x][lowest_occlusion_y - 1] & render_rule3) != 0; 
                            		lowest_occlusion_y--) ;
                            for (; highest_occlussion_y < landscapeSizeY 
                            	&& (tileCullingBitsets[plane][x][highest_occlussion_y + 1] & render_rule3) != 0; 
                            		highest_occlussion_y++) ;
                            find_lowest_occlussion_x:
                            for (; lowest_occlusion_x > 0; lowest_occlusion_x--) {
                                for (int occluded_y = lowest_occlusion_y; occluded_y <= highest_occlussion_y; occluded_y++)
                                    if ((tileCullingBitsets[plane][lowest_occlusion_x - 1][occluded_y] & render_rule3) == 0)
                                        break find_lowest_occlussion_x;
                            }
                            find_highest_occlussion_x:
                            for (; highest_occlusion_x < landscapeSizeX; highest_occlusion_x++) {
                                for (int occluded_y = lowest_occlusion_y; occluded_y <= highest_occlussion_y; occluded_y++)
                                    if ((tileCullingBitsets[plane][highest_occlusion_x + 1][occluded_y] & render_rule3) == 0)
                                        break find_highest_occlussion_x;
                            }
                            if (((highest_occlusion_x - lowest_occlusion_x) + 1) 
                            		* ((highest_occlussion_y - lowest_occlusion_y) + 1) >= 4) {
                                int lowest_occlussion_vertex_height = 
                                	vertexHeights[plane][lowest_occlusion_x][lowest_occlusion_y];
                                LandscapeScene.createCullingOcclussionBox(current_plane, lowest_occlusion_x * 128, 
                                		lowest_occlussion_vertex_height, highest_occlusion_x * 128 + 128, 
                                		highest_occlussion_y * 128 + 128, lowest_occlussion_vertex_height, lowest_occlusion_y * 128, 4);
                                for (int occluded_x = lowest_occlusion_x; occluded_x <= highest_occlusion_x; occluded_x++) {
                                    for (int occluded_y = lowest_occlusion_y; occluded_y <= highest_occlussion_y; occluded_y++)
                                        tileCullingBitsets[plane][occluded_x][occluded_y] &= ~render_rule3;
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/

	private static int getVertexHeight(int x, int y)
	{
		int height = (method176(x + 45365, y + 0x16713, 4) - 128) + (method176(x + 10294, y + 37821, 2) - 128 >> 1) + (method176(x, y, 1) - 128 >> 2);
		height = (int) ((double) height * 0.29999999999999999D) + 35;
		if (height < 10)
			height = 10;
		else if (height > 60)
			height = 60;
		return height;
	}

	public static void requestGameObjectModel(JagexBuffer buffer, ResourceProvider updateManager)
	{
		label0:
		{
			int i = -1;
			do
			{
				int objectIdOffset = buffer.getSmartB();
				if (objectIdOffset == 0)
					break label0;
				i += objectIdOffset;
				ObjectDefinition definition = ObjectDefinition.forId(i);
				definition.requestModel(updateManager);
				do
				{
					int k = buffer.getSmartB();
					if (k == 0)
						break;
					buffer.readUnsignedByte();
				}
				while (true);
			}
			while (true);
		}
	}

	public final void loadVertexHeights(int offsetY, int lengthY, int lengthX, int offsetX)
	{
		for (int y = offsetY; y <= offsetY + lengthY; y++)
		{
			for (int x = offsetX; x <= offsetX + lengthX; x++)
				if (x >= 0 && x < landscapeSizeX && y >= 0 && y < landscapeSizeY)
				{
					tileShadowIntensity[0][x][y] = 127;
					if (x == offsetX && x > 0)
						vertexHeights[0][x][y] = vertexHeights[0][x - 1][y];
					if (x == offsetX + lengthX && x < landscapeSizeX - 1)
						vertexHeights[0][x][y] = vertexHeights[0][x + 1][y];
					if (y == offsetY && y > 0)
						vertexHeights[0][x][y] = vertexHeights[0][x][y - 1];
					if (y == offsetY + lengthY && y < landscapeSizeY - 1)
						vertexHeights[0][x][y] = vertexHeights[0][x][y + 1];
				}
		}
	}

	private void renderObject(int i, LandscapeScene worldRenderer, LandscapeClippingPlane clipping, int type, int height, int l, int objectId, int face)
	{
		if (height < 0 || height >= 4)
			return;

		if (lowMemory && (renderRuleFlags[0][l][i] & 2) == 0)
		{
			if ((renderRuleFlags[height][l][i] & 0x10) != 0)
				return;
			if (getVisibilityPlaneFor(i, height, l) != onBuildTimePlane)
				return;
		}//TODO populates clipping
		if (height < lowestPlane)
			lowestPlane = height;
		int vertexHeight = vertexHeights[height][l][i];
		int vertexHeightRight = vertexHeights[height][l + 1][i];
		int vertexHeightTopRight = vertexHeights[height][l + 1][i + 1];
		int vertexHeightTop = vertexHeights[height][l][i + 1];
		int vertexMix = vertexHeight + vertexHeightRight + vertexHeightTopRight + vertexHeightTop >> 2;
		ObjectDefinition definition = ObjectDefinition.forId(objectId);
		int hash = l + (i << 7) + ((objectId & 0x7fff) << 14) + 0x40000000;
		if (!definition.hasActions)
			hash += 0x80000000;
		byte objectConfig = (byte) ((face << 6) + type);
		if (type == 22)
		{
			if (lowMemory && !definition.hasActions && !definition.aBoolean736)
				return;
			Object obj;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj = definition.getModelFromPosition(22, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj = new GameObject(objectId, face, 22, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method280(height, vertexMix, i, ((Animable) (obj)), objectConfig, hash, l, objectId);
			//if (class46.aBoolean767 && class46.hasActions && clipping != null)
			//	clipping.method213(i, l);
			return;
		}
		if (type == 10 || type == 11)
		{
			Object obj1;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj1 = definition.getModelFromPosition(10, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj1 = new GameObject(objectId, face, 10, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			if (obj1 != null)
			{
				int i5 = 0;
				if (type == 11)
					i5 += 256;
				int sizeX;
				int sizeY;
				if (face == 1 || face == 3)
				{
					sizeX = definition.sizeY;
					sizeY = definition.sizeX;
				}
				else
				{
					sizeX = definition.sizeX;
					sizeY = definition.sizeY;
				}
				if (worldRenderer.method284(hash, objectConfig, vertexMix, sizeY, ((Animable) (obj1)), sizeX, height, i5, i, l, objectId) && definition.aBoolean779)
				{
					Model model;
					if (obj1 instanceof Model)
						model = (Model) obj1;
					else
						model = definition.getModelFromPosition(10, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
					//j4: width
					//l4: height
					if (model != null)
					{
						for (int indexSizeX = 0; indexSizeX <= sizeX; indexSizeX++)
						{
							for (int indexSizeY = 0; indexSizeY <= sizeY; indexSizeY++)
							{
								int shadowIntensity = model.shadowIntensity >> 2;
								if (shadowIntensity > 30)
									shadowIntensity = 30;
								if (shadowIntensity > tileShadowIntensity[height][l + indexSizeX][i + indexSizeY])
									tileShadowIntensity[height][l + indexSizeX][i + indexSizeY] = (byte) shadowIntensity;
							}
						}
					}
				}
			}
			if (definition.walkable && clipping != null)
				clipping.markSolidClip(definition.aBoolean757, definition.sizeX, definition.sizeY, l, i, face);
			return;
		}
		if (type >= 12)
		{
			Object obj2;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj2 = definition.getModelFromPosition(type, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj2 = new GameObject(objectId, face, type, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method284(hash, objectConfig, vertexMix, 1, ((Animable) (obj2)), 1, height, 0, i, l, objectId);
			if (type >= 12 && type <= 17 && type != 13 && height > 0)
				tileCullingBitsets[height][l][i] |= 0x924;
			if (definition.walkable && clipping != null)
				clipping.markSolidClip(definition.aBoolean757, definition.sizeX, definition.sizeY, l, i, face);
			return;
		}
		if (type == 0)
		{
			Object obj3;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj3 = definition.getModelFromPosition(0, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj3 = new GameObject(objectId, face, 0, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method282(anIntArray152[face], ((Animable) (obj3)), hash, i, objectConfig, l, null, vertexMix, 0, height, objectId);
			if (face == 0)
			{
				if (definition.aBoolean779)
				{
					tileShadowIntensity[height][l][i] = 50;
					tileShadowIntensity[height][l][i + 1] = 50;
				}
				if (definition.aBoolean764)
					tileCullingBitsets[height][l][i] |= 0x249;
			}
			else if (face == 1)
			{
				if (definition.aBoolean779)
				{
					tileShadowIntensity[height][l][i + 1] = 50;
					tileShadowIntensity[height][l + 1][i + 1] = 50;
				}
				if (definition.aBoolean764)
					tileCullingBitsets[height][l][i + 1] |= 0x492;
			}
			else if (face == 2)
			{
				if (definition.aBoolean779)
				{
					tileShadowIntensity[height][l + 1][i] = 50;
					tileShadowIntensity[height][l + 1][i + 1] = 50;
				}
				if (definition.aBoolean764)
					tileCullingBitsets[height][l + 1][i] |= 0x249;
			}
			else if (face == 3)
			{
				if (definition.aBoolean779)
				{
					tileShadowIntensity[height][l][i] = 50;
					tileShadowIntensity[height][l + 1][i] = 50;
				}
				if (definition.aBoolean764)
					tileCullingBitsets[height][l][i] |= 0x492;
			}
			if (definition.walkable && clipping != null)
				clipping.markWall(i, face, l, type, definition.aBoolean757);
			if (definition.anInt775 != 16)
				worldRenderer.method290(i, definition.anInt775, l, height);
			return;
		}
		if (type == 1)
		{
			Object obj4;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj4 = definition.getModelFromPosition(1, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj4 = new GameObject(objectId, face, 1, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method282(anIntArray140[face], ((Animable) (obj4)), hash, i, objectConfig, l, null, vertexMix, 0, height, objectId);
			if (definition.aBoolean779)
				if (face == 0)
					tileShadowIntensity[height][l][i + 1] = 50;
				else if (face == 1)
					tileShadowIntensity[height][l + 1][i + 1] = 50;
				else if (face == 2)
					tileShadowIntensity[height][l + 1][i] = 50;
				else if (face == 3)
					tileShadowIntensity[height][l][i] = 50;
			if (definition.walkable && clipping != null)
				clipping.markWall(i, face, l, type, definition.aBoolean757);
			return;
		}
		if (type == 2)
		{
			int i3 = face + 1 & 3;
			Object obj11;
			Object obj12;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
			{
				obj11 = definition.getModelFromPosition(2, 4 + face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
				obj12 = definition.getModelFromPosition(2, i3, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			}
			else
			{
				obj11 = new GameObject(objectId, 4 + face, 2, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
				obj12 = new GameObject(objectId, i3, 2, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			}
			worldRenderer.method282(anIntArray152[face], ((Animable) (obj11)), hash, i, objectConfig, l, ((Animable) (obj12)), vertexMix, anIntArray152[i3], height, objectId);
			if (definition.aBoolean764)
				if (face == 0)
				{
					tileCullingBitsets[height][l][i] |= 0x249;
					tileCullingBitsets[height][l][i + 1] |= 0x492;
				}
				else if (face == 1)
				{
					tileCullingBitsets[height][l][i + 1] |= 0x492;
					tileCullingBitsets[height][l + 1][i] |= 0x249;
				}
				else if (face == 2)
				{
					tileCullingBitsets[height][l + 1][i] |= 0x249;
					tileCullingBitsets[height][l][i] |= 0x492;
				}
				else if (face == 3)
				{
					tileCullingBitsets[height][l][i] |= 0x492;
					tileCullingBitsets[height][l][i] |= 0x249;
				}
			if (definition.walkable && clipping != null)
				clipping.markWall(i, face, l, type, definition.aBoolean757);
			if (definition.anInt775 != 16)
				worldRenderer.method290(i, definition.anInt775, l, height);
			return;
		}
		if (type == 3)
		{
			Object obj5;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj5 = definition.getModelFromPosition(3, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj5 = new GameObject(objectId, face, 3, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method282(anIntArray140[face], ((Animable) (obj5)), hash, i, objectConfig, l, null, vertexMix, 0, height, objectId);
			if (definition.aBoolean779)
				if (face == 0)
					tileShadowIntensity[height][l][i + 1] = 50;
				else if (face == 1)
					tileShadowIntensity[height][l + 1][i + 1] = 50;
				else if (face == 2)
					tileShadowIntensity[height][l + 1][i] = 50;
				else if (face == 3)
					tileShadowIntensity[height][l][i] = 50;
			if (definition.walkable && clipping != null)
				clipping.markWall(i, face, l, type, definition.aBoolean757);
			return;
		}
		if (type == 9)
		{
			Object obj6;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj6 = definition.getModelFromPosition(type, face, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj6 = new GameObject(objectId, face, type, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method284(hash, objectConfig, vertexMix, 1, ((Animable) (obj6)), 1, height, 0, i, l, objectId);
			if (definition.walkable && clipping != null)
				clipping.markSolidClip(definition.aBoolean757, definition.sizeX, definition.sizeY, l, i, face);
			return;
		}
		if (definition.adjustToTerrain)
			if (face == 1)
			{
				int j3 = vertexHeightTop;
				vertexHeightTop = vertexHeightTopRight;
				vertexHeightTopRight = vertexHeightRight;
				vertexHeightRight = vertexHeight;
				vertexHeight = j3;
			}
			else if (face == 2)
			{
				int k3 = vertexHeightTop;
				vertexHeightTop = vertexHeightRight;
				vertexHeightRight = k3;
				k3 = vertexHeightTopRight;
				vertexHeightTopRight = vertexHeight;
				vertexHeight = k3;
			}
			else if (face == 3)
			{
				int l3 = vertexHeightTop;
				vertexHeightTop = vertexHeight;
				vertexHeight = vertexHeightRight;
				vertexHeightRight = vertexHeightTopRight;
				vertexHeightTopRight = l3;
			}
		if (type == 4)
		{
			Object obj7;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj7 = definition.getModelFromPosition(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj7 = new GameObject(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method283(hash, i, face << 9, height, 0, vertexMix, ((Animable) (obj7)), l, objectConfig, 0, anIntArray152[face]);
			return;
		}
		if (type == 5)
		{
			int i4 = 16;
			int k4 = worldRenderer.method300(height, l, i);
			if (k4 > 0)
				i4 = ObjectDefinition.forId(worldRenderer.method300_new(height, l, i)).anInt775;
			Object obj13;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj13 = definition.getModelFromPosition(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj13 = new GameObject(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method283(hash, i, face << 9, height, anIntArray137[face] * i4, vertexMix, ((Animable) (obj13)), l, objectConfig, anIntArray144[face] * i4, anIntArray152[face]);
			return;
		}
		if (type == 6)
		{
			Object obj8;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj8 = definition.getModelFromPosition(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj8 = new GameObject(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method283(hash, i, face, height, 0, vertexMix, ((Animable) (obj8)), l, objectConfig, 0, 256);
			return;
		}
		if (type == 7)
		{
			Object obj9;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj9 = definition.getModelFromPosition(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj9 = new GameObject(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method283(hash, i, face, height, 0, vertexMix, ((Animable) (obj9)), l, objectConfig, 0, 512);
			return;
		}
		if (type == 8)
		{
			Object obj10;
			if (definition.anInt781 == -1 && definition.childrenIDs == null)
				obj10 = definition.getModelFromPosition(4, 0, vertexHeight, vertexHeightRight, vertexHeightTopRight, vertexHeightTop, -1, null);
			else
				obj10 = new GameObject(objectId, 0, 4, vertexHeightRight, vertexHeightTopRight, vertexHeight, vertexHeightTop, definition.anInt781, true);
			worldRenderer.method283(hash, i, face, height, 0, vertexMix, ((Animable) (obj10)), l, objectConfig, 0, 768);
		}
	}

	private static int method176(int i, int j, int k)
	{
		int l = i / k;
		int i1 = i & k - 1;
		int j1 = j / k;
		int k1 = j & k - 1;
		int l1 = method186(l, j1);
		int i2 = method186(l + 1, j1);
		int j2 = method186(l, j1 + 1);
		int k2 = method186(l + 1, j1 + 1);
		int l2 = method184(l1, i2, i1, k);
		int i3 = method184(j2, k2, i1, k);
		return method184(l2, i3, k1, k);
	}

	private int getHSLBitset(int i, int j, int k)
	{
		if (k > 179)
			j >>= 1;
		if (k > 192)
			j >>= 1;
		if (k > 217)
			j >>= 1;
		if (k > 243)
			j >>= 1;
		return ((i >> 2) << 10) + ((j >> 5) << 7) + (k >> 1);
	}

	public static boolean method178(int i, int j)
	{
		ObjectDefinition class46 = ObjectDefinition.forId(i);
		if (j == 11)
			j = 10;
		if (j >= 5 && j <= 8)
			j = 4;
		return class46.method577(j);
	}

	public final void method179(int i, int j, LandscapeClippingPlane aclass11[], int l, int i1, byte abyte0[], int j1, int k1, int l1)
	{
		for (int i2 = 0; i2 < 8; i2++)
		{
			for (int j2 = 0; j2 < 8; j2++)
				if (l + i2 > 0 && l + i2 < landscapeSizeX - 1 && l1 + j2 > 0 && l1 + j2 < landscapeSizeY - 1)
					aclass11[k1].clip[l + i2][l1 + j2] &= 0xfeffffff;
		}
		JagexBuffer buffer = new JagexBuffer(abyte0);
		for (int l2 = 0; l2 < 4; l2++)
		{
			for (int i3 = 0; i3 < 64; i3++)
			{
				for (int j3 = 0; j3 < 64; j3++)
					if (l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1 && j3 < j1 + 8)
						method181(l1 + Class4.method156(j3 & 7, j, i3 & 7), 0, buffer, l + Class4.method155(j, j3 & 7, i3 & 7), k1, j, 0);
					else
						method181(-1, 0, buffer, -1, 0, 0, 0);
			}
		}
	}

	public final void method180(byte abyte0[], int i, int j, int k, int l, LandscapeClippingPlane aclass11[])
	{
		for (int i1 = 0; i1 < 4; i1++)
		{
			for (int j1 = 0; j1 < 64; j1++)
			{
				for (int k1 = 0; k1 < 64; k1++)
					if (j + j1 > 0 && j + j1 < landscapeSizeX - 1 && i + k1 > 0 && i + k1 < landscapeSizeY - 1)
						aclass11[i1].clip[j + j1][i + k1] &= 0xfeffffff;
			}
		}

		JagexBuffer buffer = new JagexBuffer(abyte0);
		for (int l1 = 0; l1 < 4; l1++)
		{
			for (int i2 = 0; i2 < 64; i2++)
			{
				for (int j2 = 0; j2 < 64; j2++)
					method181(j2 + i, l, buffer, i2 + j, l1, 0, k);
			}
		}
	}

	private void method181(int i, int j, JagexBuffer buffer, int k, int l, int i1, int k1)
	{
		if (k >= 0 && k < landscapeSizeX && i >= 0 && i < landscapeSizeY)
		{
			renderRuleFlags[l][k][i] = 0;
			do
			{
				int l1 = buffer.readUnsignedByte();
				if (l1 == 0)
					if (l == 0)
					{
						vertexHeights[0][k][i] = (-getVertexHeight(0xe3b7b + k + k1, 0x87cce + i + j)) << 3;
						return;
					}
					else
					{
						vertexHeights[l][k][i] = vertexHeights[l - 1][k][i] - 240;
						return;
					}
				if (l1 == 1)
				{
					int j2 = buffer.readUnsignedByte();
					if (j2 == 1)
						j2 = 0;
					if (l == 0)
					{
						vertexHeights[0][k][i] = (-j2) << 3;
						return;
					}
					else
					{
						vertexHeights[l][k][i] = vertexHeights[l - 1][k][i] - (j2 << 3);
						return;
					}
				}
				if (l1 <= 49)
				{
					overlayFloorId[l][k][i] = buffer.readByte();
					overlayClippingPaths[l][k][i] = (byte) ((l1 - 2) >> 2);
					overlayClippingPathRotations[l][k][i] = (byte) ((l1 - 2) + i1 & 3);
				}
				else if (l1 <= 81)
					renderRuleFlags[l][k][i] = (byte) (l1 - 49);
				else
					underlayFloorId[l][k][i] = (byte) (l1 - 81);
			}
			while (true);
		}
		do
		{
			int i2 = buffer.readUnsignedByte();
			if (i2 == 0)
				break;
			if (i2 == 1)
			{
				buffer.readUnsignedByte();
				return;
			}
			if (i2 <= 49)
				buffer.readUnsignedByte();
		}
		while (true);
	}

	private int getVisibilityPlaneFor(int i, int j, int k)
	{
		if ((renderRuleFlags[j][k][i] & 8) != 0)
			return 0;
		if (j > 0 && (renderRuleFlags[1][k][i] & 2) != 0)
			return j - 1;
		else
			return j;
	}

	public final void method183(LandscapeClippingPlane aclass11[], LandscapeScene worldRenderer, int i, int j, int k, int l, byte abyte0[], int i1, int j1, int k1)
	{
		label0:
		{
			JagexBuffer buffer = new JagexBuffer(abyte0);
			int l1 = -1;
			do
			{
				int i2 = buffer.getSmartB();
				if (i2 == 0)
					break label0;
				l1 += i2;
				int j2 = 0;
				do
				{
					int k2 = buffer.getSmartB();
					if (k2 == 0)
						break;
					j2 += k2 - 1;
					int l2 = j2 & 0x3f;
					int i3 = j2 >> 6 & 0x3f;
					int j3 = j2 >> 12;
					int k3 = buffer.readUnsignedByte();
					int l3 = k3 >> 2;
					int i4 = k3 & 3;
					if (j3 == i && i3 >= i1 && i3 < i1 + 8 && l2 >= k && l2 < k + 8)
					{
						ObjectDefinition class46 = ObjectDefinition.forId(l1);
						int j4 = j + Class4.method157(j1, class46.sizeY, i3 & 7, l2 & 7, class46.sizeX);
						int k4 = k1 + Class4.method158(l2 & 7, class46.sizeY, j1, class46.sizeX, i3 & 7);
						if (j4 > 0 && k4 > 0 && j4 < landscapeSizeX - 1 && k4 < landscapeSizeY - 1)
						{
							int l4 = j3;
							if ((renderRuleFlags[1][j4][k4] & 2) == 2)
								l4--;
							LandscapeClippingPlane clipping = null;
							if (l4 >= 0)
								clipping = aclass11[l4];
							renderObject(k4, worldRenderer, clipping, l3, l, j4, l1, i4 + j1 & 3);
						}
					}
				}
				while (true);
			}
			while (true);
		}
	}

	private static int method184(int i, int j, int k, int l)
	{
		int i1 = 0x10000 - Rasterizer.COSINE[(k << 10) / l] >> 1;
		return (i * (0x10000 - i1) >> 16) + (j * i1 >> 16);
	}

	private int getRgbLookupTableId(int i, int j)
	{
		if (i == -2)
			return 0xbc614e;
		if (i == -1)
		{
			if (j < 0)
				j = 0;
			else if (j > 127)
				j = 127;
			j = 127 - j;
			return j;
		}
		j = (j * (i & 0x7f)) >> 7;
		if (j < 2)
			j = 2;
		else if (j > 126)
			j = 126;
		return (i & 0xff80) + j;
	}

	private static int method186(int i, int j)
	{
		int k = getNoise(i - 1, j - 1) + getNoise(i + 1, j - 1) + getNoise(i - 1, j + 1) + getNoise(i + 1, j + 1);
		int l = getNoise(i - 1, j) + getNoise(i + 1, j) + getNoise(i, j - 1) + getNoise(i, j + 1);
		int i1 = getNoise(i, j);
		return (k >> 4) + (l >> 3) + (i1 >> 2);
	}

	private static int trimHSLLightness(int i, int j)
	{
		if (i == -1)
			return 0xbc614e;
		j = (j * (i & 0x7f)) >> 7;
		if (j < 2)
			j = 2;
		else if (j > 126)
			j = 126;
		return (i & 0xff80) + j;
	}

	public static void method188(LandscapeScene worldRenderer, int i, int j, int k, int l, LandscapeClippingPlane clipping, int ai[][][], int i1, int j1, int k1)
	{
		int l1 = ai[l][i1][j];
		int i2 = ai[l][i1 + 1][j];
		int j2 = ai[l][i1 + 1][j + 1];
		int k2 = ai[l][i1][j + 1];
		int l2 = l1 + i2 + j2 + k2 >> 2;
		ObjectDefinition class46 = ObjectDefinition.forId(j1);
		int i3 = i1 + (j << 7) + ((j1 << 14) & 0x7fff) + 0x40000000;
		if (!class46.hasActions)
			i3 += 0x80000000;
		byte byte1 = (byte) ((i << 6) + k);
		if (k == 22)
		{
			Object obj;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj = class46.getModelFromPosition(22, i, l1, i2, j2, k2, -1, null);
			else
				obj = new GameObject(j1, i, 22, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method280(k1, l2, j, ((Animable) (obj)), byte1, i3, i1, j1);
			//if (class46.aBoolean767 && class46.hasActions)
			//	clipping.method213(j, i1);
			return;
		}
		if (k == 10 || k == 11)
		{
			Object obj1;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj1 = class46.getModelFromPosition(10, i, l1, i2, j2, k2, -1, null);
			else
				obj1 = new GameObject(j1, i, 10, i2, j2, l1, k2, class46.anInt781, true);
			if (obj1 != null)
			{
				int j5 = 0;
				if (k == 11)
					j5 += 256;
				int k4;
				int i5;
				if (i == 1 || i == 3)
				{
					k4 = class46.sizeY;
					i5 = class46.sizeX;
				}
				else
				{
					k4 = class46.sizeX;
					i5 = class46.sizeY;
				}
				worldRenderer.method284(i3, byte1, l2, i5, ((Animable) (obj1)), k4, k1, j5, j, i1, j1);
			}
			if (class46.walkable)
				clipping.markSolidClip(class46.aBoolean757, class46.sizeX, class46.sizeY, i1, j, i);
			return;
		}
		if (k >= 12)
		{
			Object obj2;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj2 = class46.getModelFromPosition(k, i, l1, i2, j2, k2, -1, null);
			else
				obj2 = new GameObject(j1, i, k, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method284(i3, byte1, l2, 1, ((Animable) (obj2)), 1, k1, 0, j, i1, j1);
			if (class46.walkable)
				clipping.markSolidClip(class46.aBoolean757, class46.sizeX, class46.sizeY, i1, j, i);
			return;
		}
		if (k == 0)
		{
			Object obj3;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj3 = class46.getModelFromPosition(0, i, l1, i2, j2, k2, -1, null);
			else
				obj3 = new GameObject(j1, i, 0, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method282(anIntArray152[i], ((Animable) (obj3)), i3, j, byte1, i1, null, l2, 0, k1, j1);
			if (class46.walkable)
				clipping.markWall(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 1)
		{
			Object obj4;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj4 = class46.getModelFromPosition(1, i, l1, i2, j2, k2, -1, null);
			else
				obj4 = new GameObject(j1, i, 1, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method282(anIntArray140[i], ((Animable) (obj4)), i3, j, byte1, i1, null, l2, 0, k1, j1);
			if (class46.walkable)
				clipping.markWall(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 2)
		{
			int j3 = i + 1 & 3;
			Object obj11;
			Object obj12;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
			{
				obj11 = class46.getModelFromPosition(2, 4 + i, l1, i2, j2, k2, -1, null);
				obj12 = class46.getModelFromPosition(2, j3, l1, i2, j2, k2, -1, null);
			}
			else
			{
				obj11 = new GameObject(j1, 4 + i, 2, i2, j2, l1, k2, class46.anInt781, true);
				obj12 = new GameObject(j1, j3, 2, i2, j2, l1, k2, class46.anInt781, true);
			}
			worldRenderer.method282(anIntArray152[i], ((Animable) (obj11)), i3, j, byte1, i1, ((Animable) (obj12)), l2, anIntArray152[j3], k1, j1);
			if (class46.walkable)
				clipping.markWall(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 3)
		{
			Object obj5;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj5 = class46.getModelFromPosition(3, i, l1, i2, j2, k2, -1, null);
			else
				obj5 = new GameObject(j1, i, 3, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method282(anIntArray140[i], ((Animable) (obj5)), i3, j, byte1, i1, null, l2, 0, k1, j1);
			if (class46.walkable)
				clipping.markWall(j, i, i1, k, class46.aBoolean757);
			return;
		}
		if (k == 9)
		{
			Object obj6;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj6 = class46.getModelFromPosition(k, i, l1, i2, j2, k2, -1, null);
			else
				obj6 = new GameObject(j1, i, k, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method284(i3, byte1, l2, 1, ((Animable) (obj6)), 1, k1, 0, j, i1, j1);
			if (class46.walkable)
				clipping.markSolidClip(class46.aBoolean757, class46.sizeX, class46.sizeY, i1, j, i);
			return;
		}
		if (class46.adjustToTerrain)
			if (i == 1)
			{
				int k3 = k2;
				k2 = j2;
				j2 = i2;
				i2 = l1;
				l1 = k3;
			}
			else if (i == 2)
			{
				int l3 = k2;
				k2 = i2;
				i2 = l3;
				l3 = j2;
				j2 = l1;
				l1 = l3;
			}
			else if (i == 3)
			{
				int i4 = k2;
				k2 = l1;
				l1 = i2;
				i2 = j2;
				j2 = i4;
			}
		if (k == 4)
		{
			Object obj7;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj7 = class46.getModelFromPosition(4, 0, l1, i2, j2, k2, -1, null);
			else
				obj7 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method283(i3, j, i << 9, k1, 0, l2, ((Animable) (obj7)), i1, byte1, 0, anIntArray152[i]);
			return;
		}
		if (k == 5)
		{
			int j4 = 16;
			int l4 = worldRenderer.method300(k1, i1, j);
			if (l4 > 0)
				j4 = ObjectDefinition.forId(worldRenderer.method300_new(k1, i1, j)).anInt775;
			Object obj13;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj13 = class46.getModelFromPosition(4, 0, l1, i2, j2, k2, -1, null);
			else
				obj13 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method283(i3, j, i << 9, k1, anIntArray137[i] * j4, l2, ((Animable) (obj13)), i1, byte1, anIntArray144[i] * j4, anIntArray152[i]);
			return;
		}
		if (k == 6)
		{
			Object obj8;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj8 = class46.getModelFromPosition(4, 0, l1, i2, j2, k2, -1, null);
			else
				obj8 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method283(i3, j, i, k1, 0, l2, ((Animable) (obj8)), i1, byte1, 0, 256);
			return;
		}
		if (k == 7)
		{
			Object obj9;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj9 = class46.getModelFromPosition(4, 0, l1, i2, j2, k2, -1, null);
			else
				obj9 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method283(i3, j, i, k1, 0, l2, ((Animable) (obj9)), i1, byte1, 0, 512);
			return;
		}
		if (k == 8)
		{
			Object obj10;
			if (class46.anInt781 == -1 && class46.childrenIDs == null)
				obj10 = class46.getModelFromPosition(4, 0, l1, i2, j2, k2, -1, null);
			else
				obj10 = new GameObject(j1, 0, 4, i2, j2, l1, k2, class46.anInt781, true);
			worldRenderer.method283(i3, j, i, k1, 0, l2, ((Animable) (obj10)), i1, byte1, 0, 768);
		}
	}

	public static boolean method189(int i, byte[] is, int i_250_) //xxx bad method, decompiled with JODE
	{
		boolean bool = true;
		JagexBuffer buffer = new JagexBuffer(is);
		int i_252_ = -1;
		for (; ;)
		{
			int i_253_ = buffer.getSmartB();
			if (i_253_ == 0)
				break;
			i_252_ += i_253_;
			int i_254_ = 0;
			boolean bool_255_ = false;
			for (; ;)
			{
				if (bool_255_)
				{
					int i_256_ = buffer.getSmartB();
					if (i_256_ == 0)
						break;
					buffer.readUnsignedByte();
				}
				else
				{
					int i_257_ = buffer.getSmartB();
					if (i_257_ == 0)
						break;
					i_254_ += i_257_ - 1;
					int i_258_ = i_254_ & 0x3f;
					int i_259_ = i_254_ >> 6 & 0x3f;
					int i_260_ = buffer.readUnsignedByte() >> 2;
					int i_261_ = i_259_ + i;
					int i_262_ = i_258_ + i_250_;
					if (i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103)
					{
						ObjectDefinition class46 = ObjectDefinition.forId(i_252_);
						if (i_260_ != 22 || !lowMemory || class46.hasActions || class46.aBoolean736)
						{
							bool &= class46.method579();
							bool_255_ = true;
						}
					}
				}
			}
		}
		return bool;
	}

	public final void method190(int i, LandscapeClippingPlane aclass11[], int j, LandscapeScene worldRenderer, byte abyte0[])
	{
		label0:
		{
			JagexBuffer buffer = new JagexBuffer(abyte0);
			int l = -1;
			do
			{
				int i1 = buffer.getSmartB();
				if (i1 == 0)
					break label0;
				l += i1;
				int j1 = 0;
				do
				{
					int k1 = buffer.getSmartB();
					if (k1 == 0)
						break;
					j1 += k1 - 1;
					int l1 = j1 & 0x3f;
					int i2 = j1 >> 6 & 0x3f;
					int j2 = j1 >> 12;
					int k2 = buffer.readUnsignedByte();
					int l2 = k2 >> 2;
					int i3 = k2 & 3;
					int j3 = i2 + i;
					int k3 = l1 + j;
					if (j3 > 0 && k3 > 0 && j3 < landscapeSizeX - 1 && k3 < landscapeSizeY - 1)
					{
						int l3 = j2;
						if ((renderRuleFlags[1][j3][k3] & 2) == 2)
							l3--;
						LandscapeClippingPlane clipping = null;
						if (l3 >= 0 && l3 < 4)
							clipping = aclass11[l3];
						renderObject(k3, worldRenderer, clipping, l2, j2, j3, l, i3);
					}
				}
				while (true);
			}
			while (true);
		}
	}

	//private static int hueOffset = (int) (Math.random() * 17D) - 8;
	private final int[] blendedHue;
	private final int[] blendedSaturation;
	private final int[] blendedLightness;
	private final int[] blendedHueDivisor;
	private final int[] blendDirectionTracker;
	private final int[][][] vertexHeights;
	private final byte[][][] overlayFloorId;
	static int onBuildTimePlane;
	//private static int lightingOffset = (int) (Math.random() * 33D) - 16;
	private final byte[][][] tileShadowIntensity;
	private final int[][][] tileCullingBitsets;
	private final byte[][][] overlayClippingPaths;
	private static final int anIntArray137[] = {
		1, 0, -1, 0
	};
	//private static final int anInt138 = 323;
	private final int[][] tileLightingIntensity;
	private static final int anIntArray140[] = {
		16, 32, 64, 128
	};
	private final byte[][][] underlayFloorId;
	private static final int anIntArray144[] = {
		0, -1, 0, 1
	};
	static int lowestPlane = 99;
	private final int landscapeSizeX;
	private final int landscapeSizeY;
	private final byte[][][] overlayClippingPathRotations;
	private final byte[][][] renderRuleFlags;
	static boolean lowMemory = true;
	private static final int anIntArray152[] = {
		1, 2, 4, 8
	};
}
