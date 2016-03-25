package src;


public final class Resource extends NodeSub {

	public Resource() {
		incomplete = true;
	}

	int type;
	byte data[];
	int id;
	final int MODELS = 0;
	final int ANIMATIONS = 1;
	final int MIDI_SEQUENCE = 2;
	final int MAPS = 3;
	final int TEXTURES = 4;
	boolean incomplete;
	int requestAge;
}
