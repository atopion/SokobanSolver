
class Pathfind_Algorithms:
    def __init__(self, width, cleared_map):
        self.width = width
        self.cleared_map = cleared_map
        for i in range(len(cleared_map)):
            self.cleared_map[i] = Node(cleared_map[i], i)
        

    def children(self, point):
        links = [self.cleared_map[d] for d in [point.point - 1, point.point + 1, point.point - self.width, point.point + self.width]]
        return [link for link in links if link.value % 2 != 0]

    def manhattan(self, point,point2):
        return abs(point.point % self.width - point2.point % self.width) + abs(point.point // self.width - point2.point // self.width)
    
    def A_sternchen(self, start, goal):
        #start = Node(self.cleared_map[start], start)
        #goal = Node(self.cleared_map[goal], goal)
        start = self.cleared_map[start]
        goal = self.cleared_map[goal]

        #The open and closed sets
        openset = set()
        closedset = set()
        #Current point is the starting point
        current = start
        #Add the starting point to the open set
        openset.add(current)
        #While the open set is not empty
        while openset:
            #Find the item in the open set with the lowest G + H score
            current = min(openset, key=lambda o:o.G + o.H)
            #If it is the item we want, retrace the path and return it
            if current == goal:
                path = []
                while current.parent:
                    path.append(current)
                    current = current.parent
                path.append(current)
                return path[::-1]
            #Remove the item from the open set
            openset.remove(current)
            #Add it to the closed set
            closedset.add(current)
            #Loop through the node's children/siblings
            for node in self.children(current):
                #If it is already in the closed set, skip it
                if node in closedset:
                    continue
                #Otherwise if it is already in the open set
                if node in openset:
                    #Check if we beat the G score 
                    new_g = current.G + current.move_cost()
                    if node.G > new_g:
                        #If so, update the node to have a new parent
                        node.G = new_g
                        node.parent = current
                else:
                    #If it isn't in the open set, calculate the G and H score for the node
                    node.G = current.G + current.move_cost()
                    node.H = self.manhattan(node, goal)
                    #Set the parent to our current item
                    node.parent = current
                    #Add it to the set
                    openset.add(node)
        #Throw an exception if there is no path
        raise ValueError('No Path Found')
        
class Node:
    def __init__(self,value,point):
        self.value = value
        self.point = point
        self.parent = None
        self.H = 0
        self.G = 0
    def move_cost(self):
        return 1
