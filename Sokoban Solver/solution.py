#   Look for #IMPLEMENT tags in this file. These tags indicate what has
#   to be implemented to complete the warehouse domain.

#   You may add only standard python imports---i.e., ones that are automatically
#   available on TEACH.CS
#   You may not remove any imports.
#   You may not import or otherwise source any of your own files

import os  # for time functions
from search import *  # for search engines
from sokoban import SokobanState, Direction, \
    PROBLEMS  # for Sokoban specific classes and problems
import math, csv


def sokoban_goal_state(state):
    '''
  @return: Whether all boxes are stored.
  '''
    for box in state.boxes:
        if box not in state.storage:
            return False
    return True


def heur_manhattan_distance(state):
    # IMPLEMENT
    '''admissible sokoban puzzle heuristic: manhattan distance'''
    '''INPUT: a sokoban state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state to the goal.'''
    # We want an admissible heuristic, which is an optimistic heuristic.
    # It must never overestimate the cost to get from the current state to the goal.
    # The sum of the Manhattan distances between each box that has yet to be stored and the storage point nearest to it is such a heuristic.
    # When calculating distances, assume there are no obstacles on the grid.
    # You should implement this heuristic function exactly, even if it is tempting to improve it.
    # Your function should return a numeric value; this is the estimate of the distance to the goal.
    if state.boxes is None:
        return 0

    sum = 0
    for box in state.boxes:
        if box not in state.storage:
            closest = float('inf')
            for storage in state.storage:
                distance = abs(box[0] - storage[0]) + abs(box[1] - storage[1])
                if distance < closest:
                    closest = distance
            sum += closest
    return sum


# SOKOBAN HEURISTICS
def trivial_heuristic(state):
    '''trivial admissible sokoban heuristic'''
    '''INPUT: a sokoban state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state (# of moves required to get) to the goal.'''
    count = 0
    for box in state.boxes:
        if box not in state.storage:
            count += 1
    return count


