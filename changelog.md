# Complicated Bees 3.6.0

Mellariums and Gyrofuges have gotten some balance changes! They now damage frames more the more of the same frame there
is, meaning you'll have to actually automate making frames if you want continuous high-level frame output. Gyrofuge
extraction and efficiency units have been buffed, and you can now burn bees in a honey generator (or compost them!)

Mutations are now defined as recipes! Their definition hasn't changed, but their location in datapacks has: they are now
under a namespace's recipe folder. Datapacks will need to update accordingly. This is a semi-breaking change -
unfortunately, any mutation research data a player already has will be wiped. You can use commands to restore it.

## Changed

* Mutations are now defined as recipes - they have moved in the datapack structure
* Apid library no longer shows condition heading if a mutation has no conditions
* Highlights to mutation condition descriptions
* Mellariums now damage frames more the more of the same frame there is
* Bees are now compostable
* Bees can be burned in the honey generator
* Gyrofuge extraction units add more output chance, less idle rf/t, and reduce power efficiency less 
* Gyrofuge efficiency units add less idle rf/t

## Fixed

* Handheld analyzer occasionally voiding individuals
* Frames not enchantable
* Apiarist set not tagged/enchantable
* Bee staff not enchantable
* BWG compat broken