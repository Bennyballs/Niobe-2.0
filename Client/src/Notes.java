package src;

public class Notes {
	
	public void add(String note) {
		if (note.startsWith("<col=") || note.startsWith("<img="))
			note = note.split(">")[1];
		boolean found = false;
		for (int i = 0; i < notes.length; i++) {
			if (notes[i] == null) {
				notes[i] = note;
				totalNotes++;
				client.sendFrame126(note, i + 14001);
				client.sendFrame126("@lre@Notes (" + totalNotes + "/30)", 13801);
				client.sendFrame126("", 13800);
				found = true;
				break;
			}
		}
		if (!found) {
			client.sendMessage("You cannot store any more notes!");
		}
	}
	
	public void delete(int childId) {
		int index = childId - 14001;
		if (notes[index] == null)
			return;
		notes[index] = null;
		totalNotes--;
		client.sendFrame126("", childId);
		client.sendFrame126("@lre@Notes (" + totalNotes + "/30)", 13801);
		if (totalNotes <= 0) {
			client.sendFrame126("No notes", 13800);
			client.stream.createFrame(31);
		}
	}
	
	public void deleteAll() {
		for (int i = 0; i < notes.length; i++) {
			notes[i] = null;
			client.sendFrame126("", i + 14001);
		}
		totalNotes = 0;
		client.sendFrame126("@lre@Notes (" + totalNotes + "/30)", 13801);
		client.sendFrame126("No notes", 13800);
		client.stream.createFrame(31);
	}
	
	public void setColour(String colour) {
		int index = noteEdit - 14001;
		if (notes[index] == null)
			return;
		notes[index] = "<col=" + Client.hexToColor(colour) + ">" + notes[index];
		client.sendFrame126(notes[index], noteEdit);
	}
	
	public void edit(String editedNote) {
		int index = noteEdit - 14001;
		if (notes[index] == null)
			return;
		if (editedNote.startsWith("<col=") || editedNote.startsWith("<img="))
			editedNote = editedNote.split(">")[1];
		notes[index] = editedNote;
		client.sendFrame126(notes[index], noteEdit);
	}
	
	public String[] notes = new String[30];
	
	public int totalNotes = 0;
	
	public int noteEdit;
	
	public Notes(Client client) {
		this.client = client;
	}

	private Client client;
}