def heur_alternate(state):
    # IMPLEMENT
    '''a better heuristic'''
    '''INPUT: a sokoban state'''
    '''OUTPUT: a numeric value that serves as an estimate of the distance of the state to the goal.'''
    # heur_manhattan_distance has flaws.
    # Write a heuristic function that improves upon heur_manhattan_distance to estimate distance between the current state and the goal.
    # Your function should return a numeric value for the estimate of the distance to the goal.
    if state.boxes is None:
        return 0
    distances = []
    stored = []
    storage_sum = 0
    number_of_boxes = 0
    for box in state.boxes:
        if box in state.storage:
            stored.append(box)
            distances.append([box, 0])
        # if the box is being pushed to the deadlock, the game can no longer be solved and continuing the search will be meaningless
        else:
            #dead square deadlocks: boxes can no longer be moved or reach the storage
            #check if there exists a box where left is blocked by a wall or obstacle

            obstacle_at_right = ((box[0] + 1, box[1]) in state.obstacles)
            wall_at_right = (box[0] + 1 == state.width)

            obstacle_at_left = ((box[0] - 1, box[1]) in state.obstacles)
            wall_at_left = (box[0] - 1 == -1)

            obstacle_at_top = ((box[0], box[1] + 1) in state.obstacles)
            wall_at_top = (box[1] + 1 == state.height)

            obstacle_at_bottom = ((box[0], box[1] - 1) in state.obstacles)
            wall_at_bottom = (box[1] - 1 == -1)

            if obstacle_at_left or wall_at_left:
                #box is at a corner
                if obstacle_at_top or wall_at_top:
                    return float('inf')
                if obstacle_at_bottom or wall_at_bottom:
                    return float('inf')


            # check if there exists a box where right is blocked by a wall or obstacle
            if obstacle_at_right or obstacle_at_right:
                # box is at a corner
                if obstacle_at_top or wall_at_top:
                    return float('inf')
                if obstacle_at_bottom or wall_at_bottom:
                    return float('inf')


            if wall_at_left or wall_at_right:
                # if there exist two boxes next to each other at a wall, cannot be moved
                if ((box[0], box[1] + 1) in state.boxes) or ((box[0], box[1] - 1) in state.boxes):
                    return float('inf')
                # if there is no storage on the same column, deadlock
                same_column = False
                for storage in state.storage:
                    if box[0] == storage[0]:
                        same_column = True
                        break
                if not same_column:
                    return float('inf')

            if wall_at_top or wall_at_bottom:
                # if there exist two boxes next to each other at a wall, cannot be moved
                if ((box[0] - 1, box[1]) in state.boxes) or ((box[0] + 1, box[1]) in state.boxes):
                    return float('inf')
                # if there is no storage on the same row, deadlock
                same_row = False
                for storage in state.storage:
                    if box[1] == storage[1]:
                        same_row = True
                        break
                if not same_row:
                    return float('inf')

    # if there is no deadlock use manhattan distance to find the closest distance for each box
    # however the function above assigns same storage to boxes but this is not allowed
    # based on the closest distance, assign each box to each storage
            closest = float('inf')
            location = 'Not Yet'
            before = 'N/A'

            for storage in state.storage:
                distance = abs(box[0] - storage[0]) \
                           + abs(box[1] - storage[1])
                if distance < closest:
                    if storage not in stored:
                        closest = distance
                        location = storage
                        before = 'N/A'
                    # if the storage has already been assigned switch only if the
                    # cost is less than already assigned box
                    else:
                        before = stored.index(storage)
                        if distance < distances[before][1]:
                            closest = distance
                            location = storage
                        else:
                            before = 'N/A'

            stored.append(location)
            distances.append([box, closest])
            # if box has been assigned to storage that has been store, it needs
            # to be assigned to a different empty storage
            if before != 'N/A':
                storage_sum = storage_sum - distances[before][1]
                new_closest = float('inf')
                empty = None
                # find shortest distance among the storages that have not yet been assigned
                for new_storage in state.storage:
                    new_distance = abs(distances[before][0][0] - new_storage[0]) \
                                   + abs(distances[before][0][1] - new_storage[1])
                    if (new_storage not in stored) and new_distance < new_closest:
                        new_closest = new_distance
                        empty = new_storage
                stored[before] = empty
                distances[before][1] = new_closest
                storage_sum += distances[before][1]
            storage_sum += closest
        number_of_boxes += 1

    robot_sum = 0
    for i in range(number_of_boxes):
        box = distances[i][0]
        storage = stored[i]

        # box and storage are on same column
        if box[0] == storage[0]:
            # storage is above the box
            if box[1] > storage[1]:
                position = (box[0], box[1] + 1)
            # storage is below the box
            else:
                position = (box[0], box[1] - 1)

        # box and storage are on same row
        elif box[1] == storage[1]:
            # storage is left of the box
            if box[0] > storage[0]:
                position = (box[0] + 1, box[1])
            # storage is right of the box
            else:
                position = (box[0] - 1, box[1])

        # storage is left of the box
        elif box[0] > storage[0]:
            position = (box[0] + 1, box[1])
            obstacle_at_left = ((box[0] - 1, box[1]) in state.obstacles)
            wall_at_left = (box[0] - 1 == -1)
            if obstacle_at_left or wall_at_left:
                # storage is above the box
                if box[1] > storage[1]:
                    position = (box[0], box[1] + 1)
                # storage is below the box
                else:
                    position = (box[0], box[1] - 1)

        # storage is right of the box
        else:
            position = (box[0] - 1, box[1])
            obstacle_at_right = ((box[0] + 1, box[1]) in state.obstacles)
            wall_at_right = (box[0] + 1 == state.width)
            if obstacle_at_right or wall_at_right:
                # storage is above the box
                if box[1] > storage[1]:
                    position = (box[0], box[1] + 1)
                # storage is below the box
                else:
                    position = (box[0], box[1] - 1)

        robot_closest = float('inf')
        for robot in state.robots:
            robot_distance = abs(position[0] - robot[0]) \
                             + abs(position[1] - robot[1])
            if robot_distance < robot_closest:
                robot_closest = robot_distance

        if robot_closest < robot_sum:
            robot_sum += robot_closest

    return storage_sum + robot_sum




def heur_zero(state):
    '''Zero Heuristic can be used to make A* search perform uniform cost search'''
    return 0


def fval_function(sN, weight):
    # IMPLEMENT
    """
    Provide a custom formula for f-value computation for Anytime Weighted A star.
    Returns the fval of the state contained in the sNode.
    Use this function stub to encode the standard form of weighted A* (i.e. g + w*h)

    @param sNode sN: A search node (containing a SokobanState)
    @param float weight: Weight given by Anytime Weighted A star
    @rtype: float
    """

    # Many searches will explore nodes (or states) that are ordered by their f-value.
    # For UCS, the fvalue is the same as the gval of the state. For best-first search, the fvalue is the hval of the state.
    # You can use this function to create an alternate f-value for states; this must be a function of the state and the weight.
    # The function must return a numeric f-value.
    # The value will determine your state's position on the Frontier list during a 'custom' search.
    # You must initialize your search engine object as a 'custom' search engine if you supply a custom fval function.

    return sN.gval + sN.hval * weight


def fval_function_XUP(sN, weight):
    # IMPLEMENT
    """
    Another custom formula for f-value computation for Anytime Weighted A star.
    Returns the fval of the state contained in the sNode.
    Use this function stub to encode the XUP form of weighted A*

    @param sNode sN: A search node (containing a SokobanState)
    @param float weight: Weight given by Anytime Weighted A star
    @rtype: float
    """
    return (1 / (2 * weight)) * (sN.gval + sN.hval + math.sqrt((sN.gval + sN.hval) ** 2 +
           4 * weight * (weight - 1) * (sN.hval ** 2)))


