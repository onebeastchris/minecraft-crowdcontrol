using System;
using System.Collections.Generic;
using CrowdControl.Common;

namespace CrowdControl.Games.Packs
{
    public class Minecraft : SimpleTCPPack
    {
        public override string Host => "127.0.0.1";

        public override ushort Port => 58431;

        public Minecraft(IPlayer player, Func<CrowdControlBlock, bool> responseHandler, Action<object> statusUpdateHandler) : base(player, responseHandler, statusUpdateHandler) { }

        public override Game Game => new Game(108, "Minecraft (Server)", "MinecraftServer", "PC", ConnectorType.SimpleTCPConnector);

        public override List<Effect> Effects => new List<Effect>
        {
            // miscellaneous
            new Effect("Miscellaneous", "miscellaneous", ItemKind.Folder),
            new Effect("Spawn Ore Vein", "vein", "miscellaneous"),
            new Effect("Spooky Sound Effect", "sfx", "miscellaneous"),
            new Effect("Swap Locations", "swap", "miscellaneous"), // swaps the locations of every online player
            new Effect("Dinnerbone", "dinnerbone", "miscellaneous"), // flips nearby mobs upside-down
            new Effect("Clutter Inventories", "clutter", "miscellaneous"), // shuffles around a couple items in everyone's inventory
            new Effect("Open Lootbox", "lootbox", "miscellaneous"), // gives a completely random item with varying enchants and modifiers
            new Effect("Eat Chorus Fruit", "chorus_fruit", "miscellaneous"), // teleports the player to a random nearby block as if they ate a Chorus Fruit
            new Effect("Render Toasts", "toast", "miscellaneous"), // displays a bunch of "Recipe Unlocked" notifications in the top right
            new Effect("Freeze (7s)", "freeze", "miscellaneous"), // locks everyone in place for 7 seconds (camera rotation allowed)
            new Effect("Camera Lock (7s)", "camera_lock", "miscellaneous"), // locks everyone's camera in place for 7 seconds (movement allowed)
            new Effect("Camera Lock To Sky (20s)", "camera_lock_to_sky", "miscellaneous"), // locks everyone's cameras to face the sky
            new Effect("Camera Lock To Ground (20s)", "camera_lock_to_ground", "miscellaneous"), // locks everyone's cameras to face the ground
            new Effect("Place Flowers", "flowers", "miscellaneous"), // places a bunch of flowers nearby as if a Bonemeal item was used
            new Effect("Place Torches", "lit", "miscellaneous"), // places torches on every valid nearby block
            new Effect("Remove Torches", "dim", "miscellaneous"), // removes nearby torches
            new Effect("Replace Area With Gravel", "gravel_hell", "miscellaneous"), // replaces nearby stone blocks with gravel
            new Effect("Dig Hole", "dig", "miscellaneous"), // creates a small hole underneath every player
            new Effect("Zip Time", "zip", "miscellaneous"), // adds a minute to the in-game day/night cycle
            new Effect("Name Item", "name_item", "miscellaneous"), // names a held item after the viewer
            new Effect("Repair Item", "repair_item", "miscellaneous"), // sets the durability of an item to 100%
            new Effect("Damage Item", "damage_item", "miscellaneous"), // sets the durability of an item to 50%
            new Effect("Put Item on Head", "hat", "miscellaneous"), // swaps the held item and the player's head item
            new Effect("Respawn Players", "respawn", "miscellaneous"),
            new Effect("Drop Held Item", "drop_item", "miscellaneous"),
            new Effect("Delete Held Item", "delete_item", "miscellaneous"),
            new Effect("Water Bucket Clutch", "bucket_clutch", "miscellaneous"), // teleports players 30 blocks up and gives them a water bucket
            new Effect("Kill Players", "kill", "miscellaneous"),
            new Effect("Damage Players (1 Heart)", "damage_1", "miscellaneous"),
            new Effect("Heal Players (1 Heart)", "heal_1", "miscellaneous"),
            new Effect("Heal Players", "full_heal", "miscellaneous"),
            new Effect("Feed Players", "feed", "miscellaneous"),
            new Effect("Feed Players (1 Bar)", "feed_1", "miscellaneous"),
            new Effect("Starve Players", "starve", "miscellaneous"), // makes everyone hungry
            new Effect("Remove One Hunger Bar", "starve_1", "miscellaneous"),
            new Effect("Give One XP Level", "xp_plus1", "miscellaneous"),
            new Effect("Take One XP Level", "xp_sub1", "miscellaneous"),
            new Effect("Reset Experience Progress", "reset_exp_progress", "miscellaneous"), // resets the progress to the next level up
            new Effect("+1 Max Health", "max_health_plus1", "miscellaneous"),
            new Effect("-1 Max Health", "max_health_sub1", "miscellaneous"),
            new Effect("Disable Jumping (10s)", "disable_jumping", "miscellaneous"),
            new Effect("Teleport All Entities To Players", "entity_chaos", "miscellaneous"),
            // set gamemode for 30 seconds
            new Effect("Change Gamemode", "change_gamemode", ItemKind.Folder),
            new Effect("Adventure Mode (15s)", "adventure_mode", "change_gamemode"),
            new Effect("Creative Mode (15s)", "creative_mode", "change_gamemode"),
            new Effect("Spectator Mode (8s)", "spectator_mode", "change_gamemode"),
            // teleports players by a few blocks in the specified direction
            new Effect("Teleport Players", "teleportation", ItemKind.Folder),
            new Effect("Move Up", "up", "teleportation"),
            new Effect("Move Down", "down", "teleportation"),
            new Effect("Move X+", "xplus", "teleportation"),
            new Effect("Move X-", "xminus", "teleportation"),
            new Effect("Move Z+", "zplus", "teleportation"),
            new Effect("Move Z-", "zminus", "teleportation"),
            // summons a mob around each player
            new Effect("Summon Entity", "summon_entity", ItemKind.Folder),
            new Effect("Summon Charged Creeper", "entity_charged_creeper", "summon_entity"),
            new Effect("Summon Creeper", "entity_creeper", "summon_entity"),
            new Effect("Summon Skeleton", "entity_skeleton", "summon_entity"),
            new Effect("Summon Zombie", "entity_zombie", "summon_entity"),
            new Effect("Summon Zoglin", "entity_zoglin", "summon_entity"),
            new Effect("Summon Bat", "entity_bat", "summon_entity"),
            new Effect("Summon Bee", "entity_bee", "summon_entity"),
            new Effect("Summon Blaze", "entity_blaze", "summon_entity"),
            new Effect("Summon Boat", "entity_boat", "summon_entity"),
            new Effect("Summon Cat", "entity_cat", "summon_entity"),
            new Effect("Summon Cave Spider", "entity_cave_spider", "summon_entity"),
            new Effect("Summon Cod", "entity_cod", "summon_entity"),
            new Effect("Summon Cow", "entity_cow", "summon_entity"),
            new Effect("Summon Chicken", "entity_chicken", "summon_entity"),
            new Effect("Summon Dolphin", "entity_dolphin", "summon_entity"),
            new Effect("Summon Donkey", "entity_donkey", "summon_entity"),
            new Effect("Summon Drowned", "entity_drowned", "summon_entity"),
            new Effect("Summon Elder Guardian", "entity_elder_guardian", "summon_entity"),
            new Effect("Summon Enderman", "entity_enderman", "summon_entity"),
            new Effect("Summon Endermite", "entity_endermite", "summon_entity"),
            new Effect("Summon Evoker", "entity_evoker", "summon_entity"),
            new Effect("Summon Fox", "entity_fox", "summon_entity"),
            new Effect("Summon Ghast", "entity_ghast", "summon_entity"),
            new Effect("Summon Giant", "entity_giant", "summon_entity"),
            new Effect("Summon Guardian", "entity_guardian", "summon_entity"),
            new Effect("Summon Hoglin", "entity_hoglin", "summon_entity"),
            new Effect("Summon Horse", "entity_horse", "summon_entity"),
            new Effect("Summon Husk", "entity_husk", "summon_entity"),
            new Effect("Summon Lightning Bolt", "entity_lightning", "summon_entity"),
            new Effect("Summon Iron Golem", "entity_iron_golem", "summon_entity"),
            new Effect("Summon Illusioner", "entity_illusioner", "summon_entity"),
            new Effect("Summon Llama", "entity_llama", "summon_entity"),
            new Effect("Summon Magma Cube", "entity_magma_cube", "summon_entity"),
            new Effect("Summon Minecart", "entity_minecart", "summon_entity"),
            new Effect("Summon Minecart with Chest", "entity_minecart_chest", "summon_entity"),
            new Effect("Summon Minecart with Furnace", "entity_minecart_furnace", "summon_entity"),
            new Effect("Summon Minecart with Hopper", "entity_minecart_hopper", "summon_entity"),
            new Effect("Summon Minecart with TNT", "entity_minecart_tnt", "summon_entity"),
            new Effect("Summon Primed TNT", "entity_primed_tnt", "summon_entity"),
            new Effect("Summon Mule", "entity_mule", "summon_entity"),
            new Effect("Summon Mooshroom", "entity_mushroom_cow", "summon_entity"),
            new Effect("Summon Ocelot", "entity_ocelot", "summon_entity"),
            new Effect("Summon Panda", "entity_panda", "summon_entity"),
            new Effect("Summon Parrot", "entity_parrot", "summon_entity"),
            new Effect("Summon Phantom", "entity_phantom", "summon_entity"),
            new Effect("Summon Pig", "entity_pig", "summon_entity"),
            new Effect("Summon Piglin", "entity_piglin", "summon_entity"),
            new Effect("Summon Piglin Brute", "entity_piglin_brute", "summon_entity"),
            new Effect("Summon Pillager", "entity_pillager", "summon_entity"),
            new Effect("Summon Polar Bear", "entity_polar_bear", "summon_entity"),
            new Effect("Summon Pufferfish", "entity_pufferfish", "summon_entity"),
            new Effect("Summon Zombified Piglin", "entity_zombified_piglin", "summon_entity"),
            new Effect("Summon Zombie Horse", "entity_zombie_horse", "summon_entity"),
            new Effect("Summon Zombie Villager", "entity_zombie_villager", "summon_entity"),
            new Effect("Summon Wolf", "entity_wolf", "summon_entity"),
            new Effect("Summon Wither Skeleton", "entity_wither_skeleton", "summon_entity"),
            new Effect("Summon Wandering Trader", "entity_wandering_trader", "summon_entity"),
            new Effect("Summon Witch", "entity_witch", "summon_entity"),
            new Effect("Summon Vindicator", "entity_vindicator", "summon_entity"),
            new Effect("Summon Villager", "entity_villager", "summon_entity"),
            new Effect("Summon Vex", "entity_vex", "summon_entity"),
            new Effect("Summon Turtle", "entity_turtle", "summon_entity"),
            new Effect("Summon Tropical Fish", "entity_tropical_fish", "summon_entity"),
            new Effect("Summon Trader Llama", "entity_trader_llama", "summon_entity"),
            new Effect("Summon Strider", "entity_strider", "summon_entity"),
            new Effect("Summon Stray", "entity_stray", "summon_entity"),
            new Effect("Summon Squid", "entity_squid", "summon_entity"),
            new Effect("Summon Spider", "entity_spider", "summon_entity"),
            new Effect("Summon Snow Golem", "entity_snowman", "summon_entity"),
            new Effect("Summon Slime", "entity_slime", "summon_entity"),
            new Effect("Summon Silverfish", "entity_silverfish", "summon_entity"),
            new Effect("Summon Skeleton Horse", "entity_skeleton_horse", "summon_entity"),
            new Effect("Summon Shulker", "entity_shulker", "summon_entity"),
            new Effect("Summon Sheep", "entity_sheep", "summon_entity"),
            new Effect("Summon Salmon", "entity_salmon", "summon_entity"),
            new Effect("Summon Ravager", "entity_ravager", "summon_entity"),
            new Effect("Summon Rabbit", "entity_rabbit", "summon_entity"),
            new Effect("Summon Armor Stand", "entity_armor_stand", "summon_entity"),
            new Effect("Summon Axolotl", "entity_axolotl", "summon_entity"),
            new Effect("Summon Glow Squid", "entity_glow_squid", "summon_entity"),
            new Effect("Summon Goat", "entity_goat", "summon_entity"),
            // remove nearest entity
            new Effect("Remove Entity", "remove_entity", ItemKind.Folder),
            new Effect("Remove Creeper", "remove_entity_creeper", "remove_entity"),
            new Effect("Remove Skeleton", "remove_entity_skeleton", "remove_entity"),
            new Effect("Remove Zombie", "remove_entity_zombie", "remove_entity"),
            new Effect("Remove Zoglin", "remove_entity_zoglin", "remove_entity"),
            new Effect("Remove Bat", "remove_entity_bat", "remove_entity"),
            new Effect("Remove Bee", "remove_entity_bee", "remove_entity"),
            new Effect("Remove Blaze", "remove_entity_blaze", "remove_entity"),
            new Effect("Remove Boat", "remove_entity_boat", "remove_entity"),
            new Effect("Remove Cat", "remove_entity_cat", "remove_entity"),
            new Effect("Remove Cave Spider", "remove_entity_cave_spider", "remove_entity"),
            new Effect("Remove Cod", "remove_entity_cod", "remove_entity"),
            new Effect("Remove Cow", "remove_entity_cow", "remove_entity"),
            new Effect("Remove Chicken", "remove_entity_chicken", "remove_entity"),
            new Effect("Remove Dolphin", "remove_entity_dolphin", "remove_entity"),
            new Effect("Remove Donkey", "remove_entity_donkey", "remove_entity"),
            new Effect("Remove Drowned", "remove_entity_drowned", "remove_entity"),
            new Effect("Remove Elder Guardian", "remove_entity_elder_guardian", "remove_entity"),
            new Effect("Remove Enderman", "remove_entity_enderman", "remove_entity"),
            new Effect("Remove Endermite", "remove_entity_endermite", "remove_entity"),
            new Effect("Remove Evoker", "remove_entity_evoker", "remove_entity"),
            new Effect("Remove Fox", "remove_entity_fox", "remove_entity"),
            new Effect("Remove Ghast", "remove_entity_ghast", "remove_entity"),
            new Effect("Remove Giant", "remove_entity_giant", "remove_entity"),
            new Effect("Remove Guardian", "remove_entity_guardian", "remove_entity"),
            new Effect("Remove Hoglin", "remove_entity_hoglin", "remove_entity"),
            new Effect("Remove Horse", "remove_entity_horse", "remove_entity"),
            new Effect("Remove Husk", "remove_entity_husk", "remove_entity"),
            new Effect("Remove Lightning Bolt", "remove_entity_lightning", "remove_entity"),
            new Effect("Remove Iron Golem", "remove_entity_iron_golem", "remove_entity"),
            new Effect("Remove Illusioner", "remove_entity_illusioner", "remove_entity"),
            new Effect("Remove Llama", "remove_entity_llama", "remove_entity"),
            new Effect("Remove Magma Cube", "remove_entity_magma_cube", "remove_entity"),
            new Effect("Remove Minecart", "remove_entity_minecart", "remove_entity"),
            new Effect("Remove Minecart with Chest", "remove_entity_minecart_chest", "remove_entity"),
            new Effect("Remove Minecart with Furnace", "remove_entity_minecart_furnace", "remove_entity"),
            new Effect("Remove Minecart with Hopper", "remove_entity_minecart_hopper", "remove_entity"),
            new Effect("Remove Minecart with TNT", "remove_entity_minecart_tnt", "remove_entity"),
            new Effect("Remove Primed TNT", "remove_entity_primed_tnt", "remove_entity"),
            new Effect("Remove Mule", "remove_entity_mule", "remove_entity"),
            new Effect("Remove Mooshroom", "remove_entity_mushroom_cow", "remove_entity"),
            new Effect("Remove Ocelot", "remove_entity_ocelot", "remove_entity"),
            new Effect("Remove Panda", "remove_entity_panda", "remove_entity"),
            new Effect("Remove Parrot", "remove_entity_parrot", "remove_entity"),
            new Effect("Remove Phantom", "remove_entity_phantom", "remove_entity"),
            new Effect("Remove Pig", "remove_entity_pig", "remove_entity"),
            new Effect("Remove Piglin", "remove_entity_piglin", "remove_entity"),
            new Effect("Remove Piglin Brute", "remove_entity_piglin_brute", "remove_entity"),
            new Effect("Remove Pillager", "remove_entity_pillager", "remove_entity"),
            new Effect("Remove Polar Bear", "remove_entity_polar_bear", "remove_entity"),
            new Effect("Remove Pufferfish", "remove_entity_pufferfish", "remove_entity"),
            new Effect("Remove Zombified Piglin", "remove_entity_zombified_piglin", "remove_entity"),
            new Effect("Remove Zombie Horse", "remove_entity_zombie_horse", "remove_entity"),
            new Effect("Remove Zombie Villager", "remove_entity_zombie_villager", "remove_entity"),
            new Effect("Remove Wolf", "remove_entity_wolf", "remove_entity"),
            new Effect("Remove Wither Skeleton", "remove_entity_wither_skeleton", "remove_entity"),
            new Effect("Remove Wandering Trader", "remove_entity_wandering_trader", "remove_entity"),
            new Effect("Remove Witch", "remove_entity_witch", "remove_entity"),
            new Effect("Remove Vindicator", "remove_entity_vindicator", "remove_entity"),
            new Effect("Remove Villager", "remove_entity_villager", "remove_entity"),
            new Effect("Remove Vex", "remove_entity_vex", "remove_entity"),
            new Effect("Remove Turtle", "remove_entity_turtle", "remove_entity"),
            new Effect("Remove Tropical Fish", "remove_entity_tropical_fish", "remove_entity"),
            new Effect("Remove Trader Llama", "remove_entity_trader_llama", "remove_entity"),
            new Effect("Remove Strider", "remove_entity_strider", "remove_entity"),
            new Effect("Remove Stray", "remove_entity_stray", "remove_entity"),
            new Effect("Remove Squid", "remove_entity_squid", "remove_entity"),
            new Effect("Remove Spider", "remove_entity_spider", "remove_entity"),
            new Effect("Remove Snow Golem", "remove_entity_snowman", "remove_entity"),
            new Effect("Remove Slime", "remove_entity_slime", "remove_entity"),
            new Effect("Remove Silverfish", "remove_entity_silverfish", "remove_entity"),
            new Effect("Remove Skeleton Horse", "remove_entity_skeleton_horse", "remove_entity"),
            new Effect("Remove Shulker", "remove_entity_shulker", "remove_entity"),
            new Effect("Remove Sheep", "remove_entity_sheep", "remove_entity"),
            new Effect("Remove Salmon", "remove_entity_salmon", "remove_entity"),
            new Effect("Remove Ravager", "remove_entity_ravager", "remove_entity"),
            new Effect("Remove Rabbit", "remove_entity_rabbit", "remove_entity"),
            new Effect("Remove Armor Stand", "remove_entity_armor_stand", "remove_entity"),
            new Effect("Remove Axolotl", "remove_entity_axolotl", "remove_entity"),
            new Effect("Remove Glow Squid", "remove_entity_glow_squid", "remove_entity"),
            new Effect("Remove Goat", "remove_entity_goat", "remove_entity"),
            // sets the server difficulty (affects how much damage mobs deal)
            new Effect("Set Difficulty", "difficulty", ItemKind.Folder),
            new Effect("Peaceful Mode", "difficulty_peaceful", "difficulty"),
            new Effect("Easy Mode", "difficulty_easy", "difficulty"),
            new Effect("Normal Mode", "difficulty_normal", "difficulty"),
            new Effect("Hard Mode", "difficulty_hard", "difficulty"),
            // applies potion effects to every player
            new Effect("Apply Potion Effect", "apply_potion_effect", ItemKind.Folder),
            new Effect("Apply Speed Potion Effect", "potion_speed", "apply_potion_effect"),
            new Effect("Apply Slowness Potion Effect", "potion_slowness", "apply_potion_effect"),
            new Effect("Apply Haste Potion Effect", "potion_haste", "apply_potion_effect"),
            new Effect("Apply Mining Fatigue Potion Effect", "potion_mining_fatigue", "apply_potion_effect"),
            new Effect("Apply Strength Potion Effect", "potion_strength", "apply_potion_effect"),
            new Effect("Apply Healing Potion Effect", "potion_healing", "apply_potion_effect"),
            new Effect("Apply Harming Potion Effect", "potion_harming", "apply_potion_effect"),
            new Effect("Apply Jump Boost Potion Effect", "potion_jump_boost", "apply_potion_effect"),
            new Effect("Apply Nausea Potion Effect", "potion_nausea", "apply_potion_effect"),
            new Effect("Apply Regeneration Potion Effect", "potion_regeneration", "apply_potion_effect"),
            new Effect("Apply Resistance Potion Effect", "potion_resistance", "apply_potion_effect"),
            new Effect("Apply Fire Resistance Potion Effect", "potion_fire_resistance", "apply_potion_effect"),
            new Effect("Apply Water Breathing Potion Effect", "potion_water_breathing", "apply_potion_effect"),
            new Effect("Apply Invisibility Potion Effect", "potion_invisibility", "apply_potion_effect"),
            new Effect("Apply Blindness Potion Effect", "potion_blindness", "apply_potion_effect"),
            new Effect("Apply Night Vision Potion Effect", "potion_night_vision", "apply_potion_effect"),
            new Effect("Apply Hunger Potion Effect", "potion_hunger", "apply_potion_effect"),
            new Effect("Apply Weakness Potion Effect", "potion_weakness", "apply_potion_effect"),
            new Effect("Apply Poison Potion Effect", "potion_poison", "apply_potion_effect"),
            new Effect("Apply Wither Potion Effect", "potion_wither", "apply_potion_effect"),
            new Effect("Apply Health Boost Potion Effect", "potion_health_boost", "apply_potion_effect"),
            new Effect("Apply Absorption Potion Effect", "potion_absorption", "apply_potion_effect"),
            new Effect("Apply Saturation Potion Effect", "potion_saturation", "apply_potion_effect"),
            new Effect("Apply Glowing Potion Effect", "potion_glowing", "apply_potion_effect"),
            new Effect("Apply Levitation Potion Effect", "potion_levitation", "apply_potion_effect"),
            new Effect("Apply Luck Potion Effect", "potion_luck", "apply_potion_effect"),
            new Effect("Apply Bad Luck Potion Effect", "potion_bad_luck", "apply_potion_effect"),
            new Effect("Apply Slow Falling Potion Effect", "potion_slow_falling", "apply_potion_effect"),
            new Effect("Apply Conduit Power Potion Effect", "potion_conduit_power", "apply_potion_effect"),
            new Effect("Apply Dolphins Grace Potion Effect", "potion_dolphins_grace", "apply_potion_effect"),
            new Effect("Apply Bad Omen Potion Effect", "potion_bad_omen", "apply_potion_effect"),
            new Effect("Apply Hero Of The Village Potion Effect", "potion_hero_of_the_village", "apply_potion_effect"),
            // places a block at everyone's feet
            new Effect("Place Block", "place_block", ItemKind.Folder),
            new Effect("Place TNT Block", "block_tnt", "place_block"),
            new Effect("Place Fire Block", "block_fire", "place_block"),
            new Effect("Place Cobweb Block", "block_cobweb", "place_block"),
            new Effect("Place Redstone Torch Block", "block_redstone_torch", "place_block"),
            new Effect("Place Wither Rose Block", "block_wither_rose", "place_block"),
            // places a block several blocks above everyone's head
            new Effect("Place Falling Block", "place_falling_block", ItemKind.Folder),
            new Effect("Falling Anvil Block", "falling_block_anvil", "place_falling_block"),
            new Effect("Falling Sand Block", "falling_block_sand", "place_falling_block"),
            new Effect("Falling Red Sand Block", "falling_block_red_sand", "place_falling_block"),
            new Effect("Falling Gravel Block", "falling_block_gravel", "place_falling_block"),
            // sets the server weather
            new Effect("Weather", "weather", ItemKind.Folder),
            new Effect("Set Weather to Downfall", "downfall", "weather"),
            new Effect("Set Weather to Clear", "clear", "weather"),
            // apply enchants
            new Effect("Enchantments", "enchantments", ItemKind.Folder),
            new Effect("Remove Enchants", "remove_enchants", "enchantments"), // removes all enchants from the held item
            new Effect("Apply Fire Protection IV", "enchant_fire_protection", "enchantments"),
            new Effect("Apply Sharpness V", "enchant_sharpness", "enchantments"),
            new Effect("Apply Flame", "enchant_flame", "enchantments"),
            new Effect("Apply Soul Speed III", "enchant_soul_speed", "enchantments"),
            new Effect("Apply Aqua Affinity", "enchant_aqua_affinity", "enchantments"),
            new Effect("Apply Punch II", "enchant_punch", "enchantments"),
            new Effect("Apply Loyalty III", "enchant_loyalty", "enchantments"),
            new Effect("Apply Depth Strider III", "enchant_depth_strider", "enchantments"),
            new Effect("Apply Curse of Vanishing", "enchant_curse_of_vanishing", "enchantments"),
            new Effect("Apply Unbreaking III", "enchant_unbreaking", "enchantments"),
            new Effect("Apply Knockback II", "enchant_knockback", "enchantments"),
            new Effect("Apply Luck of the Sea III", "enchant_luck_of_the_sea", "enchantments"),
            new Effect("Apply Curse of Binding", "enchant_curse_of_binding", "enchantments"),
            new Effect("Apply Fortune III", "enchant_fortune", "enchantments"),
            new Effect("Apply Protection IV", "enchant_protection", "enchantments"),
            new Effect("Apply Efficiency V", "enchant_efficiency", "enchantments"),
            new Effect("Apply Mending", "enchant_mending", "enchantments"),
            new Effect("Apply Frost Walker II", "enchant_frost_walker", "enchantments"),
            new Effect("Apply Lure III", "enchant_lure", "enchantments"),
            new Effect("Apply Looting III", "enchant_looting", "enchantments"),
            new Effect("Apply Piercing IV", "enchant_piercing", "enchantments"),
            new Effect("Apply Blast Protection IV", "enchant_blast_protection", "enchantments"),
            new Effect("Apply Smite V", "enchant_smite", "enchantments"),
            new Effect("Apply Multishot", "enchant_multishot", "enchantments"),
            new Effect("Apply Fire Aspect II", "enchant_fire_aspect", "enchantments"),
            new Effect("Apply Channeling", "enchant_channeling", "enchantments"),
            new Effect("Apply Sweeping Edge III", "enchant_sweeping_edge", "enchantments"),
            new Effect("Apply Thorns III", "enchant_thorns", "enchantments"),
            new Effect("Apply Bane of Arthropods V", "enchant_bane_of_arthropods", "enchantments"),
            new Effect("Apply Respiration III", "enchant_respiration", "enchantments"),
            new Effect("Apply Riptide III", "enchant_riptide", "enchantments"),
            new Effect("Apply Silk Touch", "enchant_silk_touch", "enchantments"),
            new Effect("Apply Quick Charge III", "enchant_quick_charge", "enchantments"),
            new Effect("Apply Projectile Protection IV", "enchant_projectile_protection", "enchantments"),
            new Effect("Apply Impaling V", "enchant_impaling", "enchantments"),
            new Effect("Apply Feather Falling IV", "enchant_feather_falling", "enchantments"),
            new Effect("Apply Power V", "enchant_power", "enchantments"),
            new Effect("Apply Infinity", "enchant_infinity", "enchantments"),
            // gives 1 item
            new Effect("Give an Item", "give_item", ItemKind.Folder),
            new Effect("Give Wooden Pickaxe", "give_wooden_pickaxe", "give_item"),
            new Effect("Give Stone Pickaxe", "give_stone_pickaxe", "give_item"),
            new Effect("Give Golden Pickaxe", "give_golden_pickaxe", "give_item"),
            new Effect("Give Iron Pickaxe", "give_iron_pickaxe", "give_item"),
            new Effect("Give Diamond Pickaxe", "give_diamond_pickaxe", "give_item"),
            new Effect("Give Netherite Pickaxe", "give_netherite_pickaxe", "give_item"),
            new Effect("Give Golden Apple", "give_golden_apple", "give_item"),
            new Effect("Give Enchanted Golden Apple", "give_enchanted_golden_apple", "give_item"),
            // takes 1 item
            new Effect("Take an Item", "take_item", ItemKind.Folder),
            new Effect("Take Wooden Pickaxe", "take_wooden_pickaxe", "take_item"),
            new Effect("Take Stone Pickaxe", "take_stone_pickaxe", "take_item"),
            new Effect("Take Golden Pickaxe", "take_golden_pickaxe", "take_item"),
            new Effect("Take Iron Pickaxe", "take_iron_pickaxe", "take_item"),
            new Effect("Take Diamond Pickaxe", "take_diamond_pickaxe", "take_item"),
            new Effect("Take Netherite Pickaxe", "take_netherite_pickaxe", "take_item"),
            new Effect("Take Golden Apple", "take_golden_apple", "take_item"),
            new Effect("Take Enchanted Golden Apple", "take_enchanted_golden_apple", "take_item"),
        };
    }
}
