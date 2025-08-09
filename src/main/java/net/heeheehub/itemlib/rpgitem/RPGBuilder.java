package net.heeheehub.itemlib.rpgitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import net.heeheehub.apihub.APIHub.development.APIHub;
import net.heeheehub.apihub.APIHub.utils.ColorFormat;
import net.heeheehub.itemlib.ItemBuilder;
import net.kyori.adventure.text.Component;

public class RPGBuilder {

	private ItemStack item;
	private long element;
	private long rarity;
	private List<String> description;
	private Map<Enchantment, Integer> enchants;
	private Map<AttributeModifier, Attribute> modifiers;
	private String displayName;
	private DyeColor color;
	
	public RPGBuilder(ItemStack item) {
		this.item = item;
		this.element = -1;
		this.rarity = -1;
		this.enchants = new HashMap<Enchantment, Integer>();
		this.modifiers = new HashMap<AttributeModifier, Attribute>();
	}
	
	public RPGBuilder(Material material) {
		this(new ItemStack(material));
	}
	
	
	public RPGBuilder displayName(String dp) {
		this.displayName = dp;
		return this;
	}
	
	public RPGBuilder makeDescription(String...description) {
		this.description = Arrays.stream(description).toList();
		return this;
	}
	
	public RPGBuilder enchant(Enchantment enchantment, int level) {
		this.enchants.put(enchantment, level);
		return this;
	}
	
	public RPGBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
		AttributeModifier modifier = new AttributeModifier(
    			NamespacedKey.minecraft(UUID.randomUUID().toString()),
    			amount,
    			operation,
    			slot.getGroup()
    	);
		this.modifiers.put(modifier, attribute);
		return this;
    }
	public RPGBuilder color(DyeColor color) {
        this.color = color;
        return this;
    }
	
	public ItemStack make() {
		ItemBuilder ib = new ItemBuilder(item);
		ib.name(displayName);
		List<Component> lore = new ArrayList<>();
		lore.add(ColorFormat.format2((String) APIHub.request("rpg:rarity_display_name", rarity)));
		lore.add(ColorFormat.format2((String) APIHub.request("rpg:element_display_name", element)));
		
		if(description != null || !description.isEmpty()) {
			lore.add(ColorFormat.format2("<gray>✧────────────────────✧</gray>"));
			lore.add(ColorFormat.format2("<gradient:#FFD700:#FF8C00:#FF4500:#FF1493:#8A2BE2>✦❖✦  Description  ✦❖✦</gradient>"));
			lore.addAll(description.stream().map(e -> ColorFormat.format2(e)).toList());
		}
		
		if(!enchants.isEmpty()) {
			lore.add(ColorFormat.format2("<gray>✧────────────────────✧</gray>"));
			lore.add(ColorFormat.format2("<gradient:#FFD700:#FF8C00:#FF4500:#FF1493:#8A2BE2>✦❖✦  Enchantments  ✦❖✦</gradient>"));
			
			for(Entry<Enchantment, Integer> entry : enchants.entrySet()) {
				if(entry.getValue().intValue() > 0) {
					ib.unsafeEnchant(entry.getKey(), entry.getValue());
					
					String translationKey = entry.getKey().getTranslationKey();
					
					if(translationKey.equals("enchantment." + entry.getKey().getKey().getNamespace() + "." + entry.getKey().getKey().getKey())) {
						Component comp = Component.translatable(translationKey);
						lore.add(ColorFormat.format2("<gradient:#D2DEFF:#54B66B> ✻" + comp.toString()+ "</gradient>"));
					}else lore.add(ColorFormat.format2("<gradient:#D2DEFF:#54B66B>✻ " + capitalizeWords(entry.getKey().getKey().getKey()) + "</gradient>"));
				}
			}
		}
		
		
		if(!modifiers.isEmpty()) {
			lore.add(ColorFormat.format2("<gray>✧────────────────────✧</gray>"));
			lore.add(ColorFormat.format2("<gradient:#FFD700:#FF8C00:#FF4500:#FF1493:#8A2BE2>✦❖✦  Attribute  ✦❖✦</gradient>"));
			
			for(Entry<AttributeModifier ,Attribute> entry : modifiers.entrySet()) {
				ib.addAttribute(entry.getValue(), entry.getKey().getAmount(), entry.getKey().getOperation(), entry.getKey().getSlot());
				
				Operation op = entry.getKey().getOperation();

				String a;
				switch(op) {
					case ADD_NUMBER:
						if(entry.getKey().getAmount() < 0) {
							a = "<gradient:#D2DEFF:#B65454> ❖ " + String.format("%.2f", entry.getKey().getAmount()) + " " + entry.getKey().getName() + "</gradient>";
						}else {
							a = "<gradient:#D2DEFF:#001E6E> ❖ " + String.format("%.2f", entry.getKey().getAmount()) + " " + entry.getKey().getName() + "</gradient>";;
						}
						
						lore.add(ColorFormat.format2(a));
						break;
					case ADD_SCALAR:
					case MULTIPLY_SCALAR_1:
						if(entry.getKey().getAmount() < 0) {
							a = "<gradient:#D2DEFF:#B65454> ❖ " + String.format("%.2f", entry.getKey().getAmount() * 100) + "% " + entry.getKey().getName() + "</gradient>";
						}else {
							a = "<gradient:#D2DEFF:#001E6E> ❖ " + String.format("%.2f", entry.getKey().getAmount() * 100) + "% " + entry.getKey().getName() + "</gradient>";;
						}
						
						lore.add(ColorFormat.format2(a));
						break;
					default:
						continue;
				}
			}
		
		}
		
		lore.add(ColorFormat.format2("<gray>✧────────────────────✧</gray>"));
		ib.loreC(lore);
		ib.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
		return ib.get();
	}
	
	private static String capitalizeWords(String input) {
        String[] words = input.split("[ _]+"); 
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                           .append(word.substring(1).toLowerCase())
                           .append(" ");
            }
        }
        return capitalized.toString().trim();
    }
	
	public void setElement(long element) {
		this.element = element;
	}
	
	public void setRarity(long rarity) {
		this.rarity = rarity;
	}
	
	
	public long getElement() {
		return element;
	}
	
	public long getRarity() {
		return rarity;
	}
}
