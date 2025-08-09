package net.heeheehub.itemlib.enchantments;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.material.Crops;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.entity.EntityZapEvent;

import io.papermc.paper.enchantments.EnchantmentRarity;
import io.papermc.paper.event.block.BlockBreakProgressUpdateEvent;
import io.papermc.paper.event.entity.EntityEffectTickEvent;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;

public class EnchantmentBuilder {
	
	private boolean isBuild = false;
	private String name;
	private Enchantment enchantment;
	private Plugin owner;
	
	private EnchantmentConsumer<EntityDamageByEntityEvent> consumerOnTakeDamage = null;
	private EnchantmentConsumer<EntityDamageByEntityEvent> consumerOnDoDamage = null;
	private EnchantmentConsumer<EntityDamageEvent> consumerOnGeneralDamage = null;            // All damage types
	private EnchantmentConsumer<EntityDeathEvent> consumerOnKill = null;
	private EnchantmentConsumer<EntityDeathEvent> consumerOnDead = null;
	private EnchantmentConsumer<ProjectileHitEvent> consumerOnProjectileLand = null;         // Arrows/trident impacts
	private EnchantmentConsumer<BlockBreakEvent> consumerOnBlockBreak = null;                // Mining enhancements
	private EnchantmentConsumer<PlayerInteractEvent> consumerOnInteract = null;
	private EnchantmentConsumer<PlayerItemDamageEvent> consumerOnItemDamage = null;          // Custom durability
	private EnchantmentConsumer<PlayerMoveEvent> consumerOnMove = null;                     // Speed/depth strider
	private EnchantmentConsumer<EntityToggleGlideEvent> consumerOnGlideToggle = null;       // Elytra enchantments
	private EnchantmentConsumer<EntityAirChangeEvent> consumerOnAirChange = null;           // Underwater breathing
	private EnchantmentConsumer<EntityPotionEffectEvent> consumerOnPotionEffect = null;     // Effect modifications
	private EnchantmentConsumer<InventoryClickEvent> consumerOnInventoryClick = null;        // Binding/curse handling
	private EnchantmentConsumer<PlayerDropItemEvent> consumerOnItemDrop = null;              // Prevent cursed drops
	private EnchantmentConsumer<PlayerItemConsumeEvent> consumerOnConsume = null;            // Food/potion effects
	private EnchantmentConsumer<PlayerItemHeldEvent> consumerOnItemSwitch = null;            // Hotbar swaps
	private EnchantmentConsumer<PlayerFishEvent> consumerOnFish = null;                     // Custom fishing rewards
	private EnchantmentConsumer<PlayerHarvestBlockEvent> consumerOnHarvest = null;           // Crop collection
	private EnchantmentConsumer<EntityChangeBlockEvent> consumerOnCropTrample = null;       // Farmland protection
	private EnchantmentConsumer<ProjectileLaunchEvent> consumerOnProjectileLaunch = null; // All projectiles
	private EnchantmentConsumer<LingeringPotionSplashEvent> consumerOnLingeringPotion = null;  // Area effect clouds
	private EnchantmentConsumer<PlayerExpChangeEvent> consumerOnExpGain = null;             // Experience modifiers
	private EnchantmentConsumer<EntityPortalEnterEvent> consumerOnPortalEnter = null;       // Nether/end gates
	private EnchantmentConsumer<EntityTargetEvent> consumerOnEntityTarget = null;            // Mobs targeting player
	private EnchantmentConsumer<PlayerRiptideEvent> consumerOnRiptide = null;   			 // Trident specials
	private EnchantmentConsumer<BeaconEffectEvent> consumerOnBeaconEffected = null;	     // When player get effect by beacon
	private EnchantmentConsumer<BlockBreakProgressUpdateEvent> consumerOnBlockBreakProgressUpdate = null;
	private EnchantmentConsumer<EndermanAttackPlayerEvent> consumerOnTargetByEnderman = null;
	private EnchantmentConsumer<EntityCombustEvent> consumerOnCombust = null;
	private EnchantmentConsumer<EntityEffectTickEvent> consumerOnEffectTick = null;
	private EnchantmentConsumer<EntityPortalEnterEvent> consumerOnEnterPortal = null;
	private EnchantmentConsumer<EntityJumpEvent> consumerOnJump = null;
	private EnchantmentConsumer<EntityKnockbackEvent> consumerOnKnockBack = null;
	private EnchantmentConsumer<EntityZapEvent> consumerOnZap = null;
	private EnchantmentConsumer<PlayerVelocityEvent> consumerOnVelocityChange = null;
	private EnchantmentConsumer<TNTPrimeEvent> consumerOnTNTPrime = null;
	
