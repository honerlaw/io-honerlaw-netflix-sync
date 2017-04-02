
import $ from "jquery";

/**
 * Used to keep track of the number of simulated events that have been
 * sent out. A user event is only intercepted if the number of current
 * simulated events is 0
 */
let simulatedEvents = 0;

/**
 * Binds to the document to start intercepting specific events
 *
 * @return EventDispatcher the event dispatcher for chaining
 */
export function bind(handler) {
    $(document).on('click keyup mousedown mouseup mouseout mouseover', (e) => {
        // check for simulated events
        if(simulatedEvents > 0) {
            simulatedEvents--;
            return;
        }
        handler(e);
    });
}

/**
 * Simulates a mousemove event on a given element
 *
 * @param ele mixed The target element
 * @return EventDispatcher The event dispatcher for chaining
 */
export function move(ele) {
    if($(ele).length === 0) {
        return;
    }
    simulatedEvents++;
    let element = $(ele)[0];
    element.dispatchEvent(new MouseEvent('mousemove', {
        'bubbles': true,
        'button': 0,
        'currentTarget': element
    }));
}

/**
 * Simulates a click on a given element
 *
 * @param ele mixed the target element
 * @return EventDispatcher The event dispatcher for chaining
 */
export function click(ele) {
    simulatedEvents++;
    $(ele).click();
}

/**
 * Simulates clicking a point on a given element
 *
 * @param ele mixed The target element
 * @param x number The x coordinate
 * @param y number The y coordinate
 * @return EventDispatcher The event dispatcher for chaining
 */
export function clickPoint(ele, x, y) {
    simulatedEvents += 3;
    let element = $(ele);
    let options = {
        'bubbles': true,
        'button': 0,
        'screenX': x - $(window).scrollLeft(),
        'screenY': y - $(window).scrollTop(),
        'clientX': x - $(window).scrollLeft(),
        'clientY': y - $(window).scrollTop(),
        'offsetX': x - element.offset().left,
        'offsetY': y - element.offset().top,
        'currentTarget': element[0],
        'pageX': x,
        'pageY': y,
    };
    element[0].dispatchEvent(new MouseEvent('mousedown', options));
    element[0].dispatchEvent(new MouseEvent('mouseup', options));
    element[0].dispatchEvent(new MouseEvent('mouseout', options));
}
