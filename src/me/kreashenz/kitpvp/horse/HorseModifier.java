package me.kreashenz.kitpvp.horse;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HorseModifier {

	/*
	 * Thanks to
	 * @author DarkBladee12 for doing this!
	 */

	private Object entityHorse;
	private Object nbtTagCompound;

	public HorseModifier(LivingEntity horse) {
		if (!HorseModifier.isHorse(horse)) {
			throw new IllegalArgumentException("Entity has to be a horse!");
		}
		try {
			this.entityHorse = ReflectionUtil.getMethod("getHandle", horse.getClass(), 0).invoke(horse);
			this.nbtTagCompound = NBTUtil.getNBTTagCompound(entityHorse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HorseModifier(Object entityHorse) {
		this.entityHorse = entityHorse;
		try {
			this.nbtTagCompound = NBTUtil.getNBTTagCompound(entityHorse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HorseModifier spawn(Location loc) {
		World w = loc.getWorld();
		try {
			Object worldServer = ReflectionUtil.getMethod("getHandle", w.getClass(), 0).invoke(w);
			Object entityHorse = ReflectionUtil.getClass("EntityHorse", worldServer);
			ReflectionUtil.getMethod("setPosition", entityHorse.getClass(), 3).invoke(entityHorse, loc.getX(), loc.getY(), loc.getZ());
			ReflectionUtil.getMethod("addEntity", worldServer.getClass(), 1).invoke(worldServer, entityHorse);
			return new HorseModifier(entityHorse);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isHorse(LivingEntity le) {
		try {
			Object entityLiving = ReflectionUtil.getMethod("getHandle", le.getClass(), 0).invoke(le);
			Object nbtTagCompound = NBTUtil.getNBTTagCompound(entityLiving);
			return NBTUtil.hasKeys(nbtTagCompound, new String[] { "EatingHaystack", "ChestedHorse", "HasReproduced", "Bred", "Type", "Variant", "Temper", "Tame" });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setType(HorseType type) {
		setHorseValue("Type", type.getId());
	}

	public void setChested(boolean chested) {
		setHorseValue("ChestedHorse", chested);
	}

	public void setEating(boolean eating) {
		setHorseValue("EatingHaystack", eating);
	}

	public void setBred(boolean bred) {
		setHorseValue("Bred", bred);
	}

	public void setVariant(HorseVariant variant) {
		setHorseValue("Variant", variant.getId());
	}

	public void setTemper(int temper) {
		setHorseValue("Temper", temper);
	}

	public void setTamed(boolean tamed) {
		setHorseValue("Tame", tamed);
	}

	public void setSaddled(boolean saddled) {
		setHorseValue("Saddle", saddled);
	}

	public void setArmorItem(ItemStack i) {
		if (i != null) {
			try {
				Object itemTag = ReflectionUtil.getClass("NBTTagCompound", "ArmorItem");
				Object itemStack = ReflectionUtil.getMethod("asNMSCopy", Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack"), 1).invoke(this, i);
				ReflectionUtil.getMethod("save", itemStack.getClass(), 1).invoke(itemStack, itemTag);
				setHorseValue("ArmorItem", itemTag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			setHorseValue("ArmorItem", null);
		}
	}

	public HorseType getType() {
		return HorseType.fromId((Integer) NBTUtil.getValue(nbtTagCompound, Integer.class, "Type"));
	}

	public boolean isChested() {
		return (Boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "ChestedHorse");
	}

	public boolean isEating() {
		return (Boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "EatingHaystack");
	}

	public boolean isBred() {
		return (Boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "Bred");
	}

	public HorseVariant getVariant() {
		return HorseVariant.fromId((Integer) NBTUtil.getValue(nbtTagCompound, Integer.class, "Variant"));
	}

	public int getTemper() {
		return (Integer) NBTUtil.getValue(nbtTagCompound, Integer.class, "Temper");
	}

	public boolean isTamed() {
		return (Boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "Tame");
	}

	public boolean isSaddled() {
		return (Boolean) NBTUtil.getValue(nbtTagCompound, Boolean.class, "Saddle");
	}

	public ItemStack getArmorItem() {
		try {
			Object itemTag = NBTUtil.getValue(nbtTagCompound, nbtTagCompound.getClass(), "ArmorItem");
			Object itemStack = ReflectionUtil.getMethod("createStack", Class.forName(ReflectionUtil.getPackageName() + ".ItemStack"), 1).invoke(this, itemTag);
			return (ItemStack) ReflectionUtil.getMethod("asCraftMirror", Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack"), 1).invoke(this, itemStack);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void openInventory(Player p) {
		try {
			Object entityPlayer = ReflectionUtil.getMethod("getHandle", p.getClass(), 0).invoke(p);
			ReflectionUtil.getMethod("f", entityHorse.getClass(), 1).invoke(entityHorse, entityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LivingEntity getHorse() {
		try {
			return (LivingEntity) ReflectionUtil.getMethod("getBukkitEntity", entityHorse.getClass(), 0).invoke(entityHorse);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setHorseValue(String key, Object value) {
		NBTUtil.setValue(nbtTagCompound, key, value);
		NBTUtil.updateNBTTagCompound(entityHorse, nbtTagCompound);
	}

}