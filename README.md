# **Inventory Weight**
Inventory Weight is a **realistic encumbrance system** mod for Minecraft that applies movement penalties based on how heavy your inventory is. Every item has a weight, and carrying too much will slow you down, reduce your jump height, drain your stamina, and even attract hostile mobs.

The goal is immersive survival gameplay where inventory management matters. Pack light to stay agile, or load up on gear and accept the consequences. The mod provides rich feedback through HUD elements, sounds, particles, and status effects to keep you aware of your burden.

---

## **Key Features**
- **Dynamic weight system** - Every item has a weight based on its material (ores, ingots, tools, food, etc.).
- **Five-tier penalty system** - Progressive penalties from Light (no effect) to Crippled (barely mobile).
- **Movement penalties** - Affects walking speed, sprint speed, jump height, swimming, and climbing.
- **Combat impact** - Attack speed slows and fall damage increases when overloaded.
- **Stamina system** - Optional stamina bar that drains faster when heavily encumbered.
- **Elytra penalties** - Heavier loads cause faster descent during gliding.
- **Mob detection** - Heavy footsteps increase hostile mob detection range.
- **Dimension modifiers** - Weight feels different in the Nether (heavier) and End (lighter).
- **Transport exceptions** - Horses, minecarts, and boats reduce or eliminate penalties.
- **Rich feedback** - Tier change sounds, particles, action bar messages, and status effects.
- **Advancement system** - Unlock achievements for weight-related accomplishments.
- **Fully configurable** - All weights, thresholds, and behaviors can be customized via JSON.

---

## **How It Works**

### **Weight Calculation**
Every 10 ticks (0.5 seconds), the mod calculates your total inventory weight:

| Source | How It's Counted |
|--------|------------------|
| **Main Inventory** | Full weight of all items × stack size |
| **Hotbar** | 125% weight (quick-access penalty, configurable) |
| **Armor Slots** | Only worn armor counts (not armor in inventory) |
| **Offhand** | Full weight |
| **Shulker Boxes** | Base weight + 50% of contents (configurable) |
| **Ender Chest** | Contents are weightless (dimension storage) |

### **Weight Categories**
Items are automatically categorized based on their name and properties:

| Category | Weight | Example Items |
|----------|--------|---------------|
| **Heavy** | 5.0 | Iron ore, gold ingot, anvil, cobblestone, deepslate |
| **Medium** | 2.0 | Pickaxe, sword, redstone, wood planks, crafted blocks |
| **Light** | 0.5 | Bread, seeds, leather, wool, flowers |
| **Near-Zero** | 0.1 | Maps, books, potions, ender pearls, music discs |

### **Weight Tiers**
Your total weight determines your tier, which sets all penalty multipliers:

| Tier | Weight Range | Description |
|------|--------------|-------------|
| **Light** | 0 – 50 | No penalties. Full mobility. |
| **Medium** | 50 – 100 | Minor slowdown. Noticeable but manageable. |
| **Heavy** | 100 – 175 | Significant penalties. Sprinting is sluggish. |
| **Overloaded** | 175 – 250 | Severe penalties. Cannot sprint. |
| **Crippled** | 250+ | Barely mobile. Every action is labored. |

### **Penalty Breakdown**

| Penalty Type | Light | Medium | Heavy | Overloaded | Crippled |
|--------------|-------|--------|-------|------------|----------|
| **Movement Speed** | 100% | 90% | 75% | 55% | 40% |
| **Sprint Speed** | 100% | 95% | 70% | Disabled | Disabled |
| **Jump Height** | 100% | 95% | 80% | 60% | 45% |
| **Swim Speed** | 100% | 90% | 70% | 50% | 30% |
| **Climb Speed** | 100% | 95% | 80% | 60% | 40% |
| **Mining Speed** | 100% | 95% | 85% | 70% | 50% |
| **Attack Speed** | 100% | 98% | 90% | 80% | 65% |
| **Fall Damage** | 100% | 100% | +15% | +35% | +50% |
| **Exhaustion** | None | None | Low | Medium | High |

---

## **Stamina System**
When enabled, adds an optional stamina mechanic that drains during strenuous activities:

| Activity | Stamina Effect |
|----------|----------------|
| **Sprinting** | Drains stamina (faster at higher weight tiers) |
| **Swimming** | Drains stamina |
| **Standing/Walking** | Regenerates stamina (slower at higher weight tiers) |
| **Exhausted** | Cannot sprint until stamina recovers |

### **Stamina by Tier**

| Tier | Drain Rate | Regen Rate |
|------|------------|------------|
| **Light** | 1.0x | 1.0x |
| **Medium** | 1.3x | 0.9x |
| **Heavy** | 1.8x | 0.7x |
| **Overloaded** | 2.5x | 0.4x |
| **Crippled** | 3.5x | 0.2x |

