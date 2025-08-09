package net.heeheehub.itemlib.enchantments;

public interface EnchantmentConsumer<T> {
	
	void accept(int level, T t);
	
}
