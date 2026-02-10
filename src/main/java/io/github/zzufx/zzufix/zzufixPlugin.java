package io.github.zzufx.zzufix;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class zzufixPlugin extends JavaPlugin implements Listener {

  private boolean tntEnabled;
  private boolean projectilesEnabled;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    reloadConfig();

    tntEnabled = getConfig().getBoolean("features.tnt", true);
    projectilesEnabled = getConfig().getBoolean("features.projectiles", true);

    getServer().getPluginManager().registerEvents(this, this);
  }

  // tnt
  @EventHandler
  public void onEntitySpawn(EntitySpawnEvent event) {
    if (!tntEnabled) return;
    if (!(event.getEntity() instanceof TNTPrimed tnt)) return;

    Bukkit.getScheduler()
        .runTaskLater(
            this,
            () -> {
              if (tnt.isDead() || !tnt.isValid()) return;

              Vector v = tnt.getVelocity();
              double vx = Math.abs(v.getX());
              double vy = Math.abs(v.getY());
              double vz = Math.abs(v.getZ());

              if (vx <= 0.4 && vz <= 0.4 && vy <= 0.2) {
                snapToCenterAndStop(tnt);
              }
            },
            2L);
  }

  private void snapToCenterAndStop(TNTPrimed tnt) {
    if (tnt.isDead() || !tnt.isValid()) return;

    Location loc = tnt.getLocation();
    double x = Math.floor(loc.getX()) + 0.5;
    double y = loc.getY();
    double z = Math.floor(loc.getZ()) + 0.5;

    Location target = new Location(tnt.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
    tnt.teleport(target);
    tnt.setVelocity(new Vector(0, 0, 0));
  }

  // thrown projectiles (snowballs and eggs)
  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (!projectilesEnabled) return;
    if (!(event.getDamager() instanceof Snowball) && !(event.getDamager() instanceof Egg)) return;
    if (!(event.getEntity() instanceof Player target)) return;

    event.setDamage(1.0);

    Vector direction =
        target
            .getLocation()
            .toVector()
            .subtract(event.getDamager().getLocation().toVector())
            .normalize();

    double horizontal = 0.4;
    double vertical = 0.2;

    double randomX = (Math.random() - 0.5) * 0.1;
    double randomZ = (Math.random() - 0.5) * 0.1;

    Vector knockback = direction.multiply(horizontal);
    knockback.setX(knockback.getX() + randomX);
    knockback.setZ(knockback.getZ() + randomZ);
    knockback.setY(vertical);

    target.setVelocity(target.getVelocity().add(knockback));
  }
}
