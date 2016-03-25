package src.accounts;


import src.Client;
import src.util.TextUtils;

public class Create {

	public Create(Client client) {
		this.client = client;
	}

	/**
	 * Sets the defaults for all variables.
	 */
	public void setDefaults() {
		setDefaultInfo();
		available = false;
		created = false;
		cursorPos = 0;
		username = "";
		password = "";
		confirm = "";
		email = "";
		referrer = "";
	}

	/**
	 * Sets the default tooltips.
	 */
	public void setDefaultInfo() {
		setDefaultInfo(0);
		setDefaultInfo(1);
		setDefaultInfo(2);
		setDefaultInfo(3);
		setDefaultInfo(4);
	}

	/**
	 * Sets the default information for a specified info tooltip.
	 * @param info
	 */
	public void setDefaultInfo(int info) {
		switch (info) {
			case 0:
				usernameInfo = new String[]{ "Your username must consist of alphanumeric", "characters and spaces, and must not exceed", "12 characters." };
				break;
			case 1:
				passwordInfo = new String[]{ "Your password must consist of alphanumeric", "characters, must be at least 5 characters", "and must not exceed 20 characters." };
				break;
			case 2:
				confirmInfo = new String[]{ "Confirm your password by retyping it." };
				break;
			case 3:
				emailInfo = new String[]{ "If you do not enter a valid email address,", "you will not be able to recover your account", "if your password is changed or forgotten." };
				break;
			case 4:
				referrerInfo = new String[]{ "A referrer is not required, but if you", "wish to benefit the person referring you", "please enter their name." };
				break;
		}
	}

	public String getName() {
		if (username == null) {
			return "";
		}
		return TextUtils.fixName(username);
	}

	public String getPassword() {
		if (password == null) {
			return "";
		}
		return password;
	}

	public String getEmail() {
		if (email == null) {
			return "";
		}
		return email;
	}

