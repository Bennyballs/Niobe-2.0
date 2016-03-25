package src;

public class AlertHandler {

	public boolean hovered = false;
	public boolean close = false;
	public boolean show = false;
	public boolean remove = false;
	public Alert alert = null;
	public Client c;

	public AlertHandler(Client c) {
		this.c = c;
	}

	public void close() {
		close = true;
	}

	public void processAlerts() {
		if(alert == null)
			return;
		if (alert.active()) {
			if (close) {
				alert.close();
				close = false;
			}
			if (!alert.isClosed() && alert.extraY >= 0) {
				alert.extraY -= 5;
				if (alert.getY() + alert.extraY < alert.getY()) {
					alert.extraY = 0;
					show = true;
				}
				c.cacheSprite[92].drawSprite(alert.getX() + 7, alert.getY() + 22 + alert.extraY);
				c.cacheSprite[93].drawSprite(alert.getX(), alert.getY() + alert.extraY);
				if (show) {
					if(hovered)
						c.cacheSprite[94].drawSprite(alert.getX(), alert.getY());
					c.newBoldFont.drawCenteredString(alert.getTitle(), alert.getX() + 242, alert.getY() + 15, alert.getTitleColor(), 0);
					c.newRegularFont.drawCenteredString(alert.getLine(1), alert.getX() + 242, alert.getY() + 38, alert.getColor(2), 0);
					c.newRegularFont.drawCenteredString(alert.getLine(2), alert.getX() + 242, alert.getY() + 58, alert.getColor(2), 0);
				}
			} else if (alert.isClosed()) {
				alert.extraY += 5;
				if (alert.getY() + alert.extraY >= alert.getY() + 80) {
					remove = true;
					show = false;
					alert.active = false;
				}
				c.cacheSprite[92].drawSprite(alert.getX() + 7, alert.getY() + 22 + alert.extraY);
				c.cacheSprite[93].drawSprite(alert.getX(), alert.getY() + alert.extraY);
			}
		}
		if (remove) {
			alert = null;
			remove = false;
		}
	}
	public void processMouse(int x, int y) {
		if(alert == null)
			return;
		if (alert.active()) {
			hovered = (x >= alert.getX() && x <= alert.getX() + 484 && y >= alert.getY() && y <= alert.getY()+79);
			if(hovered) {
				c.menuActionName[1] = "Dismiss";
				c.menuActionID[1] = 476;
				c.menuActionRow = 2;
			}
		}
	}
}