	public EnchantmentBuilder(String name, Plugin owner) {
		this.name = name;
		this.owner = owner;
	}
	

	private boolean isCursed = false;
	private Set<Enchantment> conflicts;
	private EnchantmentTarget target;
	private Set<ActiveSlot> slots;
	
	public EnchantmentBuilder cursed(boolean b) {
		this.isCursed = b;
		return this;
	}
	
	public EnchantmentBuilder addConflictEnchantments(Enchantment...enchantments) {
		conflicts.addAll(Arrays.stream(enchantments).toList());
		return this;
	}
	
	public EnchantmentBuilder addConflictEnchantments(List<Enchantment> enchantments) {
		conflicts.addAll(enchantments);
		return this;
	}
	
	public EnchantmentBuilder setConflictEnchantments(List<Enchantment> enchantments) {
		conflicts = new HashSet<Enchantment>(enchantments);
		return this;
	}
	
	public EnchantmentBuilder setConflictEnchantments(Enchantment...enchantments) {
		conflicts = new HashSet<Enchantment>(Arrays.stream(enchantments).toList());
		return this;
	}
	
	public EnchantmentBuilder setEnchantmentTarget(EnchantmentTarget target) {
		this.target = target;
		return this;
	}
	
	public EnchantmentBuilder activeSlots(ActiveSlot...slots) {
		this.slots = new HashSet<>(Arrays.stream(slots).toList());
		return this;
	}
	
	public EnchantmentBuilder onTakeDamage(EnchantmentConsumer<EntityDamageByEntityEvent> event) {
		this.consumerOnTakeDamage = event;
		return this;
	}
	
	public EnchantmentBuilder onDamage(EnchantmentConsumer<EntityDamageByEntityEvent> event) {
		this.consumerOnDoDamage = event;
		return this;
	}
	
	public EnchantmentBuilder onGeneralDamage(EnchantmentConsumer<EntityDamageEvent> event) {
		this.consumerOnGeneralDamage = event;
		return this;
	}

	public EnchantmentBuilder onKill(EnchantmentConsumer<EntityDeathEvent> event) {
			this.consumerOnKill = event;
			return this;
		}
	
	public EnchantmentBuilder onDead(EnchantmentConsumer<EntityDeathEvent> event) {
			this.consumerOnDead = event;
			return this;
		}
	
	public EnchantmentBuilder onProjectileLand(EnchantmentConsumer<ProjectileHitEvent> event) {
			this.consumerOnProjectileLand = event;
			return this;
		}
	
	public EnchantmentBuilder onBlockBreak(EnchantmentConsumer<BlockBreakEvent> event) {
			this.consumerOnBlockBreak = event;
			return this;
		}
	
	public EnchantmentBuilder onInteract(EnchantmentConsumer<PlayerInteractEvent> event) {
			this.consumerOnInteract = event;
			return this;
		}
	
	public EnchantmentBuilder onItemDamage(EnchantmentConsumer<PlayerItemDamageEvent> event) {
			this.consumerOnItemDamage = event;
			return this;
		}
	
	public EnchantmentBuilder onMove(EnchantmentConsumer<PlayerMoveEvent> event) {
			this.consumerOnMove = event;
			return this;
		}
	
	public EnchantmentBuilder onGlideToggle(EnchantmentConsumer<EntityToggleGlideEvent> event) {
			this.consumerOnGlideToggle = event;
			return this;
		}
	
	public EnchantmentBuilder onAirChange(EnchantmentConsumer<EntityAirChangeEvent> event) {
			this.consumerOnAirChange = event;
			return this;
		}
	
	public EnchantmentBuilder onPotionEffect(EnchantmentConsumer<EntityPotionEffectEvent> event) {
			this.consumerOnPotionEffect = event;
			return this;
		}
	
	public EnchantmentBuilder onInventoryClick(EnchantmentConsumer<InventoryClickEvent> event) {
			this.consumerOnInventoryClick = event;
			return this;
		}
	
	public EnchantmentBuilder onItemDrop(EnchantmentConsumer<PlayerDropItemEvent> event) {
			this.consumerOnItemDrop = event;
			return this;
		}
	