def fval_function_XDP(sN, weight):
    # IMPLEMENT
    """
    A third custom formula for f-value computation for Anytime Weighted A star.
    Returns the fval of the state contained in the sNode.
    Use this function stub to encode the XDP form of weighted A*

    @param sNode sN: A search node (containing a SokobanState)
    @param float weight: Weight given by Anytime Weighted A star
    @rtype: float
    """
    return (1 / (2 * weight)) * (sN.gval + (2 * weight - 1) * sN.hval +
            math.sqrt((sN.gval - sN.hval) ** 2 + 4 * weight * sN.gval * sN.hval))


def compare_weighted_astars():
    # IMPLEMENT
    '''Compares various different implementations of A* that use different f-value functions'''
    '''INPUT: None'''
    '''OUTPUT: None'''
    """
    This function should generate a CSV file (comparison.csv) that contains statistics from
    4 varieties of A* search on 3 practice problems.  The four varieties of A* are as follows:
    Standard A* (Variant #1), Weighted A*  (Variant #2),  Weighted A* XUP (Variant #3) and Weighted A* XDP  (Variant #4).  
    Format each line in your your output CSV file as follows:

    A,B,C,D,E,F

    where
    A is the number of the problem being solved (0,1 or 2)
    B is the A* variant being used (1,2,3 or 4)
    C is the weight being used (2,3,4 or 5)
    D is the number of paths extracted from the Frontier (or expanded) during the search
    E is the number of paths generated by the successor function during the search
    F is the overall solution cost    

    Note that you will submit your CSV file (comparison.csv) with your code
    """
    header = ['Problem Number', 'A* Variant', 'Weight', 'Extracted Paths',
              'Generated Paths', 'Overall Solution Cost']

    f = open('comparison.csv', 'w', newline="")
    writer = csv.writer(f)
    writer.writerow(header)

    for problem_n in range(0, 3):
        for a_variant in range(1, 5):
            problem = PROBLEMS[problem_n]
            if a_variant == 1:
                search_engine = SearchEngine('astar', 'default')

                search_engine.init_search(problem, sokoban_goal_state,
                                          heur_manhattan_distance)
                result = search_engine.search()
                goal_node = result[0]
                stat = result[1]

                writer.writerow(
                    [problem_n, a_variant, 'N/A', stat.states_expanded,
                     stat.states_generated, goal_node.gval])
            else:
                for weight in [2, 3, 4, 5]:
                    search_engine = SearchEngine('custom', 'default')
                    if a_variant == 2:
                        wrapped_fval_function = (lambda sN: fval_function(sN, weight))
                    elif a_variant == 3:
                        wrapped_fval_function = (lambda sN: fval_function_XUP(sN, weight))
                    else:
                        wrapped_fval_function = (lambda sN: fval_function_XDP(sN, weight))

                    search_engine.init_search(problem, sokoban_goal_state,
                                              heur_manhattan_distance,
                                              wrapped_fval_function)

                    result = search_engine.search()
                    goal_node = result[0]
                    stat = result[1]
                    writer.writerow(
                        [problem_n, a_variant, weight, stat.states_expanded,
                         stat.states_generated, goal_node.gval])
    f.close()


def anytime_weighted_astar(initial_state, heur_fn, weight=1., timebound=10):
    # IMPLEMENT
    '''Provides an implementation of anytime weighted a-star, as described in the HW1 handout'''
    '''INPUT: a sokoban state that represents the start state and a timebound (number of seconds)'''
    '''OUTPUT: A goal state (if a goal is found), else False'''
    '''implementation of weighted astar algorithm'''

    search_engine = SearchEngine('custom', 'full')

    result = False
    state = initial_state

    time_left = timebound
    start_time = os.times()[0]

    for weight in [5, 3, 1]:
        while time_left > 0:
            wrapped_fval_function = (lambda sN: fval_function(sN, weight))
            search_engine.init_search(state, sokoban_goal_state, heur_fn, wrapped_fval_function)
            result = search_engine.search(time_left)[0]

            if result:
                time_left = time_left - (os.times()[0] - start_time)
                state = result
            else:
                return result

        return result


def anytime_gbfs(initial_state, heur_fn, timebound=10):
    # IMPLEMENT
    '''Provides an implementation of anytime greedy best-first search, as described in the HW1 handout'''
    '''INPUT: a sokoban state that represents the start state and a timebound (number of seconds)'''
    '''OUTPUT: A goal state (if a goal is found), else False'''
    '''implementation of anytime greedy best-first search'''

    search_engine = SearchEngine('best_first', 'full')
    search_engine.init_search(initial_state, sokoban_goal_state, heur_fn)

    start_time = os.times()[0]
    result = search_engine.search(timebound)[0]

    if result:
        time_left = timebound - (os.times()[0] - start_time)

        while time_left > 0:
            new_result = search_engine.search(time_left, (result.gval, float("inf"), float("inf")))[0]
            time_left = time_left - (os.times()[0] - start_time)
            if new_result:
                result = new_result
        return result
    else:
        return False
