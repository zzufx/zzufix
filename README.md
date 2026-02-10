# zzufix
A very simple 1.21+ Minecraft plugin that restores certain behaviors from 1.8 SportPaper and vanilla servers.
Originally made for the [Casual Modern PGM](https://github.com/CasualModernPGM) servers.

## Remove TNT offset
Prevents TNT blocks from moving to the sides a bit when primed (while keeping custom PGM projectiles functional).

Pair this with
```yml
fixes:
  prevent-tnt-from-moving-in-water: true
```
in your `root/config/paper-world-defaults.yml` file for maximum PGM TNT action.

## Restore damage and knockback from thrown projectiles
Restores 1.8 behavior for thrown snowballs and eggs (including the new types), which means it makes them deal a bit of damage and give knockback to other players.

# config.yml

```yml
features:
  tnt: true
  projectiles: true
  
```