	public EnchantmentBuilder onConsume(EnchantmentConsumer<PlayerItemConsumeEvent> event) {
			this.consumerOnConsume = event;
			return this;
		}
	
	public EnchantmentBuilder onItemSwitch(EnchantmentConsumer<PlayerItemHeldEvent> event) {
			this.consumerOnItemSwitch = event;
			return this;
		}
	
	public EnchantmentBuilder onFish(EnchantmentConsumer<PlayerFishEvent> event) {
			this.consumerOnFish = event;
			return this;
		}
	
	public EnchantmentBuilder onHarvest(EnchantmentConsumer<PlayerHarvestBlockEvent> event) {
			this.consumerOnHarvest = event;
			return this;
		}
	
	public EnchantmentBuilder onCropTrample(EnchantmentConsumer<EntityChangeBlockEvent> event) {
			this.consumerOnCropTrample = event;
			return this;
		}
	
	public EnchantmentBuilder onProjectileLaunch(EnchantmentConsumer<ProjectileLaunchEvent> event) {
			this.consumerOnProjectileLaunch = event;
			return this;
		}
	
	public EnchantmentBuilder onLingeringPotion(EnchantmentConsumer<LingeringPotionSplashEvent> event) {
			this.consumerOnLingeringPotion = event;
			return this;
		}
	
	public EnchantmentBuilder onExpGain(EnchantmentConsumer<PlayerExpChangeEvent> event) {
			this.consumerOnExpGain = event;
			return this;
		}
	
	public EnchantmentBuilder onPortalEnter(EnchantmentConsumer<EntityPortalEnterEvent> event) {
			this.consumerOnPortalEnter = event;
			return this;
		}
	
	public EnchantmentBuilder onEntityTarget(EnchantmentConsumer<EntityTargetEvent> event) {
			this.consumerOnEntityTarget = event;
			return this;
		}
	
	public EnchantmentBuilder onRiptide(EnchantmentConsumer<PlayerRiptideEvent> event) {
			this.consumerOnRiptide = event;
			return this;
		}
	
	public EnchantmentBuilder onBeaconEffected(EnchantmentConsumer<BeaconEffectEvent> event) {
			this.consumerOnBeaconEffected = event;
			return this;
		}
	
	public EnchantmentBuilder onBlockBreakProgressUpdate(EnchantmentConsumer<BlockBreakProgressUpdateEvent> event) {
			this.consumerOnBlockBreakProgressUpdate = event;
			return this;
		}
	
	public EnchantmentBuilder onTargetByEnderman(EnchantmentConsumer<EndermanAttackPlayerEvent> event) {
			this.consumerOnTargetByEnderman = event;
			return this;
		}
	
	public EnchantmentBuilder onCombust(EnchantmentConsumer<EntityCombustEvent> event) {
			this.consumerOnCombust = event;
			return this;
		}
	
	public EnchantmentBuilder onEffectTick(EnchantmentConsumer<EntityEffectTickEvent> event) {
			this.consumerOnEffectTick = event;
			return this;
		}
	
	public EnchantmentBuilder onEnterPortal(EnchantmentConsumer<EntityPortalEnterEvent> event) {
			this.consumerOnEnterPortal = event;
			return this;
		}
	
	public EnchantmentBuilder onJump(EnchantmentConsumer<EntityJumpEvent> event) {
			this.consumerOnJump = event;
			return this;
		}
	
	public EnchantmentBuilder onKnockBack(EnchantmentConsumer<EntityKnockbackEvent> event) {
			this.consumerOnKnockBack = event;
			return this;
		}
	
	public EnchantmentBuilder onZap(EnchantmentConsumer<EntityZapEvent> event) {
			this.consumerOnZap = event;
			return this;
		}
	
	public EnchantmentBuilder onVelocityChange(EnchantmentConsumer<PlayerVelocityEvent> event) {
			this.consumerOnVelocityChange = event;
			return this;
		}
	
	public EnchantmentBuilder onTNTPrime(EnchantmentConsumer<TNTPrimeEvent> event) {
			this.consumerOnTNTPrime = event;
			return this;
		}

	
	public void applyEnchantment(int level, ItemStack itemStack) {
		itemStack.addUnsafeEnchantment(enchantment, level);
	}
	
	public Enchantment getEnchantment() {
		if(isBuild) return enchantment; else return null;
	}
	