	public String getReferrer() {
		if (referrer == null) {
			return "";
		}
		return TextUtils.fixName(referrer);
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
	 * Checks to see if the password entered is a valid password.
	 * @return
	 */
	public boolean checkPassword() {
		if (getPassword() == null) {
			return false;
		}
		if (getPassword().equalsIgnoreCase(getName())) {
			passwordError = new String[]{ "Your password cannot be your username!" };
			return false;
		}
		if (getPassword().length() < 5) {
			passwordError = new String[]{ "Your password must be at least", "5 characters long." };
			return false;
		}
		return TextUtils.isAlphanumeric(getPassword());
	}

	/**
	 * Checks to see if the passwords match.
	 * @return
	 */
	public boolean confirmPassword() {
		if (getPassword() == null) {
			return false;
		}
		if (getPassword().length() > 0 && confirm.length() > 0) {
			if (getPassword().equalsIgnoreCase(confirm)) {
				return true;
			} else {
				confirmError = new String[]{ "This must match the previously", "typed password." };
				return false;
			}
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
	 * Checks to see if the information entered for referrer is valid.
	 * @return
	 */
	public boolean checkReferrer() {
		if (getReferrer() == null) {
			return false;
		}
		if (getReferrer().length() == 0) {
			return true;
		}
		if (getName().equalsIgnoreCase(getReferrer())) {
			referrerError = new String[]{ "You can't refer yourself!" };
			return false;
		}
		if (getReferrer().length() > 0) {
			if (!TextUtils.isValidName(getReferrer())) {
				referrerError = new String[]{ "Referrer name contains invalid characters." };
			}
			return TextUtils.isValidName(getReferrer());
		}
		return false;
	}

	/**
	 * Checks to see whether or not an account can be recovering.
	 * This method will check to see if all the data is sufficient enough
	 * to create a new account.
	 * @return
	 */
	public boolean canCreate() {
		return checkUsername() && checkPassword() && confirmPassword() && checkEmail() && checkReferrer();
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
		int formY = (getClient().getClientHeight() / 2) - (359 / 2);
		int inputY = formY + 65;
		//back
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 23, x + 89, formY, formY + 42)) {
			getClient().loginScreenState = getClient().LOGIN;
		}
		if (getClient().mouseInRegion2(x + 23, x + 89, formY, formY + 42)) {
			getClient().titleHover = 4;
		} else {
			getClient().titleHover = -1;
		}
		//create
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 91, x + 221, formY, formY + 42) && canCreate()) {
			created = true;
			getClient().loginFailures = 0;
			getClient().login(getName(), getPassword(), false, false);
			if(getClient().loggedIn) {
				return;
			}
		}
		//cursors
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 30, x + getClient().input.myWidth, inputY, inputY + getClient().input.myHeight)) {
			setCursorPos(0);
		}
		inputY += 55;
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 30, x + getClient().input.myWidth, inputY, inputY + getClient().input.myHeight)) {
			setCursorPos(1);
		}
		inputY += 55;
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 30, x + getClient().input.myWidth, inputY, inputY + getClient().input.myHeight)) {
			setCursorPos(2);
		}
		inputY += 55;
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 30, x + getClient().input.myWidth, inputY, inputY + getClient().input.myHeight)) {
			setCursorPos(3);
		}
		inputY += 55;
		if (getClient().getClickMode() == 1 && getClient().clickInRegion2(x + 30, x + getClient().input.myWidth, inputY, inputY + getClient().input.myHeight)) {
			setCursorPos(4);
		}
		do {
			int key = getClient().readCharacter();
			if(key == -1) {
				break;
			}
			boolean validKey = false;
			for(int index = 0; index < getClient().validUserPassChars.length(); index++) {
				if(key != getClient().validUserPassChars.charAt(index)) {
					continue;
				}
				validKey = true;
				break;
			}
			if (getCursorPos() < 4) {
				if(key == 9 || key == 10 || key == 13) {
					switch (getCursorPos()) {
						case 0:
							verified[0] = checkUsername();
							break;
						case 1:
							verified[1] = checkPassword();
							break;
						case 2:
							verified[2] = confirmPassword();
							break;
						case 3:
							verified[3] = checkEmail();
							break;
					}
					if (getCursorPos() == 1) {
						verified[1] = true;
					} else if (getCursorPos() == 2) {
						verified[2] = confirmPassword();
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
				if(key == 8 && getPassword().length() > 0) {
					password = password.substring(0, getPassword().length() - 1);
					verified[1] = checkPassword();
					verified[2] = confirmPassword();
				}
				if(validKey) {
					password += (char)key;
					verified[1] = checkPassword();
					verified[2] = confirmPassword();
				}
				if(password.length() > 20) {
					password = password.substring(0, 20);
				}
			} else if (getCursorPos() == 2) {
				if(key == 8 && confirm.length() > 0) {
					confirm = confirm.substring(0, confirm.length() - 1);
					verified[2] = confirmPassword();
				}
				if(validKey) {
					confirm += (char)key;
					verified[2] = confirmPassword();
				}
				if(confirm.length() > 20) {
					confirm = confirm.substring(0, 20);
				}
			} else if (getCursorPos() == 3) {
				if(key == 8 && email.length() > 0) {
					email = email.substring(0, email.length() - 1);
					verified[3] = checkEmail();
				}
				if(validKey) {
					email += (char) key;
					verified[3] = checkEmail();
				}
				if(email.length() > 100) {
					email = email.substring(0, 100);
				}
			} else if (getCursorPos() == 4) {
				if(key == 8 && getReferrer().length() > 0) {
					referrer = referrer.substring(0, referrer.length() - 1);
					verified[4] = checkReferrer();
				}
				if(validKey) {
					referrer += (char) key;
					verified[4] = checkReferrer();
				}
				if(getReferrer().length() > 12) {
					referrer = referrer.substring(0, 12);
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
	public String[] passwordInfo;
	public String[] confirmInfo;
	public String[] emailInfo;
	public String[] referrerInfo;
	public String[] usernameError;
	public String[] passwordError;
	public String[] confirmError;
	public String[] emailError;
	public String[] referrerError;
	public boolean available = false;
	public boolean created = false;
	public boolean[] verified = { checkUsername(), checkPassword(), confirmPassword(), checkEmail(), checkReferrer() };
	public int cursorPos;
	public String username;
	public String password;
	public String confirm;
	public String email;
	public String referrer;

}
