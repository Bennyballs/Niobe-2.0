package src.accounts;

import src.Client;
import src.util.TextUtils;

public class Recover {

	public Recover(Client client) {
		this.client = client;
	}

	/**
	 * Sets the defaults for all variables.
	 */
	public void setDefaults() {
		setDefaultInfo();
		recovering = false;
		cursorPos = 0;
		username = "";
		email = "";
	}

	/**
	 * Sets the default tooltips.
	 */
	public void setDefaultInfo() {
		setDefaultInfo(0);
		setDefaultInfo(1);
	}

	/**
	 * Sets the default information for a specified info tooltip.
	 * @param info
	 */
	public void setDefaultInfo(int info) {
		switch (info) {
			case 0:
				usernameInfo = new String[]{ "Enter the username for the account", "you wish to recover." };
				break;
			case 1:
				emailInfo = new String[]{ "Please be sure the email entered matches", "the email you registered the account with." };
				break;
		}
	}

	public String getName() {
		if (username == null) {
			return "";
		}
		return TextUtils.fixName(username);
	}

	public String getEmail() {
		if (email == null) {
			return "";
		}
		return email;
	}

	/**
	 * Checks to see if the username is a valid username.
	 * @return
	 */
	public boolean checkUsername() {
		if (getName().length() > 0) {
			if (!TextUtils.isValidName(getName())) {
				usernameError = new String[]{ "Username contains invalid characters." };
			}
			return TextUtils.isValidName(getName());
		}
		return false;
	}

	/**
	 * Checks to see if the email entered is valid.
	 * @return
	 */
	public boolean checkEmail() {
		if (getEmail() == null) {
			return false;
		}
		if (getEmail().length() == 0) {
			return false;
		}
		if (!TextUtils.isValidEmail(getEmail())) {
			emailError = new String[]{ "This is not a valid email address." };
		}
		return TextUtils.isValidEmail(getEmail());
	}

	/**
	 * Checks to see whether or not an account can be recovering.
	 * This method will check to see if all the data is sufficient enough
	 * to create a new account.
	 * @return
	 */
	public boolean canRecover() {
		return checkUsername() && checkEmail();
	}

	/**
	 * Sets the cursor position.
	 * @param pos
	 */
	public void setCursorPos(int pos) {
		cursorPos = pos;
	}

	/**
	 * Returns the cursor position.
	 * @return
	 */
	public int getCursorPos() {
		return cursorPos;
	}

	/**
	 * Process user input for the creation screen.
	 */
	public void processInput() {
		int x = (getClient().getClientWidth() / 2) - (getClient().box.myWidth / 2);
		int y = (getClient().getClientHeight() / 2) - (getClient().box.myHeight / 2);
		//back
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 23, x + 89, y, y + 42)) {
			getClient().loginScreenState = getClient().LOGIN;
		}
		if (getClient().mouseInRegion2(x + 23, x + 89, y, y + 42)) {
			getClient().titleHover = 4;
		} else {
			getClient().titleHover = -1;
		}
		//recover
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 91, x + 221, y, y + 42) && canRecover()) {
			recovering = true;
			getClient().loginFailures = 0;
			getClient().login(getName(), "", false, false);
			if(getClient().loggedIn) {
				return;
			}
		}
		//cursors
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 29, x + 29 + getClient().input.myWidth, y + 63, y + 63 + getClient().input.myHeight)) {
			setCursorPos(0);
		}
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 29, x + 29 + getClient().input.myWidth, y + 115, y + 115 + getClient().input.myHeight)) {
			setCursorPos(1);
		}
		do {
			int key = getClient().readCharacter();
			if(key == -1) {
				break;
			}
			boolean validKey = false;
			getClient();
			for(int index = 0; index < getClient().validUserPassChars.length(); index++) {
				getClient();
				if(key != getClient().validUserPassChars.charAt(index)) {
					continue;
				}
				validKey = true;
				break;
			}
			if (getCursorPos() < 1) {
				if(key == 9 || key == 10 || key == 13) {
					switch (getCursorPos()) {
						case 0:
							verified[0] = checkUsername();
							break;
						case 1:
							verified[1] = checkEmail();
							break;
					}
					cursorPos++;
				}
			} else {
				if(key == 9 || key == 10 || key == 13) {
					setCursorPos(0);
				}
			}
			if (getCursorPos() == 0) {
				if(key == 8 && getName().length() > 0) {
					username = username.substring(0, username.length() - 1);
					verified[0] = checkUsername();
				}
				if(validKey) {
					username += (char) key;
					verified[0] = checkUsername();
				}
				if(getName().length() > 12) {
					username = username.substring(0, 12);
				}
			} else if (getCursorPos() == 1) {
				if(key == 8 && email.length() > 0) {
					email = email.substring(0, email.length() - 1);
					verified[1] = checkEmail();
				}
				if(validKey) {
					email += (char) key;
					verified[1] = checkEmail();
				}
				if(email.length() > 100) {
					email = email.substring(0, 100);
				}
			}
		} while(true);
		return;
	}

	public Client getClient() {
		return client;
	}

	public Client client;
	public String[] usernameInfo;
	public String[] emailInfo;
	public String[] usernameError;
	public String[] emailError;
	public boolean recovering = false;
	public boolean[] verified = { checkUsername(), checkEmail() };
	public int cursorPos;
	public String username;
	public String email;

}