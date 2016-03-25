package src;


public class Animable extends NodeSub {

	public boolean method443(int i, int j, int k, int l, int i1, int j1, int k1,
			int l1, int i2, int id) {
		Model model = getRotatedModel();
		if (model != null) {
			modelHeight = model.modelHeight;
			return model.method443(i, j, k, l, i1, j1, k1, l1, i2, id);
		}
		return false;
	}

	Model getRotatedModel() {
		return null;
	}

	Animable() {
		modelHeight = 1000;
	}

	Class33 aClass33Array1425[];
	public int modelHeight;
}