A stamina bar appears on-screen when enabled (position configurable).

---

## **Elytra Flight**
Weight significantly impacts elytra gliding performance:

| Tier | Sink Rate Modifier | Effect |
|------|-------------------|--------|
| **Light** | 1.0x | Normal flight |
| **Medium** | 1.1x | Slightly faster descent |
| **Heavy** | 1.3x | Noticeable descent increase |
| **Overloaded** | 1.6x | Rapid descent |
| **Crippled** | 2.0x | Difficult to maintain altitude |

Heavy players will need more rockets and struggle to glide long distances.

---

## **Mob Detection System**
Heavier loads make you easier to detect by hostile mobs:

| Mechanic | How It Works |
|----------|--------------|
| **Detection Radius** | Mobs detect you from further away based on weight tier |
| **Footstep Volume** | Louder footsteps at higher weight tiers |
| **Stealth Impact** | Sneaking is less effective when heavily loaded |

### **Detection Radius by Tier**

| Tier | Detection Bonus |
|------|-----------------|
| **Light** | +0% (normal) |
| **Medium** | +10% |
| **Heavy** | +25% |
| **Overloaded** | +50% |
| **Crippled** | +75% |

---

## **Dimension & Environment Modifiers**
Weight feels different depending on where you are:

### **Dimension Multipliers**

| Dimension | Weight Multiplier | Reason |
|-----------|-------------------|--------|
| **Overworld** | 1.0x | Standard gravity |
| **Nether** | 1.15x | Oppressive heat increases burden |
| **End** | 0.85x | Lower gravity reduces effective weight |

### **Environmental Effects**

| Condition | Effect |
|-----------|--------|
| **Underwater** | Depth adds pressure penalty |
| **Rain** | Wet items weigh 10% more |
| **High Altitude (Y>128)** | Slight weight reduction |
| **Deep Caves (Y<0)** | Slight weight increase |

---

## **Transport Exceptions**
Certain vehicles reduce or eliminate weight penalties:

