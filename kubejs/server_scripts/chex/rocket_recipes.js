// Sample KubeJS script for CHEX progression gates
ServerEvents.recipes(event => {
  // Remove easy rockets (example IDs)
  event.remove({ id: 'cosmichorizons:rocket_t1' })
  event.remove({ id: 'cosmichorizons:rocket_t2' })

  // Re-add gated T1 with LV advancement
  event.shaped('cosmichorizons:rocket_t1', [
    'IFI',
    'CMC',
    'IFI'
  ], {
    I: 'minecraft:iron_block',
    F: 'minecraft:furnace',
    C: 'minecraft:copper_block',
    M: 'minecraft:redstone_block'
  }).id('chex:rocket_t1').addCondition({
    type: 'forge:mod_loaded',
    modid: 'cosmichorizons'
  })

  // Re-add gated T2 with MV advancement
  event.shaped('cosmichorizons:rocket_t2', [
    'BFB',
    'CMC',
    'BFB'
  ], {
    B: 'minecraft:brass_block',
    F: 'minecraft:blast_furnace',
    C: 'minecraft:gold_block',
    M: 'minecraft:diamond_block'
  }).id('chex:rocket_t2').addCondition({
    type: 'forge:mod_loaded',
    modid: 'cosmichorizons'
  })
})


