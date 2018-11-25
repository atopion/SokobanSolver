#   Solver class, to be started

import Map as GameMap
from StateTree import Node
from Execution import Execution
import TranspositionTable
import AssignmentAlgorithms
import os

level_name = "level2.txt"
cwd = os.getcwd()
path = os.path.join(cwd, "level", level_name)
level = open(path, "r").read()

game_map = GameMap.Map(level)
game_map.mapProduction()
game_map.mapPrinting()

assignment = AssignmentAlgorithms.AssignmentAlgorithms(game_map.width, game_map.height, game_map.getTargetsArray(), game_map.getClearedMap())
# states = StateTree.StateTree(game_map.getBoxArray(), game_map.getPlayerPosition(), assignment)

table = TranspositionTable.TranspositionTable(game_map.width, game_map.height, game_map.getBoxCount())

table.insert(game_map.getBoxArray(), game_map.getPlayerPosition())

root = Node(game_map.getBoxArray(), game_map.getPlayerPosition(), None, None, assignment.greedyAssignment(game_map.getBoxArray()))
execution = Execution(table, game_map)

solution = execution.execute(root)
result = []
res = ""
node = solution
if node is None:
    print("No Solution found")
else:
    print("")
    print("Solution found: ")
    while node.farther is not None:
        result.insert(0, node.move)
        n = node.move[1] - node.move[0]
        if n == 1: res = " right," + res
        elif n == -1: res = " left," + res
        elif n < 0: res = " up," + res
        elif n > 0: res = " down," + res
        node = node.farther
    print(result)
    print("PATH:", res)
    print("")

os.system("java -classpath \"C:\\Users\\atopi\\Codes\\SokobanRobot\\SokobanRobot\\out\\production\\SokobanRobot\" " +
          "distanceboard.Viewer \"" + path + "\" \"" + str(result) + "\"")
