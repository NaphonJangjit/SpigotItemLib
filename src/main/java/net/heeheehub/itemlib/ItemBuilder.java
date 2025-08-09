package net.heeheehub.itemlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.material.Colorable;

import net.heeheehub.apihub.APIHub.utils.ColorFormat;
import net.kyori.adventure.text.Component;

public class ItemBuilder {
	
	private ItemStack itemStack;
	
	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack.clone();
	}
	
	public ItemBuilder(Material material) {
		this(material, 1);
	}
	
	public ItemBuilder(Material material, int amount) {
		this.itemStack = new ItemStack(material, amount);
	}
	
	public ItemBuilder setMeta(ItemMeta meta) {
		itemStack.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder name(String name) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.displayName(ColorFormat.format2(name));
		return setMeta(meta);
	}
	
	public ItemBuilder amount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}
	
	public ItemBuilder lore(List<String> lore){
		ItemMeta meta = itemStack.getItemMeta();
		List<String> formattedLore = lore.stream().map(value -> ColorFormat.format(value)).toList();
		meta.setLore(formattedLore);
		return setMeta(meta);
	}
	
	public ItemBuilder loreC(List<Component> lore){
		ItemMeta meta = itemStack.getItemMeta();
		meta.lore(lore);
		return setMeta(meta);
	}
	
	public ItemBuilder lore(String...lore) {
		return lore(Arrays.asList(lore));
	}
	
	public ItemBuilder addLore(String... lore) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> currentLore = meta.getLore();
		if(currentLore == null) currentLore = new ArrayList<String>();
		for(String line : lore) currentLore.add(ColorFormat.format(line));
		meta.setLore(currentLore);
		return setMeta(meta);
	}
	
	
	
	public ItemBuilder customModelData(int data) {
        ItemMeta meta = getMeta();
        meta.setCustomModelData(data);
        return setMeta(meta);
    }
	
	public ItemBuilder addItemFlags(ItemFlag... flags) {
		ItemMeta meta = getMeta();
		meta.addItemFlags(flags);
		return setMeta(meta);
	}
	
	public ItemBuilder removeItemFlags(ItemFlag... flags) {
		ItemMeta meta = getMeta();
		meta.removeItemFlags(flags);
		return setMeta(meta);
	}
	
	public ItemBuilder unbreakable(boolean unbreakable) {
        ItemMeta meta = getMeta();
        meta.setUnbreakable(unbreakable);
        return setMeta(meta);
    }

    public ItemBuilder damage(int damage) {
        ItemMeta meta = getMeta();
        if (meta instanceof Damageable) {
            ((Damageable) meta).setDamage(damage);
        }
        return setMeta(meta);
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        ItemMeta meta = getMeta();
        meta.addEnchant(enchantment, level, true);
        return setMeta(meta);
    }

    public ItemBuilder unsafeEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = getMeta();
        meta.addEnchant(enchantment, level, false);
        return setMeta(meta);
    }

    public ItemBuilder color(DyeColor color) {
        ItemMeta meta = getMeta();
        if (meta instanceof Colorable) {
            ((Colorable) meta).setColor(color);
        }
        return setMeta(meta);
    }
	
    public ItemBuilder type(Material material) {
        if (material == null) throw new IllegalArgumentException("Material cannot be null");
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder repairCost(int cost) {
        ItemMeta meta = getMeta();
        if (meta instanceof Repairable) {
            ((Repairable) meta).setRepairCost(cost);
        }
        return setMeta(meta);
    }
    
    public ItemBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
    	ItemMeta meta = getMeta();
    	AttributeModifier modifier = new AttributeModifier(
    			NamespacedKey.minecraft(UUID.randomUUID().toString()),
    			amount,
    			operation,
    			slot.getGroup()
    	);
    	meta.addAttributeModifier(attribute, modifier);
    	return setMeta(meta);
    }
    
    public ItemBuilder clearAttribute(Attribute attribute) {
    	ItemMeta meta = getMeta();
    	meta.removeAttributeModifier(attribute);
    	return setMeta(meta);
    }
    
    public ItemStack get() {
    	return itemStack;
    }
    
	private ItemMeta getMeta() {
		return itemStack.getItemMeta();
	}
}