	public EnchantmentBuilder build() {
		if(isBuild) 
			throw new RuntimeException("Enchantment: " + name + " is already built, you cannot use .build() again");
		
	    enchantment = new BuildEnchant(name, isCursed, isCursed, conflicts, target);

	    if (this.consumerOnTakeDamage != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onDamage(EntityDamageByEntityEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnTakeDamage.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnDoDamage != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onDamage(EntityDamageByEntityEvent event) {
	            	int level = isActive(slots, event.getDamager(), enchantment);
	                if (level > 0)
	                    consumerOnDoDamage.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnGeneralDamage != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onAnyDamage(EntityDamageEvent event) {
	                int level = isActive(slots, event.getEntity(), enchantment);
	            	if (level > 0)
	                    consumerOnGeneralDamage.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnKill != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onKill(EntityDeathEvent event) {
	                int level = isActive(slots, event.getEntity().getKiller(), enchantment);
	            	if (event.getEntity().getKiller() != null &&
	                    level > 0)
	                    consumerOnKill.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnDead != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onDeath(EntityDeathEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnDead.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnProjectileLand != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onProjectileHit(ProjectileHitEvent event) {
	                if (event.getEntity().getShooter() instanceof Entity shooter) {
	                	int level = isActive(slots, shooter, enchantment);
	                	if(level > 0) 
	                		consumerOnProjectileLand.accept(level, event);
	                	
	                }
	            }
	        }, owner);
	    }

	    if (this.consumerOnBlockBreak != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onBreak(BlockBreakEvent event) {
	                int level = isActive(slots, event.getPlayer(), enchantment);
	            	if (level > 0)
	                    consumerOnBlockBreak.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnInteract != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onInteract(PlayerInteractEvent event) {
	                int level = isActive(slots, event.getPlayer(), enchantment);
	            	if (level > 0)
	                    consumerOnInteract.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnItemDamage != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onItemDamage(PlayerItemDamageEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnItemDamage.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnMove != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onMove(PlayerMoveEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnMove.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnGlideToggle != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onToggleGlide(EntityToggleGlideEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnGlideToggle.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnAirChange != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onAirChange(EntityAirChangeEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnAirChange.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnPotionEffect != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onPotion(EntityPotionEffectEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnPotionEffect.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnInventoryClick != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onClick(InventoryClickEvent event) {
	                if (event.getWhoClicked() instanceof Player player) {
	                	int level = isActive(slots, player, enchantment);
	                	if(level > 0)
	                		consumerOnInventoryClick.accept(level, event);
	                }
	            }
	        }, owner);
	    }

	    if (this.consumerOnItemDrop != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onDrop(PlayerDropItemEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnItemDrop.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnConsume != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onConsume(PlayerItemConsumeEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnConsume.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnItemSwitch != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onSwitch(PlayerItemHeldEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnItemSwitch.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnFish != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onFish(PlayerFishEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnFish.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnHarvest != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onHarvest(PlayerHarvestBlockEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (event.getPlayer() != null && level > 0)
	                    consumerOnHarvest.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnCropTrample != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onTrample(EntityChangeBlockEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0 && event.getBlock().getBlockData() instanceof Ageable)
	                    consumerOnCropTrample.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnProjectileLaunch != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onLaunch(ProjectileLaunchEvent event) {
	            	if (event.getEntity().getShooter() instanceof Entity shooter) {
	                    int level = isActive(slots, shooter, enchantment);
	                    if(level > 0)
	                    	consumerOnProjectileLaunch.accept(level, event);
	                }
	            }
	        }, owner);
	    }

	    if (this.consumerOnLingeringPotion != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onLingeringSplash(LingeringPotionSplashEvent event) {
	                if (event.getEntity().getShooter() instanceof Entity shooter) {
	                    int level = isActive(slots, shooter, enchantment);
	                    if(level > 0)
	                    	consumerOnLingeringPotion.accept(level, event);
	                }
	            }
	        }, owner);
	    }

	    if (this.consumerOnExpGain != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onExp(PlayerExpChangeEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnExpGain.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnPortalEnter != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onPortal(EntityPortalEnterEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnPortalEnter.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnEntityTarget != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onTarget(EntityTargetEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnEntityTarget.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnRiptide != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onRiptide(PlayerRiptideEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnRiptide.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnBeaconEffected != null) {
	    	Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onBeaconEffected(BeaconEffectEvent event) {
	                int level = isActive(slots, event.getPlayer(), enchantment);
	            	if(level > 0)
	                	consumerOnBeaconEffected.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnBlockBreakProgressUpdate != null) {
	    	Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onBlockBreakProgress(BlockBreakProgressUpdateEvent event) {
	                int level = isActive(slots, event.getEntity(), enchantment);
	            	if(level > 0)
	                	consumerOnBlockBreakProgressUpdate.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnTargetByEnderman != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onEndermanTarget(EndermanAttackPlayerEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if(level > 0)
	                    consumerOnTargetByEnderman.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnCombust != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onCombust(EntityCombustEvent event) {
	                int level = isActive(slots, event.getEntity(), enchantment);
	            	if (level > 0)
	                    consumerOnCombust.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnEffectTick != null) {
	    	Bukkit.getPluginManager().registerEvents(new Listener() {
	        	@EventHandler
	        	public void onKnockBack(EntityEffectTickEvent event) {
	        		int level = isActive(slots, event.getEntity(), enchantment);
	        		if(level > 0) consumerOnEffectTick.accept(level, event);
	        	}
	        }, owner);
	    }

	    if (this.consumerOnEnterPortal != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onPortal(EntityPortalEnterEvent event) {
	            	int level = isActive(slots, event.getEntity(), enchantment);
	                if (level > 0)
	                    consumerOnEnterPortal.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnJump != null) {
	    	Bukkit.getPluginManager().registerEvents(new Listener() {
	        	@EventHandler
	        	public void onKnockBack(EntityJumpEvent event) {
	        		int level = isActive(slots, event.getEntity(), enchantment);
	        		if(level > 0) consumerOnJump.accept(level, event);
	        	}
	        }, owner);
	    }

	    if (this.consumerOnKnockBack != null) {
	    	Bukkit.getPluginManager().registerEvents(new Listener() {
	        	@EventHandler
	        	public void onKnockBack(EntityKnockbackEvent event) {
	        		int level = isActive(slots, event.getEntity(), enchantment);
	        		if(level > 0) consumerOnKnockBack.accept(level, event);
	        	}
	        }, owner);
	    }

	    if (this.consumerOnZap != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	        	@EventHandler
	        	public void onZap(EntityZapEvent event) {
	        		int level = isActive(slots, event.getEntity(), enchantment);
	        		if(level > 0) consumerOnZap.accept(level, event);
	        	}
	        }, owner);
	    }

	    if (this.consumerOnVelocityChange != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onVelocity(PlayerVelocityEvent event) {
	            	int level = isActive(slots, event.getPlayer(), enchantment);
	                if (level > 0)
	                    consumerOnVelocityChange.accept(level, event);
	            }
	        }, owner);
	    }

	    if (this.consumerOnTNTPrime != null) {
	        Bukkit.getPluginManager().registerEvents(new Listener() {
	            @EventHandler
	            public void onPrime(TNTPrimeEvent event) {
	            	int level = isActive(slots, event.getPrimingEntity(), enchantment);
	                if (level > 0)
	                    consumerOnTNTPrime.accept(level, event);
	            }
	        }, owner);
	    }

	    return this;
	}

	
	private static int isActive(Set<ActiveSlot> slots, Entity entity, Enchantment ench) {
	    if (!(entity instanceof LivingEntity)) return -1;

	    LivingEntity living = (LivingEntity) entity;

	    for (ActiveSlot slot : slots) {
	        switch (slot) {
	            case MAINHAND:
	                if (hasEnchantment(living.getEquipment().getItemInMainHand(), ench)) {
	                	return living.getEquipment().getItemInMainHand().getEnchantmentLevel(ench);
	                }
	                break;
	            case OFFHAND:
	                if (hasEnchantment(living.getEquipment().getItemInOffHand(), ench)) {
	                	return living.getEquipment().getItemInOffHand().getEnchantmentLevel(ench);
	                }
	                break;
	            case ANYHAND:
	                if (hasEnchantment(living.getEquipment().getItemInMainHand(), ench)) {
	                	return living.getEquipment().getItemInMainHand().getEnchantmentLevel(ench);
	                }
	                if (hasEnchantment(living.getEquipment().getItemInOffHand(), ench)) {
	                	return living.getEquipment().getItemInOffHand().getEnchantmentLevel(ench);
	                }
	                break;
	            case HELMET:
	                if (hasEnchantment(living.getEquipment().getHelmet(), ench))  {
	                	return living.getEquipment().getHelmet().getEnchantmentLevel(ench);
	                }
	                break;
	            case CHESTPLATE:
	                if (hasEnchantment(living.getEquipment().getChestplate(), ench)) {
	                	return living.getEquipment().getChestplate().getEnchantmentLevel(ench);
	                }
	                break;
	            case LEGGINGS:
	                if (hasEnchantment(living.getEquipment().getLeggings(), ench)){
	                	return living.getEquipment().getLeggings().getEnchantmentLevel(ench);
	                }
	                break;
	            case BOOTS:
	                if (hasEnchantment(living.getEquipment().getBoots(), ench)) {
	                	return living.getEquipment().getBoots().getEnchantmentLevel(ench);
	                }
	                break;
	            case ARMORED:
	                if (hasEnchantment(living.getEquipment().getHelmet(), ench)) {
	                	return living.getEquipment().getHelmet().getEnchantmentLevel(ench);
	                }
	                if (hasEnchantment(living.getEquipment().getChestplate(), ench)) {
	                	return living.getEquipment().getChestplate().getEnchantmentLevel(ench);
	                }
	                if (hasEnchantment(living.getEquipment().getLeggings(), ench)) {
	                	return living.getEquipment().getLeggings().getEnchantmentLevel(ench);
	                }
	                if (hasEnchantment(living.getEquipment().getBoots(), ench)) {
	                	return living.getEquipment().getBoots().getEnchantmentLevel(ench);
	                }
	                break;
	            case INVENTORY:
	                if (!(entity instanceof InventoryHolder)) break;
	                Inventory inv = ((InventoryHolder) entity).getInventory();
	                for (int i = 0; i < inv.getSize(); i++) {
	                    ItemStack item = inv.getItem(i);
	                    if (hasEnchantment(item, ench)) item.getEnchantmentLevel(ench);
	                }
	                break;
	        }
	    }

	    return -1;
	}

	private static boolean hasEnchantment(ItemStack item, Enchantment ench) {
	    return item != null && item.hasItemMeta() && item.getItemMeta().hasEnchant(ench);
	}
	
	private static class BuildEnchant extends Enchantment  {

		private String name;
		private boolean isLevel;
		private NamespacedKey key;
		private boolean isCursed = false;
		private Set<Enchantment> conflicts;
		private EnchantmentTarget target;
		
		public BuildEnchant(String name, boolean isLevel, boolean isCursed, Set<Enchantment> conflicts, EnchantmentTarget target) {
			this.name = name;
			this.isLevel = isLevel;
			this.isCursed = isCursed;
			this.key = NamespacedKey.minecraft(name);
			this.conflicts = conflicts;
			this.target = target;
		}
		

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		@Override
		public int getMaxLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getStartLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public EnchantmentTarget getItemTarget() {
			// TODO Auto-generated method stub
			return target;
		}

		@Override
		public boolean isTreasure() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCursed() {
			// TODO Auto-generated method stub
			return isCursed;
		}

		@Override
		public boolean conflictsWith(Enchantment other) {
			// TODO Auto-generated method stub
			return conflicts.contains(other);
		}

		@Override
		public boolean canEnchantItem(ItemStack item) {
			return target.includes(item);
		}

		@Override
		public NamespacedKey getKey() {
			return key;
		}

		@Override
		public @NotNull Component displayName(int level) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isTradeable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isDiscoverable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getMinModifiedCost(int level) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getMaxModifiedCost(int level) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getAnvilCost() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public @NotNull EnchantmentRarity getRarity() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public float getDamageIncrease(int level, @NotNull EntityCategory entityCategory) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getDamageIncrease(int level, @NotNull EntityType entityType) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public @NotNull Set<EquipmentSlotGroup> getActiveSlotGroups() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public @NotNull Component description() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public @NotNull RegistryKeySet<ItemType> getSupportedItems() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public @Nullable RegistryKeySet<ItemType> getPrimaryItems() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getWeight() {
			return 0;
		}

		@Override
		public @NotNull RegistryKeySet<Enchantment> getExclusiveWith() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public @NotNull String translationKey() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public @NotNull String getTranslationKey() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
