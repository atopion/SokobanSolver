import math
import numpy as np
import queue

# Metric codes
PYTHAGOREAN_METRIC = 1
MANHATTAN_METRIC = 2
PULL_METRIC = 3


class Metrics:

    def __init__(self, width, height, targets, clearedBoard, metric_code):
        self.width = width
        self.height = height
        self.metric_code = metric_code
        self.distance_to_goals = np.full((len(targets), self.width * self.height), 32000, dtype=int)
        self.targets_code = {}

        target_queue = queue.Queue()
        for i in range(0, len(targets), 1):
            self.targets_code[targets[i]] = i
            self.distance_to_goals[i][targets[i]] = 0
            target_queue.put(targets[i])
            while not target_queue.empty():
                position = target_queue.get()
                for d in [-1, 1, -self.width, self.width]:
                    box_pos = position + d
                    player_pos = position + 2 * d
                    if self.distance_to_goals[i][box_pos] == 32000:
                        if not clearedBoard[box_pos] == 4 and not clearedBoard[player_pos] == 4:
                            self.distance_to_goals[i][box_pos] = self.distance_to_goals[i][position] + 1
                            target_queue.put(box_pos)

    def manhattan_distance(self, a, b):
        a_x = int(a % self.width)
        a_y = int(a / self.width)
        b_x = int(b % self.width)
        b_y = int(b / self.width)
        return Metrics.manhattan_distance_2d(a_x, a_y, b_x, b_y)

    @staticmethod
    def manhattan_distance_2d(a_x, a_y, b_x, b_y):
        return abs(a_x - b_x) + abs(a_y - b_y)

    def pythagorean_distance(self, a, b):
        a_x = int(a % self.width)
        a_y = int(a / self.width)
        b_x = int(b % self.width)
        b_y = int(b / self.width)
        return Metrics.pythagorean_distance_2d(a_x, a_y, b_x, b_y)

    @staticmethod
    def pythagorean_distance_2d(a_x, a_y, b_x, b_y):
        return math.sqrt((a_x - b_x)**2 + (a_y - b_y)**2)

    def pull_distance(self, target, pos):
        if target not in self.targets_code:
            return -1
        else:
            return self.distance_to_goals[self.targets_code[target]][pos]

    def pull_distance_2d(self, target_x, target_y, pos_x, pos_y):
        target = self.width * target_y + target_x
        pos = self.width * pos_x + pos_y
        return self.pull_distance(target, pos)

    def distance(self, target, pos):
        if self.metric_code == PYTHAGOREAN_METRIC:
            return self.pythagorean_distance(target, pos)
        if self.metric_code == MANHATTAN_METRIC:
            return self.manhattan_distance(target, pos)
        if self.metric_code == PULL_METRIC:
            return self.pull_distance(target, pos)

    def distance_2d(self, target_x, target_y, pos_x, pos_y):
        if self.metric_code == PYTHAGOREAN_METRIC:
            return self.pythagorean_distance_2d(target_x, target_y, pos_x, pos_y)
        if self.metric_code == MANHATTAN_METRIC:
            return self.manhattan_distance_2d(target_x, target_y, pos_x, pos_y)
        if self.metric_code == PULL_METRIC:
            return self.pull_distance_2d(target_x, target_y, pos_x, pos_y)
