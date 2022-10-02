package cc.dreamcode.menu.serdes.bukkit.setup;

/**
 * Interface to set up custom menu from config.
 * Here you can apply event on slots, add custom items in real-time and what you want.
 * This class works like consumer class.
 */
public interface MenuSetup<M> {

    M build();

}