| Transport | Effect |
|-----------|--------|
| **Horses, Donkeys, Llamas** | Full immunity to weight penalties |
| **Minecarts** | Full immunity to weight penalties |
| **Boats** | 50% penalty reduction (configurable) |
| **Pigs (with saddle)** | No exception (you're still carrying it) |

This encourages using mounts for long-distance travel when heavily loaded.

---

## **Feedback System**
The mod provides extensive feedback to keep you informed:

### **Visual Feedback**

| Element | Description |
|---------|-------------|
| **HUD Display** | Shows current weight and tier on-screen |
| **Stamina Bar** | Visual bar showing remaining stamina |
| **Item Tooltips** | Weight value shown on every item |
| **Tier Change Particles** | Visual effect when entering a new tier |
| **Action Bar Messages** | Contextual warnings about encumbrance |

### **Audio Feedback**

| Sound | Trigger |
|-------|---------|
| **Armor Clink** | Weight tier increased |
| **Level-Up Chime** | Weight tier decreased |
| **Heavy Footsteps** | Walking at high weight tiers (louder steps) |

### **Status Effects (Optional)**

| Effect | Condition |
|--------|-----------|
| **Slowness I** | Applied at Overloaded tier |
| **Mining Fatigue I** | Applied at Crippled tier |
| **Speed I** | Optional boost at Light tier (reward for traveling light) |

---

## **Advancements**
Unlock achievements for weight-related accomplishments:

| Advancement | Requirement |
|-------------|-------------|
| **Light Traveler** | Travel 10,000 blocks while at Light tier |
| **Pack Mule** | Reach the Heavy weight tier |
| **Beast of Burden** | Reach the Overloaded tier |
| **Immovable Object** | Reach the Crippled tier |
| **Marathon Runner** | Travel 5,000 blocks while Heavy or above |
| **Deep Diver** | Descend 20 blocks in water while heavy |
| **Mountain Climber** | Climb 100 blocks on ladders while heavy |

---

## **Statistics Tracking**
The mod tracks your weight-related statistics:

| Statistic | Description |
|-----------|-------------|
| **Peak Weight** | Highest weight ever reached |
| **Average Weight** | Running average over your play session |
| **Distance (Light)** | Total blocks traveled at Light tier |
| **Distance (Heavy)** | Total blocks traveled at Heavy+ tiers |
| **Tier Changes** | Number of times you've changed tiers |
| **Exhaustion Count** | Times stamina was fully depleted |

View your stats with `/weight stats`.

---

## **Commands**

| Command | Permission | Description |
|---------|------------|-------------|
| `/weight check` | All | View current weight, tier, and stamina |
| `/weight thresholds` | All | Display tier threshold values |
| `/weight stats` | All | View your weight statistics |
| `/weight reload` | OP | Reload config from file |
| `/weight toggle` | OP | Enable/disable weight system globally |
| `/weight hardcore` | OP | Toggle hardcore mode (1.5x penalties) |
| `/weight stamina` | OP | Toggle stamina system |
| `/weight dimension` | OP | Toggle dimension modifiers |
| `/weight effects` | OP | Toggle status effects |
| `/weight set <item> <weight>` | OP | Set custom weight for any item |

---

## **Configuration**
Config file location: `.minecraft/config/inventoryweight.json`

The config file is created automatically on first launch. All options:

```json
{
  "enabled": true,
  "showHud": true,
  "hotbarWeightMultiplier": 1.25,
  "hardcoreMode": false,
  "hardcoreMultiplier": 1.5,
  "lightTierThreshold": 0.0,
  "mediumTierThreshold": 50.0,
  "heavyTierThreshold": 100.0,
  "overloadedTierThreshold": 175.0,
  "crippledTierThreshold": 250.0,
  "categoryWeightHeavy": 5.0,
  "categoryWeightMedium": 2.0,
  "categoryWeightLight": 0.5,
  "categoryWeightNearZero": 0.1,
  "horseIgnoresWeight": true,
  "minecartIgnoresWeight": true,
  "boatPartiallyIgnoresWeight": true,
  "boatWeightReduction": 0.5,
  "showWeightInTooltip": true,
  "weightAffectsSwimming": true,
  "weightAffectsClimbing": true,
  "weightAffectsFallDamage": true,
  "weightCausesExhaustion": true,
  "weightAffectsMining": true,
  "weightAffectsAttackSpeed": true,
  "weightAffectsElytra": true,
  "shulkerBoxAddsContentWeight": true,
  "shulkerBoxBaseWeight": 3.0,
  "shulkerContentWeightMultiplier": 0.5,
  "playTierChangeSounds": true,
  "showTierChangeParticles": true,
  "staminaEnabled": false,
  "maxStamina": 100.0,
  "baseStaminaDrain": 1.0,
  "baseStaminaRegen": 0.5,
  "showStaminaBar": true,
  "staminaBarX": 10,
  "staminaBarY": 60,
  "dimensionModifiersEnabled": true,
  "netherWeightMultiplier": 1.15,
  "endWeightMultiplier": 0.85,
  "overworldWeightMultiplier": 1.0,
  "waterDepthPenaltyEnabled": true,
  "rainWeightMultiplier": 1.1,
  "altitudeModifierEnabled": true,
  "mobDetectionEnabled": true,
  "baseDetectionRadius": 16.0,
  "footstepVolumeEnabled": true,
  "weightAppliesStatusEffects": false,
  "slownessAtOverloaded": true,
  "miningFatigueAtCrippled": true,
  "speedBoostWhenLight": false,
  "customItemWeights": {}
}
```

### **Config Options Explained**

| Option | Default | Description |
|--------|---------|-------------|
| `enabled` | `true` | Master toggle for the entire mod |
| `showHud` | `true` | Display weight/tier HUD on screen |
| `hotbarWeightMultiplier` | `1.25` | Multiplier for items in hotbar slots |
| `hardcoreMode` | `false` | Enable harsher penalties |
| `hardcoreMultiplier` | `1.5` | Penalty multiplier in hardcore mode |
| `*TierThreshold` | varies | Weight thresholds for each tier |
| `categoryWeight*` | varies | Base weight for each item category |
| `horseIgnoresWeight` | `true` | No penalties while riding horses |
| `minecartIgnoresWeight` | `true` | No penalties while in minecarts |
| `boatPartiallyIgnoresWeight` | `true` | Reduced penalties in boats |
| `boatWeightReduction` | `0.5` | Penalty reduction while in boats |
| `showWeightInTooltip` | `true` | Show weight in item tooltips |
| `weightAffects*` | `true` | Toggle individual penalty types |
| `shulkerBoxAddsContentWeight` | `true` | Shulker contents add to weight |
| `shulkerBoxBaseWeight` | `3.0` | Base weight of empty shulker box |
| `shulkerContentWeightMultiplier` | `0.5` | Multiplier for shulker contents |
| `playTierChangeSounds` | `true` | Sound effects on tier change |
| `showTierChangeParticles` | `true` | Particle effects on tier change |
| `staminaEnabled` | `false` | Enable stamina system |
| `maxStamina` | `100.0` | Maximum stamina value |
| `baseStaminaDrain` | `1.0` | Base stamina drain rate |
| `baseStaminaRegen` | `0.5` | Base stamina regeneration rate |
| `showStaminaBar` | `true` | Display stamina bar on HUD |
| `staminaBarX/Y` | `10/60` | Stamina bar screen position |
| `dimensionModifiersEnabled` | `true` | Enable dimension weight modifiers |
| `*WeightMultiplier` | varies | Weight multiplier per dimension |
| `waterDepthPenaltyEnabled` | `true` | Deeper water increases penalty |
| `rainWeightMultiplier` | `1.1` | Weight multiplier when raining |
| `altitudeModifierEnabled` | `true` | Altitude affects weight |
| `mobDetectionEnabled` | `true` | Enable mob detection system |
| `baseDetectionRadius` | `16.0` | Base mob detection range |
| `footstepVolumeEnabled` | `true` | Weight affects footstep volume |
| `weightAppliesStatusEffects` | `false` | Apply status effects based on tier |
| `slownessAtOverloaded` | `true` | Slowness at Overloaded tier |
| `miningFatigueAtCrippled` | `true` | Mining Fatigue at Crippled tier |
| `speedBoostWhenLight` | `false` | Speed boost at Light tier |

### **Custom Item Weights**
Override any item's weight with custom values in the config:

```json
{
  "customItemWeights": {
    "minecraft:anvil": 15.0,
    "minecraft:netherite_block": 20.0,
    "minecraft:feather": 0.05,
    "modid:custom_item": 3.0
  }
}
```

---

## **Multiplayer Compatibility**
This mod works on both **client and server**. For full functionality:

| Setup | Result |
|-------|--------|
| **Client + Server** | Full experience with all features |
| **Client Only** | Partial (HUD works, but penalties may not sync) |
| **Server Only** | Penalties work, but no client-side HUD/tooltips |

For the best experience, install on both client and server.

### **Multiplayer Features**
- Weight calculations are done server-side for security
- Movement modifiers use vanilla attribute system
- Per-player weight data stored in concurrent hash maps
- State synchronized properly between client and server

---

## **Installation**
1. Install **Fabric Loader** (0.15.6 or newer) for Minecraft **1.20.1**.
2. Install **Fabric API** (0.92.0+1.20.1 or newer).
3. Download the mod JAR file.
4. Place the JAR into your `.minecraft/mods` folder.
5. Launch Minecraft with the Fabric profile.

---

## **Building from Source**
Clone the repository and run the Gradle build:

```bash
git clone https://github.com/AaravVishal1/Inventory-Weight
cd Inventory-Weight
./gradlew build
```

The compiled JAR will be in `build/libs/`.

For development, you can run the client directly:

```bash
./gradlew runClient
```

---

## **Requirements**

| Dependency | Version |
|------------|---------|
| Minecraft | 1.20.1 |
| Fabric Loader | 0.15.6+ |
| Fabric API | 0.92.0+1.20.1 |
| Java | 17+ |

---

## **Troubleshooting**

### Movement speed isn't affected
1. Check that the mod is enabled (`/weight toggle`).
2. Verify you're not in Creative or Spectator mode.
3. Make sure you're not riding a horse/minecart (transport exception).
4. Check your weight with `/weight check` to see your current tier.

### Weight seems too punishing
- Lower the tier thresholds in the config to require more weight.
- Reduce category weights for item types you carry often.
- Disable hardcore mode if enabled.
- Disable individual penalties (swimming, climbing, etc.) as needed.

### Stamina drains too fast
- Reduce `baseStaminaDrain` in the config.
- Increase `maxStamina` for a larger pool.
- Carry lighter loads more often.

### HUD/tooltips not showing
- Enable `showHud` and `showWeightInTooltip` in config.
- Make sure you have the client-side mod installed.
- Try reloading with `/weight reload`.

### Items have wrong weights
- Use `/weight set <item> <weight>` to assign custom weights.
- Edit `customItemWeights` in the config file directly.
- Category assignment is based on item name keywords.

### Mod conflicts with other mods
- The mod uses standard Fabric events and mixins.
- If another mod modifies player movement, there may be conflicts.
- Report issues with specific mod combinations.

---

## **Technical Details**

### **Performance**
- Weight recalculated every 10 ticks (0.5 seconds)
- Immediate recalculation on inventory change events
- Minimal performance impact during normal gameplay

### **Mixins Used**

| Mixin | Purpose |
|-------|---------|
| `PlayerEntityMixin` | Movement speed modification |
| `LivingEntityMixin` | Jump velocity and knockback |
| `ServerPlayerEntityMixin` | Server-side sprint prevention |
| `ClientPlayerEntityMixin` | Client-side sprint prevention |
| `FallDamageMixin` | Fall damage scaling |
| `SwimmingMixin` | Swimming speed reduction |
| `ClimbingMixin` | Ladder/vine climbing speed |
| `HungerMixin` | Weight-based exhaustion |
| `BlockBreakMixin` | Mining speed reduction |
| `ElytraMixin` | Elytra flight penalties |
| `FootstepMixin` | Footstep volume modification |

---

## **Compatibility**
- Compatible with most Fabric mods
- Uses standard Fabric events and mixins
- Does not modify vanilla items or add new content
- Works with modded items (auto-categorized by name)

---

## **License**
This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.