#!/usr/bin/python35

import logging
from time import localtime, strftime

import mechanism
import pill_recog
from scheduler import Scheduler

pill_dispenser = mechanism.Mechanism()

state_logger = logging.getLogger('state_logger')
state_logger.setLevel(logging.DEBUG)


class StateMachine:
    def __init__(self):
        self.handlers = {}
        self.start_state = None
        self.end_states = []

    def add_state(self, name, handler, end_state=False):
        name = name.upper()
        self.handlers[name] = handler
        if end_state:
            self.end_states.append(name)

    def set_start(self, name):
        self.start_state = name.upper()

    def run(self, cargo):
        try:
            handler = self.handlers[self.start_state]
        except:
            raise Exception("must call .set_start() before .run()")
        if not self.end_states:
            raise Exception("at least one stat must be an end_state")

        while True:
            (new_state, cargo) = handler(cargo)
            if new_state.upper() in self.end_states:
                state_logger.info("reached " + new_state)
                break
            else:
                handler = self.handlers[new_state.upper()]


def state(state_transition):
    def wrapper():
        state_logger.info(state_transition.__name__)
        output = state_transition()
        state_logger.info(output[0])

    return wrapper


@state
def start_transitions():
    # check if the schedules are current
    # if it is, go to read_schedules
    # else, update schedules
    is_db_current = Scheduler.check_schedule()
    if not is_db_current:
        Scheduler.update_schedules()
    schedule = Scheduler.get_schedule()
    new_state = "read_schedule_state"
    return (new_state, schedule)


@state
def read_schedule_transitions(schedule):
    # check if it is time to dispense
    # if it is time, align pill chamber
    # else, loop back
    curr_time = strftime("%Y-%m-%d %H:%M:%S", localtime())  # will probably change format
    if curr_time in schedule:
        new_state = "align_pill_chamber_state"
        cargo = Scheduler.get_pill(curr_time)
    else:
        new_state = "read_schedule_state"
        cargo = schedule
    return (new_state, cargo)  # will probably not have to return scheude here


@state
def align_pill_chamber_transitions(pill_to_disp):
    # if pill_curr == pill_to_disp move to pushkey forward
    # else, move chamber to next pill then loop back
    pill_curr = pill_dispenser.get_current_pill()
    if pill_curr != pill_to_disp:
        next_state = "align_pill_chamber_state"
        cargo = pill_to_disp
    elif pill_curr == pill_to_disp:
        pill_dispenser.dispense_pill()
        next_state = "picture_state"  # should probably refector name to be more accurate
        cargo = None
    else:
        raise Exception("something has gone wrong in align_pill_chamber_transitions")
    return (next_state, cargo)


@state
def picture_transitions():
    # if pill is present, move to evaluate pill
    # if pill is not present, shake pill then loop back
    picture = pill_recog.take_picture()
    if pill_recog.pill_present():
        next_state = "evaluate_pill_state"
        cargo = picture
    else:
        pill_dispenser.shake_chamber()
        next_state = "picture_state"
        cargo = None
    return (next_state, cargo)
    pass


@state
def evaluate_pill_transitions(picture):
    # if confidence is >75%, open the hatch (success) then update DB
    # else, do a dispense error (failure)
    confidence = pill_recog.evaluate_picture(picture)
    if confidence >= .75:
        pill_dispenser.open_hatch()
        next_state = "start_transitions_state"
    else:
        next_state = "dispense_error_state"
    return (next_state, confidence)
    pass


if __name__ == "__main__":
    m = StateMachine()
    m.add_state("start", start_transitions)
    m.add_state("read schedule", read_schedule_transitions)
    m.add_state("align pill chamber", align_pill_chamber_transitions)
    m.add_state("picture", picture_transitions)
    m.add_state("evaluate pill", evaluate_pill_transitions)
    m.run(None)
    print("Completed Successfully")
