package org.niobe;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import org.niobe.net.NetworkChannelHandler;
import org.niobe.net.NetworkConstants;
import org.niobe.net.PipelineFactory;
import org.niobe.task.TaskManager;
import org.niobe.world.Player;
import org.niobe.world.World;
import org.niobe.world.content.PlayerSaving;
import org.niobe.world.content.clan.ClanChatManager;

/**
 * Niobe's main starting point where netty
 * is initialized and {@link org.niobe.net.NetworkConstants.GAME_PORT}
 * is bound.
 *
 * @author relex lawl
 */
public final class GameServer {
	
	/**
	 * The {@link GameServer} logger to print information and warnings.
	 */
	private static final Logger logger = Logger.getLogger(GameServer.class.getName());
	
	/**
	 * The amount of cores that this machine will use for updating.
	 * {@link org.niobe.world.update.GameUpdate} for more information.
	 */
	public static final int UPDATE_CORES = 2;
	
	public static void main(String[] args) throws Exception {
		logger.info("Launching Niobe, please be patient while configurations are being loaded...");
		/*
		 * Loading the server's shutdown hook,
		 * which handles actions that should be completed
		 * before server is terminated by the command System.exit(1).
		 */
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		/*
		 * Initializes the world loader. 
		 */
		getWorld().init();
		/*
		 * Setting up bootstrap for netty.
		 */
		ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        serverBootstrap.setPipelineFactory(new PipelineFactory(new NetworkChannelHandler(), new HashedWheelTimer()));
        serverBootstrap.bind(new InetSocketAddress(NetworkConstants.GAME_PORT));
        logger.info("Port " + NetworkConstants.GAME_PORT + " has been bound.");
	}

	/**
	 * The {@link org.niobe.world.World} instance.
	 */
	private static final World WORLD = new World();
	
	/**
	 * The {@link org.niobe.task.TaskManager} singleton instance.
	 */
	private static final TaskManager TASK_MANAGER = new TaskManager();
	
	/**
	 * Gets the singleton for {@link org.niobe.world.World}.
	 * @return	The world instance.
	 */
	public static World getWorld() {
		return WORLD;
	}
	
	/**
	 * Gets the {@link org.niobe.task.TaskManager}
	 * singleton.
	 * @return	The {@link #TASK_MANAGER} instance.
	 */
	public static TaskManager getTaskManager() {
		return TASK_MANAGER;
	}
	
	/**
	 * This file manages the actions that should be taken
	 * upon exiting or terminating the server application.
	 * 
	 * @author relex lawl
	 */
	private static final class ShutdownHook extends Thread {

		/**
		 * The ShutdownHook logger to print out information.
		 */
		private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());
		
		@Override
		public void run() {
			logger.info("The shutdown hook is processing all required actions...");
			for (Player player : GameServer.getWorld().getPlayers()) {
				if (player != null) {
					PlayerSaving.save(player);
				}
			}
			ClanChatManager.save();
			logger.info("The shudown hook actions have been completed, shutting the server down...");
		}
	}

}
