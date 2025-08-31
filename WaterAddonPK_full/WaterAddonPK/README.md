# WaterAddonPK (ProjectKorra Water – Core Set)

This is a ProjectKorra addon that adds a curated set of **Water (Core)** abilities with a few fully implemented examples and stubs for the rest so you can expand quickly.

## Implemented examples
- WaterWhip (offense, lash + knockback)
- WaterShield (defense, projectile deflect)
- HealingWaters (support, HoT, stronger in rain)
- TidalRush (mobility, surf forward)

All other designed abilities are added as **stubs** with progression timers and config hooks so you can fill in mechanics.

## Build
```bash
mvn clean package
```
Output JAR in `target/WaterAddonPK-1.0.0-shaded.jar` (if shaded) or `target/WaterAddonPK-1.0.0.jar`.

## Server Setup
1. Place ProjectKorra and this addon jar into `/plugins`.
2. Start the server to generate configs. Tweak values in `config.yml`.
3. Bind abilities via PK's binding system (e.g., `/b bind WaterWhip`), then use as instructed.

> If the JitPack dependency for ProjectKorra doesn't resolve, set PK as `provided` and drop the PK jar into your local Maven repo or your IDE path.
