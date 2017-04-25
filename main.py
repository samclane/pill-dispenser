#!/usr/bin/python35

import logging
from time import localtime, strftime

import mechanism
import pill_recog
import scheduler

from functools import wraps

pill_dispenser = mechanism.Mechanism()

logger = logging.getLogger("dispenser")
logger.setLevel(logging.INFO)

scld = scheduler.Scheduler()


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
                logger.info("reached " + new_state)
                break
            else:
                handler = self.handlers[new_state.upper()]


def state(state_transition):
    @wraps(state_transition)
    def wrapper(*args, **kwargs):
        logger.warning("Running: " + state_transition.__name__)
        output = state_transition(*args, **kwargs)
        logger.warning("    MoveTo: " + output[0])
        return output

    return wrapper


@state
def start_transitions(cargo):
    # check if the schedules are current
    # if it is, go to read_schedules
    # else, update schedules
    scld.check_schedule()
    schedule = scld.get_schedule()
    new_state = "read_schedule_state"
    return (new_state, schedule)


@state
def read_schedule_transitions(cargo):
    # check if it is time to dispense
    # if it is time, align pill chamber
    # else, loop back
    lt = localtime()
    curr_time = scheduler.datetime(lt.tm_wday, "{}:{}".format(lt.tm_hour, lt.tm_sec))
    if curr_time in cargo:
        new_state = "align_pill_chamber_state"
        cargo = scld.get_pills(curr_time)
    else:
        new_state = "read_schedule_state"
        cargo = cargo
    return (new_state, cargo)  # will probably not have to return scheude here


@state
def align_pill_chamber_transitions(cargo):
    # if pill_curr == cargo move to pushkey forward
    # else, move chamber to next pill then loop back
    pill_curr = pill_dispenser.get_current_pill()
    if pill_curr != cargo:
        next_state = "align_pill_chamber_state"
        cargo = cargo
    elif pill_curr == cargo:
        pill_dispenser.dispense_pill()
        next_state = "picture_state"  # should probably refector name to be more accurate
        cargo = "None"
    else:
        raise Exception("something has gone wrong in align_pill_chamber_transitions")
    return (next_state, cargo)


@state
def picture_transitions(cargo):
    # if pill is present, move to evaluate pill
    # if pill is not present, shake pill then loop back
    if pill_recog.pill_present():
        next_state = "evaluate_pill_state"
        cargo = "None"
    else:
        pill_dispenser.shake_chamber()
        next_state = "picture_state"
        cargo = "None"
    return (next_state, cargo)
    pass


@state
def evaluate_pill_transitions(cargo):
    # if confidence is >75%, open the hatch (success) then update DB
    # else, do a dispense error (failure)
    confidence = pill_recog.evaluate_picture(cargo)
    if confidence >= .75:
        pill_dispenser.open_hatch()
        next_state = "start_transitions_state"
    else:
        next_state = "dispense_error_state"
    return (next_state, confidence)
    pass

@state
def dispense_error_transitions(cargo):
    logger.error("Confidence too low ({})".format(cargo))
    return ("None", "None")


if __name__ == "__main__":
    m = StateMachine()
    m.add_state("start_transitions_state", start_transitions)
    m.add_state("read_schedule_state", read_schedule_transitions)
    m.add_state("align_pill_chamber_state", align_pill_chamber_transitions)
    m.add_state("picture_state", picture_transitions)
    m.add_state("evaluate_pill_state", evaluate_pill_transitions)
    m.add_state("dispense_error_state", dispense_error_transitions, end_state=True)
    m.set_start("start_transitions_state")
    m.run("None")
    print("Completed Successfully")
