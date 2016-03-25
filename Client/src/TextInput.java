package src;


final class TextInput {

	public static String method525(int textLength, JagexBuffer stream) {
		int length = 0;
		for (int index = 0; index < textLength; index++) {
			int character = stream.readUnsignedByte();
			receivedText[length] = validCharacters[character];
			length++;
		}
		boolean capitalize = true;
		for (int index = 0; index < length; index++) {
			char c = receivedText[index];
			if (capitalize && c >= 'a' && c <= 'z') {
				receivedText[index] += '\uFFE0';
				capitalize = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				capitalize = true;
			}
			if (index != 0) {
				receivedText[index] = processCapitalization(c, index, length);
			}
		}
		String processedText = new String(receivedText, 0, length);
		return processedText;
	}

	public static void method526(String text, JagexBuffer stream) {
		if (text.length() > 80) {
			text = text.substring(0, 80);
		}
		text = text.toLowerCase();
		for (int index = 0; index < text.length(); index++) {
			char c = text.charAt(index);
			int k = 0;
			for (int character = 0; character < validCharacters.length; character++) {
				if (c != validCharacters[character]) {
					continue;
				}
				k = character;
				break;
			}
			stream.writeByte(k);
		}
	}

	public static char processCapitalization(char character, int index, int length) {
		char prev = receivedText[index - 1];
		char next = receivedText[index + 1];
		char i = new String("i").charAt(0);
		char I = new String("I").charAt(0);
		if (character == i) {
			if (Character.isWhitespace(prev)) {
				if (index == length - 1) {
					return receivedText[index] = I;
				} else {
					if (Character.isWhitespace(next)) {
						return receivedText[index] = I;
					} else if (!Character.isLetterOrDigit(next)) {
						return receivedText[index] = I;
					}
				}
			}
		}
		return character;
	}

	public static String processText(String text) {
		if (text.contains("LOL")) {
			return text;
		}
		stream.currentOffset = 0;
		method526(text, stream);
		int j = stream.currentOffset;
		stream.currentOffset = 0;
		String processedText = method525(j, stream);
		return processedText;
	}

	private static final char[] receivedText = new char[100];
	private static final JagexBuffer stream = new JagexBuffer(new byte[100]);
	private static char validCharacters[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h',
			'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
			'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')',
			'-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%',
			'"', '[', ']', '>', '<', '^', '/', '_' };

}