package org.niobe.world.content.combat.action;

import java.util.Random;

import org.niobe.GameServer;
import org.niobe.model.Damage;
import org.niobe.model.GameCharacter;
import org.niobe.model.Damage.Hit;
import org.niobe.model.Graphic;
import org.niobe.model.Graphic.GraphicHeight;
import org.niobe.model.Item;
import org.niobe.model.SkillManager.Skill;
import org.niobe.model.container.impl.Equipment;
import org.niobe.model.weapon.Weapon;
import org.niobe.model.weapon.Weapon.WeaponExperienceStyle;
import org.niobe.util.MathUtil;
import org.niobe.world.GroundItem;
import org.niobe.world.Player;
import org.niobe.world.Projectile;
import org.niobe.world.content.combat.CombatManager;
import org.niobe.world.content.combat.formula.ArmourBonus;
import org.niobe.world.content.combat.formula.ArmourBonus.BarrowsType;

/**
 * An implementation of {@link CombatAction} used for ranged
 * combat.
 *
 * @author relex lawl
 */
public final class RangedCombatAction extends CombatAction {
		
	@Override
	public boolean canInitiateCombat(GameCharacter source, GameCharacter victim) {
		if (source.isPlayer()) {
			Player player = (Player) source;
			
			Item arrows = player.getEquipment().getItems()[Equipment.AMMUNITION_SLOT];
			Item weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];

			BowType bowType = weapon.getDefinition().getBowType();
			ArrowType arrowType = arrows.getDefinition().getArrowType();
			RangedWeaponType rangedWeaponType = weapon.getDefinition().getRangedWeaponType();
			int level = player.getSkillManager().getMaxLevel(Skill.RANGED);
			
			if (rangedWeaponType == null && !player.getEquipment().isSlotOccupied(Equipment.AMMUNITION_SLOT)) {
				String ammunition = ((bowType == BowType.KARILS_CROSSBOW || bowType == BowType.BRONZE_CROSSBOW || bowType == BowType.IRON_CROSSBOW || bowType == BowType.STEEL_CROSSBOW
						|| bowType == BowType.MITH_CROSSBOW || bowType == BowType.ADAMANT_CROSSBOW || bowType == BowType.RUNE_CROSSBOW) ? "bolts" : "arrows");
				player.getPacketSender().sendMessage("There are no " + ammunition + " left in your quiver.");
				return false;
			}
			
			if (rangedWeaponType != null && level < rangedWeaponType.levelRequirement ||
					arrowType != null && level < arrowType.levelRequirement) {
				int requirement = rangedWeaponType != null ? rangedWeaponType.levelRequirement :
									arrowType.levelRequirement;
				player.getPacketSender().sendMessage("You need a ranged level of " + requirement + " to use this ammunition.");
				return false;
			}
			if (bowType != null && bowType != BowType.CRYSTAL_BOW) {
				
				if(bowType == BowType.DARK_BOW && arrows.getAmount() < 2) {
					player.getPacketSender().sendMessage("You need atleast 2 arrows to use the dark bow.");
					return false;					
				}
				
				boolean hasArrows = false;
				for (ArrowType validArrows : bowType.validArrows) {
					if (validArrows == arrowType) {
						hasArrows = true;
						break;
					}
				}
				if (!hasArrows) {
					player.getPacketSender().sendMessage("You can't use " + arrows.getDefinition().getName()
							+ "s with a " + weapon.getDefinition().getName() + ".");
					return false;
				}
			}
			
			if (bowType == null && rangedWeaponType == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getDistanceRequirement(GameCharacter source, GameCharacter victim) {
		if (source.isPlayer()) {
			Player player = (Player) source;
			
			Item weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];

			BowType bowType = weapon.getDefinition().getBowType();
			RangedWeaponType rangedWeaponType = weapon.getDefinition().getRangedWeaponType();
			
			int style = player.getFields().getCombatAttributes().getAttackStyle();
			
			if (bowType != null) {
				return bowType.distances[style];
			} else if (rangedWeaponType != null) {
				return rangedWeaponType.distances[style];
			}
		}
		return 4;
	}
	
	@Override
	public int getHitDelay(GameCharacter source, GameCharacter victim) {
		int hitDelay = 2;
		if (source.getPosition().getDistance(victim.getPosition()) > 2) {
			hitDelay++;
		}
		if (source.isPlayer()) {
			Player player = (Player) source;
			Item weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];
			BowType bowType = weapon.getDefinition().getBowType();
			
			if (bowType == BowType.DARK_BOW) {
				hitDelay++;
			}
		}
		return hitDelay;
	}
	
