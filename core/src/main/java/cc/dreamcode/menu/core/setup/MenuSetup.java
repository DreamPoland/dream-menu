package cc.dreamcode.menu.core.setup;

/**
 * Interface to easy build menu by interface - like consumer class.
 * Here you can apply event on slots, add custom items in real-time and what you want.
 * Build by creating new instance. (clean-code style)
 */
public interface MenuSetup<M> {

    M build();

}
