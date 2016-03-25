package org.niobe.net.session.impl;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.niobe.GameServer;
import org.niobe.net.codec.GamePacketDecoder;
import org.niobe.net.codec.GamePacketEncoder;
import org.niobe.net.login.LoginConstants;
import org.niobe.net.login.LoginResponse;
import org.niobe.net.security.IsaacRandomPair;
import org.niobe.net.security.credential.LoginCredential;
import org.niobe.net.session.Session;
import org.niobe.util.NameUtil;
import org.niobe.world.Player;
import org.niobe.world.content.PlayerPunishment;
import org.niobe.world.content.PlayerSaving;
import org.niobe.world.content.PlayerSaving.LoginType;
import org.niobe.world.content.StarterKit;

/**
 * An implementation of {@link org.niobe.net.session.Session} used for the 
 * channelhandler's attachment in the login procedure.
 *
 * @author relex lawl
 */
public final class LoginSession extends Session {

	/**
	 * The LoginSession constructor.
	 * @param channel	The channel being used to login.
	 * @param context	The channel handler context.
	 */
	public LoginSession(Channel channel, ChannelHandlerContext context) {
		super(channel);
		this.context = context;
	}
	
	/**
	 * The channel handler context.
	 */
	private final ChannelHandlerContext context;
	
	/**
	 * Gets the login response opcode.
	 * @param player	The {@link org.niobe.world.Player} attempting to login.
	 * @return			The response for {@param player} login.
	 */
	private static final int getResponse(Player player) {
		if (GameServer.getWorld().getPlayersOnline() >= 2000) {
			return LoginConstants.LOGIN_WORLD_FULL;
		} else if (!NameUtil.isValidName(player.getCredentials().getUsername())) {
			return LoginConstants.LOGIN_INVALID_CREDENTIALS;
		} else if (PlayerPunishment.banned("./data/bans/" + player.getCredentials().getUsername().toLowerCase() + ".txt")) {
			return LoginConstants.LOGIN_DISABLED_ACCOUNT;
		} else if (GameServer.getWorld().isPlayerOnline(player.getCredentials().getUsername())) {
			return LoginConstants.LOGIN_ACCOUNT_ONLINE;
		} else if (GameServer.getWorld().getPlayersOnline() >= 2000) {
			return LoginConstants.LOGIN_WORLD_FULL;
		}
		return LoginConstants.LOGIN_SUCCESSFUL;
	}

	@Override
	public void receiveMessage(Object message) throws Exception {
		if (message.getClass() != LoginCredential.class) {
			return;
		}
		Channel channel = getChannel();
		LoginCredential credentials = (LoginCredential) message;
		Player player = new Player(credentials.getCredentials(), channel);
		int response = getResponse(player);
		LoginType type = PlayerSaving.load(player);
		if (type == LoginType.WRONG_CREDENTIALS) {
			response = LoginConstants.LOGIN_INVALID_CREDENTIALS;
		} else if (type == LoginType.INPUT_FAILURE) {
			response = LoginConstants.LOGIN_COULD_NOT_COMPLETE;
		}
		ChannelFuture future = channel.write(new LoginResponse(response, player.getRights().ordinal(), 0));
		if (response == LoginConstants.LOGIN_SUCCESSFUL) {
			IsaacRandomPair pair = credentials.getIsaacs();
			channel.getPipeline().addFirst("eventEncoder", new GamePacketEncoder(pair.getEncode()));
			channel.getPipeline().addBefore("handler", "eventDecoder", new GamePacketDecoder(pair.getDecode()));
			channel.getPipeline().remove("loginDecoder");
			channel.getPipeline().remove("loginEncoder");	
			player.setSession(new GameSession(channel, player));
			context.setAttachment(player.getSession());
			GameServer.getWorld().register(player);
			if (type == LoginType.NEW_ACCOUNT) {
				StarterKit.give(player);
			}
		} else {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void finalize() throws Exception {
		
	}
}