	@Override
	public void hit(GameCharacter source, GameCharacter victim, Damage damage) {
		if (source.isPlayer()) {
			Player player = (Player) source;
			Item arrows = player.getEquipment().getItems()[Equipment.AMMUNITION_SLOT];
			Item weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];
	
			BowType bowType = weapon.getDefinition().getBowType();
			ArrowType arrowType = arrows.getDefinition().getArrowType();
			RangedWeaponType rangedWeaponType = weapon.getDefinition().getRangedWeaponType();
			
			Item cape = player.getEquipment().getItems()[Equipment.CAPE_SLOT];
			boolean avasDevice = cape.getDefinition().getName().toLowerCase().startsWith("ava's");
			
			if (bowType != null) {
				if (bowType == BowType.CRYSTAL_BOW) {
					arrowType = ArrowType.CRYSTAL_ARROW;
				}
				Graphic pullback = arrowType.pullback;
				Graphic projectileGraphic = new Graphic(arrowType.projectile, GraphicHeight.HIGH);
				
				if (source.getFields().getCombatAttributes().isUsingSpecialAttack()) {
					if (bowType == BowType.DARK_BOW) {
						projectileGraphic = arrows.getDefinition().getName().toLowerCase().startsWith("draggon arrow") ?
										new Graphic(1099) : new Graphic(1101);					
					} else if (bowType == BowType.MAGIC_SHORTBOW || bowType == BowType.MAGIC_LONGBOW) {
						projectileGraphic = new Graphic(249);
					}
				}
				
				source.performGraphic(pullback);
				
				Projectile projectile = new Projectile(source.getPosition(), victim.getPosition(), projectileGraphic, victim, 40, 30, 30);
				GameServer.getWorld().register(projectile);
						
				if (!avasDevice || avasDevice && MathUtil.random(7) == 0) {
					player.getEquipment().delete(new Item(arrows.getId()), Equipment.AMMUNITION_SLOT);
				}
			}
			
			if (rangedWeaponType != null) {
				Graphic pullback = rangedWeaponType.pullback;
				Graphic projectileGraphic = new Graphic(rangedWeaponType.projectile);
				
				source.performGraphic(pullback);
				
				Projectile projectile = new Projectile(source.getPosition(), victim.getPosition(), projectileGraphic, victim, 40, 30, 30);
				GameServer.getWorld().register(projectile);
				
				if (!avasDevice || avasDevice && MathUtil.random(7) == 0) {
					player.getEquipment().delete(new Item(weapon.getId()), Equipment.WEAPON_SLOT);
				}
			}
		}
	}

	@Override
	public void specialHitAction(GameCharacter source, GameCharacter victim, Damage damage) {
		super.specialHitAction(source, victim, damage);
		if (source.isPlayer()) {
			Player player = (Player) source;
			
			Item weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT];
			Item arrows = player.getEquipment().getItems()[Equipment.AMMUNITION_SLOT];
	
			ArrowType arrowType = arrows.getDefinition().getArrowType();
			RangedWeaponType rangedWeaponType = weapon.getDefinition().getRangedWeaponType();
			
			if (ArmourBonus.hasBarrows(player, BarrowsType.KARIL) &&
					victim.isPlayer()) {
				Player other = (Player) victim;
				int random = MathUtil.random(8);
				if (random == 3) {
					other.getSkillManager().setCurrentLevel(Skill.AGILITY,
							other.getSkillManager().getMaxLevel(Skill.AGILITY) / 4);
					other.performGraphic(new Graphic(401, GraphicHeight.HIGH));
				}
			}
			
			Item cape = player.getEquipment().getItems()[Equipment.CAPE_SLOT];
			boolean avasDevice = cape.getDefinition().getName().toLowerCase().startsWith("ava's");
			
			if (!avasDevice && new Random().nextDouble() < (rangedWeaponType != null ? rangedWeaponType.dropRate : arrowType.dropRate)) {
				GameServer.getWorld().register(new GroundItem(new Item(rangedWeaponType != null ? weapon.getId() : arrows.getId()), victim.getPosition().copy(), player));
			}
			
			String name = player.getWeapon().getDefinition().getName();
			if (victim.getFields().getPoisonDamage() <= 0) {
				int max = 68;
				if (name.contains("(p") && MathUtil.random(8) == 0) {
					if (name.contains("(p++)")) {
						CombatManager.poison(victim, max);
					} else if (name.contains("(p+)")) {
						CombatManager.poison(victim, max - 10);
					} else if (name.contains("(p)")) {
						CombatManager.poison(victim, max - 20);
					}
				}
			}
			
			//TODO special attacks, such as enchanted bolts

		}
	}
	
	@Override
	public void addExperience(Player player, Damage damage) {
		Weapon weapon = player.getWeapon();
		WeaponExperienceStyle experience = weapon.getCombatDefinition().getExperience()[player.getFields().getCombatAttributes().getAttackStyle()];
		Skill[] skills = experience == WeaponExperienceStyle.RANGED_SHARED ? 
							new Skill[] {Skill.RANGED, Skill.DEFENCE} : new Skill[] {Skill.RANGED};
		int shared = skills.length;
		for (Hit hits : damage.getHits()) {
			int hit = hits.getDamage();
			if (damage.getHits().length >= 2)
				hit += damage.getHits()[1].getDamage();
			for (Skill skill : skills) {
				player.getSkillManager().addExperience(skill, (int) (((hit * .40) * skill.getExperienceMultiplier()) / shared));
			}
			player.getSkillManager().addExperience(Skill.CONSTITUTION, ((int) (hit * .5) * Skill.CONSTITUTION.getExperienceMultiplier()));
		}
	}
	
	/**
	 * An enum that represents a type of range bow.
	 * @author Michael
	 *
	 */
	public enum BowType {
		LONGBOW(		new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW },  
						new int[] { 10, 10, 10 }),
				
		SHORTBOW(		new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW }, 
						new int[] { 7, 7, 9 }),
				
		OAK_SHORTBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW }, 
						new int[] { 7, 7, 9 }),
				
		OAK_LONGBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW },
						new int[] { 10, 10, 10 }),
				
		WILLOW_LONGBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW },
						new int[] { 10, 10, 10 }),
				
		WILLOW_SHORTBOW(new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW }, 
						new int[] { 7, 7, 9 }),
						
		MAPLE_LONGBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW }, 
						new int[] { 10, 10, 10 }),
						
		MAPLE_SHORTBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW }, 
						new int[] { 7, 7, 9 }),
					   
		YEW_LONGBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW,
							ArrowType.ADAMANT_ARROW }, 
						new int[] { 10, 10, 10 }),
				  
		YEW_SHORTBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW,
							ArrowType.ADAMANT_ARROW }, 
						new int[] { 7, 7, 9 }),
				  
		MAGIC_LONGBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW,
							ArrowType.ADAMANT_ARROW, ArrowType.RUNE_ARROW }, 
						new int[] { 10, 10, 10 }),
				  
		MAGIC_SHORTBOW(	new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW,
							ArrowType.ADAMANT_ARROW, ArrowType.RUNE_ARROW }, 
						new int[] { 7, 7, 9 }),
						
		CRYSTAL_BOW(	new ArrowType[0], 
						new int[] { 10, 10, 10 }),
						
		KARILS_CROSSBOW(	new ArrowType[] { ArrowType.BOLT_RACK }, 
						new int[] { 7, 7, 9 }),
						
		DARK_BOW(		new ArrowType[] { ArrowType.BRONZE_ARROW, ArrowType.IRON_ARROW,
							ArrowType.STEEL_ARROW, ArrowType.MITHRIL_ARROW,
							ArrowType.ADAMANT_ARROW, ArrowType.RUNE_ARROW,
							ArrowType.DRAGON_ARROW }, 
						new int[] { 10, 10, 10 }),
						
		BRONZE_CROSSBOW(	new ArrowType[] { ArrowType.BRONZE_BOLT, ArrowType.IRON_BOLT }, 
						new int[] { 7, 7, 9 }),
						
		IRON_CROSSBOW(		new ArrowType[] { ArrowType.BRONZE_BOLT, ArrowType.IRON_BOLT }, 
						new int[] { 7, 7, 9 }),
						
		STEEL_CROSSBOW(		new ArrowType[] { ArrowType.BRONZE_BOLT, ArrowType.IRON_BOLT,
							ArrowType.STEEL_BOLT }, 
						new int[] { 7, 7, 9 }),
				   
		MITH_CROSSBOW(		new ArrowType[] { ArrowType.BRONZE_BOLT, ArrowType.IRON_BOLT,
							ArrowType.STEEL_BOLT, ArrowType.MITHRIL_BOLT }, 
						new int[] { 7, 7, 9 }),
						
		ADAMANT_CROSSBOW(	new ArrowType[] { ArrowType.BRONZE_BOLT, ArrowType.IRON_BOLT,
							ArrowType.STEEL_BOLT, ArrowType.MITHRIL_BOLT,
							ArrowType.ADAMANT_BOLT }, 
						new int[] { 7, 7, 9 }),
						
		RUNE_CROSSBOW(		new ArrowType[] { ArrowType.BRONZE_BOLT, ArrowType.IRON_BOLT,
							ArrowType.STEEL_BOLT, ArrowType.MITHRIL_BOLT,
							ArrowType.ADAMANT_BOLT, ArrowType.RUNE_BOLT }, 
						new int[] { 7, 7, 9 }),
		;
		
		/**
		 * The arrows this bow can use.
		 */
		private final ArrowType[] validArrows;
		
		/**
		 * The distances required to be near the victim
		 * based on the mob's combat style.
		 */
		private final int[] distances;
		
		private BowType(ArrowType[] validArrows, int[] distances) {
			this.validArrows = validArrows;
			this.distances = distances;
		}
	}
	
	/**
	 * An enum for all arrow types, this includes the drop rate percentage of this arrow 
	 * (the higher quality the less likely it is to disappear).
	 * @author Michael Bull
	 * @author Sir Sean
	 */
	public enum ArrowType {
		
		BRONZE_ARROW(1, 0.75, new Graphic(19, 0, GraphicHeight.HIGH), 10),
		
		IRON_ARROW(5, 0.7, new Graphic(18, 0, GraphicHeight.HIGH), 9),
		
		STEEL_ARROW(15, 0.65, new Graphic(20, 0, GraphicHeight.HIGH), 11),
		
		MITHRIL_ARROW(20, 0.6, new Graphic(21, 0, GraphicHeight.HIGH), 12),
		
		ADAMANT_ARROW(30, 0.5, new Graphic(22, 0, GraphicHeight.HIGH), 13),
		
		RUNE_ARROW(40, 0.4, new Graphic(24, 0, GraphicHeight.HIGH), 15),
		
		BOLT_RACK(50, 1.1, null, 27),
		
		DRAGON_ARROW(60, 0.3, new Graphic(1111, 0, GraphicHeight.HIGH), 1115),
		
		BRONZE_BOLT(1, 0.75, null, 27),
		
		IRON_BOLT(5, 0.7, null, 27),
		
		STEEL_BOLT(15, 0.65, null, 27),
		
		MITHRIL_BOLT(20, 0.6, null, 27),
		
		ADAMANT_BOLT(30, 0.5, null, 27),
		
		RUNE_BOLT(40, 0.4, null, 27),
		
		CRYSTAL_ARROW(50, 1.1, new Graphic(249, 0, GraphicHeight.HIGH), 250)
		;

		/**
		 * The ranged level required to use the bow type.
		 */
		private final int levelRequirement;
		
		/**
		 * The percentage chance for the arrow to disappear once fired.
		 */
		private final double dropRate;
		
		/**
		 * The pullback graphic.
		 */
		private final Graphic pullback;
		
		/**
		 * The projectile id.
		 */
		private final int projectile;
		
		
		private ArrowType(int levelRequirement, double dropRate, Graphic pullback, int projectile) {
			this.levelRequirement = levelRequirement;
			this.dropRate = dropRate;
			this.pullback = pullback;
			this.projectile = projectile;
		}
	}
	
	/**
	 * An enum that represents all range weapons, e.g. throwing knives and javelins.
	 * @author Michael
	 *
	 */
	public enum RangedWeaponType {
		
		BRONZE_KNIFE(1, 0.75, new Graphic(219, 0, GraphicHeight.HIGH), 212, new int[] { 4, 4, 6 }),
		
		IRON_KNIFE(5, 0.7, new Graphic(220, 0, GraphicHeight.HIGH), 213, new int[] { 4, 4, 6 }),
		
		STEEL_KNIFE(15, 0.65, new Graphic(221, 0, GraphicHeight.HIGH), 214, new int[] { 4, 4, 6 }),
		
		MITHRIL_KNIFE(20, 0.6, new Graphic(223, 0, GraphicHeight.HIGH), 216, new int[] { 4, 4, 6 }),
		
		ADAMANT_KNIFE(30, 0.5, new Graphic(224, 0, GraphicHeight.HIGH), 217, new int[] { 4, 4, 6 }),
		
		RUNE_KNIFE(40, 0.4, new Graphic(225, 0, GraphicHeight.HIGH), 218, new int[] { 4, 4, 6 }),
		
		BLACK_KNIFE(30, 0.6, new Graphic(222, 0, GraphicHeight.HIGH), 215, new int[] { 4, 4, 6 }),
		
		BRONZE_DART(1, 0.75, new Graphic(1234, 0, GraphicHeight.HIGH), 226, new int[] { 3, 3, 5 }),
		
		IRON_DART(5, 0.7, new Graphic(1235, 0, GraphicHeight.HIGH), 227, new int[] { 3, 3, 5 }),
		
		STEEL_DART(15, 0.65, new Graphic(1236, 0, GraphicHeight.HIGH), 228, new int[] { 3, 3, 5 }),
		
		MITHRIL_DART(20, 0.6, new Graphic(1237, 0, GraphicHeight.HIGH), 229, new int[] { 3, 3, 5 }),
		
		ADAMANT_DART(30, 0.5, new Graphic(1239, 0, GraphicHeight.HIGH), 230, new int[] { 3, 3, 5 }),
		
		RUNE_DART(40, 0.4, new Graphic(1240, 0, GraphicHeight.HIGH), 231, new int[] { 3, 3, 5 }),
		
		BLACK_DART(30, 0.6, new Graphic(1238, 0, GraphicHeight.HIGH), 231, new int[] { 3, 3, 5 }),

		BRONZE_THROWNAXE(1, 0.75, new Graphic(42, 0, GraphicHeight.HIGH), 36, new int[] { 4, 4, 6 }),
		
		IRON_THROWNAXE(5, 0.7, new Graphic(43, 0, GraphicHeight.HIGH), 35, new int[] { 4, 4, 6 }),
		
		STEEL_THROWNAXE(15, 0.65, new Graphic(44, 0, GraphicHeight.HIGH), 37, new int[] { 4, 4, 6 }),
		
		MITHRIL_THROWNAXE(20, 0.6, new Graphic(45, 0, GraphicHeight.HIGH), 38, new int[] { 4, 4, 6 }),
		
		ADAMANT_THROWNAXE(30, 0.5, new Graphic(46, 0, GraphicHeight.HIGH), 39, new int[] { 4, 4, 6 }),
		
		RUNE_THROWNAXE(40, 0.4, new Graphic(48, 0, GraphicHeight.HIGH), 41, new int[] { 4, 4, 6 }),

		BRONZE_JAVELIN(1, 0.75, new Graphic(206, 0, GraphicHeight.HIGH), 200, new int[] { 4, 4, 6 }),
		
		IRON_JAVELIN(5, 0.7, new Graphic(207, 0, GraphicHeight.HIGH), 201, new int[] { 4, 4, 6 }),
		
		STEEL_JAVELIN(15, 0.65, new Graphic(208, 0, GraphicHeight.HIGH), 202, new int[] { 4, 4, 6 }),
		
		MITHRIL_JAVELIN(20, 0.6, new Graphic(209, 0, GraphicHeight.HIGH), 203, new int[] { 4, 4, 6 }),
		
		ADAMANT_JAVELIN(30, 0.5, new Graphic(210, 0, GraphicHeight.HIGH), 204, new int[] { 4, 4, 6 }),
		
		RUNE_JAVELIN(40, 0.4, new Graphic(211, 0, GraphicHeight.HIGH), 205, new int[] { 4, 4, 6 }),
		
		OBSIDIAN_RING(50, 0.45, null, 442, new int[] { 7, 7, 9 }),
		;

		/**
		 * The ranged level required to use the bow type.
		 */
		private final int levelRequirement;
		
		/**
		 * The percentage chance for the arrow to disappear once fired.
		 */
		private double dropRate;
		
		/**
		 * The pullback graphic.
		 */
		private Graphic pullback;
		
		/**
		 * The projectile id.
		 */
		private int projectile;
		
		/**
		 * The distances required for each attack type.
		 */
		private int[] distances;

		private RangedWeaponType(int levelRequirement, double dropRate, Graphic pullback, int projectile, int[] distances) {
			this.levelRequirement = levelRequirement;
			this.dropRate = dropRate;
			this.pullback = pullback;
			this.projectile = projectile;
			this.distances = distances;
		}
	}
}
