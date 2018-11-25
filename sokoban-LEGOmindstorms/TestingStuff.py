import Map as GameMap

box = lambda x: x == 2
level = open("level.txt", "r").read()

game_map = GameMap.Map(level)
map = game_map.getGameMap()
print("GAME MAP: ", map)

boxArray = game_map.getBoxArray()
print("BOX ARRAY: ", boxArray)
