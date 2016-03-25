package src;


final class LandscapeClippingPlane {

	public LandscapeClippingPlane() {
		insetX = 0;
		insetY = 0;
		width = 104;
		height = 104;
		clip = new int[width][height];
		reset();
	}

	public void reset() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++)
				if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
					clip[x][y] = 0xffffff;
				else
					clip[x][y] = 0x1000000;
		}
	}

	public void markWall(int y, int face, int x, int type, boolean solid) {
		x -= insetX;
		y -= insetY;
		if (type == 0) {
			if (face == 0) {
				addClip(x, y, 128);
				addClip(x - 1, y, 8);
			}
			if (face == 1) {
				addClip(x, y, 2);
				addClip(x, y + 1, 32);
			}
			if (face == 2) {
				addClip(x, y, 8);
				addClip(x + 1, y, 128);
			}
			if (face == 3) {
				addClip(x, y, 32);
				addClip(x, y - 1, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (face == 0) {
				addClip(x, y, 1);
				addClip(x - 1, y + 1, 16);
			}
			if (face == 1) {
				addClip(x, y, 4);
				addClip(x + 1, y + 1, 64);
			}
			if (face == 2) {
				addClip(x, y, 16);
				addClip(x + 1, y - 1, 1);
			}
			if (face == 3) {
				addClip(x, y, 64);
				addClip(x - 1, y - 1, 4);
			}
		}
		if (type == 2) {
			if (face == 0) {
				addClip(x, y, 130);
				addClip(x - 1, y, 8);
				addClip(x, y + 1, 32);
			}
			if (face == 1) {
				addClip(x, y, 10);
				addClip(x, y + 1, 32);
				addClip(x + 1, y, 128);
			}
			if (face == 2) {
				addClip(x, y, 40);
				addClip(x + 1, y, 128);
				addClip(x, y - 1, 2);
			}
			if (face == 3) {
				addClip(x, y, 160);
				addClip(x, y - 1, 2);
				addClip(x - 1, y, 8);
			}
		}
		if (solid) {
			if (type == 0) {
				if (face == 0) {
					addClip(x, y, 0x10000);
					addClip(x - 1, y, 4096);
				}
				if (face == 1) {
					addClip(x, y, 1024);
					addClip(x, y + 1, 16384);
				}
				if (face == 2) {
					addClip(x, y, 4096);
					addClip(x + 1, y, 0x10000);
				}
				if (face == 3) {
					addClip(x, y, 16384);
					addClip(x, y - 1, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (face == 0) {
					addClip(x, y, 512);
					addClip(x - 1, y + 1, 8192);
				}
				if (face == 1) {
					addClip(x, y, 2048);
					addClip(x + 1, y + 1, 32768);
				}
				if (face == 2) {
					addClip(x, y, 8192);
					addClip(x + 1, y - 1, 512);
				}
				if (face == 3) {
					addClip(x, y, 32768);
					addClip(x - 1, y - 1, 2048);
				}
			}
			if (type == 2) {
				if (face == 0) {
					addClip(x, y, 0x10400);
					addClip(x - 1, y, 4096);
					addClip(x, y + 1, 16384);
				}
				if (face == 1) {
					addClip(x, y, 5120);
					addClip(x, y + 1, 16384);
					addClip(x + 1, y, 0x10000);
				}
				if (face == 2) {
					addClip(x, y, 20480);
					addClip(x + 1, y, 0x10000);
					addClip(x, y - 1, 1024);
				}
				if (face == 3) {
					addClip(x, y, 0x14000);
					addClip(x, y - 1, 1024);
					addClip(x - 1, y, 4096);
				}
			}
		}
	}

	public void markSolidClip(boolean impenetrable, int width, int height, int x, int y, int face) {
		int occupied = 256;
		if (impenetrable)
			occupied += 0x20000;
		x -= insetX;
		y -= insetY;
		if (face == 1 || face == 3) {
			int startWidth = width;
			width = height;
			height = startWidth;
		}
		for (int i2 = x; i2 < x + width; i2++)
			if (i2 >= 0 && i2 < this.width) {
				for (int j2 = y; j2 < y + height; j2++)
					if (j2 >= 0 && j2 < this.height)
						addClip(i2, j2, occupied);

			}

	}

	public void markClip(int y, int x) {
		x -= insetX;
		y -= insetY;
		clip[x][y] |= 0x200000;
	}

	private void addClip(int i, int j, int k) {
		clip[i][j] |= k;
	}

	public void unmarkWall(int face, int type, boolean solid, int x, int y) {
		x -= insetX;
		y -= insetY;
		if (type == 0) {
			if (face == 0) {
				removeClip(128, x, y);
				removeClip(8, x - 1, y);
			}
			if (face == 1) {
				removeClip(2, x, y);
				removeClip(32, x, y + 1);
			}
			if (face == 2) {
				removeClip(8, x, y);
				removeClip(128, x + 1, y);
			}
			if (face == 3) {
				removeClip(32, x, y);
				removeClip(2, x, y - 1);
			}
		}
		if (type == 1 || type == 3) {
			if (face == 0) {
				removeClip(1, x, y);
				removeClip(16, x - 1, y + 1);
			}
			if (face == 1) {
				removeClip(4, x, y);
				removeClip(64, x + 1, y + 1);
			}
			if (face == 2) {
				removeClip(16, x, y);
				removeClip(1, x + 1, y - 1);
			}
			if (face == 3) {
				removeClip(64, x, y);
				removeClip(4, x - 1, y - 1);
			}
		}
		if (type == 2) {
			if (face == 0) {
				removeClip(130, x, y);
				removeClip(8, x - 1, y);
				removeClip(32, x, y + 1);
			}
			if (face == 1) {
				removeClip(10, x, y);
				removeClip(32, x, y + 1);
				removeClip(128, x + 1, y);
			}
			if (face == 2) {
				removeClip(40, x, y);
				removeClip(128, x + 1, y);
				removeClip(2, x, y - 1);
			}
			if (face == 3) {
				removeClip(160, x, y);
				removeClip(2, x, y - 1);
				removeClip(8, x - 1, y);
			}
		}
		if (solid) {
			if (type == 0) {
				if (face == 0) {
					removeClip(0x10000, x, y);
					removeClip(4096, x - 1, y);
				}
				if (face == 1) {
					removeClip(1024, x, y);
					removeClip(16384, x, y + 1);
				}
				if (face == 2) {
					removeClip(4096, x, y);
					removeClip(0x10000, x + 1, y);
				}
				if (face == 3) {
					removeClip(16384, x, y);
					removeClip(1024, x, y - 1);
				}
			}
			if (type == 1 || type == 3) {
				if (face == 0) {
					removeClip(512, x, y);
					removeClip(8192, x - 1, y + 1);
				}
				if (face == 1) {
					removeClip(2048, x, y);
					removeClip(32768, x + 1, y + 1);
				}
				if (face == 2) {
					removeClip(8192, x, y);
					removeClip(512, x + 1, y - 1);
				}
				if (face == 3) {
					removeClip(32768, x, y);
					removeClip(2048, x - 1, y - 1);
				}
			}
			if (type == 2) {
				if (face == 0) {
					removeClip(0x10400, x, y);
					removeClip(4096, x - 1, y);
					removeClip(16384, x, y + 1);
				}
				if (face == 1) {
					removeClip(5120, x, y);
					removeClip(16384, x, y + 1);
					removeClip(0x10000, x + 1, y);
				}
				if (face == 2) {
					removeClip(20480, x, y);
					removeClip(0x10000, x + 1, y);
					removeClip(1024, x, y - 1);
				}
				if (face == 3) {
					removeClip(0x14000, x, y);
					removeClip(1024, x, y - 1);
					removeClip(4096, x - 1, y);
				}
			}
		}
	}

	public void unmarkSolidClip(int i, int width, int x, int y, int height, boolean solid) {
		int occupied = 256;
		if (solid)
			occupied += 0x20000;
		x -= insetX;
		y -= insetY;
		if (i == 1 || i == 3) {
			int startWidth = width;
			width = height;
			height = startWidth;
		}
		for (int l1 = x; l1 < x + width; l1++)
			if (l1 >= 0 && l1 < this.width) {
				for (int i2 = y; i2 < y + height; i2++)
					if (i2 >= 0 && i2 < this.height)
						removeClip(occupied, l1, i2);

			}

	}

	private void removeClip(int i, int j, int k) {
		clip[j][k] &= 0xffffff - i;
	}

	public void unmarkConcealed(int y, int x) {
		x -= insetX;
		y -= insetY;
		clip[x][y] &= 0xdfffff;
	}

	public boolean reachedWall(int destinationX, int currentX, int currentY, int destinationFace, int destinationType, int destinationY) {
		if (currentX == destinationX && currentY == destinationY)
			return true;
		currentX -= insetX;
		currentY -= insetY;
		destinationX -= insetX;
		destinationY -= insetY;
		if (destinationType == 0)
			if (destinationFace == 0) {
				if (currentX == destinationX - 1 && currentY == destinationY)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x1280120) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 0x1280102) == 0)
					return true;
			} else if (destinationFace == 1) {
				if (currentX == destinationX && currentY == destinationY + 1)
					return true;
				if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280108) == 0)
					return true;
				if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280180) == 0)
					return true;
			} else if (destinationFace == 2) {
				if (currentX == destinationX + 1 && currentY == destinationY)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x1280120) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 0x1280102) == 0)
					return true;
			} else if (destinationFace == 3) {
				if (currentX == destinationX && currentY == destinationY - 1)
					return true;
				if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280108) == 0)
					return true;
				if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280180) == 0)
					return true;
			}
		if (destinationType == 2)
			if (destinationFace == 0) {
				if (currentX == destinationX - 1 && currentY == destinationY)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1)
					return true;
				if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280180) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 0x1280102) == 0)
					return true;
			} else if (destinationFace == 1) {
				if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280108) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1)
					return true;
				if (currentX == destinationX + 1 && currentY == destinationY)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 0x1280102) == 0)
					return true;
			} else if (destinationFace == 2) {
				if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280108) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x1280120) == 0)
					return true;
				if (currentX == destinationX + 1 && currentY == destinationY)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1)
					return true;
			} else if (destinationFace == 3) {
				if (currentX == destinationX - 1 && currentY == destinationY)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x1280120) == 0)
					return true;
				if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x1280180) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1)
					return true;
			}
		if (destinationType == 9) {
			if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x20) == 0)
				return true;
			if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 2) == 0)
				return true;
			if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 8) == 0)
				return true;
			if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x80) == 0)
				return true;
		}
		return false;
	}

	public boolean reachedDecoration(int destinationX, int destinationY, int currentY, int destinationType, int destinationFace, int currentX) {
		if (currentX == destinationX && currentY == destinationY)
			return true;
		currentX -= insetX;
		currentY -= insetY;
		destinationX -= insetX;
		destinationY -= insetY;
		if (destinationType == 6 || destinationType == 7) {
			if (destinationType == 7)
				destinationFace = destinationFace + 2 & 3;
			if (destinationFace == 0) {
				if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x80) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 2) == 0)
					return true;
			} else if (destinationFace == 1) {
				if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 8) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 2) == 0)
					return true;
			} else if (destinationFace == 2) {
				if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 8) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x20) == 0)
					return true;
			} else if (destinationFace == 3) {
				if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x80) == 0)
					return true;
				if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x20) == 0)
					return true;
			}
		}
		if (destinationType == 8) {
			if (currentX == destinationX && currentY == destinationY + 1 && (clip[currentX][currentY] & 0x20) == 0)
				return true;
			if (currentX == destinationX && currentY == destinationY - 1 && (clip[currentX][currentY] & 2) == 0)
				return true;
			if (currentX == destinationX - 1 && currentY == destinationY && (clip[currentX][currentY] & 8) == 0)
				return true;
			if (currentX == destinationX + 1 && currentY == destinationY && (clip[currentX][currentY] & 0x80) == 0)
				return true;
		}
		return false;
	}

	public boolean reachedFacingObject(int i, int destinationX, int k, int l, int i1, int j1, int k1) {
		int l1 = (destinationX + j1) - 1;
		int i2 = (i + l) - 1;
		if (k >= destinationX && k <= l1 && k1 >= i && k1 <= i2)
			return true;
		if (k == destinationX - 1 && k1 >= i && k1 <= i2
				&& (clip[k - insetX][k1 - insetY] & 8) == 0
				&& (i1 & 8) == 0)
			return true;
		if (k == l1 + 1 && k1 >= i && k1 <= i2
				&& (clip[k - insetX][k1 - insetY] & 0x80) == 0
				&& (i1 & 2) == 0)
			return true;
		return k1 == i - 1 && k >= destinationX && k <= l1
				&& (clip[k - insetX][k1 - insetY] & 2) == 0
				&& (i1 & 4) == 0 || k1 == i2 + 1 && k >= destinationX && k <= l1
				&& (clip[k - insetX][k1 - insetY] & 0x20) == 0
				&& (i1 & 1) == 0;
	}

	private final int insetX;
	private final int insetY;
	private final int width;
	private final int height;
	public final int[][] clip;
}
